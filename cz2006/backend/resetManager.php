<?php
include('dbManager.php');
include_once('email.php');

$email = $_POST['email'];
$conn = dbConnect();

$sql =  "SELECT Email FROM `User_Main` WHERE `Email`='$email'";
$result = $conn->query($sql);
dbDisconnect($conn);

if (isset($result) && !empty($result) && $result->num_rows > 0)
{
	SendEmail($email);

	echo "<script>window.location.replace('../login.php')</script>";
}
else
{
	echo "<script>window.location.replace('../forgetpassword.php?error=1')</script>";
}
?>