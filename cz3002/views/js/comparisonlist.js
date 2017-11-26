function FavClicked(event)
{
    $.get("/AddToFav/" + event.name, function(callback) {});
    event.className = "btn btn-danger";
    event.onclick = function(){ UnfavClicked(this); };
}

function UnfavClicked(event)
{
    $.get("/RemoveFromFav/" + event.name, function(callback) {});
    event.className = "btn btn-success";
    event.onclick = function(){ FavClicked(this); };
}
