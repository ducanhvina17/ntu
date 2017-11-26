var b;

$(document).ready(function () {
    $("#username, #password").keyup(checkForm);
    empty = true;
    b = document.getElementById("loginBtn");
    b.disabled = true;
});

function checkForm() {
    var username = $("#username").val();
    var password = $("#password").val();

    if ((/[^a-z0-9@.]/i).test(username))
    {
        $("#errorMsg").html("Invalid username!");
        b.disabled = true;
    }
    else
    {
        $("#errorMsg").html("");
        b.disabled = username == "" || password == "";
    }

}

function isEmpty()
{
    var schoolname = document.getElementById("schoolname");
    if(schoolname.value == "")
    {
        alert("Your search field is empty. Please try again.");
        return false;
    }
    else
    {
        return true;
    }
}
