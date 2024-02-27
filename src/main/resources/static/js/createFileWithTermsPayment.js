window.onload = function () {
    let finesAndPenalties = document.getElementById("finesAndPenalties");
    let finesAndPenaltiesError = document.getElementById("finesAndPenaltiesError");
    let dueDate = document.getElementById("dueDate");
    let dueDateError = document.getElementById("dueDateError");
    let FL = document.getElementById("FL");
    let FLError = document.getElementById("FLError");
    let phoneNumber = document.getElementById("phoneNumber");
    let phoneNumberError = document.getElementById("phoneNumberError");
    let formCreateFile = document.getElementById("formCreateFile");
    let invoiceNumber = document.getElementById("invoiceNumber");

    finesAndPenalties.oninput = function () {
        this.value = this.value.replace(/(\d+\.\d\d\d+)|[А-Яа-яA-za-z]/g, '');
    }

    finesAndPenalties.addEventListener('invalid', function (event) {
        event.preventDefault();
        if ( ! event.target.validity.valid ) {
            finesAndPenaltiesError.style.display = 'block';
            finesAndPenalties.classList.add('error');
        }
    });

    dueDate.addEventListener('invalid', function(event){
        event.preventDefault();
        if ( ! event.target.validity.valid ) {
            dueDateError.style.display = 'block';
            dueDate.classList.add('error');
        }
    });

    FL.addEventListener('invalid', function(event){
        event.preventDefault();
        if ( ! event.target.validity.valid ) {
            FLError.style.display = 'block';
            FL.classList.add('error');
        }
    });

    phoneNumber.addEventListener('invalid', function(event){
        event.preventDefault();
        if ( ! event.target.validity.valid ) {
            phoneNumberError.style.display = 'block';
            phoneNumber.classList.add('error');
        }
    });

    formCreateFile.addEventListener('click', function (event) {
        finesAndPenaltiesError.style.display = 'none';
        finesAndPenalties.classList.remove("error");
        dueDateError.style.display = 'none';
        dueDate.classList.remove("error");
        FLError.style.display = 'none';
        FL.classList.remove("error");
        phoneNumberError.style.display = 'none';
        phoneNumber.classList.remove("error");
    });

    formCreateFile.addEventListener('submit', function (event) {
        let alertMessage = 'Документ с условиями и сроками оплаты счета был создан и сохранен в pdf-формате: D:/diploma/проект/pdf/payment_' + invoiceNumber.value + '.pdf';
        alert(alertMessage);
    });
}