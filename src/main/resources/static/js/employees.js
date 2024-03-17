function clearLocalStorage() {
    localStorage.removeItem("roles");
    localStorage.removeItem("role");
}
window.onload = function () {
    let filterForEmployees = document.getElementById("filterForEmployees");
    let formFilter = document.getElementById('formFilter');
    let formBtns = document.getElementById('formBtnsEmp');
    let positions = document.getElementById('positions');
    localStorage.removeItem("positionsFilter");
    localStorage.removeItem("positionFilter");

    if(localStorage.getItem('positionsFilter')) {
        let array = JSON.parse(localStorage.getItem("positionsFilter"));
        for(let i = 0; i < array.length; i++) {
            let option = document.createElement('option');
            option.value = array[i];
            option.text = array[i+1];
            if(option.value === localStorage.getItem('positionFilter')) {
                option.selected = true;
            }
            ++i;
            document.getElementById("positions").add(option);
        }
    }

    filterForEmployees.addEventListener('click', function (event) {
        formFilter.style.display = 'block';
        formBtns.style.left = '-140px';
    });

    formFilter.addEventListener('click', function (e) {
        let arr = [];
        for(let option of positions.options) {
            arr.push(option.value);
            arr.push(option.text);
        }
        let position = positions.options[positions.selectedIndex].value;
        localStorage.setItem('positionsFilter', JSON.stringify(arr));
        localStorage.setItem('positionFilter', position);
    });
}