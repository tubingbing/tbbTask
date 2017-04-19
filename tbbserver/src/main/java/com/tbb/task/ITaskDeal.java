package com.tbb.task;

import java.util.List;

/**
 * @author: tubingbing
 * @Date: 2017/4/18
 * @Time: 16:54
 */
public interface ITaskDeal<T> {

    List<T> select(int taskItemTotal, int taskItem, int fetchNum)throws Exception;
    void execute(List<T> tasks)throws Exception;

}
