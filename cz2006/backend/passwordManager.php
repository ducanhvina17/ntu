<?php
include('dbManager.php');
session_start();

$email = $_SESSION['rid'];
$password = $_POST['password'];

$password = md5($password);

$conn = dbConnect();
$sql =  "UPDATE `User_Main` SET `Password` = '$password' WHERE `Email` = '$email'";
$conn->query($sql);
dbDisconnect($conn);
echo "<script>window.location.replace('../login.php')</script>";
?>