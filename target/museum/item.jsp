<%@ page isELIgnored="false" %>

<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<c:set var="title" value="Item - ${item.name}" scope="request"/>
<c:import url="includes/head.jsp"/>

<c:import url="includes/header.jsp"/>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<main id="main">

    <form:form action="${pageContext.request.contextPath}/updateItem"
               method="post"
               modelAttribute="updatedItem"
                class="mb-3"
    >

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
                        <img src="${pageContext.request.contextPath}/resources/images/edit-icon.svg" class="icon"
                             onclick="updateField(this)"/>
                    </td>

                </tr>
                <tr>
                    <th>Description</th>
                    <td>
                        <span class="active">${item.description}</span>
                        <form:input path="description" type="text" class="inactive" value="${item.description}"/>
                    </td>
                    <td>
                        <img src="${pageContext.request.contextPath}/resources/images/edit-icon.svg" class="icon"
                             onclick="updateField(this)"/>
                    </td>
                </tr>
                <tr>
                    <th>Date acquired</th>
                    <td>
                                <span class="active">
                                    <fmt:formatDate pattern="MM/dd/yyyy" value="${item.dateAcquired}"/>
                                </span>
                        <form:input path="dateAcquired" class="inactive" type="date"/>
                    </td>
                    <td>
                        <img src="${pageContext.request.contextPath}/resources/images/edit-icon.svg" class="icon"
                             onclick="updateField(this)"/>
                    </td>
                </tr>

                <tr>
                    <th>Is museum item</th>
                    <td>
                        <span class="active">${item.isMuseumItem == 1 ? "Yes" : "No"}</span>
                        <form:input path="isMuseumItem" type="text" class="inactive"
                                    value="${item.isMuseumItem}" placeholder="0 for nonmuseum and 1 for museum item"/>
                    </td>
                    <td>
                        <img src="${pageContext.request.contextPath}/resources/images/edit-icon.svg" class="icon"
                             onclick="updateField(this)"/>
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
                            <form:input path="lostItem.description" type="text" class="inactive"
                                        value="${item.lostItem.description}"/>
                        </td>
                        <td>
                            <img src="${pageContext.request.contextPath}/resources/images/edit-icon.svg" class="icon"
                                 onclick="updateField(this)"/>
                        </td>
                    </tr>
                    <tr>
                        <th>Date when lost</th>
                        <td>
                                <span class="active">
                                        <fmt:formatDate pattern="MM/dd/yyyy" value="${item.lostItem.dateLost}"/>
                                </span>
                            <form:input path="lostItem.dateLost" type="date" class="inactive"/>
                        </td>
                        <td>
                            <img src="${pageContext.request.contextPath}/resources/images/edit-icon.svg" class="icon"
                                 onclick="updateField(this)"/>
                        </td>
                    </tr>

                        <%--                        <form:input path="dateAcquired" class="inactive" type="date" />--%>
                    <form:input path="lostItem.id" type="hidden" class="inactive" value="${item.id}"/>
                </table>
            </div>
            </div>
        </c:if>
        <hr>

        <div class="row">
        <h3>Location information</h3>

        <c:if test="${not empty item.locations}">
            <div class="col-lg-12 col-sm-12">
                    <%--                        <form:input type="text" path="locations[${lc.index}].storageType"/>--%>

                <table class="table table-bordered table-striped">
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
                                <span class="active loc-info"> ${location.storageType}</span>
                                <form:input type="text" class="inactive loc-input"
                                            path="locations[${lc.index}].storageType" value="${location.storageType}"/>
                            </td>
                            <td>
                                <span class="active loc-info"> ${location.description}</span>
                                <form:input type="text" class="inactive loc-input"
                                            path="locations[${lc.index}].description" value="${location.description}"/>
                            </td>
                            <td>
                                        <span class="active loc-info">
                                                <fmt:formatDate pattern="MM/dd/yyyy" value="${location.dateWhenPut}"/>
                                        </span>
                                <form:input type="date" class="inactive loc-input"
                                            path="locations[${lc.index}].dateWhenPut" value="${location.dateWhenPut}"/>
                            </td>
                            <td>
                                <img src="${pageContext.request.contextPath}/resources/images/edit-icon.svg"
                                     class="icon" onclick="updateRow(this)"/>
                                    <%--                                        <button type="button" class="btn btn-outline-info" onclick="updateRow(this)">Update</button>--%>
                            </td>
                        </tr>
                        <form:input type="hidden" path="locations[${lc.index}].id" value="${location.id}"/>

                    </c:forEach>


                    </tbody>
                </table>
            </div>
            </div>
        </c:if>
        <c:if test="${not empty item.employeeItems}">

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
                <c:forEach items="${item.employeeItems}" varStatus="empItem" var="employeeItem">
                    <tr>
                        <td>${item.name}</td>
                        <td>${employeeItem.employee.firstName} ${employeeItem.employee.lastName}</td>
                            <%--                                    <form:input type="number" class="inactive1" path="employeeItems" value="${employeeItem.worthValue}"/>--%>

                        <td>
                            <span class="active ei-info"> ${employeeItem.worthValue} $</span>
                            <form:input type="text" class="ei-input inactive"
                                        path="employeeItems[${empItem.index}].worthValue" value="${employeeItem.worthValue}"/>
                            <img src="${pageContext.request.contextPath}/resources/images/edit-icon.svg"
                                 class="icon" onclick="updateWorthVal(this)"/>
                        </td>
                        <form:input type="hidden" path="employeeItems[${empItem.index}].id" value="${employeeItem.id}"/>
                        <form:input type="hidden" path="employeeItems[${empItem.index}].employee.id" value="${employeeItem.employee.id}"/>
                        <form:input type="hidden" path="employeeItems[${empItem.index}].item.id" value="${employeeItem.item.id}"/>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            </div>
        </c:if>
        <div class="row center">
            <div class="col text-center center">
                <button type="submit" class="btn btn-success mx-auto">Update</button>
            </div>
        </div>
        <form:input type="hidden" path="id" value="${item.id}"/>

    </form:form>


    <hr>
