var area_list;
var cca_list;
var subject_list;
var mrt_list;
var cl;

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

function AddCompareClicked(event)
{
    if (cl >= 3)
    {
        alert("Maxmimum 3 schools to compare!");
    }
    else
    {
        $.get("/AddToCompare/" + event.name, function(callback) {});
        event.className = "btn btn-danger";
        event.onclick = function(){ RmCompareClicked(this); };
        cl++;
    }
}

function RmCompareClicked(event)
{
    $.get("/RemoveFromCompare/" + event.name, function(callback) {});
    event.className = "btn btn-success";
    event.onclick = function(){ AddCompareClicked(this); };
    cl--;
}

$(document).ready(function()
{
    var areas = area_list.split(",");
    $("#area").autocomplete({
        source: areas
    });

    var ccas = cca_list.replace(/&amp;/g, "&");
    ccas = ccas.replace(/&#39;/g, "&").split(",");
    $("#cca").autocomplete({
        source: ccas
    });

    var subjects = subject_list.replace(/&amp;/g, "&");
    subjects = subjects.replace(/&#39;/g, "&").split(",");
    $("#subject").autocomplete({
        source: subjects
    });

    var mrts = mrt_list.split(",");
    $("#mrt").autocomplete({
        source: mrts
    });
});

function GetSecondarySchool(area_list, cca_list, subject_list, mrt_list)
{
    this.area_list = area_list;
    this.cca_list = cca_list;
    this.subject_list = subject_list;
    this.mrt_list = mrt_list;
}

function ValidateInput()
{
    var pat;

    var scoreB = true;
    var score = document.getElementById('score');
    if (score !== "")
    {
        pat = /[^0-9]/i;
        scoreB = CheckValue(score, pat);
    }

    var areaB = true;
    var area = document.getElementById('area');
    if (area !== "")
    {
        pat = /[^a-z ]/i;
        areaB = CheckValue(area, pat);
    }

    var ccaB = true;
    var cca = document.getElementById('cca');
    if (cca !== "")
    {
        pat = /[^a-z 0-9,()\/\-$:'*.&@!]/i;
        ccaB = CheckValue(cca, pat);
    }

    var subjectB = true;
    var subject = document.getElementById('subject');
    if (subject !== "")
    {
        pat = /[^a-z ,&\-()'123\/*]/i;
        subjectB = CheckValue(subject, pat);
    }

    var codeB = true;
    var code = document.getElementById('code');
    if (code !== "")
    {
        pat = /[^0-9]/i;
        codeB = CheckValue(code, pat);
    }

    var mrtB = true;
    var mrt = document.getElementById('mrt');
    if (mrt !== "")
    {
        pat = /[^a-z ]/i;
        mrtB = CheckValue(mrt, pat);
    }

    var busB = true;
    var bus = document.getElementById('bus');
    if (bus !== "")
    {
        pat = /[^a-z0-9]/i;
        busB = CheckValue(bus, pat);
    }

    if (!(scoreB && areaB && ccaB && subjectB && codeB && mrtB && busB))
    {
        alert("Invalid input. Enter again!");
        return false;
    }

    return true;
}

function CheckValue(item, pat)
{
    if (pat.test(item.value))
    {
        item.style.color = "#ff0000";
        return false;
    }

    item.style.color = "#000000";
    return true;
}

function CheckCompare(c)
{
    cl = c;
}
