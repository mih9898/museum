<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>

<!DOCTYPE html>
<c:set var="name" value="Fred" />
<html>
<head>
    <title>Demo</title>
</head>
<body>
<p>
    Hi, ${name}!
</p>
<a href="${pageContext.request.contextPath}/test">test</a>
</body>
</html>