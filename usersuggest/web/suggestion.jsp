<%--
  Created by IntelliJ IDEA.
  User: apple
  Date: 2018/7/15
  Time: 下午6:59
  To change this template use File | Settings | File Templates.
--%>


<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>反馈详情</title>

    <script type="text/javascript">

        function myfun(var1)
        {
            //alert(var1);
            location.href="delete.jsp?idd="+var1;

        }

    </script>

</head>
<body>
<div style="margin: auto; width: 80%">
    <sql:setDataSource var="snapshot"
                       driver="com.mysql.jdbc.Driver"
                       url="jdbc:mysql://119.23.70.24/car?useSSL=false&characterEncoding=utf8"
                       user="root"
                       password="qwaszx" />

    <sql:query dataSource="${snapshot}" var="result">
        SELECT * from user_suggestion where comf='0';
    </sql:query>
    <table border="1" width="100%">
        <tr>
            <th>id</th>
            <th>用户名</th>
            <th>详情</th>
            <th>时间</th>
            <th>操作</th>
        </tr>
        <c:forEach var="row" items="${result.rows}">
            <tr>
                <td><c:out value="${row.id}" /></td>
                <td><c:out value="${row.username}" /></td>
                <td><c:out value="${row.detail}" /></td>
                <td><c:out value="${row.time}" /></td>
                <td><button class="bt" value="${row.id}" data="${row.id}" onClick="myfun(this.value)">删除</button></td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>