<%--    //TODO: put to the location block -> --%>
<%--            inputs as inactive tds + btn with js to make them visible--%>
    <h3>Generate new location</h3>
    <form action="${pageContext.request.contextPath}/addNewLocation"
          class="row ms-3 p-3"
          method="post"
    >
        <div class="row mb-3">
            <label for="storageType" class="col-sm-2 col-form-label">Storage type:</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="storageType" id="storageType">
            </div>
        </div>
        <div class="row mb-3">
            <label for="locDescription" class="col-sm-2 col-form-label">Location description:</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" name="locDescription" id="locDescription">
            </div>
        </div>
        <div class="row mb-3">
            <label for="dateWhenPut" class="col-sm-2 col-form-label">Date when put:</label>
            <div class="col-sm-10">
                <input type="date" class="form-control" name="dateWhenPut" id="dateWhenPut">
            </div>
        </div>
        <div class="row center">
            <div class="col text-center center">
                <button type="submit" class="btn btn-success mx-auto">Update</button>
            </div>
        </div>
        <input type="hidden" name="id" value="${item.id}">
    </form>


    <hr>


    </main>


<script>

    function updateWorthVal(updateBtn) {
        updateBtn.classList.add("inactive");
        let td = updateBtn.parentNode;
        let readVal = td.firstElementChild;
        readVal.classList.remove("active");
        readVal.classList.add("inactive");
        let input = readVal.nextElementSibling;
        input.classList.remove("inactive");

        console.log(updateBtn);
        console.log(td);
        console.log(readVal);
        console.log(input);


    }

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
        let tableRawDOM = updateBtn.parentNode.parentNode;
        updateBtn.parentNode.classList.add("inactive");
        [...tableRawDOM.querySelectorAll(".loc-info")].forEach(el => {
                el.classList.remove("active");
                el.classList.add("inactive");
            }
        );

        [...tableRawDOM.querySelectorAll(".loc-input")].forEach(el => el.classList.remove("inactive"));
    }
</script>

