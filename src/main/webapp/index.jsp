<%@ page isELIgnored="false"%>

<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<c:set var="title" value="Museum of Interesting Things - Home" scope="request"/>
<c:import url="includes/head.jsp"/>

<c:import url="includes/header.jsp"/>

<main id="main">

    <div class="container" id="content">
        <h2>There are plenty of very nice garden spots available.</h2>
        <div class="test">
            <img id="museumImg" alt="fgrzegorz brzeczyszczykiewicz" src="https://penobscotmarinemuseum.org/welcome/wp-content/uploads/2018/03/6-28.jpg">
            <p class="museumDesc"> A beautiful place where you will discover something new -- the Museum of Interesting Things. This place was opened almost one hundred years
                ago and is run by family Brzeczyszczykiewicz. In 1919, a founder Grzegorz wanted to create a place where people would fall into childhood,
                a place where any man would be flamed by finding new fascinating items that he never has seen. That's how he founded the Museum where all
                kinds of strange curiosities and rarities would be displayed for curious spectators. And Grzegorz Brzeszczykiewwicz's idea and dream are
                working to this day. The museum reveals and extends man's imagination. The museum offers different thematic rooms where each one would find the
                new fascinating discoveries.
            </p>
        </div>
        <hr>

        <div id="gardeningTipsComplementary">
            <h3>Has curious odd item and want to contribute to museum?</h3>
            <p id="asideGardenTips"> Bring it to us! You can extend the variety of our displays/items by bringing unusual items.
                Even if you don't know what is item for, still bring it. We can appraise it and if it is something unusual then we can even buy it!
            </p>
        </div>

    </div>
</main>
<c:import url="includes/footer.jsp"/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-U1DAWAznBHeqEIlVSCgzq+c9gqGAJn5c/t99JyeKa9xxaYpSvHU5awsuZVVFIhvj" crossorigin="anonymous"></script>
