function toggleTable()
{
    if(document.getElementById("advanced").style.display == "none")
    {
        document.getElementById("advanced").style.display = "";
        document.getElementById("advanced_submit").style.display = "inline";
        document.getElementById("simple_submit").style.display = "none";
        document.getElementById("ShowBtn").style.display = "none";
        document.getElementById("HideBtn").style.display = "inline";
    }
    else
    {
        document.getElementById("advanced").style.display = "none";
        document.getElementById("advanced_submit").style.display = "none";
        document.getElementById("simple_submit").style.display = "inline";
        document.getElementById("ShowBtn").style.display = "inline";
        document.getElementById("HideBtn").style.display = "none";
    }
}
