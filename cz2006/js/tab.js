function onload(){
	transitMode("DRIVING");
}

function transitMode(mode) {
    var i;
    var x = document.getElementsByClassName("mode");
    for (i = 0; i < x.length; i++) {
       x[i].style.display = "none";
    }
    document.getElementById(mode).style.display = "block";
}