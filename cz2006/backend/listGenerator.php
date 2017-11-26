<?php

include_once('dbManager.php');

$conn = dbConnect();

$cca_typeahead=array(
	'cca_options'=>"'"
);

$sql = "select * from cca_list";
$result=$conn->query($sql);

$i = 1;

if ($result->num_rows > 0)	// if the number of result is greater than 0
{
	while($row = $result->fetch_assoc())	// get each result and put them into the array
	{
		$row_item = str_replace("'", "&#39;", $row['CCA']);

		if($i == $result->num_rows){
			$cca_typeahead['cca_options'] .= $row_item."'";
		}
		else
		{
			$cca_typeahead['cca_options'] .= $row_item."', '";
		}

		$i++;
	}

}

$subject_typeahead=array(
	'subjects'=>"'"
);

$sql= "select subjects from subjects";
$result=$conn->query($sql);

$i = 1;

if ($result->num_rows > 0)	// if the number of result is greater than 0
{
	while($row = $result->fetch_assoc())	// get each result and put them into the array
	{
		$row_item = str_replace("'", "&#39;", $row['subjects']);

		if($i == $result->num_rows){
			$subject_typeahead['subjects'] .= $row_item."'";
		}
		else
		{
			$subject_typeahead['subjects'] .= $row_item."', '";
		}

		$i++;
	}

}

$mrt_typeahead=array(
	'nearest_mrt'=>"'"
);

$sql= "select nearest_mrt from nearest_mrt";
$result=$conn->query($sql);

$i = 1;

if ($result->num_rows > 0)	// if the number of result is greater than 0
{
	while($row = $result->fetch_assoc())	// get each result and put them into the array
	{
		if($i == $result->num_rows){
			$mrt_typeahead['nearest_mrt'] .= $row['nearest_mrt']."'";
		}
		else
		{
			$mrt_typeahead['nearest_mrt'] .= $row['nearest_mrt']."', '";
		}

		$i++;
	}

}
$j = 0;


$area_typeahead=array(
	'area'=>"'"
);

$sql = "select area_name from area_name";
$result=$conn->query($sql);

$j = 1;

if ($result->num_rows > 0)	// if the number of result is greater than 0
{
	while($row = $result->fetch_assoc())	// get each result and put them into the array
	{
		if($j == $result->num_rows){
			$area_typeahead['area'] .= $row['area_name']."'";
		}
		else
		{
			$area_typeahead['area'] .= $row['area_name']."', '";
		}

		$j++;
	}

}

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

$uni_typeahead = array(
	'university_name' => "'",
	'location' => "'",
	'course_name' => "'"
	);
$sql = "select distinct school_name, area from school where type = 'University'";
$result = $conn->query($sql);

$i = 1;

if ($result->num_rows > 0)	// if the number of result is greater than 0
{
	while($row = $result->fetch_assoc())	// get each result and put them into the array
	{
		if($i == $result->num_rows){
			$uni_typeahead['university_name'] .= $row['school_name']."'";
			$uni_typeahead['location'] .= $row['area']."'";
		} else {
			$uni_typeahead['university_name'] .= $row['school_name']."', '";
			$uni_typeahead['location'] .= $row['area']."', ' ";
		}

		$i++;
	}

}
// Uni-typeahead
$uni_typeahead = array(
	'university_name' => "'",
	'location' => "'",
	'course_name' => "'"
	);
$sql = "select distinct school_name, area from school where type = 'University'";
$result = $conn->query($sql);

$i = 1;

if ($result->num_rows > 0)	// if the number of result is greater than 0
{
	while($row = $result->fetch_assoc())	// get each result and put them into the array
	{
		if($i == $result->num_rows){
			$uni_typeahead['university_name'] .= $row['school_name']."'";
			$uni_typeahead['location'] .= $row['area']."'";
		} else {
			$uni_typeahead['university_name'] .= $row['school_name']."', '";
			$uni_typeahead['location'] .= $row['area']."', ' ";
		}

		$i++;
	}

}

$sql = "select distinct course_name from university";
$result = $conn->query($sql);
$i = 1;

if ($result->num_rows > 0)	// if the number of result is greater than 0
{
	while($row = $result->fetch_assoc())	// get each result and put them into the array
	{
		if($i == $result->num_rows){
			$uni_typeahead['course_name'] .= $row['course_name']."'";
		} else {
			$uni_typeahead['course_name'] .= $row['course_name']."', '";
		}

		$i++;
	}

}

// jc_typeahead
$jc_typeahead = array(
	'location' => "'"
	);
$sql = "select distinct area from school where type = 'Junior College'";
$result = $conn->query($sql);

$i = 1;

if ($result->num_rows > 0)	// if the number of result is greater than 0
{
	while($row = $result->fetch_assoc())	// get each result and put them into the array
	{
		if($i == $result->num_rows){
			$jc_typeahead['location'] .= $row['area']."'";
		} else {
			$jc_typeahead['location'] .= $row['area']."', ' ";
		}

		$i++;
	}

}

// poly_typeahead

$poly_typeahead = array(
	'area' => "'",
	'course_cluster' => "'",
	'courseTitle' => "'"
	);
$sql = "select distinct area, Course_cluster, Course_Title from school left join poly on school.school_id = poly.school_id where school.type = 'Polytechnic'";
$result = $conn->query($sql);

$i = 1;

if ($result->num_rows > 0)	// if the number of result is greater than 0
{
	while($row = $result->fetch_assoc())	// get each result and put them into the array
	{
		if($i == $result->num_rows){
			$poly_typeahead['area'] .= $row['area']."'";
			$poly_typeahead['course_cluster'] .= $row['Course_cluster']."'";
			$poly_typeahead['courseTitle'] .= $row['Course_Title']."'";
		} else {
			$poly_typeahead['area'] .= $row['area']."','";
			$poly_typeahead['course_cluster'] .= $row['Course_cluster']."','";
			$poly_typeahead['courseTitle'] .= $row['Course_Title']."','";
		}

		$i++;
	}

}

dbDisconnect($conn)
?>