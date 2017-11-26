<?php
include('dbManager.php');
session_start();

$username = $_POST['username'];
$password = $_POST['password'];
$email = $_POST['email'];
$password = md5($password);
$conn = dbConnect();

$sql =  "SELECT `Username` FROM `User_Main`";
$result = $conn->query($sql);

if ($result->num_rows > 0)
{
	while($obj=mysqli_fetch_object($result))
	{
		if ($username == $obj->Username)
		{
			echo "<script>window.location.replace('../register.php?error=1')</script>";
			exit();
		}
	}
}

$sql =  "INSERT INTO `User_Main` (`Username`, `Password`, `Email`) VALUES ('$username', '$password', '$email')";
$conn->query($sql);
dbDisconnect($conn);
echo "<script>window.location.replace('../login.php')</script>";
?>