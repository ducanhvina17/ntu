var b;

$(document).ready(function () {
    $("#username, #password").keyup(checkForm);
    empty = true;
    b = document.getElementById("registerBtn");
    b.disabled = true;
});

function checkForm() {
    var username = $("#username").val();
    var password = $("#password").val();

    if (username != "" && password != "")
        b.disabled = false;
    else
        b.disabled = true;
}