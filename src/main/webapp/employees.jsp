<%@ page isELIgnored="false" %>

<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<c:set var="title" value="All employees" scope="request"/>
<c:import url="includes/head.jsp"/>

<c:import url="includes/header.jsp"/>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<main id="main">
    <div class="container" id="content">
        <h1>Employees</h1>
        <div class="row mb-2" id="movieTableInfo">

            <table class="table table-bordered table-striped">
                <thdead>
                    <tr>
                        <th>Full name</th>
                        <th>Position</th>
                        <th>Salary</th>
                        <th>City</th>
                        <th>State</th>
                        <th>With us</th>
                    </tr>
                </thdead>
                <tbody>
<c:forEach items="${employees}" var="emp">
    <tr>
        <td>${emp.firstName} ${emp.lastName}</td>
        <td>${emp.position}</td>
        <td>${emp.salary}</td>
        <td>${emp.city}</td>
        <td>${emp.state}</td>
        <td>${emp.withUs}</td>
    </tr>
</c:forEach>
                </tbody>
            </table>
        </div>
    </div>


</main>



