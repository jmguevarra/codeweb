<?php
	require "connection.php";
	
	if(isset($_POST['yr_sec']) && isset($_POST['sec_id'])){
		$yr_sec = $_POST['yr_sec'];
		$sec_id = $_POST['sec_id'];
		
		
		$updateQuery = "delete from cw_sections where sec_id = $sec_id  ";
		$result = mysqli_query($my_connection, $updateQuery);
			if($result > 0){
				$deleteQuery = "delete from users_tbl where Section = '$yr_sec' ";
				$delete = mysqli_query($my_connection, $deleteQuery);
					if($delete > 0){
						echo "success";
						exit;
					}else{
						echo "failed";
						exit;
					}
			}else{
				echo "failed";
				exit;
			}
	}
	
	if(isset($_POST['old_yr_sec']) && isset($_POST['old_sec_id'])){
		$yr_sec = $_POST['old_yr_sec'];
		$sec_id = $_POST['old_sec_id'];
		
		
		$updateQuery = "delete from old_cw_sections where sec_id = $sec_id  ";
		$result = mysqli_query($my_connection, $updateQuery);
			if($result > 0){
				$deleteQuery = "delete from old_users_tbl where Section = '$yr_sec' ";
				$delete = mysqli_query($my_connection, $deleteQuery);
					if($delete > 0){
						echo "success";
						exit;
					}else{
						echo "failed";
						exit;
					}
			}else{
				echo "failed";
				exit;
			}
	}
	if(isset($_POST['ID'])){
		$ID = $_POST['ID'];
		
		
		$deleteQuery = "delete from users_tbl where ID = $ID   ";
		$delete = mysqli_query($my_connection, $deleteQuery);
			if($delete > 0){
				$Query = "delete from score_tbl where ID = $ID   ";
				$dlt = mysqli_query($my_connection, $Query);
					if($dlt > 0){
						echo "success";
						exit;
					}else{
						echo "failed";
						exit;
					}
			}else{
				echo "failed";
				exit;
			}
			
	}


	if(isset($_POST['old_ID'])){
		$ID = $_POST['old_ID'];
		
		
		$deleteQuery = "delete from old_users_tbl where ID = $ID   ";
		$delete = mysqli_query($my_connection, $deleteQuery);
			if($delete > 0){
				$Query = "delete from old_score_tbl where ID = $ID   ";
				$dlt = mysqli_query($my_connection, $Query);
					if($dlt > 0){
						echo "success";
						exit;
					}else{
						echo "failed";
						exit;
					}
			}else{
				echo "failed";
				exit;
			}
			
	}	

	if(isset($_POST['Lesson_ID'])){
		$ID = $_POST['Lesson_ID'];
		
		$deleteQuery = "delete from cw_lessons where lsn_id = $ID ";
		$delete = mysqli_query($my_connection, $deleteQuery);
			
			if($delete > 0){
				echo "success";
				exit;
			}else{
				echo "failed";
				exit;
			}
			
	}
		
		if(isset($_POST['quiz_id'])){
		$ID = $_POST['quiz_id'];
		
		$deleteQuery = "delete from cw_quiz where quiz_Id = $ID ";
		$delete = mysqli_query($my_connection, $deleteQuery);
			
			if($delete > 0){
				echo "success";
				exit;
			}else{
				echo "failed";
				exit;
			}
	}
				
?>