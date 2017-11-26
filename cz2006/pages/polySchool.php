<?php include_once('header.php') ?>
<?php include_once('../backend/searchManager.php') ?>
<?php include_once('../backend/listGenerator.php') ?>

<script>
function toggleTable(){
	if(document.getElementById("advanced").style.display == "none"){ //show
		document.getElementById("advanced").style.display = "";
		document.getElementById("advanced_submit").style.display = "inline";
		document.getElementById("simple_submit").style.display = "none";
		document.getElementById("ShowBtn").style.display = "none";
		document.getElementById("HideBtn").style.display = "inline";
	}else{ //hide
		document.getElementById("advanced").style.display = "none";
		document.getElementById("advanced_submit").style.display = "none";
		document.getElementById("simple_submit").style.display = "inline";
		document.getElementById("ShowBtn").style.display = "inline";
		document.getElementById("HideBtn").style.display = "none";
	}
}
</script>
<link href="../css/table.css" rel="stylesheet">
<link href="../css/btn.css" rel="stylesheet">
<!-- Begin page content -->
<div class="container">
	<form name="searchForm" action="polySchool.php" method="get" onsubmit="return ValidatePoly()">
		<table class="table table-striped" width="100%">
			<tr align="center" >
				<td colspan=10><h1>Poly Search Page</h1></td>
			</tr>
			<tr>
				<td align="right" >
					Cut Off Point
				</td>
				<td align="left">
					<input type="text" name="cut_off_point" id="polycof" required \>
				</td>
				<td align="right" >
					Area
				</td>
				<td align="left">
					<input type="text" name="area" class="typeahead_area" id="polyarea" \>
				</td>
			</tr>
			<tr>
				<td align="right">
					Course Cluster
				</td>
				<td align="left">
					<input type="text" name="course_cluster" class="typeahead_course_cluster" id="polyccluster" />
				</td>
				<td align="right" >
					Course Title
				</td>
				<td align="left">
					<input type="text" name="courseTitle" class="typeahead_courseTitle" id="polyctitle" />
				</td>
			</tr>
			<tr align="center">
				<td colspan=10 style="text-align: center">
					<input type="submit" id="simple_submit" value="Submit" class="btn btn-default" \>
					<input type="button" id="ShowBtn" onclick="toggleTable();" value="Show Advanced Settings" class="btn btn-default" />
				</td>
			</tr>
		</table>
		<table class="table table-striped" border="0" width="100%" id="advanced" style="display: none;">
			<tr align="center">
				<td colspan=10><h3>Advanced Search</h3></td>
			</tr>
			<tr>
				<td align="right">Code: </td>
				<td align="left" colspan=2><input type="textfield" name= "code" class="btn btn-default" size="10" id="polycode" /></td>
				<td align="right">MRT: </td>
				<td align="left" colspan=2><input type="textfield" name= "MRT" class="btn btn-default typeahead_mrt_name" size="10" id="polymrt" /></td>
				<td align="right">Bus: </td>
				<td align="left" colspan=2><input type="textfield" name= "Bus" class="btn btn-default" size="10" id="polybus" /></td>
				<td align="center" co>Availability of Shuttle Bus <input type="checkbox" id="Shuttle_Bus"></td>
			</tr>
			<tr>
			<td colspan=10 align="center" style="text-align: center">
				<input type="submit" value="Submit" id="advanced_submit" class="btn btn-default" />
				<input type="button" id="HideBtn" onclick="toggleTable();" value="Hide Advanced Settings" class="btn btn-default" />
			</td>
			</tr>
		</table>
	</form>
	<?php
	$area = " ";
	$courseTitle = " ";
	$course_cluster = " ";
	$score = " ";
	$code = " ";
	$bus = " ";
	$mrt = " ";

	if( isset($_GET['area'])){
		$area = $_GET['area'];
	}
	if( isset($_GET['courseTitle'])){
		$courseTitle = $_GET['courseTitle'];
	}
	if( isset($_GET['course_cluster'])){
		$course_cluster = $_GET['course_cluster'];
	}
	if( isset($_GET['cut_off_point'])){
		$score = $_GET['cut_off_point'];
	}
	if( isset($_GET['code'])){
		$code = $_GET['code'];
	}
	if( isset($_GET['bus'])){
		$bus = $_GET['bus'];
	}
	if( isset($_GET['mrt'])){
		$mrt = $_GET['mrt'];
	}

	if(isset($_GET['area']) || isset($_GET['course_cluster']) || isset($_GET['courseTitle']) || isset($_GET['cut_off_point']) || isset($_GET['code']) || isset($_GET['bus']) || isset($_GET['mrt']))
	{
		$results = searchPoly($area, $course_cluster, $courseTitle, $score, $mrt, $bus, $code);
		?>
		<table class="table table-striped table-bordered secondaryTable" >
			<tr>
				<th>Name</th>
				<th>Course Cluster</th>
				<th>Course Title</th>
				<th>Area</th>
				<th>Location</th>
				<th>Telephone</th>
				<th>Email</th>
				<th>Website</th>
				<th>MRT</th>
				<th>Bus</th>
				<th>Options</th>
			</tr>
			<?php foreach ($results as $result){ ?>
			<tr>
				<td>
					<a href="IndividualSchool.php?school_name=<?php echo $result['school_name']?>" ><?php echo $result['school_name'] ?></a>
				</td>
				<td><?php echo $result['course_cluster'] ?></td>
				<td><?php echo $result['courseTitle'] ?></td>
				<td><?php echo $result['school_area'] ?></td>
				<td><?php echo $result['school_location'] ?></td>
				<td><?php echo $result['school_telephone'] ?></td>
				<td><?php echo $result['school_email'] ?></td>
				<td><?php echo $result['school_website'] ?></td>
				<td><?php echo $result['Nearest_MRT'] ?></td>
				<td><?php echo $result['Busnumber'] ?></td>
				<td style="text-align:center">
					<?php
					if(isset($_COOKIE['signed_in_id'])){
						$fav_list = get_fav_list($_COOKIE['signed_in_id']);//return an array
					}

					if(!in_array($result['school_name'], $_SESSION['clist'])){
						echo '<form action="addToCompare.php" method="POST" style="display:inline">
						<button name="btn add" class="button add" value="'.$result['school_name'].'">Add to Compare</button>
						</form>';
					}else if(in_array($result['school_name'],$_SESSION['clist'])){
						echo '<form action="addToCompare.php" method="POST" style="display:inline">
						<button name="remove" class="button delete" value="'.$result['school_name'].'">Remove from Compare</button>
						</form>';
					}

					if(in_array($result['school_name'],$fav_list)){ //display unfavourite button if in favourite list
						echo '<form method="POST" action="addToFav.php" ><button name="unfavorite" value="'.$result['school_name'].'" class="button delete">Unfavorite</button></form>';
					}else { //display add to favourite button if not in favourite list
						echo '<form method="POST" action="addToFav.php" ><button name="favorite" value="'.$result['school_name'].'" class="button add">Favorite</button></form>';
					}
					?>
				</td>
			</tr>
			<?php } ?>
		</table>
	<?php } ?>
</div>

<script>

$(document).ready(function()
{
	var area = [<?php echo $poly_typeahead['area'];?>];
	var course_cluster = [<?php echo $poly_typeahead['course_cluster'];?>];
	var courseTitle = [<?php echo $poly_typeahead['courseTitle'];?>];
	var mrt_name = [<?php echo $mrt_typeahead['nearest_mrt']; ?>];

	$('.typeahead_area').typeahead({
		hint: true,
		highlight: true,
		minLength: 1
	},{
		name: 'area',
		source: substringMatcher(area)
	});

	$('.typeahead_course_cluster').typeahead({
		hint: true,
		highlight: true,
		minLength: 1
	},{
		name: 'course_cluster',
		source: substringMatcher(course_cluster)
	});

	$('.typeahead_courseTitle').typeahead({
		hint: true,
		highlight: true,
		minLength: 1
	},{
		name: 'courseTitle',
		source: substringMatcher(courseTitle)
	});
	$('.typeahead_mrt_name').typeahead({
		hint: true,
		highlight: true,
		minLength: 1
	},{
		name: 'mrt_name',
		source: substringMatcher(mrt_name)
	});

});
</script>

<?php include_once('../footer.php') ?>