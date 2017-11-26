var list;

$(document).ready(function()
{
    var names = list.split(",");
    $("#navsearch").autocomplete({
        source: names
    });
});

function isEmpty()
{
    var navsearch = document.getElementById("navsearch");

    if (navsearch.value === "")
    {
        alert("Your search field is empty. Please try again.");
        return false;
    }
    else
        return true;
}

function GetSchool(l)
{
    list = l;
}
