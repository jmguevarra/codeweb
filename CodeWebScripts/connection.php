<?php
	$my_database = "codeweb";
	$my_username = "root";
	$my_password = "";
	$my_server = "localhost";
	
	$my_connection = mysqli_connect($my_server,$my_username,$my_password,$my_database) or die ("Unable to Connect");
	
?>