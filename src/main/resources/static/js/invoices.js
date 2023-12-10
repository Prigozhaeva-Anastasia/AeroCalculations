window.onload = function () {
    let paymentState = document.getElementById("paymentState");
    let arrPaymentStateHid = document.querySelectorAll(".paymentStateHid");
    let arrPaymentState = document.querySelectorAll(".paymentState")
    let formPaymentState = document.getElementById("formPaymentState");

    for (let i =0; i < arrPaymentStateHid.length; i++) {
        arrPaymentState[i].value = arrPaymentStateHid[i].value;
        if (arrPaymentState[i].value == "Оплачен") {
            arrPaymentState[i].style.backgroundColor = "#35D05F";
            arrPaymentState[i].style.color = "white";
        } else if (arrPaymentState[i].value == "Не оплачен") {
            arrPaymentState[i].style.backgroundColor = "rgba(272, 77, 77)";
            arrPaymentState[i].style.color = "white";
        } else {
            arrPaymentState[i].style.backgroundColor = "#FBF723";
            arrPaymentState[i].style.color = "white";
        }
    }

    arrPaymentState.forEach(function(state) {
        state.addEventListener('change', function() {
            if (state.value == "Оплачен") {
                state.style.backgroundColor = "#35D05F";
                state.style.color = "white";
            } else if (state.value == "Не оплачен") {
                state.style.backgroundColor = "rgba(272, 77, 77)";
                state.style.color = "white";
            } else {
                state.style.backgroundColor = "#FBF723";
                state.style.color = "white";
            }
            formPaymentState.submit();
        });
    });
}