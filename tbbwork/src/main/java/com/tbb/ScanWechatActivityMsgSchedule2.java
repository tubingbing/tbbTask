package com.tbb;

import com.tbb.task.ITaskDeal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by liufei8 on 2015/8/14.
 */
@Component("taskTask2")
public class ScanWechatActivityMsgSchedule2 implements ITaskDeal<Object> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScanWechatActivityMsgSchedule2.class);
    @Override
    public void execute(List<Object> tasks) throws Exception {
        System.out.println(tasks.size());
    }

    @Override
    public List<Object> select(int taskItemNum, int taskNum, int eachFetchDataNum) throws Exception{
        //LOGGER.info("run ScanWechatActivityMsgSchedule start:" + System.currentTimeMillis());
        System.out.println("taskTask2--"+taskItemNum+"--"+taskNum+"--"+eachFetchDataNum);
        List<Object> list = new ArrayList();
        for(int i=0;i<10;i++){
            list.add(i);
        }
        return list;
    }
}
