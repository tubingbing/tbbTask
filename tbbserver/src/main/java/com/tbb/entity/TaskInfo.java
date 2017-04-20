package com.tbb.entity;

import java.io.Serializable;

/**
 * @author: tubingbing
 * @Date: 2017/4/18
 * @Time: 16:40
 */
public class TaskInfo implements Serializable {

    private String beanName;
    private String threadNum;
    private String fetchNum;
    private String permitRunStartTime;
    private int state = 0; // 0开始 1暂停

    public String getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(String threadNum) {
        this.threadNum = threadNum;
    }

    public String getFetchNum() {
        return fetchNum;
    }

    public void setFetchNum(String fetchNum) {
        this.fetchNum = fetchNum;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getPermitRunStartTime() {
        return permitRunStartTime;
    }

    public void setPermitRunStartTime(String permitRunStartTime) {
        this.permitRunStartTime = permitRunStartTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "TaskInfo{" +
                "beanName='" + beanName + '\'' +
                ", fetchNum='" + fetchNum + '\'' +
                ", permitRunStartTime='" + permitRunStartTime + '\'' +
                ", state=" + state +
                '}';
    }
}
