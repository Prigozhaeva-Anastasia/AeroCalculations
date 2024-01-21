window.onload = function () {
    let saveBtnInvoice = document.getElementById("saveBtnInvoice");
    saveBtnInvoice.addEventListener('click', function (e) {
        var formData = {
            invoiceNumber: $('#invoiceNumber').val(),
            invoiceCreationDate: $('#invoiceCreationDate').val(),
            currency: $('#currency').val(),
            flightId: $('#flightId').val(),
            airportServices: collectCheckedValues('airportServices'),
            groundHandlingServices: collectCheckedValues('groundHandlingServices')
        };
        $.ajax({
            type: 'POST',
            url: '/api/invoices/create',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            success: function (response) {
                window.location.href = '/invoices/confirmForm?invoiceNumber=' + $('#invoiceNumber').val();
            },
            error: function (error) {
                console.error('Произошла ошибка при отправке данных на сервер', error);
            }
        });
    });
    function collectCheckedValues(checkboxName) {
        return $('input[name="' + checkboxName + '"]:checked').map(function () {
            return {
                id: $(this).closest('.divForServ').find('input[name="id"]').val(),
                amount: $(this).closest('.divForServ').find('input[name="amount"]').val(),
                service: {
                    id: $(this).closest('.divForServ').find('input[name="serviceId"]').val(),
                    name: this.value
                }
            };
        }).get();
    }
}