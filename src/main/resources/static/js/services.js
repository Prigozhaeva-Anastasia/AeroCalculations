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
            alert("��� ������� ��������� ������, ��������� xml-����!");
        }
    });
}

window.onload = function () {
    let filterForServices = document.getElementById('filterForServices');
    let formFilter = document.getElementById('formFilterForServices');
    filterForServices.addEventListener('click', function (event) {
        formFilter.style.display = 'block';
    });
}