<?PHP
    include_once("connection.php");
	
	if(isset($_POST['addAlert']) && $_POST['addAlert'] == "addAlert" && isset($_POST['yr_sec'])){
		$yr_sec = $_POST['yr_sec'];
		
	    $checkQuery = "select * from cw_sections where yr_sec='$yr_sec'";
		$check = mysqli_query($my_connection, $checkQuery);
	
		if(mysqli_num_rows($check) > 0){
			echo "same";
			exit;
		}else{
			echo "failed";
			exit;
		}
	}
	
?>