<?php
	require "connection.php";

//Admin Data
	if(isset($_POST['adminData'])){
		$adminID = $_POST['adminData'];
	
		$loadQuery = "select * from users_tbl where ID = $adminID ";
		$result = mysqli_query($my_connection, $loadQuery);

		while($row = mysqli_fetch_assoc($result)){
				$data[] = $row;
		}
		echo json_encode($data);
		mysqli_close($my_connection);

	}


//AdminDataList
if(isset($_POST['type_admin'])){
	$adminData = $_POST['type_admin'];
		$loadQuery = "select * from users_tbl where Type ='$adminData'"; 
			$result = mysqli_query($my_connection, $loadQuery);
			
			while($row = mysqli_fetch_assoc($result)){
					$data[] = $row;
			}
		echo json_encode($data);
		mysqli_close($my_connection);
		
	}


//Sign-Up
	if(isset($_POST['Student_ID'])){
		
		$Student_ID = $_POST['Student_ID'];
		$acct_check =  mysqli_query($my_connection,"select Username, Password, Fullname from users_tbl where Student_ID = '$Student_ID'"); 
		$row = mysqli_fetch_array($acct_check);
		$Username  = $row[0];
		$Password  = $row[1];
		$Fullname  = $row[2];

		if($Username == null && $Password == null && $Fullname != null){
			$result =  mysqli_query($my_connection,"select * from users_tbl where Student_ID = '$Student_ID'"); 
			while($row = mysqli_fetch_assoc($result)){
				$data[] = $row;
			}
				echo json_encode($data);
		}else{
			echo "created";
			exit;
		}

		mysqli_close($my_connection);
	}


//Load Lessson 
	if(isset($_POST['Course']) && isset($_POST['Category'])){
		$course = $_POST['Course'];
		$category = $_POST['Category'];
		
		$loadQuery = "SELECT * FROM cw_lessons where lsn_ptCat = '$category' and lsn_course = '$course' ORDER BY lsn_no ASC"; 
		$result = mysqli_query($my_connection, $loadQuery);
	
		while($row = mysqli_fetch_assoc($result)){
				$data[] = $row;
		}
		echo json_encode($data);
		mysqli_close($my_connection);		
	}

	

//Dont know
	
	if(isset($_POST['overviewData'])){
			$position = $_POST['overviewData'];
			
			$loadQuery = "select lsn_lesson, lsn_code, lsn_result, lsn_trivia from cw_html_overview_lesson where lsn_id=$position"; 
			$result = mysqli_query($my_connection, $loadQuery);
			
			while($row = mysqli_fetch_assoc($result)){
					$data[] = $row;
			}
		echo json_encode($data);	
		mysqli_close($my_connection);
	}	
	
	
//sections Scripts

	if(isset($_POST['sections']) &&  $_POST['sections'] == 'sections'){
		$loadQuery = "select * from cw_sections"; 
			$result = mysqli_query($my_connection, $loadQuery);
			
			if($result == null){
				echo "failed";
				exit;
			}else{
				while($row = mysqli_fetch_assoc($result)){
						$data[] = $row;
				}
			echo json_encode($data);
			mysqli_close($my_connection);
		}
		
	}
// old Sec
	if(isset($_POST['old_sections']) &&  $_POST['old_sections'] == 'old_sections'){

		$loadQuery = "select * from old_cw_sections ORDER BY sec_id asc"; 
			$result = mysqli_query($my_connection, $loadQuery);
			
			while($row = mysqli_fetch_assoc($result)){
					$data[] = $row;
			}
		echo json_encode($data);
		mysqli_close($my_connection);
	}


	if(isset($_POST['list'])){
		$yr_sec = $_POST['list'];
		
		$loadQuery = "select * from users_tbl where Section = '$yr_sec' order by Fullname asc";
		$result = mysqli_query($my_connection, $loadQuery);
		
		while($row = mysqli_fetch_assoc($result)){
			$data[] = $row;
		}
		echo json_encode($data);
		mysqli_close($my_connection);
	}

	if(isset($_POST['oldlist'])){
		$yr_sec = $_POST['oldlist'];
	
		$loadQuery = "select * from old_users_tbl where Section = '$yr_sec' order by Fullname desc";
		$result = mysqli_query($my_connection, $loadQuery);
		
		while($row = mysqli_fetch_assoc($result)){
			$data[] = $row;
		}
		echo json_encode($data);
		mysqli_close($my_connection);
	}
	//Load All Courses
	if(isset($_POST['Courses']) && $_POST['Courses'] == 'courses'){
		
		$loadQuery = "select * from cw_lessons ORDER BY lsn_id DESC";
		$result = mysqli_query($my_connection, $loadQuery);
		
		while($row = mysqli_fetch_assoc($result)){
			$data[] = $row;
		}
		echo json_encode($data);
		mysqli_close($my_connection);
	}

