<%--
  Created by IntelliJ IDEA.
  User: tubingbing
  Date: 2017/4/20
  Time: 9:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script>
        var m ='${message}';
        if(m!=''){
            alert(m);
        }
    </script>
</head>
<body>
<form action="/task/ctSubmit.xhtml" method="post" >
    <table>
        <tr>
            <td>任务bean：</td>
            <td>
                <input type="text" name="beanName" value="${taskInfo.beanName}"/>
            </td>
        </tr>
        <tr>
            <td>触发规则(Cron表达式)：</td>
            <td>
                <input type="text" name="permitRunStartTime" value="${taskInfo.permitRunStartTime}"/>
            </td>
        </tr>
        <tr>
            <td>每次获取数据量：</td>
            <td>
                <input type="text" name="fetchNum" value="${taskInfo.fetchNum}"/>
            </td>
        </tr>
        <tr>
            <td>线程数：</td>
            <td>
                <input type="text" name="threadNum" value="${taskInfo.threadNum}"/>
                <input type="hidden" name="path" value="${path}"/>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="submit" value="提交"/>
            </td>
        </tr>
    </table>
</form>

</body>
</html>
