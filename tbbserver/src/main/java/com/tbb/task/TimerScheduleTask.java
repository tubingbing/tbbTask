package com.tbb.task;

import com.tbb.entity.TaskInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Timer;

/**
 * @author: tubingbing
 * @Date: 2017/4/19
 * @Time: 9:45
 */
public class TimerScheduleTask extends java.util.TimerTask{
    private static transient Log log = LogFactory.getLog(TimerScheduleTask.class);
    TaskManager manager;
    Timer timer;
    String cronTabExpress;
    TaskInfo taskInfo;
    public int state ;

    public TimerScheduleTask(TaskManager aManager,Timer aTimer,TaskInfo taskInfo) {
        this.manager = aManager;
        this.timer = aTimer;
        this.taskInfo = taskInfo;
    }
    public void run() {
        try {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            manager.start();
            if(taskInfo!=null){
                if(taskInfo.getState()==0) {
                    manager.execute(taskInfo);
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    public String getCronTabExpress() {
        return cronTabExpress;
    }

    public void setCronTabExpress(String cronTabExpress) {
        this.cronTabExpress = cronTabExpress;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
