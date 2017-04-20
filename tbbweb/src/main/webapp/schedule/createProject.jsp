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
<form action="/task/cpSubmit.xhtml" >
    <table>
        <tr>
            <td>项目名称</td>
            <td>
                <input type="text" name="path" />
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
