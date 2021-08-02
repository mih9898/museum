<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Test</title>
</head>
<body>
    <h2>Testing time</h2>
    Item: ${item}
    Employee: ${employee}

    <br>
    <hr>
    <form:form method="POST" action="${pageContext.request.contextPath}/test" enctype="multipart/form-data">
        <table>
            <tr>
                <td>Select a file to upload</td>
                <td><input type="file" name="file" /></td>
            </tr>
            <tr>
                <td><input type="submit" value="Submit" /></td>
            </tr>
        </table>

    </form:form>
}

    <br>
    <hr>

    <table>
        <tr>
        <c:forEach items="${cols}" var="col">
            <th>
                <c:out value="${col}" />
            </th>
        </c:forEach>
        </tr>
    <c:forEach items="${rows}" var="row">
        <tr>
            <c:forEach items="${row}" var="col">
                <td><c:out value="${col}" /></td>
            </c:forEach>
        </tr>
    </c:forEach>

</table>
</body>
</html>