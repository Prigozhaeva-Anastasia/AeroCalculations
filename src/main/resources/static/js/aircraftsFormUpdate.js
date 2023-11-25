window.onload = function () {
    let airlines = document.querySelector("select");
    let form = document.getElementById('form');

    form.addEventListener('click', function (e) {
        let arr = [];
        for(let option of airlines.options) {
            arr.push(option.value);
            arr.push(option.text);
        }
        let airline = airlines.options[airlines.selectedIndex].value;
        localStorage.setItem('airlines', JSON.stringify(arr));
        localStorage.setItem('airline', airline);
    });

    if(localStorage.getItem('airlines')) {
        let array = JSON.parse(localStorage.getItem("airlines"));
        for(let i = 0; i < array.length; i++) {
            let option = document.createElement('option');
            option.value = array[i];
            option.text = array[i+1];
            if(option.value === localStorage.getItem('airline')) {
                option.selected = true;
            }
            ++i;
            document.getElementById("airlines").add(option);
        }
    }
}