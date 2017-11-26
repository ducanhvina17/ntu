<?php
include('dbManager.php');

function searchSchool($school_name)
{
	$conn = dbConnect();
	$school_name=strtoupper($school_name);
	$sql = "select * from school where upper(school.school_name) like '%$school_name' ";
	$result = $conn->query($sql);
	if ($result == false){
 		echo "No search found!";
		die;
	}

	$search_result_school = array();
	$i = 0;

	if (!empty($result) && $result->num_rows > 0)	// if the number of result is greater than 0
	{
		while($row = $result->fetch_assoc())	// get each result and put them into the array
		{
			$school_name = $row["school_name"];
			$school_code = $row["school_code"];
			$school_type = $row["type"];
			$school_area = $row["area"];
			$school_location = $row["location"];
			$school_telephone = $row["Telephone"];
			$school_email = $row["Email"];
			$school_website = $row["website"];
			$MRT = $row["Nearest_MRT"];
			$Bus = $row["Bus_number"];
			$cca = $row["CCA"];
			$school_subject = $row["subjects"];

			$search_result_school[$i] = [
				'school_name' => $school_name,
				'school_code' => $school_code,
				'school_type' => $school_type,
				'school_area' => $school_area,
				'school_location' => $school_location,
				'school_telephone' => $school_telephone,
				'school_email' => $school_email,
				'school_website' => $school_website,
				'Nearest_MRT' => $MRT,
				'Bus_number' => $Bus,
				'CCA' => $cca,
				'school_subject' => $school_subject];

			$i++;
		}
	}
	else
	{
		$search_result_school = $search_result_school;
	}

	dbDisconnect($conn);

	return $search_result_school;
}

function searchPrimarySchool($area, $cca, $subject, $code, $MRT, $Bus, $ShuttleBus)
{
	$conn = dbConnect();


	$whereClause = " where 1=1 ";

	if(!empty($area))
	{
		$area=strtoupper($area);
		$whereClause .= " and upper(school.area) like '%$area%'";
	}

	if(!empty($cca))
	{
		$cca=strtoupper($cca);
		$whereClause .= " and upper(school.CCA) like '%$cca%'";
	}

	if(!empty($subject))
	{
		$subject=strtoupper($subject);
		$whereClause .= " and upper(school.subjects) like '%$subject%'";
	}

	if(!empty($MRT))
		$whereClause .= " and school.Nearest_MRT like '%$MRT%'";

	if(!empty($Bus))
		$whereClause .= " and school.Bus_number like '%$Bus%'";

	if(!empty($code))
				$whereClause .= " and school.Bus_number like '%$code%'";

	//if(!empty($ShuttleBus))
		//$whereClause .= " and school.subjects like '%$ShuttleBus%'";
		$whereClause .= " and school.type like 'Primary'";

	$sql = "select * from school".$whereClause;

	$result = $conn->query($sql);
	if ($result == false){
 		echo "No search found!";
		die;
	}

	// Create an empty array to store each row of school information
	$search_result_school = array();
	$i = 0;

	if (!empty($result) && $result->num_rows > 0)	// if the number of result is greater than 0
	{
		while($row = $result->fetch_assoc())	// get each result and put them into the array
		{
			$school_name = $row["school_name"];
			$school_code = $row["school_code"];
			$school_type = $row["type"];
			$school_area = $row["area"];
			$school_location = $row["location"];
			$school_telephone = $row["Telephone"];
			$school_email = $row["Email"];
			$school_website = $row["website"];
			$MRT = $row["Nearest_MRT"];
			$Bus = $row["Bus_number"];
			$cca = $row["CCA"];
			$school_subject = $row["subjects"];



			$search_result_school[$i] = [
				'school_name' => $school_name,
				'school_code' => $school_code,
				'school_type' => $school_type,
				'school_area' => $school_area,
				'school_location' => $school_location,
				'school_telephone' => $school_telephone,
				'school_email' => $school_email,
				'school_website' => $school_website,
				'Nearest_MRT' => $MRT,
				'Bus_number' => $Bus,
				'CCA' => $cca,
				'school_subject' => $school_subject];

			$i++;
		}
	}
	else
	{
		$search_result_school = $search_result_school;
	}

	dbDisconnect($conn);

	return $search_result_school;
}

