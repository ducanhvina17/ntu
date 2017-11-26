function ValidatePrimary()
{
	var primaryareaB = true;
	var primaryarea = document.getElementById('primaryarea');
	if (primaryarea !== "")
	{
		var pat = /[^a-z ]/i;
		primaryareaB = CheckValue(primaryarea, pat);
	}

	var primaryccaB = true;
	var primarycca = document.getElementById('primarycca');
	if (primarycca !== "")
	{
		var pat = /[^a-z 0-9,()\/\-$:'*.&@!]/i;
		primaryccaB = CheckValue(primarycca, pat);
	}

	var primarysubjectB = true;
	var primarysubject = document.getElementById('primarysubject');
	if (primarysubject !== "")
	{
		var pat = /[^a-z ,&\-()'123\/*]/i;
		primarysubjectB = CheckValue(primarysubject, pat);
	}

	var primarycodeB = true;
	var primarycode = document.getElementById('primarycode');
	if (primarycode !== "")
	{
		var pat = /[^0-9]/i;
		primarycodeB = CheckValue(primarycode, pat);
	}

	var primarymrtB = true;
	var primarymrt = document.getElementById('primarymrt');
	if (primarymrt !== "")
	{
		var pat = /[^a-z ]/i;
		primarymrtB = CheckValue(primarymrt, pat);
	}

	var primarybusB = true;
	var primarybus = document.getElementById('primarybus');
	if (primarybus !== "")
	{
		var pat = /[^a-z0-9]/i;
		primarybusB = CheckValue(primarybus, pat);
	}

	if (!(primaryareaB && primaryccaB && primarysubjectB && primarycodeB && primarymrtB && primarybusB))
	{
		alert("Invalid input. Enter again!");
		return false;
	}

	return true;
}

function ValidateSecondary()
{
	var secondaryscoreB = true;
	var secondaryscore = document.getElementById('secondaryscore');
	if (secondaryscore !== "")
	{
		var pat = /[^0-9]/i;
		secondaryscoreB = CheckValue(secondaryscore, pat);
	}

	var secondaryareaB = true;
	var secondaryarea = document.getElementById('secondaryarea');
	if (secondaryarea !== "")
	{
		var pat = /[^a-z ]/i;
		secondaryareaB = CheckValue(secondaryarea, pat);
	}

	var secondaryccaB = true;
	var secondarycca = document.getElementById('secondarycca');
	if (secondarycca !== "")
	{
		var pat = /[^a-z 0-9,()\/\-$:'*.&@!]/i;
		secondaryccaB = CheckValue(secondarycca, pat);
	}

	var secondarysubjectB = true;
	var secondarysubject = document.getElementById('secondarysubject');
	if (secondarysubject !== "")
	{
		var pat = /[^a-z ,&\-()'123\/*]/i;
		secondarysubjectB = CheckValue(secondarysubject, pat);
	}

	var secondarycodeB = true;
	var secondarycode = document.getElementById('secondarycode');
	if (secondarycode !== "")
	{
		var pat = /[^0-9]/i;
		secondarycodeB = CheckValue(secondarycode, pat);
	}

	var secondarymrtB = true;
	var secondarymrt = document.getElementById('secondarymrt');
	if (secondarymrt !== "")
	{
		var pat = /[^a-z ]/i;
		secondarymrtB = CheckValue(secondarymrt, pat);
	}

	var secondarybusB = true;
	var secondarybus = document.getElementById('secondarybus');
	if (secondarybus !== "")
	{
		var pat = /[^a-z0-9]/i;
		secondarybusB = CheckValue(secondarybus, pat);
	}

	if (!(secondaryscoreB && secondaryareaB && secondaryccaB && secondarysubjectB && secondarycodeB && secondarymrtB && secondarybusB))
	{
		alert("Invalid input. Enter again!");
		return false;
	}

	return true;
}

function ValidatePoly()
{
	var polycofB = true;
	var polycof = document.getElementById('polycof');
	if (polycof !== "")
	{
		var pat = /[^0-9]/i;
		polycofB = CheckValue(polycof, pat);
	}

	var polyareaB = true;
	var polyarea = document.getElementById('polyarea');
	if (polyarea !== "")
	{
		var pat = /[^a-z ]/i;
		polyareaB = CheckValue(polyarea, pat);
	}

	var polycclusterB = true;
	var polyccluster = document.getElementById('polyccluster');
	if (polyccluster !== "")
	{
		var pat = /[^a-z &]/i;
		polycclusterB = CheckValue(polyccluster, pat);
	}

	var polyctitleB = true;
	var polyctitle = document.getElementById('polyctitle');
	if (polyctitle !== "")
	{
		var pat = /[^a-z &\/()\-,]/i;
		polyctitleB = CheckValue(polyctitle, pat);
	}

	var polycodeB = true;
	var polycode = document.getElementById('polycode');
	if (polycode !== "")
	{
		var pat = /[^0-9]/i;
		polycodeB = CheckValue(polycode, pat);
	}

	var polymrtB = true;
	var polymrt = document.getElementById('polymrt');
	if (polymrt !== "")
	{
		var pat = /[^a-z ]/i;
		polymrtB = CheckValue(polymrt, pat);
	}

	var polybusB = true;
	var polybus = document.getElementById('polybus');
	if (polybus !== "")
	{
		var pat = /[^a-z0-9]/i;
		polybusB = CheckValue(polybus, pat);
	}

	if (!(polycofB && polyareaB && polycclusterB && polyctitleB && polycodeB && polymrtB && polybusB))
	{
		alert("Invalid input. Enter again!");
		return false;
	}
	else if (item == "polycof" && !(item.value >= 0 && item.value <= 45))
    {
	    alert("Cut Off Point must be between 0 and 45!");
		item.style.color = "#ff0000";
		return false;
    }

	return true;
}

function ValidateJC()
{
	var jccopB = true;
	var jccop = document.getElementById('jccop');
	if (jccop !== "")
	{
		var pat = /[^0-9]/i;
		jccopB = CheckValue(jccop, pat);
	}

	var jcareaB = true;
	var jcarea = document.getElementById('jcarea');
	if (jcarea !== "")
	{
		var pat = /[^a-z ]/i;
		jcareaB = CheckValue(jcarea, pat);
	}

	var jcsubjectB = true;
	var jcsubject = document.getElementById('jcsubject');
	if (jcsubject !== "")
	{
		var pat = /[^a-z ,&\-()'123\/*]/i;
		jcsubjectB = CheckValue(jcsubject, pat);
	}

	var jccodeB = true;
	var jccode = document.getElementById('jccode');
	if (jccode !== "")
	{
		var pat = /[^0-9]/i;
		jccodeB = CheckValue(jccode, pat);
	}

	var jcmrtB = true;
	var jcmrt = document.getElementById('jcmrt');
	if (jcmrt !== "")
	{
		var pat = /[^a-z ]/i;
		jcmrtB = CheckValue(jcmrt, pat);
	}

	var jcbusB = true;
	var jcbus = document.getElementById('jcbus');
	if (jcbus !== "")
	{
		var pat = /[^a-z0-9]/i;
		jcbusB = CheckValue(jcbus, pat);
	}

	if (!(jccopB && jcarea && jcsubjectB && jccodeB && jcmrtB && jcbusB))
	{
		alert("Invalid input. Enter again!");
		return false;
	}

	return true;
}

function ValidateITE()
{
	var iteenglishB = true;
	var iteenglish = document.getElementById('iteenglish');
	if (iteenglish !== "")
	{
		var pat = /[^0-9]/i;
		iteenglishB = CheckValue(iteenglish, pat);
	}

	var itemathB = true;
	var itemath = document.getElementById('itemath');
	if (itemath !== "")
	{
		var pat = /[^0-9]/i;
		itemathB = CheckValue(itemath, pat);
	}

	var itecodeB = true;
	var itecode = document.getElementById('itecode');
	if (itecode !== "")
	{
		var pat = /[^0-9]/i;
		itecodeB = CheckValue(itecode, pat);
	}

	var itemrtB = true;
	var itemrt = document.getElementById('itemrt');
	if (itemrt !== "")
	{
		var pat = /[^a-z ]/i;
		itemrtB = CheckValue(itemrt, pat);
	}

	var itebusB = true;
	var itebus = document.getElementById('itebus');
	if (itebus !== "")
	{
		var pat = /[^a-z0-9]/i;
		itebusB = CheckValue(itebus, pat);
	}

	if (!(iteenglishB && itemathB && itecodeB && itemrtB && itebusB))
	{
		alert("Invalid input. Enter again!");
		return false;
	}

	return true;
}

function ValidateUni()
{
	var uninameB = true;
	var uniname = document.getElementById('uniname');
	if (uniname !== "")
	{
		var pat = /[^a-z ]/i;
		uninameB = CheckValue(uniname, pat);
	}

	var unisubjectB = true;
	var unisubject = document.getElementById('unisubject');
	if (unisubject !== "")
	{
		var pat = /[^a-z ,&\-()'123\/*]/i;
		unisubjectB = CheckValue(unisubject, pat);
	}

	var uniareaB = true;
	var uniarea = document.getElementById('uniarea');
	if (uniarea !== "")
	{
		var pat = /[^a-z ]/i;
		uniareaB = CheckValue(uniarea, pat);
	}

	var unicodeB = true;
	var unicode = document.getElementById('unicode');
	if (unicode !== "")
	{
		var pat = /[^0-9]/i;
		unicodeB = CheckValue(unicode, pat);
	}

	var unimrtB = true;
	var unimrt = document.getElementById('unimrt');
	if (unimrt !== "")
	{
		var pat = /[^a-z ]/i;
		unimrtB = CheckValue(unimrt, pat);
	}

	var unibusB = true;
	var unibus = document.getElementById('unibus');
	if (unibus !== "")
	{
		var pat = /[^a-z0-9]/i;
		unibusB = CheckValue(unibus, pat);
	}

	if (!(uninameB && unisubjectB && uniareaB && unicodeB && unimrtB && unibusB))
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