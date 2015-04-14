<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Welcome Page</title>
</head>
<body>
    <jsp:useBean id="bean" class="issues.pax.web.jsp.test.beans.SomeBean"/>
    <c:out value="${bean.hello}"/>
</body>
</html>
