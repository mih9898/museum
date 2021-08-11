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
            <a href="images/food.jpg"><img id="museumImg" alt="full bucket of fresh vegetables" src="https://i.etsystatic.com/15568096/r/il/e1ced4/2010945485/il_794xN.2010945485_a6x4.jpg"></a>
            <p class="gardenDescript"><span class="fancyLetter">B</span>eautiful gardens are at the heart of any community. Willow Gardens'
                provide an entry point for individuals and households to get involved in the whole project.
                Community gardeners use the land to grow their own food, which has a positive impact on their household food security.
                We provide recreational outdoor activity as well as a fun and friendly environment for people of all ages, incomes, and cultures
                to work the land together and share their gardening knowledge and ideas.
                Gardeners assist in site maintenance and management through volunteer commitments.
            </p>
        </div>
        <hr>

        <div id="gardeningTipsComplementary">
            <h3>Already has a plot?</h3>
            <p id="asideGardenTips">Want to start digging but don't know where to begin? <a href="gardeningTips.html"> Use this beginner's guide</a>
                to gardening for the answers to your gardening questions. Keep reading for all of the basic gardening tips and tricks you'll need to get started.
            </p>
        </div>
        <hr>
        <p>What do you receive if you buy a plot?</p>
        <ul>
            <li>The county provides the land to rent, liability insurance, and water, which is made available throughout the summer.</li>
            <li>Upgraded water lines are maintained.</li>
            <li>Grass borders are mowed to prevent rodent habitat.</li>
            <li>A Port-a-Potty is provided for your convenience.</li>
            <li>Free garden seeds, teaching materials, and advice is provided from mentors in our newsletters.</li>
            <li>The garden is tilled in the fall and early spring as soil conditions allow.</li>
            <li>The plots get measured, marked, and monitored.</li>
        </ul>
        <a href="buyPlot.html">Click here to buy a plot!</a>

    </div>
</main>
<c:import url="includes/footer.jsp"/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-U1DAWAznBHeqEIlVSCgzq+c9gqGAJn5c/t99JyeKa9xxaYpSvHU5awsuZVVFIhvj" crossorigin="anonymous"></script>
