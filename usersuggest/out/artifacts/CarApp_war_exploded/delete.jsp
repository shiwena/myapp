
<%--
  Created by IntelliJ IDEA.
  User: apple
  Date: 2018/7/16
  Time: 下午10:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>删除</title>
</head>
<body>

    <c:out value="${param.idd}"></c:out>

    <div style="margin: auto; width: 80%">
        <sql:setDataSource var="connection" driver="com.mysql.jdbc.Driver"
                           url="jdbc:mysql://119.23.70.24/car?useSSL=false&characterEncoding=utf8"
                           user="root" password="qwaszx" />


        <c:set var = "empId" value = "${param.idd}"/>
        <sql:update dataSource="${connection}" var="count">
            UPDATE user_suggestion SET comf='1' WHERE id=?
            <sql:param value = "${empId}" />
        </sql:update>
    </div>

<c:out value="删除成功"></c:out>

</body>
</html>
