function getPathOfImportFile() {
    let chooseFile = document.getElementById('importBtn');
    let pathOfImportFile = "D:/diploma/import/" + chooseFile.files[0].name;

    $.ajax({
        url: "/api/providedServices/import",
        type: 'POST',
        data: {"path": pathOfImportFile},
        success: function (response) {
            let str =  window.location.href;
            window.location.href = str;
        },
        error: () => {
            alert("При импорте произошла ошибка, проверьте xml-файл!");
        }
    });
}