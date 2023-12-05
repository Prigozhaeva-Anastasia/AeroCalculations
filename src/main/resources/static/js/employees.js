function clearLocalStorage() {
    localStorage.removeItem("roles");
    localStorage.removeItem("role");
}
window.onload = function () {
    let filterForEmployees = document.getElementById("filterForEmployees");
    let formFilter = document.getElementById('formFilter');
    let formBtns = document.getElementById('formBtnsEmp');

    filterForEmployees.addEventListener('click', function (event) {
        formFilter.style.display = 'block';
        formBtns.style.left = '-140px';
    });
}