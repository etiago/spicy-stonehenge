<?php
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<?php
require_once ("request_processor.php");

$successfulRegistration = false;
$invalidInformation = false;

if (isset($_POST['REGISTERUSER']))
{
	/*New user registration*/
	$userID = $_POST['REQUESTEDID'];
	$openBalance = $_POST['OPENBALANCE'];
	$fullname = $_POST['FULLNAME'];
	$email = $_POST['EMAIL'];
	$address = $_POST['ADDRESS'];
	$password = $_POST['PASSWORD'];
	$creditcard = $_POST['CREDITCARD'];
	$confpassword = $_POST['CONFIRMATIONPASSWORD'];
	/*add currency type*/
	$currencytype = $_POST['CURRENCYTYPE'];

	if ($userID == NULL || $password != $confpassword)
	{
		$invalidInformation = TRUE;
	}
	else
	{
		$response = RegisterUser($userID, $password, $fullname, 
			$address, $email, $creditcard, $openBalance, $currencytype);
		if ($response)
		{
			$successfulRegistration = TRUE;
		}
		else
		{
			$invalidInformation = TRUE;
		}
	}
}
?>

<html xmlns="http://www.w3.org/1999/xhtml">

	<head>
		<meta http-equiv="content-type" content="text/html;charset=utf-8" />
		<meta name="generator" content="Adobe GoLive" />
		<title>Apache Stonehenge - PHP StockTrader - Register</title>
		<link href="style.css" rel="stylesheet" type="text/css" media="all" />
	</head>

	<body>
		<div id="content">
			<div id="header">
				<div class="logo"><img src="images/logo.png"></div>
			</div>
			<div id="header-links">
				<table>
					<tr>
					<td>
						<a href="index.php">Welcome</a>
					</td>
					<td>
						<a href="home.php">Home</a>
					</td>
					<td>
						<a href="account.php">Account</a>
					</td>
					<td>
						<a href="portfolio.php">Portfolio</a>
					</td>
					<td>
						<a href="quotes.php">Quotes/Trade</a>
					</td>
					<td>
						<a href="glossary.php">Glossary</a>
					</td>
					<td>
						<a href="config.php">Config</a>
					</td>
					<td>
						<a href="login.php">Login/Logout</a>
					</td>
					</tr>
				</table>
			</div>
			<div id="middle">
				<div class="main-title">
					<h1>Register</h1>
					<script type="text/javascript">
						var thisdate = new Date();
						document.writeln(thisdate.toLocaleString());
					</script>
				</div>

				<?php
				if ($successfulRegistration)
				{
					print("<p style=\"color: red\" align=\"center\">
						Registration was successful, please <a href =\"login.php\">login</a>.</p>");
				}

				else
				{
					if ($invalidInformation)
					{
						print("<p style=\"color: red\" align=\"center\">Please enter valid information.</p>");
					}
					print ("<table class=\"profile\" cellspacing=\"0\" width=\"100%\">
					<thead>
					<tr>
					<th>
					Create Account Profile:
					</th>
					</tr>
					</thead>
					<tbody>
					<tr>
						<td>
							<form action=\"register.php\" method=\"post\">
							<table cellspacing=\"0\" align=\"center\">
								<tr>
									<td colspan=\"4\" >
										&nbsp;
									</td>
								</tr>
								<tr>
									<td>Requested ID:</td>
									<td><input name=\"REQUESTEDID\" type=\"text\" id=\"\" size=\"25\"/></td>
								</tr>
								<tr>
									<td>Opening Balance:</td>
									<td><input type=\"text\" name=\"OPENBALANCE\" value=\"100000\" id=\"\" size=\"25\"/></td>
									<td>Currency Type:</td>
									<td><select name=\"CURRENCYTYPE\" align=\"center\">
										<option value=\"€\">EUR</option>
										<option value=\"$\">USD</option>
										<option value=\"￥\">RMB</option>
									</select></td>

								</tr>
								<tr>
									<td>Full Name:</td>
									<td><input type=\"text\" name=\"FULLNAME\" id=\"\" size=\"25\"/></td>
									<td>Email Address:</td>
									<td><input type=\"text\" name=\"EMAIL\" id=\"\" size=\"25\"/></td>
								</tr>
								<tr>
									<td>Address:</td>
									<td><input name=\"ADDRESS\" type=\"text\" id=\"\" size=\"25\"/></td>
									<td>Password:</td>
									<td><input name=\"PASSWORD\" type=\"password\" id=\"\" size=\"25\"/></td>
								</tr>
								<tr>
									<td>Credit Card:</td>
									<td><input name=\"CREDITCARD\" type=\"text\" id=\"\" size=\"25\"/></td>
									<td>Confirm Password:</td>
									<td><input name=\"CONFIRMATIONPASSWORD\" type=\"password\" id=\"\" size=\"25\"/></td>
								</tr>
								<tr>
									<td colspan=\"4\" class=\"button\">
										<input type=\"submit\" name=\"REGISTERUSER\" value=\"Register\" class=\"button\"/>
									</td>
								</tr>
								
							</table>
							</form>	

						</td>
					</tr>
					</tbody>
					</table>");
				}
			?>   
			</div>
			<div id="footer">
				<div style="float:right;">Powered by 
				<a href="http://wso2.org/projects/wsf/php"><img align="top" src="images/powered-by-logo.gif" style="margin-top:-3px; margin-left: 0px;"/></a></div>
			</div>

		</div>
	</body>

</html>
