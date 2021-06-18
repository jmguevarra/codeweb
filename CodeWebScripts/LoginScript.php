<?php
	require "connection.php";
//Searching for the Login
 if(isset($_POST['username']) && isset($_POST['password'])){

	$user_name =$_POST['username'];
	$user_pass = $_POST['password'];
	$myLoginQuery = "select * from users_tbl where Username = '$user_name' and Password = '$user_pass'";
	
	$loginResult = mysqli_query($my_connection, $myLoginQuery);	
	if(mysqli_num_rows($loginResult) > 0){
		$selectQuery = "select * from users_tbl where Username = '$user_name' and Password = '$user_pass'";
		$result = mysqli_query($my_connection,$selectQuery);
		while($row=mysqli_fetch_array($result)){
		   $data[] = $row;
		}
		echo json_encode($data);
	}else{
		echo 'invalid';
	}

	mysqli_close($my_connection);

}
//Student ID Searching
if(isset($_POST['Student_ID'])){

	$Student_ID = $_POST['Student_ID'];
	$Id_Query = "select * from users_tbl where Student_ID = '$Student_ID'"; 
	$Id_Result = mysqli_query($my_connection, $Id_Query);

	if(mysqli_num_rows($Id_Result) > 0){
		echo 'success';
		exit;
	}else{
		echo 'failed';
		exit;
	}

	mysqli_close($my_connection);
}
	
?>