function searchSecondarySchool($type, $category, $score, $area, $cca, $subject,$MRT, $Bus, $ShuttleBus)
{
	$conn = dbConnect();

	$whereClause = "where 1=1 ";

	if(!empty($area))
	{
		$area=strtoupper($area);
		$whereClause .= " and upper(school.area) like '%$area%'";
	}

	if(!empty($cca))
	{
		$cca=strtoupper($cca);
		$whereClause .= " and upper(school.CCA) like '%$cca%'";
	}

	if(!empty($subject))
	{
		$subject=strtoupper($subject);
		$whereClause .= " and upper(school.subjects) like '%$subject%'";
	}

	if(!empty($MRT))
		$whereClause .= " and school.Nearest_MRT like '%$MRT%'";

	if(!empty($Bus))
		$whereClause .= " and school.Bus_number like '%$Bus%'";

	//if(!empty($ShuttleBus))
		//$whereClause .= " and school.subjects like '%$ShuttleBus%'";
		$whereClause .= " and school.type like 'Secondary'";

	if(!empty($type))
	{
		if($type=="range")
		{
			switch($category)
			{
				case "E":
					$sql = "select school.*, PSLE_score.psle_E_min as lowest, PSLE_score.psle_E_max as highest from school left join PSLE_score on PSLE_score.school_id = school.school_id ".$whereClause."and $score BETWEEN PSLE_score.psle_E_min and PSLE_score.psle_E_max";
					break;
				case "N":
					$sql = "select school.*, PSLE_score.psle_N_min as lowest, PSLE_score.psle_N_max as highest from school left join PSLE_score on PSLE_score.school_id = school.school_id ".$whereClause ." and $score BETWEEN PSLE_score.psle_N_min and PSLE_score.psle_N_max";
					break;
				case "N/T":
					$sql = "select school.*, PSLE_score.psle_NT_min as lowest, PSLE_score.psle_N/T_max as highest from school left join PSLE_score on PSLE_score.school_id = school.school_id ".$whereClause ." and $score BETWEEN PSLE_score.psle_NT_min and PSLE_score.psle_NT_max";
					break;
				default:
					$sql = "select school.*, PSLE_score.psle_E_min as lowest, PSLE_score.psle_E_max as highest from school left join PSLE_score on PSLE_score.school_id = school.school_id ".$whereClause ." and $score BETWEEN PSLE_score.psle_E_min and PSLE_score.psle_E_max ";
					break;
			}
		}
	}

	if($type=="min")
	{
		switch($category)
		{
			case "E":
				$sql = "select school.*, PSLE_score.psle_E_min as lowest from school left join PSLE_score on PSLE_score.school_id = school.school_id ".$whereClause ."and PSLE_score.psle_E_min>=$score";
				break;
			case "N":
				$sql = "select school.*, PSLE_score.psle_N_min as lowest from school left join PSLE_score on PSLE_score.school_id = school.school_id ".$whereClause ."and PSLE_score.psle_N_min>=$score";
				break;
			case "N/T":
				$sql = "select school.*, PSLE_score.psle_NT_min as lowest from school left join PSLE_score on PSLE_score.school_id = school.school_id ".$whereClause ."and PSLE_score.psle_NT_min>=$score";
				break;
			default:
				$sql = "select school.*, PSLE_score.psle_E_min as lowest from school left join PSLE_score on PSLE_score.school_id = school.school_id ".$whereClause ."and PSLE_score.psle_E_min>=$score";
				break;
		}
	}

	if(empty($type))
	{
		switch($category)
		{
			case "E":
				$sql = "select school.*, PSLE_score.psle_E_min as lowest, PSLE_score.psle_E_max as highest from school left join PSLE_score on PSLE_score.school_id = school.school_id ".$whereClause."and $score BETWEEN PSLE_score.psle_E_min and PSLE_score.psle_E_max";
				break;
			case "N":
				$sql = "select school.*, PSLE_score.psle_N_min as lowest, PSLE_score.psle_N_max as highest from school left join PSLE_score on PSLE_score.school_id = school.school_id ".$whereClause ." and $score BETWEEN PSLE_score.psle_N_min and PSLE_score.psle_N_max";
				break;
			case "N/T":
				$sql = "select school.*, PSLE_score.psle_NT_min as lowest, PSLE_score.psle_N/T_max as highest from school left join PSLE_score on PSLE_score.school_id = school.school_id ".$whereClause ." and $score BETWEEN PSLE_score.psle_NT_min and PSLE_score.psle_NT_max";
				break;
			default:
				$sql = "select school.*, PSLE_score.psle_E_min as lowest, PSLE_score.psle_E_max as highest from school left join PSLE_score on PSLE_score.school_id = school.school_id ".$whereClause ." and $score BETWEEN PSLE_score.psle_E_min and PSLE_score.psle_E_max ";
				break;
		}
	}

	$result = $conn->query($sql);
	if ($result == false){
		echo "No search found!";
		die;
	}

	// Create an empty array to store each row of school information
	$search_result_school = array();
	$i = 0;
	
	if (!empty($result) && $result->num_rows > 0)	// if the number of result is greater than 0
	{
		while($row = $result->fetch_assoc())	// get each result and put them into the array
		{
			$school_name = $row["school_name"];
			$school_code = $row["school_code"];
			$school_type = $row["type"];
			$school_area = $row["area"];
			$school_location = $row["location"];
			$school_telephone = $row["Telephone"];
			$school_email = $row["Email"];
			$school_website = $row["website"];
			$MRT = $row["Nearest_MRT"];
			$Bus = $row["Bus_number"];
			$cca = $row["CCA"];
			$school_subject = $row["subjects"];


			if(empty($row['highest']))
				$psle_score = $row["lowest"];
			else
				$psle_score = $row['lowest']."-".$row['highest'];


			$search_result_school[$i] = [
				'school_name' => $school_name,
				'school_code' => $school_code,
				'school_type' => $school_type,
				'school_area' => $school_area,
				'school_location' => $school_location,
				'school_telephone' => $school_telephone,
				'school_email' => $school_email,
				'school_website' => $school_website,
				'Nearest_MRT' => $MRT,
				'PSLE_score'=>$psle_score,
				'Bus_number' => $Bus,
				'CCA' => $cca,
				'school_subject' => $school_subject];

			$i++;
		}
	}
	else
	{
		$search_result_school = $search_result_school;
	}

	dbDisconnect($conn);

	return $search_result_school;
}

