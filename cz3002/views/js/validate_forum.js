function isCommentEmpty()
{
    var comment = document.getElementById("comment");
    if(comment.value === "")
    {
        alert("Comment is empty!");
        return false;
    }
    else
    {
        return true;
    }
}

function isThreadEmpty()
{
    var thread = document.getElementById("thread");
    if(thread.value === "")
    {
        alert("thread is empty!");
        return false;
    }
    else
    {
        return true;
    }
}
