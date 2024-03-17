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
        error: () => {
            alert("При импорте произошла ошибка, проверьте xml-файл!");
        }
    });
}

function clearLocalStorage() {
    localStorage.removeItem("airlines");
    localStorage.removeItem("airline");
}
window.onload = function () {
    let formFilter = document.getElementById('formFilterAircraft');
    let filterForAircrafts = document.getElementById('filterForAircrafts');
    let airlines = document.querySelector("select");

    formFilter.addEventListener('click', function (e) {
        let arr = [];
        for(let option of airlines.options) {
            arr.push(option.value);
            arr.push(option.text);
        }
        let airline = airlines.options[airlines.selectedIndex].value;
        localStorage.setItem('airlinesFilter', JSON.stringify(arr));
        localStorage.setItem('airlineFilter', airline);
    });

    if(localStorage.getItem('airlinesFilter')) {
        let array = JSON.parse(localStorage.getItem("airlinesFilter"));
        for(let i = 0; i < array.length; i++) {
            let option = document.createElement('option');
            option.value = array[i];
            option.text = array[i+1];
            if(option.value === localStorage.getItem('airlineFilter')) {
                option.selected = true;
            }
            ++i;
            document.getElementById("airlines").add(option);
        }
    }

    filterForAircrafts.addEventListener('click', function (event) {
        formFilter.style.display = 'block';
    });
}