//Select Courses Sp
	if(isset($_POST['sp_course']) && $_POST['sp_course'] == 'sp_course'){
		
		$loadQuery = "select * from cw_courses";
		$result = mysqli_query($my_connection, $loadQuery);
		
		while($row = mysqli_fetch_assoc($result)){
			$data[] = $row;
		}
		
		echo json_encode($data);
		mysqli_close($my_connection);
	}
//Select Category Sp
	if(isset($_POST['category'])){
		$crs_title = $_POST['category'];
		
		$loadQuery = "select * from cw_crs_category where cat_course = '$crs_title'";
		$result = mysqli_query($my_connection, $loadQuery);
		
		while($row = mysqli_fetch_assoc($result)){
			$data[] = $row;
		}
		
		echo json_encode($data);
		mysqli_close($my_connection);
	}
//Select Lesson Sp
	if(isset($_POST['topic'])){
		$cat_title = $_POST['topic'];
		
		$loadQuery = "select * from cw_lessons where lsn_category = '$cat_title'";
		$result = mysqli_query($my_connection, $loadQuery);
		
		while($row = mysqli_fetch_assoc($result)){
			$data[] = $row;
		}
		
		echo json_encode($data);
		mysqli_close($my_connection);
	}

	if(isset($_POST['ID'])){
		$ID = $_POST['ID'];
		
		$loadQuery = "select * from users_tbl where ID = $ID";
		$result = mysqli_query($my_connection, $loadQuery);
		
		while($row = mysqli_fetch_assoc($result)){
			$data[] = $row;
		}
		
		echo json_encode($data);
		mysqli_close($my_connection);
	}

	if(isset($_POST['old_ID'])){
		$ID = $_POST['old_ID'];
		
		$loadQuery = "select * from old_users_tbl where ID = $ID";
		$result = mysqli_query($my_connection, $loadQuery);
		
		while($row = mysqli_fetch_assoc($result)){
			$data[] = $row;
		}
		
		echo json_encode($data);
		mysqli_close($my_connection);
	}

	// Top Students
	if(isset($_POST['Top']) && $_POST['Top'] == 'Top'){
		
		$loadQuery = "SELECT * FROM users_tbl  ORDER BY Points desc LIMIT 5"; 
		$result = mysqli_query($my_connection, $loadQuery);
	
		while($row = mysqli_fetch_assoc($result)){
				$data[] = $row;
		}
		echo json_encode($data);
		mysqli_close($my_connection);
	}

	//Individual Performance
	if(isset($_POST['Scr_Fullname'])  && isset($_POST['Scr_ID'])){
		$ID = $_POST['Scr_ID'];
		$Fullname = $_POST['Scr_Fullname'];

		$loadQuery = "SELECT * FROM score_tbl where  ID = $ID and Fullname = '$Fullname'"; 
		$result = mysqli_query($my_connection, $loadQuery);
	
		while($row = mysqli_fetch_assoc($result)){
				$data[] = $row;
		}
		echo json_encode($data);
		mysqli_close($my_connection);
	}

	if(isset($_POST['old_Scr_Fullname'])  && isset($_POST['old_Scr_ID'])){
		$ID = $_POST['old_Scr_ID'];
		$Fullname = $_POST['old_Scr_Fullname'];

		$loadQuery = "SELECT * FROM old_scores_tbl where  ID = $ID and Fullname = '$Fullname'"; 
		$result = mysqli_query($my_connection, $loadQuery);
	
		while($row = mysqli_fetch_assoc($result)){
				$data[] = $row;
		}
		echo json_encode($data);
		mysqli_close($my_connection);
	}

	if(isset($_POST['Quiz_Fullname'])  && isset($_POST['Quiz_ID'])){
		$ID = $_POST['Quiz_ID'];
		$Fullname = $_POST['Quiz_Fullname'];

		$loadQuery = "SELECT * FROM users_tbl where  ID = $ID and Fullname = '$Fullname'"; 
		$result = mysqli_query($my_connection, $loadQuery);
	
		while($row = mysqli_fetch_assoc($result)){
				$data[] = $row;
		}
		echo json_encode($data);
		mysqli_close($my_connection);
	}

	if(isset($_POST['old_Quiz_Fullname'])  && isset($_POST['old_Quiz_ID'])){
		$ID = $_POST['old_Quiz_ID'];
		$Fullname = $_POST['old_Quiz_Fullname'];

		$loadQuery = "SELECT * FROM old_users_tbl where  ID = $ID and Fullname = '$Fullname'"; 
		$result = mysqli_query($my_connection, $loadQuery);
	
		while($row = mysqli_fetch_assoc($result)){
				$data[] = $row;
		}
		echo json_encode($data);
		mysqli_close($my_connection);
	}


	if(isset($_POST['Quiz']) && isset($_POST['Course']) ){
		$quizCourse = $_POST['Course'];
		
		$loadQuery = "SELECT * FROM cw_quiz  WHERE quiz_Course = '$quizCourse' ORDER BY RAND() LIMIT 10"; 
		$result = mysqli_query($my_connection, $loadQuery);
	
		while($row = mysqli_fetch_assoc($result)){
				$data[] = $row;
		}

		echo json_encode($data);
		mysqli_close($my_connection);

	}


	if(isset($_POST['Quizzes']) && $_POST['Quizzes'] == 'Quizzes'){
		
		$loadQuery = "SELECT * FROM cw_quiz ORDER BY quiz_Id desc"; 
		$result = mysqli_query($my_connection, $loadQuery);
	
		while($row = mysqli_fetch_assoc($result)){
				$data[] = $row;
		}

		echo json_encode($data);
		mysqli_close($my_connection);

	}

	// Select Course 

	if(isset($_POST['excerCourse'])){
		$course = $_POST['excerCourse'];
		
		$loadQuery = "SELECT * FROM cw_lessons where lsn_course = '$course'"; 
		$result = mysqli_query($my_connection, $loadQuery);
	
		while($row = mysqli_fetch_assoc($result)){
				$data[] = $row;
		}

		echo json_encode($data);
		mysqli_close($my_connection);

	}
//get Max lsn_no
	if(isset($_POST['mxCourse']) && isset($_POST['mxCategory'])){
		$crs = $_POST['mxCourse'];
		$cat = $_POST['mxCategory'];

		$loadQuery = "SELECT MAX(lsn_no) FROM cw_lessons where lsn_category = '$cat' and lsn_course = '$crs'"; 
		$result = mysqli_query($my_connection, $loadQuery);
		$row = mysqli_fetch_array($result);
		
		if($row != ""){
			$lsn_no = $row[0] + 1;
			echo $lsn_no;
		}else{
			echo "failed";
			exit;
		}

	}
	
	if(isset($_POST['Videos']) && $_POST['Videos'] == 'Videos'){
		
		$loadQuery = "select * from cw_lessons where lsn_video !=\"\" ORDER BY lsn_id DESC";
		$result = mysqli_query($my_connection, $loadQuery);
		
		while($row = mysqli_fetch_assoc($result)){
			$data[] = $row;
		}
		echo json_encode($data);
		mysqli_close($my_connection);
	}
	
	if(isset($_POST['year']) && $_POST['year'] == 'year'){
		$year = date('Y');
		echo $year;
	}

?>

