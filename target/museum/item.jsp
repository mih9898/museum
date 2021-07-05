<%@ page isELIgnored="false" %>

<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<c:set var="title" value="Item - ${item.name}" scope="request"/>
<c:import url="includes/head.jsp"/>

<c:import url="includes/header.jsp"/>

<main id="main">

    <form:form action="${pageContext.request.contextPath}/updateItem" method="post" modelAttribute="updatedItem">
        <div class="container" id="content">
            <h1>${item.name} information</h1>
            <div class="row mb-2" id="movieTableInfo">
                <div class="col-lg-6 col-sm-12">
                    <table class="table table-bordered table-striped">
                        <legend>General information</legend>
                        <tr>
                            <th>Name</th>
                            <td>
                                <span class="active">${item.name}</span>
                                <form:input class="inactive" type="text" path="name" value="${item.name}"/>
                            </td>
                            <td>
                                <button type="button" class="btn btn-outline-info" onclick="updateField(this)">Update</button>
                            </td>

                        </tr>
                        <tr>
                            <th>Description</th>
                            <td>
                                <span class="active">${item.description}</span>
                                <form:input path="description" type="text" class="inactive" value="${item.description}"/>
                            </td>
                            <td>
                                <button type="button" class="btn btn-outline-info" onclick="updateField(this)">Update</button>
                            </td>
                        </tr>
                        <tr>
                            <th>Date acquired</th>
                            <td>
                                <span class="active">${item.dateAcquired}</span>
                                <form:input path="dateAcquired" class="inactive" type="date" />
                            </td>
                            <td>
                                <button type="button" class="btn btn-outline-info" onclick="updateField(this)">Update</button>
                            </td>
                        </tr>

                     <tr>
                            <th>Is museum item</th>
                            <td>
                                <span class="active">${item.isMuseumItem == 1 ? "Yes" : "No"}</span>
                                <form:input path="isMuseumItem" type="text" class="inactive"
                                            value="${item.isMuseumItem}" placeholder="0 for nonmuseum and 1 for museum item" />
                            </td>
                            <td>
                                <button type="button" class="btn btn-outline-info" onclick="updateField(this)">Update</button>
                            </td>
                        </tr>
                    </table>
                </div>


                <c:if test="${not empty item.lostItem}">

                <div class="col-lg-6 col-sm-12">
                    <table class="table table-bordered table-striped">
                        <legend>Lost item information</legend>
                        <tr>
                            <th>Description</th>
                            <td>
                                <span class="active">${item.lostItem.description}</span>
                                <form:input path="lostItem.description" type="text" class="inactive" value="${item.lostItem.description}"/>
                            </td>
                            <td>
                                <button type="button" class="btn btn-outline-info" onclick="updateField(this)">Update</button>
                            </td>
                        </tr>
                        <tr>
                            <th>Date when lost</th>
                            <td>
                                <span class="active">${item.lostItem.dateLost}</span>
                                <form:input path="lostItem.dateLost" type="date" class="inactive" />
                            </td>
                            <td>
                                <button type="button" class="btn btn-outline-info" onclick="updateField(this)">Update</button>
                            </td>
                        </tr>

<%--                        <form:input path="dateAcquired" class="inactive" type="date" />--%>

                    </table>
                </div>
            </div>
            </c:if>
            <hr>

            <div class="row">
                <c:if test="${not empty item.locations}">
                    <div class="col-lg-6 col-sm-12">
<%--                        <form:input type="text" path="locations[${lc.index}].storageType"/>--%>

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
                            <c:forEach items="${item.locations}" varStatus="lc" var="location">
                                <tr>
                                    <td>
                                        <span class="active"> ${location.storageType}</span>
                                        <form:input type="text" class="inactive1" path="locations[${lc.index}].storageType" value="${location.storageType}"/>
                                    </td>
                                    <td>
                                        <span class="active"> ${location.description}</span>
                                        <form:input type="text" class="inactive1" path="locations[${lc.index}].description" value="${location.description}"/>
                                    </td>
                                    <td>
                                        <span class="active"> ${location.dateWhenPut}</span>
                                        <form:input type="date" class="inactive1" path="locations[${lc.index}].dateWhenPut" value="${location.dateWhenPut}"/>
                                    </td>
                                    <td>
                                        <button type="button" class="btn btn-outline-info" onclick="updateRow(this)">Update</button>
                                    </td>
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
                            <c:forEach items="${item.employeeItems}" varStatus="emp" var="employeeItem">
                                <tr>
                                    <td>${item.name}</td>
                                    <td>${employeeItem.employee.firstName} ${employeeItem.employee.lastName}</td>
<%--                                    <form:input type="number" class="inactive1" path="employeeItems[${emp.index}].worthValue" value="${employeeItem.worthValue}"/>--%>

                                    <td>${employeeItem.worthValue} $</td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:if>
            </div>
        </div>
        <td><input type="submit" value="Submit"/></td>

    </form:form>
</main>


<script>
    function updateField(updateBtn) {
        updateBtn.parentNode.classList.add("inactive");
        let previousTd = updateBtn.parentNode.previousElementSibling;
        let firstPreviousTdChild = previousTd.firstElementChild;
        let lastPreviousTdChild = previousTd.lastElementChild;
        firstPreviousTdChild.classList.add("inactive");
        firstPreviousTdChild.classList.remove("active");
        lastPreviousTdChild.classList.remove("inactive");

        let successGenInfoBtn = document.querySelector("#successGenInfoBtn");
        if (!successGenInfoBtn.classList.contains("active")) {
            successGenInfoBtn.classList.add("active");
        }
    }

    function updateRow(updateBtn) {
       //TODO
    }
</script>

