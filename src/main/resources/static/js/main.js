window.onload = function () {
    let yearInput = document.getElementById('year');
    let yearForAirlines = document.getElementById('yearForAirlines');

    yearInput.addEventListener('input', function () {
        let yearValue = yearInput.value;
        for (let i = 1; i <= 12; i++) {
            let linkId = 'monthLink' + i;
            let monthLink = document.getElementById(linkId);
            let newHref = '/reports/flightDynamics?year=' + yearValue + '&month=' + i;
            monthLink.href = newHref;
        }
    });
    yearForAirlines.addEventListener('input', function () {
        let yearValue = yearForAirlines.value;
        for (let i = 1; i <= 12; i++) {
            let linkId = 'month' + i;
            let monthLink = document.getElementById(linkId);
            let newHref = '/reports/changesInNumOfAirlinesDynamics?year=' + yearValue + '&month=' + i;
            monthLink.href = newHref;
        }
    });
}