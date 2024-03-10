window.onload = function () {
    let unreadable = document.getElementById("unreadable");
    let readable = document.getElementById("readable");
    let unreadableLabel = document.getElementById("unreadableLabel");
    let readableLabel = document.getElementById("readableLabel");
    let confirmMessage = document.getElementById("confirmMessage");
    let doneIcon = document.getElementById("doneIcon");
    let notDoneIcon = document.getElementById("notDoneIcon");

    if (window.location.href.includes("unread")) {
        unreadable.style.backgroundColor = "black";
        unreadableLabel.style.color = "#fff";
    } else {
        readable.style.backgroundColor = "black";
        readableLabel.style.color = "#fff";
    }

    let o = confirmMessage.innerHTML;
    if (o == "Документ подтвержден") {
        confirmMessage.style.visibility = 'visible';
        confirmMessage.style.color = 'green';
        doneIcon.style.visibility = 'visible';
    } else if (o == "Документ не подтвержден") {
        confirmMessage.style.visibility = 'visible';
        confirmMessage.style.color = 'red';
        notDoneIcon.style.visibility = 'visible';
    }
}