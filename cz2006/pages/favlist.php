<?php include_once('header.php') ?>
<!DOCTYPE html>

<html>
<head>
<META NAME="GENERATOR" Content="Microsoft Visual Studio">
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=UTF-8">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.3/css/font-awesome.min.css">
<TITLE>Favorite List</TITLE>
<style>


	button.accordion{
		background-color: #eee;
		color: #000;
		cursor: pointer;
		padding: 20px;
		width:100%;
		border: none;
		text-align: left;
		outline: none;
		font-size: 15px;
		transition: 0.4s;
		margin-bottom: 0px;
		margin-top: 0px;

	}


	button.fav {
		background-color: #eee;
		color: #444;
		cursor: pointer;
		border: none;
		outline: none;
		transition: 0.4s;
		left:79%;
		display: inline-block;
		position:absolute;
		margin-top: 12px;

	}

	.button {
display: inline-block;
white-space: nowrap;
background-color: #6DB7E7;
background-image: linear-gradient(top, #eee, #ccc);
filter: progid:DXImageTransform.Microsoft.gradient(startColorStr='#eeeeee', EndColorStr='#cccccc');
border: 1px solid #777;
padding: 0 1.5em;
margin: 0.5em;
font: bold 1em/2em Arial, Helvetica;
text-decoration: none;
color: #000;
text-shadow: 0 1px 0 rgba(255,255,255,.8);
border-radius: .2em;
box-shadow: 0 0 1px 1px rgba(255,255,255,.8) inset, 0 1px 0 rgba(0,0,0,.3);
width: 220px;
position: absolute;
		margin-top: 13px;
		left: 83%;
}

.button:hover {
background-color: lightcoral;
background-image: linear-gradient(top, #fafafa, #ddd);
filter: progid:DXImageTransform.Microsoft.gradient(startColorStr='#fafafa', EndColorStr='#dddddd');
}

.button:active {
box-shadow: 0 0 4px 2px rgba(0,0,0,.3) inset;
position: relative;
top: 1px;
}

.button:focus {
outline: 0;
background: #fafafa;
}

.button:before {
background: #ccc;
background: rgba(0,0,0,.1);
float: left;
width: 1.6em;
text-align: center;
font-size: 1.5em;
margin: 0 1em 0 -1em;
padding: 0 .2em;
box-shadow: 1px 0 0 rgba(0,0,0,.5), 2px 0 0 rgba(255,255,255,.5);
border-radius: .15em 0 0 .15em;
pointer-events: none;
}

/* Hexadecimal entities for the icons */

.add:before {
content: "\271A";
}

.delete:before {
content: "\2718";
}

	button.accordion.active, button.accordion:hover {
		background-color: #6C81E4;
	}

	div.panel {
		padding: 0 18px;
		background-color: white;
		max-height: 0;
		overflow: hidden;
		transition: 0.6s ease-in-out;
		opacity: 0;
	}

		div.panel.show {
			opacity: 1;
			max-height: 500px;
		}

		article header ul .gk-comment:before {
content: '\f086';
font-family: FontAwesome;
	/* more styles for the icon, like color, font-size, position, etc. */
}
</style>
</head>
<BODY>
<div align="center"><div style=" width: 300px; height: 100%; margin-top: 50px; margin-bottom: 50px"><h1 align="center"><font size=50>Favorite List</font></h1></div></div>
<?php

	if (isset($_COOKIE['signed_in_id']))
		$fav_list = get_fav_list($_COOKIE['signed_in_id']);

	if (!$fav_list) {
		//die('Invalid query: ' . mysql_error());
		echo '<div align="center"><div align="center" style="width: 1000px; height: 30px; background-color: white;"> <h3 style="background-color: white;">~~List is empty~~</h3></div></div>';
		
		
	}
/*
	if(isset($_POST['unfav'])){
		$school_name = $_POST['unfav'];
		remove_from_fav_list('user1',$school_name);
		echo $school_name.' has been successfully removed from the favourite list';
		//header('Location: favlist.php');
	}
*/
	//while($school = $result->fetch_assoc()){
	foreach ($fav_list as $schoolname){

		echo '<form action="addToFav.php" method="POST" ><button name="unfavorite" class="fav" value="'.$schoolname.'" ><i style="font-size:30px; color: #FFD700; " class="fa">&#xf005;</i></button></form>';

		if(!in_array($schoolname,$_SESSION['clist'])){
			echo '<div style="top:15%"><form action="addToCompare.php" method="POST" ><button name="compare" class="button add" value="'.$schoolname.'" onclick="toggle()">Add to Compare</button></form></div>';
		}else if(in_array($schoolname,$_SESSION['clist'])){
			echo '<div style="top:15%"><form action="addToCompare.php" method="POST" ><button name="remove" class="button delete" value="'.$schoolname.'" onclick="toggle()">Remove from Compare</button></form></div>';
		}

		echo '<table><tr><button class="accordion" >';echo $schoolname;echo'</button>';
		echo '<div class="panel">';
		$results = searchSchool($schoolname);
		foreach ($results as $school){
			echo '<br />';
			echo 'Address: '.$school['school_location'].'<br />'.'<br />';
			echo 'Email: '.$school['school_email'].'<br />'.'<br />';
			echo 'School Code: '.$school['school_code'].'<br />'.'<br />'; //replace with description if have
		}
		echo '</div></tr></table>'; //description
	}
?>

    <script>
		/*function toggle(){
			if(document.getElementById("cbutton").contains(document.getElementById("add"))){
				document.getElementById("compare").innerHTML="<button name="compare" id="remove" class="compare" value="<?php echo $row['schoolname']?>'" onclick="toggle()">remove from Comparison</button>";
			}else if(document.getElementById("cbutton").contains(document.getElementById("remove"))){
				document.getElementById("compare").innerHTML="<button name="compare" id="add" class="compare" value="<?php echo $row['schoolname']?>'" onclick="toggle()">add to Comparison</button>";
			}
		}*/

        var acc = document.getElementsByClassName("accordion");
        var i;

        for (i = 0; i < acc.length; i++) {
            acc[i].onclick = function accordion () {
                this.classList.toggle("active");
                this.nextElementSibling.classList.toggle("show");
                accordion();
            }
        }

    </script>
<?php include_once('../footer.php') ?>

</BODY>
</html>