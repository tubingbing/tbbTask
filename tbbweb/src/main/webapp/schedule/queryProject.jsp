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
        deleteProject =function(path){
            window.location.href="/task/dp.xhtml?path="+path;
        }
        queryTask = function (path) {
            window.location.href="/task/qt.xhtml?path="+path;
        }
        createProject = function () {
            window.location.href="/task/cp.xhtml";
        }
    </script>
</head>
<body>
<form action="/task/qp.xhtml" >
    <input type="button" value="创建项目" onclick="createProject();"/>
    <table>
        <tr>
            <th>项目名称</th>
            <th>zk地址</th>
            <th>操作</th>
        </tr>
        <c:forEach items="${list}" var="name">
            <tr>
                <td >${name}</td>
                <td >${path}/${name}</td>
                <td>
                    <input type="button" value="任务" onclick="queryTask('${path}/${name}');"/>
                    <input type="button" value="删除" onclick="deleteProject('${path}/${name}');"/>
                </td>
            </tr>
        </c:forEach>

    </table>
</form>
</body>
</html>
