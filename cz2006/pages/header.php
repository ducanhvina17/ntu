<?php
session_start();
include_once('../backend/searchManager.php');
if(!isset($_SESSION["clist"]) || empty($_SESSION["clist"])){$_SESSION["clist"] = array();}
if(!isset($fav_list))  $fav_list= array();
?>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name="description" content="Website.">
	<title>Schools in Singapore</title>

	<!-- CSS -->
	<link href="../css/bootstrap.min.css" rel="stylesheet">
	<link href="../css/style.css" rel="stylesheet">

	<!-- JavaScript -->
	<script src="../js/jquery.min.js"></script>
	<script src="../js/bootstrap.min.js"></script>
	<script src="../js/script.js"></script>
	<script src="../js/inputValidation.js"></script>
	<script src="../js/typeahead.js/bloodhound.min.js"></script>
	<script src="../js/typeahead.js/typeahead.bundle.min.js"></script>
	<script src="../js/typeahead.js/typeahead.jquery.min.js"></script>
	<?php
		$conn = dbConnect();
		$schools_typeahead = array(
        'school_name' => "'"
        );
    $sql = "select distinct schools from schools";
    $result = $conn->query($sql);
    $i = 1;

    if ($result->num_rows > 0)  // if the number of result is greater than 0
    {
        while($row = $result->fetch_assoc())    // get each result and put them into the array
        {
            $row_item = str_replace("'", "&#39;", $row['schools']);

            if($i == $result->num_rows){
                $schools_typeahead['school_name'] .= $row_item."'";
            }
            else {
                $schools_typeahead['school_name'] .= $row_item."', '";
            }

            $i++;
        }

    }
	 ?>
	 <style>
		 body{
			 background-image:url(../img/bg.jpg);
		 }
	</style>
	<script>
	$(document).ready(function(){
		var schoolName = [<?php echo $schools_typeahead['school_name']; ?>];

		$('.typeahead_schoolName').typeahead({
			hint: true,
			highlight: true,
			minLength: 1
		},{
			name: 'schoolName',
			source: substringMatcher(schoolName)
		});


	});

	function isEmpty(){
		var school_name = document.getElementById("school_name");
		if(school_name.value == ""){
			alert("Your search field is empty. Please try again.");
			return false;
		} else {
			return true;
		}
	}
</script>
</head>
<body class="index" id="page-top" data-spy="scroll" data-target=".navbar" data-offset="150">
	<!-- Fixed navbar -->
	<nav class="navbar navbar-default navbar-fixed-top">
		<div class="container">
			<div class="navbar-header">
				<a class="navbar-brand" href="../index.php"><img style="margin-top: -25px" src="../img/logo.png"></a>
			</div>
			<div id="navbar" class="collapse navbar-collapse pull-right">
				<ul class="nav navbar-nav">
					<li><a href="../index.php">Home</a></li>
					<li><a href="about.php">About Us</a></li>
					<li class="dropdown">
						<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">School <span class="caret"></span></a>
						<ul class="dropdown-menu">
						<li><a href="primaryschool.php">Primary School</a></li>
                        <li><a href="secondaryschool.php">Secondary School</a></li>
                        <li><a href="polySchool.php">Polytechnic</a></li>
                        <li><a href="JCSchool.php">Junior College</a></li>
						<li><a href="iteSchool.php">ITE</a></li>
                        <li><a href="university.php">University</a></li>
						</ul>
					</li>
					<li><a href="comparisonlist.php">Comparison List</a></li>
					<li><a href="forum.php">Forum</a></li>
					<?php

					if (isset($_COOKIE['signed_in_id']))
					{
						echo '<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">' . $_COOKIE['signed_in_id'] . '<span class="caret"></span></a><ul class="dropdown-menu"><li><a href="favlist.php">Favourite List</a></li><li><a href="../backend/logoutManager.php">Logout</a></li></ul></li>';
					}
					else
					{
						echo '<li><a href="../login.php">Login</a></li>';
					}
					?>
					<form class="navbar-form navbar-right" action="individualSchool.php" method="get" onSubmit="return isEmpty()">
						<div class="form-group">
							<input type="text" name="school_name" id="school_name" class="form-control typeahead_schoolName" placeholder="Search">
						</div>
						<button type="submit" class="btn btn-default">Search</button>
					</form>
				</ul>
			</div>
		</div>
	</nav>