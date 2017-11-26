<?php include_once('header.php'); ?>
<?php include_once('../backend/listGenerator.php') ?>
<link href="../css/table.css" rel="stylesheet">
<link href="../css/btn.css" rel="stylesheet">
<script type="text/javascript" src="../css/fancybox/jquery.fancybox.js?v=2.1.5"></script>
<link rel="stylesheet" type="text/css" href="../css/fancybox/jquery.fancybox.css?v=2.1.5" media="screen" />
<style>
	button {border-radius: 10%;
	background-color: #5D66C5;
	color: #fff;
	height: 30px;
	margin-bottom: 3px;}
</style>
<script type="text/javascript">
$(document).ready(function(){
	$(".detail").click(function(){
	var p_id = $(this).attr('id');
		if(p_id!='')
		{
		 $.ajax({
				type:"post",
				url:"compare.php",
				data:{p_id:p_id,type:'detail'},
				cache: false,
				success:function(data){
				$.fancybox(data, {
					fitToView: true,
					width: 700,
					height: 700,
					autoSize: true,
					closeClick: false,
					openEffect: 'none',
					closeEffect: 'refresh'
				});

				}
		   });
		}
	});
});

function compare()
{
	var total_check = new Array();
	$('.products:checked').each(function () {
		total_check.push($(this).val());
	});

	if (total_check != '') {
		if (total_check.length <= '5') {
		var i = 0;
		var pidArray = new Object();
		$('.products:checked').each(function () {
		total_check.push($(this).val());
		var id = $(this).attr('id');
		pidArray[i] = {
			pid: id
		};
		i++;
	});
	var data = JSON.stringify(pidArray);
	$('#wait').show();
			$.ajax({
				url: "compare.php",
				type: "POST",
				data: {pids:data,type:'compare'},
				cache: false,
				success: function (data) {
				$('#wait').hide();
					$.fancybox(data, {
						fitToView: true,
						width: 700,
						height: 500,
						autoSize: true,
						closeClick: false,
						openEffect: 'none',
						closeEffect: 'refresh'
					});
				}
			});
		} else {
		alert("You can only compare up to five schools at once");
			return false;
		}
	} else {
		alert("Please select the school(s)");
		return false;
	}
}
</script>
</head>
<body>
<div class="wrapper">
<div class="header">
</div>


<div class="body1">
<div class="body2">
<div class="main_table">
<div align="center"><div style="width: 600px; height: 100%;"><h1 align="center"><font size=50>School Comparison List</font></h1></div></div>
<br>
<span align="center"><img id="wait" style="display:none;margin-left:300px;" src="image/loading.gif"></span>
<?php

		if (!empty($_SESSION['clist']))
		{
				echo '
				<table width="100%">
				<tr>
					<td width="10%"><a href="javascript:void(0)" onclick="compare();" style="color:blue;font-size:15px;"><b>Compare</b></a></td>
					<td width="20%"><font size=5>School Logo</font></td>
			          <td width="20%"><font size=5>School Name</font></td>
			          <td width="20%"><font size=5>School Code</font></td>
			          <td width="20%"><font size=5>Details</font></td>
					</tr>';

			$array = $_SESSION['clist'];
			foreach ($array as $item) {
				//echo $item;
				$school = searchSchool($item);

				foreach ($school as $data){
					//var_dump($data);
					echo '<tr><td><input type="checkbox" name="products[]" class="products" id="'.$data['school_name'].'"></td>';
					echo '<td><img src="../img/School_Logo/'.str_replace(" ", "-",$data['school_name']).'.jpg" width="80" height="80px;"></td>';
					echo '<td>'.$data['school_name'].'</td>';
					echo '<td>'.$data['school_code'].'</td>';
					echo '<td><a href="javascript:void(0);" class="detail" id="detail-'.$data['school_name'].'">Details</a></td>';
					echo '<td>';

					if(isset($_COOKIE['signed_in_id'])){
						$fav_list = get_fav_list($_COOKIE['signed_in_id']);//return an array
					}

					if(in_array($data['school_name'],$fav_list)){ //display unfavourite button if in favourite list

						echo '<form method="POST" action="addToFav.php" ><button name="unfavorite" class="button delete" value="'.$data['school_name'].'" class="btn btn-success">Unfavorite</button></form>';

					}else { //display add to favourite button if not in favourite list
						echo '<form method="POST" action="addToFav.php" ><button name="favorite" class="button star" value="'.$data['school_name'].'" class="btn btn-success">Favorite</button></form>';

					}

					echo '<form action="addToCompare.php" method="POST" ><button name="remove" class="button delete" value="'.$data['school_name'].'" >Remove</button></form>
						</td></tr>';

				}
			}
		}
		?>

</table>
<?php
if (empty($_SESSION['clist']))
	echo '<div class="text-center"><br><br><h2>~~~ List is empty ~~~</h2></div>';
?>
</div>
</div></div>
</div>