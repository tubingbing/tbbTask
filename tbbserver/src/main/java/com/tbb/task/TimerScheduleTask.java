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
    TaskInfo taskInfo;

    public TimerScheduleTask(TaskManager aManager,TaskInfo taskInfo) {
        this.manager = aManager;
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
}
