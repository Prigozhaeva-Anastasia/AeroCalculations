function clearLocalStorage() {
    localStorage.removeItem("aircrafts");
    localStorage.removeItem("aircraft");
    localStorage.removeItem("depCities");
    localStorage.removeItem("depCity");
    localStorage.removeItem("arrCities");
    localStorage.removeItem("arrCity");
}
function getPathOfImportFile() {
    let chooseFile = document.getElementById('importBtn');
    let pathOfImportFile = "D:/diploma/import/" + chooseFile.files[0].name;

    $.ajax({
        url: "/api/flights/import",
        type: 'POST',
        data: {"path": pathOfImportFile},
        success: () => {
            location.reload()
        },
        error: () => {
            alert("При импорте произошла ошибка, проверьте xml-файл!");
        }
    });
}
window.onload = function () {
    let filterForFlights = document.getElementById('filterForFlights');
    let formFilterForFlights = document.getElementById('formFilterForFlights');
    let formBtns = document.getElementById('formBtns');
    filterForFlights.addEventListener('click', function (event) {
        formFilterForFlights.style.display = 'block';
        formBtns.style.left = '-20px';
    });
}