<?php include_once('header.php') ?>
<?php echo '<script src="js/checkRegistrationForm.js"></script>'; ?>
<!-- Begin page content -->
<div class="container">
	<div class="row">
		<h3 id="errorMsg">
			<?php
			if(isset($_GET['error']))
			{
				echo "<h3>Username already exist!</h3>";
			}
			?>
		</h3>
		<div class="col-xs-12 text-center">
			<h1>Register</h1>
		</div>
	</div>
	<div class="row">
		<form name="registerForm" action="backend/registerManager.php" method="post">
			<div class="col-xs-4"></div>
			<div class="col-xs-4">
				<div class="row login-row-margin">
					<div class="col-xs-12 text-left">
						<input type="text" id="email" name="email" class="form-control" placeholder="email" />
						<br>
					</div>
				</div>
				<div class="row login-row-margin">
					<div class="col-xs-12 text-left">
						<input type="text" id="username" name="username" class="form-control" placeholder="User Name" />
						<br>
					</div>
				</div>
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
						<button id="registerBtn" class="btn btn-primary" type="submit">Register</button>
						<br>
					</div>
				</div>
			</div>
			<div class="col-xs-4"></div>
		</form>
	</div>
</div>
<?php include_once('footer.php') ?>