<%@ page isELIgnored="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<c:set var="title" value="Sign up" scope="request"/>
<c:import url="includes/head.jsp"/>

<c:import url="includes/header.jsp"/>
<body>
<%--<h4 id="warning-sign-up">${warning}</h4>--%>
<div class="container">

    <fieldset class="row mb-1 ms-3 p-3">

        <form:form id="formSignUp" method="POST"
                   action="${pageContext.request.contextPath}/signup" class="form-signin" modelAttribute="employee">

            <h2 class="form-signin-heading">Please sign up</h2>
            <h4 id="warning-sign-up">${warning}</h4>

            <div class="row mb-3">
                <form:label for="username" path="user.username" class="col-sm-2 col-form-label">Username</form:label>
                <div class="col-sm-10">
                    <form:input id="username" class="form-control" path="user.username"/>
                </div>
            </div>

            <span class="lead text-center text-danger" id="message"></span>

            <div class="row mb-3">
                <form:label for="password" path="user.password" class="col-sm-2 col-form-label">Password</form:label>
                <div class="col-sm-10">
                    <form:input type="password" id="password" class="form-control" name="password"
                                path="user.password"/>
                </div>
            </div>

            <div class="row mb-3">
                <label for="confirm_password" class="col-sm-2 col-form-label">Retype Password</label>
                <div class="col-sm-10">
                    <input type="password" id="confirm_password" class="form-control" name="confirm_password" required>
                </div>
            </div>


            <div class="row mb-3">
                <label for="checkboxIsEmp" class="col-sm-2 col-form-label">Is employee?</label>
                <div class="col-sm-10">
                    <input type="checkbox" id="checkboxIsEmp" class="form-check-input" onclick="showHideEmployeeInputs(this)"/>
                </div>
            </div>


            <div class="emp-signin-inputs inactive">
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
                    <form:label path="position" class="col-sm-2 col-form-label">Position</form:label>
                    <div class="col-sm-10">
                        <form:input path="position" type="text" class="form-control"/>
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

                <div class="row mb-3">
                    <form:label path="withUs" class="col-sm-2 col-form-label">With us</form:label>
                    <div class="col-sm-10">
                        <form:checkbox path="withUs" class="form-check-input"/>
                    </div>
                </div>

                <div class="row mb-3">
                    <label for="phoneNumber" class="col-sm-2 col-form-label">Phone number</label>
                    <div class="col-sm-10">
                        <input id="phoneNumber" name="phoneNumber" type="number" value="0" class="form-control"/>
                    </div>
                </div>

            </div>

            <div class="row center">
                <div class="col text-center center">
                    <button type="submit" class="btn btn-success mx-auto">Register</button>
                </div>
            </div>

        </form:form>
    </fieldset>


</div>


<script>

    const showHideEmployeeInputs = (isEmpCheckbox) => {
        let empInputsDiv = document.querySelector(".emp-signin-inputs");
        console.log(isEmpCheckbox);
        if (isEmpCheckbox.checked) {
            empInputsDiv.classList.remove("inactive");
        } else {
            empInputsDiv.classList.add("inactive");
        }
    }

    $('form#formSignUp').submit(function (event) {
        if ($('#password').val() == $('#confirm_password').val()) {
            $('#message').html('Matching').css('color', 'green');
            return;
        }
        $("#message").text("Passwords are not matching!").css('color', 'red').show().fadeOut(3000);
        event.preventDefault();
    });
</script>

</body>
