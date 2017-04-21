package com.tbb.web;

import com.tbb.zk.ZkManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.FileReader;
import java.util.Properties;

/**
 * @author: tubingbing
 * @Date: 2017/4/19
 * @Time: 18:59
 */
@Component
public class ZKManagerFactory {
    public final static String configFile = "/zkconfig.properties";

    private ZkManager zkManager;

    @PostConstruct
    public void init() {
        try {
            File file = new File(this.getClass().getResource("/").getPath() + configFile);

            if (file.exists()) {
                Properties p = new Properties();
                FileReader reader = new FileReader(file);
                p.load(reader);
                reader.close();
                zkManager = new ZkManager(p.getProperty("zkConnectString"), p.getProperty("rootPath"), p.getProperty("userName"),
                        p.getProperty("password"), p.getProperty("zkSessionTimeout"));
                zkManager.init();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void destory() {
        if (zkManager != null) {
            zkManager.close();
        }
    }

    public ZkManager getZkManager() {
        return zkManager;
    }

    public void setZkManager(ZkManager zkManager) {
        this.zkManager = zkManager;
    }
}
