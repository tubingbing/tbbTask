<%--
  Created by IntelliJ IDEA.
  User: tubingbing
  Date: 2017/4/20
  Time: 9:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
    <script>
        var m ='${message}';
        if(m!=''){
            alert(m);
        }
        createTask =function(path,beanName){
            if(beanName ==undefined) {
                window.location.href="/task/ct.xhtml?path="+path;
                return;
            }
            window.location.href="/task/ct.xhtml?path="+path+"&beanName="+beanName;
        }
        deleteTask =function(path,beanName){
            window.location.href="/task/dt.xhtml?path="+path+"&beanName="+beanName;
        }
        updateTaskState =function (path,beanName,state) {
            window.location.href="/task/ut.xhtml?path="+path+"&beanName="+beanName+"&state="+state;
        }
    </script>
</head>
<body>
<form action="/task/qt.xhtml" >
    <input type="button" value="创建任务" onclick="createTask('${path}');"/>
    <table>
        <tr>
            <th>任务名称</th>
            <th>任务状态</th>
            <th>触发规则</th>
            <th>线程数</th>
            <th>每次获取数据量</th>
            <th>操作</th>
        </tr>
        <c:forEach items="${list}" var="taskInfo">
            <tr>
                <td >${taskInfo.beanName}</td>
                <td >${taskInfo.state}</td>
                <td >${taskInfo.permitRunStartTime}</td>
                <td >${taskInfo.threadNum}</td>
                <td >${taskInfo.fetchNum}</td>
                <td>
                    <input type="button" value="编辑" onclick="createTask('${path}','${taskInfo.beanName}');"/>
                    <input type="button" value="删除" onclick="deleteTask('${path}','${taskInfo.beanName}');"/>
                    <c:if test="${taskInfo.state=='0'}">
                        <input type="button" value="暂停" onclick="updateTaskState('${path}','${taskInfo.beanName}',1);"/>
                    </c:if>
                    <c:if test="${taskInfo.state=='1'}">
                        <input type="button" value="启动" onclick="updateTaskState('${path}','${taskInfo.beanName}',0);"/>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>
</form>
</body>
</html>
