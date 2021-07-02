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
            <div class="row row-cols-3 row-cols-lg-5 g-6 g-lg-4">
                <c:forEach items="${items}" var="item">
                        <div class="col">
                            <div class="p-3 border bg-light test-item card-img-top" >Item-image</div>
                            <h5 class="card-title">${item.name}</h5>

                            <a href="#" class="btn btn-primary">Go somewhere</a>
                        </div>
                </c:forEach>
            </div>

            <hr>

            <div class="container">
                <div class="row gx-5">
                    <c:forEach items="${items}" var="item">
                        <div class="col border">
                            <div class="p-3 border bg-light test-item card-img-top" >Item-image</div>
                            <h5 class="card-title">${item.name}</h5>

                            <a href="#" class="btn btn-primary">Go somewhere</a>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</main>