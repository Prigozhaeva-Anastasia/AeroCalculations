function getPathOfImportFile() {
    let chooseFile = document.getElementById('importBtn');
    let pathOfImportFile = "D:/diploma/import/" + chooseFile.files[0].name;

    $.ajax({
        url: "/api/services/import",
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
    let filterForServices = document.getElementById('filterForServices');
    let formFilter = document.getElementById('formFilter');
    let formBtns = document.getElementById('formBtns');
    filterForServices.addEventListener('click', function (event) {
        formFilter.style.display = 'block';
        formBtns.style.left = '-20px';
    });
}