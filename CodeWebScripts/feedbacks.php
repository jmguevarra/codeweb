<?php
	require "connection.php";

//Load
	if(isset($_POST['feedbacks'])){

		$loadQuery = "select * from cw_feedbacks order by ID desc";
		$result = mysqli_query($my_connection, $loadQuery);

		while($row = mysqli_fetch_assoc($result)){
				$data[] = $row;
		}
		echo json_encode($data);
		mysqli_close($my_connection);

	}
//Insert
	if(isset($_POST['ID']) && isset($_POST['Image']) && isset($_POST['Fullname']) && isset($_POST['Stud_ID']) && isset($_POST['Section']) && isset($_POST['Message'])){
		$id = $_POST['ID'];
		$Image = $_POST['Image'];
		$Fullname = $_POST['Fullname'];
		$Stud_ID = $_POST['Stud_ID'];
		$Section = $_POST['Section'];
		$Message = $_POST['Message'];
		$Date = date('m/d/y');

			$inQuery = "insert into cw_feedbacks(`Image`,`Fullname`,`Student_ID`,`Section`,`Message`,`Date`) values ('$Image','$Fullname','$Stud_ID','$Section','$Message','$Date')";
			$result = mysqli_query($my_connection, $inQuery);
				if($result > 0){
					echo "success";
					exit;
				}else{
					echo "failed";
				}
		
    	mysqli_close($my_connection);

	}



?>