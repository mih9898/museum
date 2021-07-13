<%@ page isELIgnored="false"%>

<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<c:set var="title" value="Museum of Interesting Things - Home" scope="request"/>
<c:import url="includes/head.jsp"/>

<c:import url="includes/header.jsp"/>

<main id="main">

    <div class="container" id="content">
        <h1>Home</h1>
        <h2>Items</h2>


            <div class="container">
                <div class="row gx-5">
                    <c:forEach items="${items}" var="item">
                        <div class="col-3 border p-3">
                            <div class="p-3 bg-light test-item card-img-top" >Item-image</div>
                            <h5 class="bold">${item.name}</h5>
                            <div class="row">
                                <div class="col">
                                    <a href="${pageContext.request.contextPath}/item?id=${item.id}">
                                        <button type="button" class="btn btn-outline-primary">More Info</button>
                                    </a>
                                </div>
                                <div class="col">
                                    <form action="deleteItem" method="post">
                                        <button type="submit" class="btn btn-outline-primary">Delete</button>
                                        <input type="hidden" name="itemId" value="${item.id}">
                                    </form>
<%--                                    <a href="${pageContext.request.contextPath}/deleteItem?id=${item.id}">--%>

                                    </a>
                                </div>
                            </div>
<%--                            <a href="${pageContext.request.contextPath}/item?id=${item.id}">--%>
<%--                                <button type="button" class="btn btn-outline-primary">More Info</button>--%>
<%--                            </a>--%>
<%--                            <a href="${pageContext.request.contextPath}/item?id=${item.id}">--%>
<%--                                <button type="button" class="btn btn-outline-primary">Delete</button>--%>
<%--                            </a>--%>
<%--                            TODO; delete btn + related controller?--%>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</main>