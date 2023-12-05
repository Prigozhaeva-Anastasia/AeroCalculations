function checkSelectColor(select) {
    let selectedValue = select.value;
    if (selectedValue !== 'default') {
        select.style.color = "white";
    }
}
function clearLocalStorage() {
    localStorage.removeItem("roles");
    localStorage.removeItem("role");
}
window.onload = function () {
    let formEmp = document.getElementById('registrationForm');
    let roles = document.querySelector("select");

    formEmp.addEventListener('click', function (e) {
        let arr = [];
        for(let option of roles.options) {
            arr.push(option.value);
            arr.push(option.text);
        }
        let role = roles.options[roles.selectedIndex].value;
        localStorage.setItem('roles', JSON.stringify(arr));
        localStorage.setItem('role', role);
    });

    if(localStorage.getItem('roles')) {
        let array = JSON.parse(localStorage.getItem("roles"));
        for(let i = 0; i < array.length; i++) {
            let option = document.createElement('option');
            option.value = array[i];
            option.text = array[i+1];
            if(option.value === localStorage.getItem('role')) {
                option.selected = true;
            }
            ++i;
            document.getElementById("roles").add(option);
        }
    }
}