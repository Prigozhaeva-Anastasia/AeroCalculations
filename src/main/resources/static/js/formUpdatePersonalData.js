window.onload=function() {
    let lastName = document.getElementById('lastName');
    let lastNameError = document.getElementById('lastNameError');
    let firstName = document.getElementById('firstName');
    let firstNameError = document.getElementById('firstNameError');
    let patronymic = document.getElementById('patronymic');
    let patronymicError = document.getElementById('patronymicError');
    let position = document.getElementById('patronymic');
    let positionError = document.getElementById('positionError');
    let phoneNumber = document.getElementById('phoneNumber');
    let phoneNumberError = document.getElementById('phoneNumberError');
    let password = document.getElementById('password');
    let passwordError = document.getElementById('passwordError');
    let passwordConfirmation = document.getElementById('passwordConfirmation');
    let passwordConfirmationError = document.getElementById('passwordConfirmationError');
    let editBtn = document.getElementById('editBtn');
    let changePassword = document.getElementById('changePassword');
    let hiddenTR1 = document.getElementById('hiddenTR1');
    let hiddenTR2 = document.getElementById('hiddenTR2');
    let hiddenTR3 = document.getElementById('hiddenTR3');
    let chooseFile = document.getElementById('imgBtn');
    let hidden = document.getElementById('hiddenLog');
    let img = document.getElementById('empImg');

    password.value = "#";
    passwordConfirmation.value = password.value;

    lastName.addEventListener('invalid', function(event){
        event.preventDefault();
        if ( ! event.target.validity.valid ) {
            lastNameError.style.display = 'block';
            lastName.classList.add('error');
        }
    });

    firstName.addEventListener('invalid', function(event){
        event.preventDefault();
        if ( ! event.target.validity.valid ) {
            firstNameError.style.display = 'block';
            firstName.classList.add('error');
        }
    });

    patronymic.addEventListener('invalid', function(event){
        event.preventDefault();
        if ( ! event.target.validity.valid ) {
            patronymicError.style.display = 'block';
            patronymic.classList.add('error');
        }
    });

    position.addEventListener('invalid', function(event){
        event.preventDefault();
        if ( ! event.target.validity.valid ) {
            positionError.style.display = 'block';
            position.classList.add('error');
        }
    });

    phoneNumber.addEventListener('invalid', function(event){
        event.preventDefault();
        if ( ! event.target.validity.valid ) {
            phoneNumberError.style.display = 'block';
            phoneNumber.classList.add('error');
        }
    });

    changePassword.addEventListener('click', function (event) {
        password.value = "";
        passwordConfirmation.value = "";
        hiddenTR1.style.visibility = 'visible';
        hiddenTR2.style.visibility = 'visible';
        hiddenTR3.style.display = 'none';
        editBtn.style.marginTop = "70px";
    });

    password.addEventListener('invalid', function(event){
        event.preventDefault();
        if ( ! event.target.validity.valid ) {
            passwordError.style.display = 'block';
            password.classList.add('error');
        }
    });

    editBtn.addEventListener('click', function (event) {
        if (password.value !== passwordConfirmation.value) {
            event.preventDefault();
            passwordConfirmationError.style.display = 'block';
            passwordConfirmation.classList.add('error');
        }
    });

    chooseFile.addEventListener('change', function (e) {
        let fileName = "/images/employees/" + chooseFile.files[0].name;
        img.src = fileName;
        hidden.value = "";
        localStorage.setItem('fileName', fileName);
    });

    if (localStorage.getItem('fileName')) {
        img.srcset = localStorage.getItem('fileName');
    }
}