package com.tbb.task;

import com.tbb.entity.TaskInfo;
import com.tbb.util.JacksonUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author: tubingbing
 * @Date: 2017/4/19
 * @Time: 9:38
 */
public class TaskManager<T> {
    /**
     * 向配置中心更新信息的定时器
     */
    private Timer heartBeatTimer;

    private String path;
    private TaskManagerFactory factory;
    protected List<T> taskList = Collections.synchronizedList(new ArrayList<T>());

    public TaskManager(String path,TaskManagerFactory factory) throws Exception{
        this.path = path;
        this.factory = factory;
        heartBeatTimer = new Timer();
        start();
    }

    public void start() throws Exception{
        TaskInfo taskInfo = null;
        try {
            String info = factory.getZkManager().getData(path);
            taskInfo = JacksonUtils.toObj(info, TaskInfo.class);
        }catch (Exception e){
             e.printStackTrace();
        }
        TimerScheduleTask scheduleTask = new TimerScheduleTask(this, taskInfo);
        if(taskInfo==null){
            heartBeatTimer.schedule(scheduleTask, 5000);
            return;
        }
        try {
            if (StringUtils.isEmpty(taskInfo.getPermitRunStartTime() )) {
                heartBeatTimer.schedule(scheduleTask, 0);
            } else {
                String tmpStr = taskInfo.getPermitRunStartTime();
                CronExpression cexpStart = new CronExpression(tmpStr);
                Date current = new Date(System.currentTimeMillis());
                Date firstStartTime = cexpStart.getNextValidTimeAfter(current);
                heartBeatTimer.schedule(scheduleTask, firstStartTime);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        
    }
    public void execute(TaskInfo taskInfo) throws Exception{
        List<String> serverList = factory.getZkManager().getChild(factory.getZkPath() + "/server");
        int taskItems = 0;
        for (int i = 0; i < serverList.size(); i++) {
            if (serverList.get(i).equals(factory.getServiceId())) {
                taskItems = i;
                break;
            }
        }
        Object bean = factory.getApplicationContext().getBean(taskInfo.getBeanName());
        if (bean == null) {
            throw new Exception("SpringBean " + taskInfo.getBeanName() + " 不存在");
        }
        if (!(bean instanceof ITaskDeal)) {
            throw new Exception("SpringBean " + taskInfo.getBeanName() + " 没有实现 ITaskDeal");
        }
        final ITaskDeal taskDeal = (ITaskDeal) bean;
        List<T> tempList = taskDeal.select(serverList.size(), taskItems, Integer.parseInt(taskInfo.getFetchNum()));
        if(tempList != null){
            this.taskList.addAll(tempList);
        }
        final int threadNum = Integer.parseInt(taskInfo.getThreadNum());
        if(taskList.size()==0){return;}
        if (threadNum > 1){
            final int[] nums = assignTaskNumber(threadNum,taskList.size());
            for(int i=0;i<threadNum;i++){
                final int n=nums[i];
                new Thread("multi deal execute"){
                    @Override
                    public void run(){
                        List<T> list = getScheduleTaskIdMulti(n);
                        try {
                            if(list != null){
                                taskDeal.execute(list);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        }else {
            taskDeal.execute(taskList);
            taskList.clear();
        }

    }
    public synchronized List<T> getScheduleTaskIdMulti(int num) {
        List<T> list = new ArrayList<T>(num);
        for (int j=0;j<num;j++){
            T t = this.taskList.remove(0);  // 按正序处理
            list.add(t);
        }
        return list;
    }
    
    public int[] assignTaskNumber(int serverNum,int taskItemNum){
        int[] taskNums = new int[serverNum];
        int numOfSingle = taskItemNum / serverNum;
        int otherNum = taskItemNum % serverNum;
        for (int i = 0; i < taskNums.length; i++) {
            if (i < otherNum) {
                taskNums[i] = numOfSingle + 1;
            } else {
                taskNums[i] = numOfSingle;
            }
        }
        return taskNums;
    }

    public TaskManagerFactory getFactory() {
        return factory;
    }

    public String getPath() {
        return path;
    }

}
