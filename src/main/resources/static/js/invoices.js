function saveMsg(element) {
    let invoiceNumber = element.getAttribute('data-invoice-number');
    let alertMessage = 'Счет был сохранен в pdf-формате: D:/diploma/проект/pdf/' + invoiceNumber + '.pdf';
    alert(alertMessage);
}

window.onload = function () {
    let arrPaymentStateHid = document.querySelectorAll(".paymentStateHid");
    let filterForInvoices = document.getElementById('filterForInvoices');
    let formFilterForInvoices = document.getElementById('formFilterForInvoices');
    let formBtnsInvoice = document.getElementById('formBtnsInvoice');
    let statuses = document.getElementsByClassName('pState');
    let createFiles = document.getElementsByClassName('createFile');

    for (let i = 0; i < arrPaymentStateHid.length; i++) {
        let formPaymentState = document.getElementById("formPaymentState_" + i);
        let state = document.getElementById("paymentState_" + i);
        state.value = arrPaymentStateHid[i].defaultValue;
        if (state.value == "Оплачен") {
            state.style.backgroundColor = "#35D05F";
            state.style.color = "white";
        } else if (state.value == "Не оплачен") {
            state.style.backgroundColor = "#8f8c8c";
            state.style.color = "white";
        } else if (state.value == "Отправлен") {
            state.style.backgroundColor = "#FBF723";
            state.style.color = "white";
        } else if (state.value == "Просрочен") {
            state.style.backgroundColor = "rgba(272, 77, 77)";
            state.style.color = "white";
        }
        state.addEventListener('change', function () {
            formPaymentState.submit();
        });
    }
    for (let i = 0; i < statuses.length; i++) {
        if (statuses[i].value !== "Не оплачен") {
            createFiles[i].disabled = true;
        }
    }
    filterForInvoices.addEventListener('click', function (event) {
        formFilterForInvoices.style.display = 'block';
        formBtnsInvoice.style.left = '-145px';
    });
};