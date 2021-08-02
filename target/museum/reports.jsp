<%@ page isELIgnored="false"%>

<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="title" value="Museum of Interesting Things - Home" scope="request"/>
<c:import url="includes/head.jsp"/>

<c:import url="includes/header.jsp"/>

<main id="main">

    <div class="container" id="content">
        <h1>Reoprts</h1>
        <form:form action="${pageContext.request.contextPath}/generateReports"
                   method="get"
                   class="mb-3"
        >
        <div class="list-group">
            <label class="list-group-item">
                <input class="form-check-input me-1" type="checkbox" name="reports" value="avgValues">
                Average items' worth value on each room
                <ul>
                    <li>
                        <small class="text-muted">Info on the room, the detailed location and the average value. </small>
                    </li>
                    <li>
                        <small class="text-muted">Sorted by the room and then the location</small>
                    </li>
                </ul>
            </label>
            <label class="list-group-item">
                <input class="form-check-input me-1" name="reports" type="checkbox" value="overallItemDays">
                Number of days artifact(s) have been on display in museum(overall)
                <ul>
                    <li>
                        <small class="text-muted">Days on display are calculated by finding the earliest date an item was on display and subtracting it from today’s date</small>
                    </li>
                    <li>
                        <small class="text-muted">If the item was lost, then the days on display are calculated by finding the earliest date an item was on display and subtracting it from the date it was lost.</small>
                    </li>
                </ul>
            </label>
            <label class="list-group-item">
                <input class="form-check-input me-1" name="reports" type="checkbox" value="CurrentItemsOnDisplays">
                Current items on display
                <ul>
                    <li>
                        <small class="text-muted">Lost items are not displayed.</small>
                    </li>
                    <li>
                        <small class="text-muted">If an item has moved, only the last location should be displayed</small>
                    </li>
                    <li>
                        <small class="text-muted">Sorted by the artifact and then the room</small>
                    </li>
                </ul>
            </label>
            <input type="submit" value="Generate reports">
        </div>
        </form:form>
    </div>
</main>