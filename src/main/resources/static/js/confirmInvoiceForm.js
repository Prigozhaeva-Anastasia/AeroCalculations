function saveMsg() {
    let form = document.getElementById('invoiceForm');
    let invoiceNumber = document.getElementById('invoiceNumber');
    let alertMessage = 'Счет был сохранен в pdf-формате: D:/diploma/проект/pdf/' + invoiceNumber.value + '.pdf';
    alert(alertMessage);
    form.submit();
}