function searchJC($area, $subjects, $type, $score, $MRT, $Bus, $code)
{
	$conn = dbConnect();

	$whereClause = " where school.type = 'Junior College' ";

	if(!empty($area))
	{
		$area=strtoupper($area);
		$whereClause .= " and upper(school.area) like '%$area%'";
	}

	if(!empty($subjects))
	{
		$subjects=strtoupper($subjects);
		$whereClause .= " and school.subjects like '%$subjects%'";
	}

	if(!empty($MRT))
		$whereClause .= " and school.Nearest_MRT like '%$MRT%'";

	if(!empty($Bus))
		$whereClause .= " and school.Bus_number like '%$Bus%'";

	if(!empty($code))
		$whereClause .= " and school.school_code like '%$code%'";

	if(!empty($type)){
		if($type=="Art")
		{
		  $sql = "select * from school".$whereClause." and jc_art<=$score";
		}
		else
		{
			$sql = "select * from school".$whereClause." and jc_science<=$score";
		}
	}

	$whereClause .= " and school.type like 'Junior College'";

    $result = $conn->query($sql);
	//echo $sql; die;
	if ($result == false){
 		echo "No search found!";
		die;
	}

	// Create an empty array to store each row of school information
	$search_result_school = array();
	$i = 0;

	if (!empty($result) && $result->num_rows > 0)	// if the number of result is greater than 0
	{
		while($row = $result->fetch_assoc())	// get each result and put them into the array
		{
			$school_name = $row["school_name"];
			$school_code = $row["school_code"];
			$school_type = $row["type"];
			$school_area = $row["area"];
			$school_location = $row["location"];
			$school_telephone = $row["Telephone"];
			$school_email = $row["Email"];
			$school_website = $row["website"];
			$MRT = $row["Nearest_MRT"];
			$Bus = $row["Bus_number"];
			$cca = $row["CCA"];
			$school_subject = $row["subjects"];


			$search_result_school[$i] = [
				'school_name' => $school_name,
				'school_code' => $school_code,
				'school_type' => $school_type,
				'school_area' => $school_area,
				'school_location' => $school_location,
				'school_telephone' => $school_telephone,
				'school_email' => $school_email,
				'school_website' => $school_website,
				'Nearest_MRT' => $MRT,
				'Busnumber' => $Bus,
				'CCA' => $cca,
				'school_subject' => $school_subject];
			$i++;
		}
	}
	else
	{
		$search_result_school = $search_result_school;
	}

	dbDisconnect($conn);

	return $search_result_school;
}

