<?php include_once('../backend/searchManager.php'); ?>
<?php
$type = trim($_REQUEST['type']);
$res = array();
$res[0]="<tr><th align='left'><strong>School Attributes</strong></th>";
$res[1]="<tr height='77' align='center'><td align='left'>School Logo</td>";
$res[2]="<tr height='40' align='center'><td align='left'>School Name</td>";
$res[3]="<tr height='40' align='center'><td align='left'>School Code</td>";
$res[4]="<tr height='40' align='center'><td align='left'>Type</td>";
$res[5]="<tr height='40' align='center'><td align='left'>Location</td>";
$res[6]="<tr height='40' align='center'><td align='left'>Area Name</td>";
$res[7]="<tr height='40' align='center'><td align='left'>Telephone</td>";
$res[8]="<tr height='40' align='center'><td align='left'>Email</td>";
$res[9]="<tr height='40' align='center'><td align='left'>Website</td>";
$res[10]="<tr height='40' align='center'><td align='left'>Nearest MRT</td>";
$res[11]="<tr height='40' align='center'><td align='left'>Bus Number</td>";
$res[12]="<tr height='150' align='center'><td align='left'>CCA</td>";
$res[13]="<tr height='180' align='center'><td align='left'>Subjects</td>";

if($type=='detail')
{
$pid = explode('-',trim($_REQUEST['p_id']));
$scode = $pid[1];
$sql = get_comparables_school($scode);
$data = $sql->fetch_assoc();

$res[0].="<th align='left'><strong>School Details</strong></th>";
$res[1].="<td align='left'><img src='../img/School_Logo/".str_replace(" ", "-",$data['school_name']).".jpg' width='75px' height='75px'></td>";
$res[2].="<td align='left'>".$data['school_name']."</td>";
$res[3].="<td align='left'>".$data['school_code']."</td>";
$res[4].="<td align='left'>".$data['type']."</td>";
$res[5].="<td align='left'>".$data['location']."</td>";
$res[6].="<td align='left'>".$data['area']."</td>";
$res[7].="<td align='left'>".$data['Telephone']."</td>";
$res[8].="<td align='left'>".$data['Email']."</td>";
$res[9].="<td align='left'>".$data['website']."</td>";
$res[10].="<td align='left'>".$data['Nearest_MRT']."</td>";
$res[11].="<td align='left'>".$data['Bus_number']."</td>";
$res[12].="<td align='left'>".$data['CCA']."</td>";
	if (strpos($data['school_name'], 'ITE') !== false)
	{
		$r = get_ite_courses();
		$res[13].="<td align='left'>";
		while($row = $r->fetch_assoc())
			$res[13].=$row['ITE_Course'].'<br>';
		$res[13].="</td>";
	}
	else if (strpos($data['school_name'], 'Polytechnic') !== false)
	{
		$r = get_poly_courses();
		$res[13].="<td align='left'>";
		while($row = $r->fetch_assoc())
			$res[13].=$row['Course_Title'].'<br>';
		$res[13].="</td>";
	}
	else if (strpos($data['school_name'], 'NUS') !== false ||
				strpos($data['school_name'], 'NTU') !== false ||
				strpos($data['school_name'], 'SMU') !== false)
	{
		$r = get_uni_courses();
		$res[13].="<td align='left'>";
		while($row = $r->fetch_assoc())
			$res[13].=$row['course_name'].'<br>';
		$res[13].="</td>";
	}
	else
		$res[13].="<td align='left'>".$data['subjects']."</td>";
}
else if($type=='compare')
{
$Totalpids = (array)json_decode(stripslashes($_REQUEST['pids']));
foreach($Totalpids as $product)
{
$sql = get_comparables_school($product->pid);
$data = $sql->fetch_assoc();

$res[0].="<th align='left'><strong>School Details</strong></th>";
$res[1].="<td align='left'><img src='../img/School_Logo/".str_replace(" ", "-",$data['school_name']).".jpg' width='75px' height='75px'></td>";
$res[2].="<td align='left'>".$data['school_name']."</td>";
$res[3].="<td align='left'>".$data['school_code']."</td>";
$res[4].="<td align='left'>".$data['type']."</td>";
$res[5].="<td align='left'>".$data['location']."</td>";
$res[6].="<td align='left'>".$data['area']."</td>";
$res[7].="<td align='left'>".$data['Telephone']."</td>";
$res[8].="<td align='left'>".$data['Email']."</td>";
$res[9].="<td align='left'>".$data['website']."</td>";
$res[10].="<td align='left'>".$data['Nearest_MRT']."</td>";
$res[11].="<td align='left'>".$data['Bus_number']."</td>";
$res[12].="<td align='left'>".$data['CCA']."</td>";
	if (strpos($data['school_name'], 'ITE') !== false)
	{
		$r = get_ite_courses();
		$res[13].="<td align='left'>";
		while($row = $r->fetch_assoc())
			$res[13].=$row['ITE_Course'].'<br>';
		$res[13].="</td>";
	}
	else if (strpos($data['school_name'], 'Polytechnic') !== false)
	{
		$r = get_poly_courses();
		$res[13].="<td align='left'>";
		while($row = $r->fetch_assoc())
			$res[13].=$row['Course_Title'].'<br>';
		$res[13].="</td>";
	}
	else if (strpos($data['school_name'], 'NUS') !== false ||
				strpos($data['school_name'], 'NTU') !== false ||
				strpos($data['school_name'], 'SMU') !== false)
	{
		$r = get_uni_courses();
		$res[13].="<td align='left'>";
		while($row = $r->fetch_assoc())
			$res[13].=$row['course_name'].'<br>';
		$res[13].="</td>";
	}
	else
		$res[13].="<td align='left'>".$data['subjects']."</td>";
}
}

for($i=0;$i<count($res);$i++){
	$res[$i].="</tr>";
}
$table = "";
for($j=0;$j<count($res);$j++){
	$table.= $res[$j];
}

echo "<table cellspacing='2' cellpadding='0' align='left' width='200' border='1'>".$table."</table>";
?>
