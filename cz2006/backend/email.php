<?php
include_once ("Mail.php");

function SendEmail($email)
{
	$methods = openssl_get_cipher_methods();
	$method = $methods[12];
	$key = 'educarepasswordshit123';
	$encrypted = openssl_encrypt($email, $method, $key);
	$link = str_replace("+", "%2B", $encrypted);

	try
	{
		$from = 'educaresys.sg@gmail.com';
		$to = $email;
		$subject = 'Reset Password';
		$body = "You have requested to change your password.\n".
				"Click the link below to reset your password!\n".
				"http://127.0.0.1/ResetPassword.php?id=".
				$link;

		$headers = array(
		    'From' => $from,
		    'To' => $to,
		    'Subject' => $subject
		);

		$smtp = Mail::factory('smtp', array(
		        'host' => 'ssl://smtp.gmail.com',
		        'port' => '465',
		        'auth' => true,
		        'username' => 'educaresys.sg@gmail.com',
		        'password' => 'justdoit123'
		    ));

		// Send the mail
		$mail = $smtp->send($to, $headers, $body);
	}
	catch (Exception $e)
	{
	    echo "Exception: " . $e->getMessage();
    }
}
?>