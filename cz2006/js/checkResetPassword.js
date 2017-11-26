var b;
var empty;

$(document).ready(function () {
    $("#password, #confirmPassword").keyup(checkPasswordMatch);
    empty = true;
    b = document.getElementById("resetBtn");
    b.disabled = true;
});

function checkPasswordMatch() {
    var password = $("#password").val();
    var confirmPassword = $("#confirmPassword").val();

    if (password != "" && confirmPassword != "")
    {
        if (password != confirmPassword)
        {
            $("#errorMsg").html("Passwords do not match!");
            b.disabled = true;
        }
        else
        {
            $("#errorMsg").html("");
			b.disabled = false;
        }
    }
}