<%@ page isELIgnored="false" %>

<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<c:set var="title" value="Employee - ${employee.firstName} ${employee.lastName}" scope="request"/>
<c:import url="includes/head.jsp"/>

<c:import url="includes/header.jsp"/>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="input" uri="http://www.springframework.org/tags/form" %>

<main id="main">

    <form:form action="${pageContext.request.contextPath}/updatedEmp"
               method="post"
               modelAttribute="updatedEmp"
               class="mb-3"
    >

    <div class="container" id="content">
        <h1>${item.name} information</h1>
        <div class="row mb-2">
            <div class="col-lg-6 col-sm-12">
                <table class="table table-bordered table-striped">
                    <legend>${employee.firstName} ${employee.lastName}'s information</legend>
                    <tr>
                        <th>First name</th>
                        <td>
                            <span class="active">${employee.firstName}</span>
                            <form:input class="inactive" type="text" path="firstName" value="${employee.firstName}"/>
                        </td>
                        <td>
                            <img src="${pageContext.request.contextPath}/resources/images/edit-icon.svg" class="icon"
                                 onclick="updateField(this)"/>
                        </td>
                    </tr>

                    <tr>
                        <th>Middle name</th>
                        <td>
                            <span class="active">${empty employee.middleName ? "n/a" : employee.middleName}</span>
                            <form:input class="inactive" type="text" path="middleName" value="${employee.middleName}"/>
                        </td>
                        <td>
                            <img src="${pageContext.request.contextPath}/resources/images/edit-icon.svg" class="icon"
                                 onclick="updateField(this)"/>
                        </td>
                    </tr>

                    <tr>
                        <th>Last name</th>
                        <td>
                            <span class="active">${employee.lastName}</span>
                            <form:input class="inactive" type="text" path="lastName" value="${employee.lastName}"/>
                        </td>
                        <td>
                            <img src="${pageContext.request.contextPath}/resources/images/edit-icon.svg" class="icon"
                                 onclick="updateField(this)"/>
                        </td>
                    </tr>

                    <tr>
                        <th>Position</th>
                        <td>
                            <span class="active">${employee.position}</span>
                            <form:input class="inactive" type="text" path="position" value="${employee.position}"/>
                        </td>
                        <td>
                            <img src="${pageContext.request.contextPath}/resources/images/edit-icon.svg" class="icon"
                                 onclick="updateField(this)"/>
                        </td>
                    </tr>

                    <tr>
                        <th>Salary</th>
                        <td>
                            <span class="active">${employee.salary}</span>
                            <form:input class="inactive" type="number" path="salary" value="${employee.salary}"/>
                        </td>
                        <td>
                            <img src="${pageContext.request.contextPath}/resources/images/edit-icon.svg" class="icon"
                                 onclick="updateField(this)"/>
                        </td>
                    </tr>

                    <tr>
                        <th>Address</th>
                        <td>
                            <span class="active">${employee.address}</span>
                            <form:input class="inactive" type="text" path="address" value="${employee.address}"/>
                        </td>
                        <td>
                            <img src="${pageContext.request.contextPath}/resources/images/edit-icon.svg" class="icon"
                                 onclick="updateField(this)"/>
                        </td>
                    </tr>

                    <tr>
                        <th>City</th>
                        <td>
                            <span class="active">${employee.city}</span>
                            <form:input class="inactive" type="text" path="city" value="${employee.city}"/>
                        </td>
                        <td>
                            <img src="${pageContext.request.contextPath}/resources/images/edit-icon.svg" class="icon"
                                 onclick="updateField(this)"/>
                        </td>
                    </tr>

                    <tr>
                        <th>State</th>
                        <td>
                            <span class="active">${employee.state}</span>
                            <form:input class="inactive" type="text" path="state" value="${employee.state}"/>
                        </td>
                        <td>
                            <img src="${pageContext.request.contextPath}/resources/images/edit-icon.svg" class="icon"
                                 onclick="updateField(this)"/>
                        </td>
                    </tr>

                    <tr>
                        <th>Zip code</th>
                        <td>
                            <span class="active">${employee.zipAddress}</span>
                            <form:input class="inactive" type="text" path="zipAddress" value="${employee.zipAddress}"/>
                        </td>
                        <td>
                            <img src="${pageContext.request.contextPath}/resources/images/edit-icon.svg" class="icon"
                                 onclick="updateField(this)"/>
                        </td>
                    </tr>

                    <tr>
                        <th>With us</th>
                        <td>
                            <span class="active">${employee.withUs}</span>
                            <form:checkbox class="inactive" path="withUs" value="${employee.withUs}"/>
                        </td>
                        <td>
                            <img src="${pageContext.request.contextPath}/resources/images/edit-icon.svg" class="icon"
                                 onclick="updateField(this)"/>
                        </td>
                    </tr>
                    <form:hidden path="id" value="${employee.id}" />
                </table>
            </div>


            <hr>

            <div class="row mb-2" id="movieTableInfo">
                <div class="col-lg-6 col-sm-12">
            <table class="table table-bordered table-striped">
                <tr>
                <th>Phone number(s)</th>
                </tr>
                <c:forEach items="${employee.phoneNumbers}" varStatus="ph" var="phone">
                    <tr>

                        <td>
                            <span class="active">${phone.phoneNumber}</span>
                            <form:input type="number" class="inactive"
                                        path="phoneNumbers[${ph.index}].phoneNumber" value="${phone.phoneNumber}"/>
                        </td>
                        <td>
                            <img src="${pageContext.request.contextPath}/resources/images/edit-icon.svg" class="icon"
                                 onclick="updateField(this)"/>
                        </td>
                        <form:hidden path="phoneNumbers[${ph.index}].id" value="${phone.id}" />
                    </tr>
                </c:forEach>
            </table>
                </div>
            </div>


            <hr>

            <div class="row center">
                <div class="col text-center center">
                    <button type="submit" class="btn btn-success mx-auto">Update</button>
                </div>
            </div>
                <form:input type="hidden" path="id" value="${item.id}"/>

            </form:form>


            <hr>

            <h3>Generate new phone number</h3>
            <form:form action="${pageContext.request.contextPath}/addPhone" modelAttribute="newPhone" method="post">
            <div class="row mb-3">
                <form:label path="phoneNumber" class="col-sm-2 col-form-label">Phone number:</form:label>
                <div class="col-sm-10">
                    <form:input type="number" path="phoneNumber" class="form-control" />
                </div>
            </div>

            <div class="row center">
                <div class="col text-center center">
                    <button type="submit" class="btn btn-success mx-auto">Add</button>
                </div>
            </div>

        <input:hidden path="employee.id" value="${employee.id}" />
            </form:form>

</main>


<script>
    function updateField(updateBtn) {
        updateBtn.parentNode.classList.add("inactive");
        let previousTd = updateBtn.parentNode.previousElementSibling; //td where val in span + input are
        let firstPreviousTdChild = previousTd.firstElementChild; // emp td vaue in span(read-only)
        let lastPreviousTdChild = previousTd.lastElementChild;   // hidden input
        let tdValue = firstPreviousTdChild.innerHTML;




        if (tdValue === "true" || tdValue === "false") {
            firstPreviousTdChild.nextElementSibling.classList.remove("inactive");
        }




        firstPreviousTdChild.classList.add("inactive");
        firstPreviousTdChild.classList.remove("active");
        lastPreviousTdChild.classList.remove("inactive");

        let successGenInfoBtn = document.querySelector(".submit-btn");
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
