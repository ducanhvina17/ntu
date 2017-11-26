var b;
var empty;

$(document).ready(function ()
{
    $("#email, #username").keyup(checkForm);
    $("#password, #confirmPassword").keyup(checkPasswordMatch);
    empty = true;
    b = document.getElementById("registerBtn");
    b.disabled = true;
});

function checkForm()
{
    var email = $("#email").val();
    var username = $("#username").val();

    if ((/[^a-z0-9@.]/i).test(email))
    {
        $("#errorMsg").html("Invalid email!");
        b.disabled = true;
    }
    else if ((/[^a-z0-9]/i).test(username))
    {
        $("#errorMsg").html("Invalid username!");
        b.disabled = true;
    }
    else
    {
        $("#errorMsg").html("");

        if (email !== "" && username !== "")
        {
            empty = false;
            checkPasswordMatch();
        }
        else
        {
            empty = true;
            b.disabled = true;
        }
    }
}

function checkPasswordMatch()
{
    var password = $("#password").val();
    var confirmPassword = $("#confirmPassword").val();

    if (password !== "" && confirmPassword !== "")
    {
        if (password != confirmPassword)
        {
            $("#errorMsg").html("Passwords do not match!");
            b.disabled = true;
        }
        else
        {
            var email = $("#email").val();
            var username = $("#username").val();
            $("#errorMsg").html("");
            if (!empty)
                b.disabled = false;
        }
    }
}

function isEmpty()
{
    var schoolname = document.getElementById("schoolname");

    if(schoolname.value === "")
    {
        alert("Your search field is empty. Please try again.");
        return false;
    }
    else
        return true;
}
