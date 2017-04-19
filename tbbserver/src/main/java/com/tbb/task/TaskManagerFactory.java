package com.tbb.task;

import com.tbb.zk.ZkManager;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;

import java.net.InetAddress;
import java.util.List;
import java.util.UUID;

/**
 * 任务工厂
 *
 * @author: tubingbing
 * @Date: 2017/4/18
 * @Time: 15:34
 */
public class TaskManagerFactory implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private ZkManager zkManager;

    private String zkAddress;
    private String zkPath;
    private String zkUserName;
    private String zkPassword;
    private String zkSessionOut;
    //private String zkIsCheckParentPath;

    private String serviceId;
    private String ip;

    public void init() throws Exception{
        zkManager = new ZkManager(zkAddress,zkPath,zkUserName,zkPassword,zkSessionOut);
        zkManager.init();
        if (null == this.ip) {
            try {
                this.ip = InetAddress.getLocalHost().getHostAddress();
            }catch (Exception e){
                this.ip = "";
            }
        }
        serviceId = this.ip+"_"+UUID.randomUUID().toString();
        zkManager.create(zkPath+"/server",null);
        zkManager.create(zkPath+"/server/"+serviceId,null);
        zkManager.delete(zkPath+"/baseTask");
        //zkManager.setData(zkPath+"/baseTask/taskTask","{\"beanName\":\"taskTask\",\"fetchNum\":2,\"permitRunStartTime\":\"0/10 * * * * ?\",\"state\":0,\"threadNum\":5}");
        //zkManager.setData(zkPath+"/baseTask/taskTask2","{\"beanName\":\"taskTask2\",\"fetchNum\":2,\"permitRunStartTime\":\"0/5 * * * * ?\",\"state\":0,\"threadNum\":3}");
        computerStart();
    }
    
    /**
     * 开始的时候，计算第一次执行时间
     * @throws Exception
     */
    public void computerStart() throws Exception{
        List<String> list = zkManager.getChild(zkPath + "/baseTask");
        //只有当存在可执行队列后再开始启动队列
        if (!CollectionUtils.isEmpty(list)) {
            for (final String path : list) {
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            new TaskManager(zkPath + "/baseTask/"+path,TaskManagerFactory.this);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

            }
        }
    }
    public void destory(){
        if (zkManager!=null){
            zkManager.close();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public String getZkAddress() {
        return zkAddress;
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress;
    }

    public String getZkPath() {
        return zkPath;
    }

    public void setZkPath(String zkPath) {
        this.zkPath = zkPath;
    }

    public String getZkUserName() {
        return zkUserName;
    }

    public void setZkUserName(String zkUserName) {
        this.zkUserName = zkUserName;
    }

    public String getZkPassword() {
        return zkPassword;
    }

    public void setZkPassword(String zkPassword) {
        this.zkPassword = zkPassword;
    }

    public String getZkSessionOut() {
        return zkSessionOut;
    }

    public void setZkSessionOut(String zkSessionOut) {
        this.zkSessionOut = zkSessionOut;
    }

    public ZkManager getZkManager() {
        return zkManager;
    }

    public void setZkManager(ZkManager zkManager) {
        this.zkManager = zkManager;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    /* public String getZkIsCheckParentPath() {
        return zkIsCheckParentPath;
    }

    public void setZkIsCheckParentPath(String zkIsCheckParentPath) {
        this.zkIsCheckParentPath = zkIsCheckParentPath;
    }*/

}


