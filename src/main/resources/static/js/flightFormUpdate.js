window.onload = function () {
    let aicrafts = document.getElementById("aircrafts");
    let depCities = document.getElementById("depCity");
    let arrCities = document.getElementById("arrCity");
    let form = document.getElementById('form');

    form.addEventListener('click', function (e) {
        let arr1 = [];
        for(let option of aicrafts.options) {
            arr1.push(option.value);
            arr1.push(option.text);
        }
        let arr2 = [];
        for(let option of depCities.options) {
            arr2.push(option.value);
            arr2.push(option.text);
        }
        let arr3 = [];
        for(let option of arrCities.options) {
            arr3.push(option.value);
            arr3.push(option.text);
        }
        let aircraft = aicrafts.options[aicrafts.selectedIndex].value;
        let arrCity = arrCities.options[arrCities.selectedIndex].value;
        let depCity = depCities.options[depCities.selectedIndex].value;
        localStorage.setItem('aircrafts', JSON.stringify(arr1));
        localStorage.setItem('aircraft', aircraft);
        localStorage.setItem('depCities', JSON.stringify(arr2));
        localStorage.setItem('depCity', depCity);
        localStorage.setItem('arrCities', JSON.stringify(arr3));
        localStorage.setItem('arrCity', arrCity);
    });

    if(localStorage.getItem('aircrafts')) {
        let array = JSON.parse(localStorage.getItem("aircrafts"));
        for(let i = 0; i < array.length; i++) {
            let option = document.createElement('option');
            option.value = array[i];
            option.text = array[i+1];
            if(option.value === localStorage.getItem('aircraft')) {
                option.selected = true;
            }
            ++i;
            document.getElementById("aircrafts").add(option);
        }
    }
    if(localStorage.getItem('depCities')) {
        let array = JSON.parse(localStorage.getItem("depCities"));
        for(let i = 0; i < array.length; i++) {
            let option = document.createElement('option');
            option.value = array[i];
            option.text = array[i+1];
            if(option.value === localStorage.getItem('depCity')) {
                option.selected = true;
            }
            ++i;
            document.getElementById("depCity").add(option);
        }
    }
    if(localStorage.getItem('arrCities')) {
        let array = JSON.parse(localStorage.getItem("arrCities"));
        for(let i = 0; i < array.length; i++) {
            let option = document.createElement('option');
            option.value = array[i];
            option.text = array[i+1];
            if(option.value === localStorage.getItem('arrCity')) {
                option.selected = true;
            }
            ++i;
            document.getElementById("arrCity").add(option);
        }
    }
}