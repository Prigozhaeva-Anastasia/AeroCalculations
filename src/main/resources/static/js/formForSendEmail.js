window.onload = function () {
    let toWhom = document.getElementById("toWhom");
    let toWhomError = document.getElementById("toWhomError");
    let formForEmail = document.getElementById("formForEmail");
    let errorLblForEmail = document.getElementById("notExistentRecipientEmail");

    toWhom.addEventListener('invalid', function(event){
        event.preventDefault();
        if ( ! event.target.validity.valid ) {
            toWhomError.style.display = 'block';
            toWhom.classList.add('error');
        }
    });

    formForEmail.addEventListener('click', function (event){
        toWhom.classList.remove('error');
        toWhomError.style.display = 'none';
        errorLblForEmail.style.display = 'none';
    });

    formForEmail.addEventListener('submit', function (event){
        let email = document.getElementById("toWhom");
        alert("Сообщение было отправлено по адресу " + email.value);
    });
}