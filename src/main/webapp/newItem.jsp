<%@ page isELIgnored="false"%>

<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<c:set var="title" value="New Item" scope="request"/>
<c:import url="includes/head.jsp"/>

<c:import url="includes/header.jsp"/>

<main id="main">

    <div class="container" id="content">
        <h1>Add new Item</h1>
        <form>
            <div class="row mb-3">
                <label for="itemName" class="col-sm-2 col-form-label">Name</label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" name="itemName" id="itemName">
                </div>
            </div>
            <div class="row mb-3">
                <label for="itemDescription" class="col-sm-2 col-form-label">Description</label>
                <div class="col-sm-10">
                    <textarea class="form-control" name="itemDescription" id="itemDescription"></textarea>
                </div>
            </div>
            <div class="row mb-3">
                <label for="itemHowAcquired" class="col-sm-2 col-form-label">How acquired</label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" id="itemHowAcquired">
                </div>
            </div>
            <div class="row mb-3">
                <label for="dateAcquired" class="col-sm-2 col-form-label">Date acquired</label>
                <div class="col-sm-10">
                    <input type="date" class="form-control" id="dateAcquired">
                </div>
            </div>

            <div class="row mb-3">
                <label for="isLost"  class="col-sm-2 col-form-label">Is lost?</label>
                <div class="col-sm-10">
                    <input class="form-check-input" type="checkbox" id="isLost">
                </div>

                <fieldset class="row mb-1 ms-3 p-3"  id="lostItemFormLabels">
                    <legend class="col-form-label col-sm-2 pt-0">Lost item info</legend>
                <div class="row mb-3">
                    <label for="itemLostDesc" class="col-sm-2 col-form-label">Description (how item was lost/found)</label>
                    <div class="col-sm-10">
                        <textarea class="form-control" name="itemLostDesc" id="itemLostDesc"></textarea>
                    </div>
                </div>

                <div class="row mb-3">
                    <label for="dateLost" class="col-sm-2 col-form-label">Date lost</label>
                    <div class="col-sm-10">
                        <input type="date" class="form-control" id="dateLost">
                    </div>
                </div>
                </fieldset>


            </div>

            <div class="row mb-3">
                <label for="isMuseumItem" class="col-sm-2 col-form-label">Is museum item?</label>
                <div class="col-sm-10">
                    <input class="form-check-input" type="checkbox" id="isMuseumItem">
                </div>
            </div>

            <button type="submit" class="btn btn-primary">Submit</button>
        </form>
    </div>


</main>

<script>
    const showHideFormPartForLostItem = () => {
        let lostItemFieldset = document.querySelector('#lostItemFormLabels');
        //let showHideCheckbox = document.querySelector("#isLost");

        if(!lostItemFieldset.classList.contains("active")) {
            lostItemFieldset.classList.add("active");
        } else {
            lostItemFieldset.classList.remove("active");
        }
        console.log("js is launched...")
    }

    window.onload = () => {
        let showHideCheckbox = document.querySelector("#isLost");
        showHideCheckbox.addEventListener("click", showHideFormPartForLostItem);
    }
</script>