function searchPoly($area, $course_cluster, $courseTitle, $score, $MRT, $Bus, $code)
{
	$conn = dbConnect();

	$whereClause = " where school.type = 'Polytechnic' ";

	if(!empty($area))
	{
		$area=strtoupper($area);
		$whereClause .= " and upper(school.area) like '%$area%'";
	}

	if(!empty($course_cluster))
		$whereClause .= " and poly.Course_Cluster like '%$course_cluster%'";

	if(!empty($Course_Title))
		$whereClause .= " and poly.Course_Title like '%$Course_Title%'";

	if(!empty($MRT))
		$whereClause .= " and school.Nearest_MRT like '%$MRT%'";

	if(!empty($Bus))
		$whereClause .= " and school.Bus_number like '%$Bus%'";

	if(!empty($code))
		$whereClause .= " and school.school_code like '%$code%'";

	$whereClause .= " and school.type like 'Polytechnic'";

	$sql = "select * from school left join poly on school.school_id = poly.school_id ".$whereClause." and poly.JAE_Score<=$score";

    $result = $conn->query($sql);
	if ($result == false){
 		echo "No search found!";
		die;
	}

	// Create an empty array to store each row of school information
	$search_result_school = array();
	$i = 0;

	if (!empty($result) && $result->num_rows > 0)	// if the number of result is greater than 0
	{
		while($row = $result->fetch_assoc())	// get each result and put them into the array
		{
			$school_name = $row["school_name"];
			$school_code = $row["school_code"];
			$school_type = $row["type"];
			$school_area = $row["area"];
			$school_location = $row["location"];
			$school_telephone = $row["Telephone"];
			$school_email = $row["Email"];
			$school_website = $row["website"];
			$MRT = $row["Nearest_MRT"];
			$Bus = $row["Bus_number"];
			$course_cluster = $row["Course_Cluster"];
			$courseTitle = $row["Course_Title"];
			$cca = $row["CCA"];

			$search_result_school[$i] = [
				'school_name' => $school_name,
				'school_code' => $school_code,
				'school_type' => $school_type,
				'school_area' => $school_area,
				'school_location' => $school_location,
				'school_telephone' => $school_telephone,
				'school_email' => $school_email,
				'school_website' => $school_website,
				'Nearest_MRT' => $MRT,
				'Busnumber' => $Bus,
				'course_cluster' => $course_cluster,
				'courseTitle' => $courseTitle,
				'CCA' => $cca
				];
			$i++;
		}
	}
	else
	{
		$search_result_school = $search_result_school;
	}

	dbDisconnect($conn);

	return $search_result_school;
}
function searchITE($English, $Mathematics, $certification, $option, $mrt, $bus,$code)
{
	$conn = dbConnect();

	$whereClause = " where 1=1 ";

	if(!empty($English))
		$whereClause .= " and ITE.english>=$English ";

	if(!empty($Mathematics))
		$whereClause .= " and ITE.mathematics>=$Mathematics";
	if(!empty($MRT))
		$whereClause .= " and school.Nearest_MRT like '%$MRT%'";

	if(!empty($Bus))
		$whereClause .= " and school.Bus_number like '%$Bus%'";

	if(!empty($code))
		$whereClause .= " and school.school_code like '%$code%'";

	$whereClause .= " and school.type like 'ITE'";

	if($certification=="Nlevel")
	{
		if($option=="oneSubject")
		$sql = "select * from school left join ite on school.school_id = ite.school_id".$whereClause. " and ITE.Certification ='Nlevel' and ITE.OneSubject=1";
		else
		$sql = "select * from school left join ite on school.school_id = ite.school_id".$whereClause. " and ITE.Certification = 'Nlevel' and ITE.TwoSubject=1";

	}
    else if($certification=="Olevel")
	{
		if($option=="oneSubject")
		$sql = "select * from school left join ite on school.school_id = ite.school_id".$whereClause. " and ITE.Certification ='Olevel' and ITE.OneSubject=1";
		else
		$sql = "select * from school left join ite on school.school_id = ite.school_id".$whereClause. " and ITE.Certification ='Olevel' and ITE.TwoSubject=1";

	}

	else
	{
		if($option=="oneSubject")
		$sql = "select * from school left join ite on school.school_id = ite.school_id".$whereClause. " and ITE.Certification like '%Olevel/Nlevel%' and ITE.OneSubject=1";
		else
		$sql = "select * from school left join ite on school.school_id = ite.school_id".$whereClause. " and ITE.Certification like '%Olevel/Nlevel%' and ITE.TwoSubject=1";

	}

    $result = $conn->query($sql);
	if ($result == false){
 		echo "No search found!";
		die;
	}

	// Create an empty array to store each row of school information
	$search_result_school = array();
	$i = 0;

	if (!empty($result) && $result->num_rows > 0)	// if the number of result is greater than 0
	{
		while($row = $result->fetch_assoc())	// get each result and put them into the array
		{
			$school_name = $row["school_name"];
			$course_name = $row["ITE_Course"];
			$school_code = $row["school_code"];
			$school_type = $row["type"];
			$school_area = $row["area"];
			$school_location = $row["location"];
			$school_telephone = $row["Telephone"];
			$school_email = $row["Email"];
			$school_website = $row["website"];
			$MRT = $row["Nearest_MRT"];
			$Bus = $row["Bus_number"];
			$cca = $row["CCA"];

			$search_result_school[$i] = [
				'school_name' => $school_name,
				'ITE_Course'=>$course_name,
				'school_code' => $school_code,
				'school_type' => $school_type,
				'school_area' => $school_area,
				'school_location' => $school_location,
				'school_telephone' => $school_telephone,
				'school_email' => $school_email,
				'school_website' => $school_website,
				'Nearest_MRT' => $MRT,
				'Busnumber' => $Bus,
				'CCA' => $cca
				];
			$i++;
		}
	}
	else
	{
		$search_result_school = $search_result_school;
	}

	dbDisconnect($conn);

	return $search_result_school;
}

