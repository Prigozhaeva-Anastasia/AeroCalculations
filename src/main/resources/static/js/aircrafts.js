function getPathOfImportFile() {
    let chooseFile = document.getElementById('importBtn');
    let pathOfImportFile = "D:/diploma/import/" + chooseFile.files[0].name;
    $.ajax({
        url: "/api/aircrafts/import",
        type: 'POST',
        data: {"path": pathOfImportFile},
        success: () => {
            location.reload()
        },
        error: (err) => {
            alert(err);
        }
    });
}