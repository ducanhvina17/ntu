<?php
header('Location: ' . $_SERVER['HTTP_REFERER']);
include_once('../backend/searchManager.php');

if (!isset($_COOKIE['signed_in_id'])){
	header('Location: ' . '../login.php');
}else{

	$fav_list = get_fav_list($_COOKIE['signed_in_id']);

	//var_dump($_POST);
	if(isset($_POST['favorite']))
	{
		// echo "testing";
		$school_name = $_POST['favorite'];
		
		if(!in_array($_POST['favourite'],$fav_list)){
			add_to_fav_list($_COOKIE['signed_in_id'], $school_name);
		}else{
			echo 'Duplicate exist';
		}
	}
	else if(isset($_POST['unfavorite']))
	{
		$school_name = $_POST['unfavorite'];
		if(in_array($school_name,$fav_list)){
			remove_from_fav_list($_COOKIE['signed_in_id'], $school_name);
		}else{
			echo 'Record not found!';
		}
	}
}
?>