function searchUni($university_name, $area, $course_name, $MRT, $Bus, $code)
{
	$conn = dbConnect();

	$whereClause = " where school.type = 'University' ";

	if(!empty($university_name))
		$whereClause .= " and university.university_name like '%$university_name%'";

	if(!empty($area))
	{
		$area=strtoupper($area);
		$whereClause .= " and upper(school.area) like '%$area%'";
	}

	if(!empty($course_name))
		$whereClause .= " and university.course_name like '%$course_name%'";

	if(!empty($MRT))
		$whereClause .= " and school.Nearest_MRT like '%$MRT%'";

	if(!empty($Bus))
		$whereClause .= " and school.Bus_number like '%$Bus%'";

	if(!empty($code))
		$whereClause .= " and school.school_code like '%$code%'";

		$whereClause .= " and school.type like 'University'";


	$sql = "select * from university left join school on university.school_id = school.school_id ".$whereClause;

	$result = $conn->query($sql);
	if ($result == false){
 		echo "No search found!";
		die;
	}

	// Create an empty array to store each row of school information
	$search_result_school = array();
	$i = 0;

	if (!empty($result) && $result->num_rows > 0)	// if the number of result is greater than 0
	{
		while($row = $result->fetch_assoc())	// get each result and put them into the array
		{

			$search_result_school[$i] = [
				'University_name' => $row["University_name"],
				'course_name' => $row["course_name"],
				'poly_10_percent' => $row["poly_10_percent"],
				'poly_90_percent' => $row["poly_90_percent"],
				'JC_10_percent' => $row["JC_10_percent"],
				'JC_90_percent' => $row["JC_90_percent"],
				'location' => $row["location"],
				'Telephone' => $row["Telephone"],
				'website' => $row["website"],
				'Email' => $row["Email"],
				'Nearest_MRT' => $row["Nearest_MRT"],
				'Bus_number' => $row["Bus_number"],
				];

			$i++;
		}
	}
	else
	{
		$search_result_school = $search_result_school;
	}
	dbDisconnect($conn);



	return $search_result_school;
}

