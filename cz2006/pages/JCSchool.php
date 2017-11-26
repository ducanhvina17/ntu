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
	<form form name="searchForm" action="JCSchool.php" method="get" onsubmit="return ValidateJC()">
		<table class="table table-striped" width="100%">
			<tr align="center" >
				<td colspan=10><h1>Junior College Search Page</h1></td>
			</tr>
			<tr>
				<td align="right" >
					Type
				</td>
				<td align="left" >
					<select id="type" name="type">
						<option value="art">Art</option>
						<option value="science">Science</option>
					</select>
				</td>
				<td align="right" >
					Minimum L1R5 score:
				</td>
				<td align="left">
					<?php
						if(isset($_GET['score'])){
							echo '<input type="text" name="score" id="jccop" value="'.$_GET['score'].'" required \>';
						} else {
							echo '<input type="text" name="score" id="jccop" value="" required \>';
						}
					?>
				</td>
			</tr>
			<tr>
				<td align="right" >
					Area
				</td>
				<td align="left">
					<input type="text" name="area" class="typeahead_area_name" id="jcarea" \>
				</td>
				<td align="right" colspan=1>
					Subjects
				</td>
				<td align="left" colspan=3	>
					<?php
						if(isset($_GET['subjects'])){
							echo '<input type="text" name="subject" class="typeahead_subject_name" id="jcsubject" value="'.$_GET['subjects'].'"  \>';
						} else {
							echo '<input type="text" name="subject" class="typeahead_subject_name" id="jcsubject" \>';
						}
					?>
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
				<td align="left" colspan=2><input type="textfield" name= "code" class="btn btn-default" size="10" id="jccode" /></td>
				<td align="right">MRT: </td>
				<td align="left" colspan=2><input type="textfield" name= "MRT" class="btn btn-default typeahead_mrt_name" size="10" id="jcmrt" /></td>
				<td align="right">Bus: </td>
				<td align="left" colspan=2><input type="textfield" name= "Bus" class="btn btn-default" size="10" id="jcbus" /></td>
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
	$area=" ";
	$subject=" ";
	$type=" ";
	$score=" ";
	$code="";
	$bus="";
	$mrt="";

	if(isset($_GET['area'])){
		$area = $_GET['area'];
	}
	if( isset($_GET['subject'])){
		$subject = $_GET['subject'];
	}
	if( isset($_GET['type'])){
		$type = $_GET['type'];
	}
	if( isset($_GET['score'])){
		$score = $_GET['score'];
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
	if(isset($_GET['area']) || isset($_GET['subject']) || isset($_GET['type']) || isset($_GET['score']) ||
		isset($_GET['code']) || isset($_GET['bus']) || isset($_GET['mrt']))
	{
		   $results = searchJC($area, $subject, $type, $score,$mrt,$bus,$code);
	     ?>


		<table class="table table-striped table-bordered secondaryTable" >
			<tr>
				<th>Name</th>
				<th>Location</th>
				<th>Area</th>
				<th>Telephone</th>
				<th>Email</th>
				<th>Website</th>
				<th>Nearest MRT</th>
				<th>Bus</th>
				<th>Options</th>
			</tr>
			<?php foreach ($results as $result){ ?>
			<tr>
				<td>
					<a href="IndividualSchool.php?school_name=<?php echo $result['school_name']?>" ><?php echo $result['school_name'] ?></a>
				</td>
				<td><?php echo $result['school_location'] ?></td>
				<td><?php echo $result['school_area'] ?></td>
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
					echo '
					<div class = "button-wrap">
					<form action="addToCompare.php" method="POST" style="display:inline">
					<button name="compare" class="button add" value="'.$result['school_name'].'">Add to Compare</button>
					</form>
					</div>';
				}else if(in_array($result['school_name'],$_SESSION['clist'])){
					echo '
					<div class = "button-wrap">
					<form action="addToCompare.php" method="POST" style="display:inline">
					<button name="remove" class="button delete" value="'.$result['school_name'].'">Remove from Compare</button>
					</form>
					</div>';
				}

				if(in_array($result['school_name'],$fav_list)){ //display unfavourite button if in favourite list
					echo '<form method="POST" action="addToFav.php" ><button name="unfavorite" value="'.$result['school_name'].'" class="button delete">Unfavorite</button></form>';
				}else { //display add to favourite button if not in favourite list
					echo '<form method="POST" action="addToFav.php" ><button name="favorite" value="'.$result['school_name'].'" class="button star">Favorite</button></form>';
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
	var area_name = [<?php echo $area_typeahead['area']; ?>];
	var subject_name = [<?php echo $subject_typeahead['subjects']; ?>];
	var mrt_name = [<?php echo $mrt_typeahead['nearest_mrt']; ?>];
	$('.typeahead_area_name').typeahead({
		hint: true,
		highlight: true,
		minLength: 1
	},{
		name: 'area_name',
		source: substringMatcher(area_name)
	});
	$('.typeahead_subject_name').typeahead({
		hint: true,
		highlight: true,
		minLength: 1
	},{
		name: 'subject_name',
		source: substringMatcher(subject_name)
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