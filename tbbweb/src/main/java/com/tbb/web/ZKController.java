package com.tbb.web;

import com.tbb.entity.TaskInfo;
import com.tbb.util.JacksonUtils;
import com.tbb.zk.ZkManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: tubingbing
 * @Date: 2017/4/19
 * @Time: 19:03
 */
@Controller
@RequestMapping("/task")
public class ZKController {

    @Resource
    private ZKManagerFactory zkManagerFactory;

    @RequestMapping("/qp")
    public String queryProject(Model model){
        try {
            String path = zkManagerFactory.getZkManager().getPath();
            List<String> list = zkManagerFactory.getZkManager().getChild(path);
            model.addAttribute("list",list);
            model.addAttribute("path",path);
            return "queryProject";
        }catch (Exception e){
            //e.printStackTrace();
            model.addAttribute("list",new ArrayList<String>());
            return "queryProject";
        }
    }
    @RequestMapping("/cp")    //创建
    public String createProject(){
        return "createProject";
    }

    @RequestMapping("/cpSubmit")    //创建
    public String createProjectSubmit(String path,Model model){
        try {
            zkManagerFactory.getZkManager().create(zkManagerFactory.getZkManager().getPath()+"/"+path, null);
           return queryProject(model);
        }catch (Exception e){
           // e.printStackTrace();
            model.addAttribute("message","创建失败！");
            return "createProject";
        }
    }
    @RequestMapping("/dp") //删除
    public String deleteProject(String path,Model model){
        try {
            zkManagerFactory.getZkManager().delete(path);
            return queryProject(model);
        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("message","删除失败！");
            return queryProject(model);
        }
    }
    @RequestMapping("/qt")
    public String queryTask(String path,Model model){
        try {
            String basePath =path+"/baseTask";
            zkManagerFactory.getZkManager().create(basePath, null);
            List<String> list = zkManagerFactory.getZkManager().getChild(basePath);
            List<TaskInfo> taskInfoList = new ArrayList<TaskInfo>();
            for(String str : list){
                String json = zkManagerFactory.getZkManager().getData(basePath+"/"+str);
                TaskInfo info = JacksonUtils.toObj(json,TaskInfo.class);
                taskInfoList.add(info);
            }
            model.addAttribute("list",taskInfoList);
            model.addAttribute("path",path);
            return "queryTask";
        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("list",null);
            return "queryTask";
        }
    }

    @RequestMapping("/ct")  //编辑，创建 暂停 开启
    public String createTask(Model model,String path,String beanName){
        try {
            TaskInfo info = null;
            if (!StringUtils.isEmpty(beanName)){
                String basePath =path+"/baseTask";
                zkManagerFactory.getZkManager().create(basePath, null);
                String str = zkManagerFactory.getZkManager().getData(basePath+"/"+beanName);
                info = JacksonUtils.toObj(str,TaskInfo.class);
            }
            model.addAttribute("taskInfo",info);
            model.addAttribute("path",path);
            return "createTask";
        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("taskInfo",null);
            model.addAttribute("path",path);
            return "createTask";
        }
    }
    @RequestMapping("/ctSubmit")  //编辑，创建 暂停 开启
    public String createTaskSubmit(TaskInfo taskInfo, String path, Model model){
        try {
            System.out.println(taskInfo.toString()+"--"+path);
            String basePath =path+"/baseTask";
            zkManagerFactory.getZkManager().create(basePath, null);
            zkManagerFactory.getZkManager().create(basePath+"/"+taskInfo.getBeanName(), null);
            zkManagerFactory.getZkManager().setData(basePath+"/"+taskInfo.getBeanName(), JacksonUtils.toStr(taskInfo));
            return queryTask(path,model);
        }catch (Exception e){
            e.printStackTrace();
            return queryTask(path,model);
        }
    }
    @RequestMapping("/dt") //删除
    public String deleteTask(TaskInfo info,String path,Model model){
        try {
            String basePath =path+"/baseTask";
            zkManagerFactory.getZkManager().delete(basePath+"/"+info.getBeanName());
            return queryTask(path,model);
        }catch (Exception e){
            e.printStackTrace();
            return queryTask(path,model);
        }
    }
    @RequestMapping("/ut") //删除
    public String updateTask(TaskInfo info,String path,Model model){
        try {
            String basePath =path+"/baseTask";
            String str = zkManagerFactory.getZkManager().getData(basePath+"/"+info.getBeanName());
            TaskInfo taskInfo = JacksonUtils.toObj(str,TaskInfo.class);
            taskInfo.setState(info.getState());
            zkManagerFactory.getZkManager().setData(basePath+"/"+info.getBeanName(),JacksonUtils.toStr(taskInfo));
            return queryTask(path,model);
        }catch (Exception e){
            e.printStackTrace();
            return queryTask(path,model);
        }
    }



}
