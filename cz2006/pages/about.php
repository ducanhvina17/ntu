<?php include_once('header.php') ?>
<!doctype html>
<html>
<head>
<meta charset="UTF-8">
<title>About Us</title>
<!-- CSS -->
	<link href="../css/about/about-us.css" rel="stylesheet">
	<link href="../css/about/framework.css" rel="stylesheet">
</head>
<body>

<!--<script src="../js/about/jquery-1.9.0.min.js" type="text/javascript"></script>-->
<script src="../js/about/jquery-ui-1.10.0.tabs.min.js" type="text/javascript"></script>
<script type="text/javascript">$(document).ready(function(){$("#tabcontainer").tabs({event:"click"})})</script>

<div class="wrapper row2">
  <div id="container" class="clear">
    <!-- ####################################################################################################### -->
    <!-- ####################################################################################################### -->
    <!-- ####################################################################################################### -->
    <!-- ####################################################################################################### -->
    <div id="about-us" class="clear">
      <!-- ####################################################################################################### -->
      <section id="tabcontainer" class="clear">
        <ul id="tabnav">
          <li><a href="#tabs-1">Background</a></li>
          <li><a href="#tabs-2">Our Site</a></li>
          <li><a href="#tabs-3">Affiliation</a></li>
          <li><a href="#tabs-4">Team Members</a></li>
        </ul>
        <!-- Tab Content -->
        <div id="tabs-1" class="tabcontainer clear" align="center">
         <div style="background-color: white; height: 500px;">
          <img class="imgholder" src="../img/logo2.png" alt="" >
            <h1>Background</h1>
            <p align="left" style="width: 950px;"> <font size="4">Due to the large diversity of schools, students often faced the problem of choosing the most suitable school for them. As a fellow students, we know that there are alot of factors to consider such as location, CCA, subjects/module offered, achievement etc, to consider when choosing the right one. </font></p></div>
            <div class="one_half" align="center">
          </div>
        </div>
        <!-- ## TAB 2 ## -->
        <div id="tabs-2" class="tabcontainer clear" align="center">
         <div style="background-color: white; height: 500px;">
          <img class="imgholder" src="../img/logo2.png" alt="" >
		  <h1>Our Site</h1>
          <p align="left" style="width: 950px;"> <font size="4">This web application is designed to help people to efficiently plan and choose their choice of school. Our site offers a wide list of filters that can be used to search the most suitable school for the user. We also include a navigation system which allows the use to search and view the surrounding and the route to the school. User can also add the school to the comparison list via the add to comparison list
		  button or favourite the school via the favourite button.</font></p> </div>
        </div>
        <!-- ## TAB 3 ## -->
        <div id="tabs-3" class="tabcontainer clear" align="center">
          <div style="background-color: white; height: 500px;">
		  <img class="imgholder" src="../img/logo2.png" alt="" >
          <h1>Affiliation</h1>
          <img src="../img/moe-logo.png" alt="MOE SG logo" />
          <img src="../img/SCSE_logo.jpg" alt="NTU SCSE log"/>
			  </div>
        </div>
        <!-- ## TAB 3 ## -->
        <div id="tabs-4" class="tabcontainer clear" align="center">
         <div style="background-color: white; height: 500px;">
		 <img class="imgholder" src="../img/logo2.png" alt="" >
          <h1>Team Members</h1>
          <p style="width: 950px;"> <font size="4">Huang Wen Jie (Project Leader)</font></p>
          <p style="width: 950px;"> <font size="4">Chua Wei Kiat</font></p>
          <p style="width: 950px;"> <font size="4">Chua Jing Yi</font></p>
          <p style="width: 950px;"> <font size="4">Chua Wan Lin</font></p>
          <p style="width: 950px;"> <font size="4">Lee Xing Zhao</font></p>
          <p style="width: 950px;"> <font size="4">Pang Ya Ran</font></p>
			</div>
        </div>
        <!-- / Tab Content -->

<?php include_once('../footer.php') ?>
</body>
</html>
