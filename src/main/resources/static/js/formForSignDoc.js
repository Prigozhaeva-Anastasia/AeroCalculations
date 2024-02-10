window.onload = function () {
    let invoiceDoc = document.getElementById("invoiceDoc");
    let invoiceDocError = document.getElementById("invoiceDocError");
    let paymentTermsDoc = document.getElementById("paymentTermsDoc");
    let paymentTermsDocError = document.getElementById("paymentTermsDocError");
    let formSignDocs = document.getElementById("formSignDocs");

    invoiceDoc.addEventListener('invalid', function(event){
        event.preventDefault();
        if ( ! event.target.validity.valid ) {
            invoiceDocError.style.display = 'block';
            invoiceDoc.classList.add('error');
        }
    });

    paymentTermsDoc.addEventListener('invalid', function(event){
        event.preventDefault();
        if ( ! event.target.validity.valid ) {
            paymentTermsDocError.style.display = 'block';
            paymentTermsDoc.classList.add('error');
        }
    });

    formSignDocs.addEventListener('click', function (event) {
        invoiceDoc.classList.remove('error');
        invoiceDocError.style.display = 'none';
        paymentTermsDoc.classList.remove('error');
        paymentTermsDocError.style.display = 'none';
    });
}