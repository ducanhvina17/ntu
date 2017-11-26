<?php include_once('header.php') ?>
<?php echo '<script src="js/checkForgetForm.js"></script>'; ?>
<!-- Begin page content -->
<div class="container">
	<div class="row">
		<h3 id="errorMsg">
			<?php
			if(isset($_GET['error']))
			{
				echo "<h3>Invalid email!</h3>";
			}
			?>
		</h3>
		<div class="col-xs-12 text-center">
			<h1>Forget Password</h1>
		</div>
	</div>
	<div class="row">
		<form name="loginForm" action="backend/resetManager.php" method="post">
			<div class="col-xs-4"></div>
			<div class="col-xs-4">
				<div class="row login-row-margin">
					<div class="col-xs-12 text-left">
						<input type="text" id="email" name="email" class="form-control" placeholder="E-mail" />
						<br>
					</div>
				</div>
				<div class="row login-row-margin">
					<div class="col-xs-6 text-center">
						<button id="resetBtn" class="btn btn-primary" type="submit">Reset</button>
					</div>
				</div>
			</div>
			<div class="col-xs-4"></div>
		</form>
	</div>
</div>
<?php include_once('footer.php') ?>