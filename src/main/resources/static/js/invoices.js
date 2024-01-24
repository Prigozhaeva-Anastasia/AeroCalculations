window.onload = function () {
    let arrPaymentStateHid = document.querySelectorAll(".paymentStateHid");
    for (let i = 0; i < arrPaymentStateHid.length; i++) {
        let formPaymentState = document.getElementById("formPaymentState_" + i);
        let state = document.getElementById("paymentState_" + i);
        state.value = arrPaymentStateHid[i].defaultValue;
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
        state.addEventListener('change', function () {
            formPaymentState.submit();
        });
    }
};