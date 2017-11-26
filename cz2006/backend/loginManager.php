<?php
include('dbManager.php');
// header('Location: ' . $_SERVER['HTTP_REFERER']);
$username = $_POST['username'];
$password = $_POST['password'];
$password = md5($password);
$conn = dbConnect();

$sql =  "SELECT * FROM `User_Main` WHERE `Username`='$username' and `Password`='$password'";
$result = $conn->query($sql);
dbDisconnect($conn);

if ($result->num_rows > 0)
{
	while($obj=mysqli_fetch_object($result))
	{
		setcookie('signed_in_id', $obj->Username, time() + (60*60*24*100), "/");
		setcookie('email', $obj->Email, time() + (60*60*24*100), "/");
	}
	
	echo "<script>window.location.replace('../index.php')</script>";
}
else
{
	echo "<script>window.location.replace('../login.php?error=1')</script>";
}
?>