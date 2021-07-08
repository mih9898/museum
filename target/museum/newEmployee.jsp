<%@ page isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<c:set var="title" value="New Employee" scope="request"/>
<c:import url="includes/head.jsp"/>

<c:import url="includes/header.jsp"/>

<main id="main">

    <div class="container" id="content">
        <h1>Add new Employee</h1>

        <fieldset class="row mb-1 ms-3 p-3">

            <form:form action="${pageContext.request.contextPath}/addEmployee" method="POST"
                       modelAttribute="newEmployee">
                <div class="row mb-3">
                    <form:label path="firstName" class="col-sm-2 col-form-label">First name</form:label>
                    <div class="col-sm-10">
                        <form:input path="firstName" type="text" class="form-control"/>
                    </div>
                </div>

                <div class="row mb-3">
                    <form:label path="middleName" class="col-sm-2 col-form-label">Middle name</form:label>
                    <div class="col-sm-10">
                        <form:input path="middleName" type="text" class="form-control"/>
                    </div>
                </div>

                <div class="row mb-3">
                    <form:label path="lastName" class="col-sm-2 col-form-label">Last name</form:label>
                    <div class="col-sm-10">
                        <form:input path="lastName" type="text" class="form-control"/>
                    </div>
                </div>

                <div class="row mb-3">
                    <form:label path="salary" class="col-sm-2 col-form-label">Salary</form:label>
                    <div class="col-sm-10">
                        <form:input type="number" step="0.01" path="salary" class="form-control"/>
                    </div>
                </div>

                <div class="row mb-3">
                    <form:label path="address" class="col-sm-2 col-form-label">Address</form:label>
                    <div class="col-sm-10">
                        <form:input path="address" type="text" class="form-control"/>
                    </div>
                </div>

                <div class="row mb-3">
                    <form:label path="city" class="col-sm-2 col-form-label">City</form:label>
                    <div class="col-sm-10">
                        <form:input path="city" type="text" class="form-control"/>
                    </div>
                </div>

                <div class="row mb-3">
                    <form:label path="state" class="col-sm-2 col-form-label">State</form:label>
                    <div class="col-sm-10">
                        <form:input path="state" type="text" class="form-control"/>
                    </div>
                </div>

                <div class="row mb-3">
                    <form:label path="zipAddress" class="col-sm-2 col-form-label">Zip address</form:label>
                    <div class="col-sm-10">
                        <form:input path="zipAddress" type="text" class="form-control"/>
                    </div>
                </div>

<%--                <div class="row mb-3">--%>
<%--                    <label for="checkboxWithUs" class="col-sm-2 col-form-label">With us</label>--%>
<%--                    <div class="col-sm-10">--%>
<%--                        <input id="checkboxWithUs" name="withUs" class="form-check-input" type="checkbox"  />--%>
<%--                    </div>--%>
<%--                </div>--%>

                <div class="row mb-3">
                    <form:label path="withUs" class="col-sm-2 col-form-label">With us</form:label>
                    <div class="col-sm-10">
                        <form:checkbox path="withUs" class="form-check-input"  />
                    </div>
                </div>

                <div class="row mb-3">
                    <label for="phoneNumber" class="col-sm-2 col-form-label">Phone number</label>
                    <div class="col-sm-10">
                            <input id="phoneNumber" name="phoneNumber" type="number" class="form-control"/>
                    </div>
                </div>







                <div class="row center">
                    <div class="col text-center center">
                        <button type="submit" class="btn btn-success mx-auto">Add Employee</button>
                    </div>
                </div>

            </form:form>
        </fieldset>

        <hr>



    </div>


</main>

<script>
    // const showHideFormPartForLostItem = () => {
    //     let lostItemFieldset = document.querySelector('#lostItemFormLabels');
    //     //let showHideCheckbox = document.querySelector("#isLost");
    //
    //     if (!lostItemFieldset.classList.contains("active")) {
    //         lostItemFieldset.classList.add("active");
    //     } else {
    //         lostItemFieldset.classList.remove("active");
    //     }
    //     console.log("js is launched...")
    // }
    //
    // window.onload = () => {
    //     let showHideCheckbox = document.querySelector("#isLost");
    //     showHideCheckbox.addEventListener("click", showHideFormPartForLostItem);
    // }
</script>