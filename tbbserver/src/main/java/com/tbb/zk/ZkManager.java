package com.tbb.zk;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.springframework.util.CollectionUtils;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * zookeeper
 *
 * @author: tubingbing
 * @Date: 2017/4/18
 * @Time: 15:50
 */
public class ZkManager implements Watcher{
    private static transient Log log = LogFactory.getLog(ZkManager.class);

    private ZooKeeper zk;
    private List<ACL> acl = new ArrayList<ACL>();

    private String address;
    private String path;
    private String userName;
    private String password;
    private String sessionOut;
    //private boolean isCheckParentPath;

    public ZkManager(String address,String path,String userName,String password,String sessionOut/*,String isCheckParentPath*/) throws Exception{
        this.address = address;
        this.path = path;
        this.userName = userName;
        this.password = password;
        this.sessionOut = sessionOut;
        //this.isCheckParentPath = Boolean.valueOf(isCheckParentPath);
        createZK();
    }

    private void createZK() throws Exception{
        zk = new ZooKeeper(address, Integer.parseInt(sessionOut), this) ;
        String authString = userName + ":"+ password;
        zk.addAuthInfo("digest", authString.getBytes());
        acl.add(new ACL(ZooDefs.Perms.ALL, new Id("digest",
                DigestAuthenticationProvider.generateDigest(authString))));
        acl.add(new ACL(ZooDefs.Perms.READ, ZooDefs.Ids.ANYONE_ID_UNSAFE));
        init();
    }

    public void init() throws Exception {
        //删除历史数据
        deleteOld(path+"/server");
        //当zk状态正常后才能调用
        if(zk.exists(path,false) == null){
            String[] list = path.split("/");
            String zkPath = "";
            for (String str : list) {
                if (!str.equals("")) {
                    zkPath = zkPath + "/" + str;
                    if (zk.exists(zkPath, false) == null) {
                        zk.create(zkPath, null, acl, CreateMode.PERSISTENT);
                    }
                }
            }
        }
    }

    public void deleteOld(String path)throws Exception{
        try {
            List<String> list = getChild(path);
            if (CollectionUtils.isEmpty(list)) {
                return;
            }
            for (String str : list) {
                if (zk.exists(path + "/" + str, false) != null) {
                    deleteOld(path + "/" + str);
                    if (str.contains(InetAddress.getLocalHost().getHostAddress())) {
                        this.delete(path + "/" + str);
                    }
                }
            }
        }catch (Exception e){
            
        }
    }
    public void delete(String path) throws Exception{
        zk.delete(path,-1);
    }

    public void create(String path,String data) throws Exception{
        if (zk.exists(path, false) == null) {
            if (data==null){
                zk.create(path, null, acl, CreateMode.PERSISTENT);
            }else {
                zk.create(path, data.getBytes(), acl, CreateMode.PERSISTENT);
            }
        }
    }

    public String getData(String path) throws Exception{
        return new String(zk.getData(path,null,null));
    }

    public void setData(String path,String data) throws Exception{
        zk.setData(path,data.getBytes(),-1);
    }

    public List<String> getChild(String path) throws Exception{
        return zk.getChildren(path,null);
    }

    @Override
    public void process(WatchedEvent event) {
        if (event.getState() == Event.KeeperState.SyncConnected) {
            log.info("收到ZK连接成功事件！");
        } else if (event.getState() == Event.KeeperState.Expired) {
            log.error("会话超时，等待重新建立ZK连接...");
            try {
                reConnection();
            } catch (Exception e) {
                log.error(e.getMessage(),e);
            }
        }else{
            log.info("已经触发了" + event.getType() + ":"+ event.getState() + "事件！" + event.getPath());
        }
    }

    /**
     * 重新連接zookeeper
     * @throws Exception
     */
    public synchronized void  reConnection() throws Exception{
        if (this.zk.getState() == ZooKeeper.States.CLOSED) {
            if (this.zk != null) {
                this.zk.close();
                this.zk = null;
            }
            this.createZK();
        }
    }

    public void close(){
        try {
            zk.close();
        }catch (Exception e){
        }
    }

    public String getPath() {
        return path;
    }
}
