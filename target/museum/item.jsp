<%@ page isELIgnored="false" %>

<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<c:set var="title" value="Item - ${item.name}" scope="request"/>
<c:import url="includes/head.jsp"/>

<c:import url="includes/header.jsp"/>

<main id="main">

    <div class="container" id="content">
        <h1>${item.name} information</h1>
        <div class="row mb-2" id="movieTableInfo">
            <div class="col-lg-6 col-sm-12">
                <table class="table table-bordered table-striped">
                    <legend>General information</legend>
                    <tr>
                        <th>Name</th>
                        <td>${item.name}</td>
                    </tr>
                    <tr>
                        <th>Description</th>
                        <td>${item.description}</td>
                    </tr>
                    <tr>
                        <th>Date acquired</th>
                        <td>${item.dateAcquired}</td>
                    </tr>
                    <tr>
                        <th>Is museum item</th>
                        <td>${item.isMuseumItem == 1 ? "Yes" : "No"}</td>
                    </tr>
                </table>
            </div>


            <c:if test="${not empty item.lostItem}">

            <div class="col-lg-6 col-sm-12">
                <table class="table table-bordered table-striped">
                    <legend>Lost item information</legend>
                    <tr>
                        <th>Description</th>
                        <td>${item.lostItem.description}</td>
                    </tr>
                    <tr>
                        <th>Date when lost</th>
                        <td>${item.lostItem.dateLost}</td>
                    </tr>
                </table>
            </div>
        </div>
        </c:if>
        <hr>

        <div class="row">
            <c:if test="${not empty item.locations}">
                <div class="col-lg-6 col-sm-12">
                    <table class="table table-bordered table-striped">
                        <legend>Location information</legend>
                        <thdead>
                            <tr>
                                <th>Storage type</th>
                                <th>Description</th>
                                <th>Date when put</th>
                            </tr>
                        </thdead>
                        <tbody>
                        <c:forEach items="${item.locations}" var="location">
                            <tr>
                                <td>${location.storageType}</td>
                                <td>${location.description}</td>
                                <td>${location.dateWhenPut}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:if>
            <c:if test="${not empty item.employeeItems}">
                <div class="col-lg-6 col-sm-12">
                    <table class="table table-bordered table-striped">
                        <legend>Employee(s) appraise information</legend>
                        <thdead>
                            <tr>
                                <th>Item name</th>
                                <th>Employee name</th>
                                <th>Appraised value</th>
                            </tr>
                        </thdead>
                        <tbody>
                        <c:forEach items="${item.employeeItems}" var="employeeItem">
                            <tr>
                                <td>${item.name}</td>
                                <td>${employeeItem.employee.firstName} ${employeeItem.employee.lastName}</td>
                                <td>${employeeItem.worthValue} $</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:if>
        </div>
    </div>
</main>

