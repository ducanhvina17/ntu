var b;
var empty;

$(document).ready(function () {
    $("#email").keyup(checkForm);
    b = document.getElementById("resetBtn");
    b.disabled = true;
});

function checkForm() {
    var email = $("#email").val();

	if ((/[^a-z0-9@.]/i).test(email))
	{	
		$("#errorMsg").html("Invalid email!");
		return;
	}
	else {
		$("#errorMsg").html("");
	}

    if (email != "")
    {
		b.disabled = false;
    }
}