//added for favourite list
function get_fav_list($username)
{
	$conn = dbConnect();
	$result = $conn->query("SELECT * FROM fav_list WHERE username like '%$username';");

	$search_result_school = array();
	$i = 0;

	if (!empty($result) && $result->num_rows > 0)	// if the number of result is greater than 0
	{
		while($row = $result->fetch_assoc())	// get each result and put them into the array
		{
			$school_name = $row["schoolname"];
			array_push($search_result_school, $school_name);
		}
	}
	else
	{
		$search_result_school = $search_result_school;
	}

	dbDisconnect($conn);

	return $search_result_school;
}

function add_to_fav_list($username, $school_name)
{
	$conn = dbConnect();
	return $conn->query("INSERT into fav_list value('$username','$school_name');");
	// dbDisconnect($conn);
}

function remove_from_fav_list($username, $school_name)
{
	$conn = dbConnect();
	return $conn->query("DELETE FROM fav_list where username like '$username' AND schoolname like '$school_name';");
	// dbDisconnect($conn);
}

//added for compare list
function get_all_compare_list($username)
{
	$conn = dbConnect();
	return $conn->query("Select * FROM school");
}

function get_compare_list($username,$start_from,$num_rec_per_page)
{
	$conn = dbConnect();
	return $conn->query("Select * FROM school");
}

function get_comparables_school($id)
{
	$conn = dbConnect();
	return $conn->query("SELECT * FROM school where school_name='$id'");
}

function get_ite_courses()
{
	$conn = dbConnect();
	return $conn->query("SELECT ITE_Course FROM ite");
}

function get_poly_courses()
{
	$conn = dbConnect();
	return $conn->query("SELECT Course_Title FROM poly");
}

function get_uni_courses()
{
	$conn = dbConnect();
	return $conn->query("SELECT course_name FROM university");
}
?>