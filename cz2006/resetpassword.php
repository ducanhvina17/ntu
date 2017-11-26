<?php
include_once('header.php');

$id = $_GET['id'];
$methods = openssl_get_cipher_methods();
$method = $methods[12];

$key = 'educarepasswordshit123';
$decrypted = openssl_decrypt((string)$id, $method, $key);

$_SESSION['rid']=$decrypted;
?>
<!-- Begin page content -->
<div class="container">
	<div class="row">
		<h3 id="errorMsg">
		</h3>
		<div class="col-xs-12 text-center">
			<h1>Reset Password</h1>
		</div>
	</div>
	<div class="row">
		<form name="registerForm" action="backend/passwordManager.php" method="post">
			<div class="col-xs-4"></div>
			<div class="col-xs-4">
				<div class="row login-row-margin">
					<div class="col-xs-12 text-left">
						<input type="password" id="password" name="password" class="form-control" placeholder="Password" />
						<br>
					</div>
				</div>
				<div class="row login-row-margin">
					<div class="col-xs-12 text-left">
						<input type="password" id="confirmPassword" name="confirmPassword" class="form-control" placeholder="Confirm Password" />
						<br>
					</div>
				</div>
				<div class="row login-row-margin">
					<div class="col-xs-12 text-center">
						<button id="resetBtn" class="btn btn-primary" type="submit">Reset</button>
					</div>
				</div>
			</div>
			<div class="col-xs-4"></div>
		</form>
	</div>
</div>
<?php include_once('footer.php') ?>