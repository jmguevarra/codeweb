<?PHP
    include_once("connection.php");
	
	//Update Whole Sections
	if(isset($_POST['newSec']) && isset($_POST['sec_id']) && isset($_POST['slct_sec'])){
		$yr_sec = $_POST['newSec'];
		$sec_id = $_POST['sec_id'];
		$slct_sec = $_POST['slct_sec'];
		$batch = date('Y');
		
		$checkQuery = "select * from cw_sections where yr_sec='$yr_sec' and batch = '$batch'";
		$check = mysqli_query($my_connection, $checkQuery);
		
		if(mysqli_num_rows($check) > 0){
			echo "same";
			exit;
		}else{
			$updateQuery = "update cw_sections set yr_sec = '$yr_sec' where sec_id = $sec_id and batch = '$batch'";
			$result = mysqli_query($my_connection, $updateQuery);
			if($result > 0){
				$updateSec = "update users_tbl set Section = '$yr_sec' where Section = '$slct_sec' and Batch = '$batch' ";
			    $resultSec = mysqli_query($my_connection, $updateSec);
				if( $resultSec > 0){
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
		
	}

	//Update Whole OldSections
	if(isset($_POST['old_newSec']) && isset($_POST['old_sec_id']) && isset($_POST['old_slct_sec'])){
		$yr_sec = $_POST['old_newSec'];
		$sec_id = $_POST['old_sec_id'];
		$slct_sec = $_POST['old_slct_sec'];
		$batch = date('Y');
		
		$checkQuery = "select * from old_cw_sections where yr_sec='$yr_sec' and batch = '$batch'";
		$check = mysqli_query($my_connection, $checkQuery);
		
		if(mysqli_num_rows($check) > 0){
			echo "same";
			exit;
		}else{
			$updateQuery = "update old_cw_sections set yr_sec = '$yr_sec' where sec_id = $sec_id and batch = '$batch'";
			$result = mysqli_query($my_connection, $updateQuery);
			if($result > 0){
				$updateSec = "update old_users_tbl set Section = '$yr_sec' where Section = '$slct_sec' and Batch = '$batch' ";
			    $resultSec = mysqli_query($my_connection, $updateSec);
				if( $resultSec > 0){
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
		
	}
	//Update Students
	if(isset($_POST['ID']) && isset($_POST['Image']) && isset($_POST['Fullname']) && isset($_POST['Section']) && isset($_POST['Number']) && isset($_POST['Email'])){
		$ID = $_POST['ID'];
		$Fullname = $_POST['Fullname'];
		$Section = $_POST['Section'];
		$Email = $_POST['Email'];
		$Number = $_POST['Number'];
		$Image = $_POST['Image'];
	
		if($Image == ""){
			$updateQuery = "update users_tbl set Fullname = '$Fullname', Section ='$Section', Email = '$Email', Phone_number ='$Number' where ID = $ID  ";
			$result = mysqli_query($my_connection, $updateQuery);
				if($result > 0){
					$Query = "update score_tbl set Fullname = '$Fullname', Section ='$Section' where ID = $ID";
					$update = mysqli_query($my_connection, $Query);
						if($update > 0){
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
		}else{
			$now = DateTime::createFromFormat('U.u', microtime(true));
			$id = $now->format('YmdHisu');
			$imageName = "cw_img/$id.jpg";
			$imagePath = "http://192.168.10.1/CodeWebScripts/$imageName";

			if(file_put_contents($imageName, base64_decode($Image)) != false){
				$updateQuery = "update users_tbl set Image ='$imagePath', Fullname = '$Fullname', Section ='$Section', Email = '$Email', Phone_number ='$Number' where ID = $ID ";
				$result = mysqli_query($my_connection, $updateQuery);
				if($result > 0){
					$Query = "update score_tbl set Fullname = '$Fullname', Section ='$Section' where ID = $ID  ";
					$update = mysqli_query($my_connection, $Query);
						if($update > 0){
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
			}else{
				echo "failed";
				exit;
			}
		}
		
	}


	if(isset($_POST['old_ID']) && isset($_POST['old_Image']) && isset($_POST['old_Fullname']) && isset($_POST['old_Section']) && isset($_POST['old_Number']) && isset($_POST['old_Email'])){
		$ID = $_POST['old_ID'];
		$Fullname = $_POST['old_Fullname'];
		$Section = $_POST['old_Section'];
		$Email = $_POST['old_Email'];
		$Number = $_POST['old_Number'];
		$Image = $_POST['old_Image'];
	
		if($Image == ""){
			$updateQuery = "update old_users_tbl set Fullname = '$Fullname', Section ='$Section', Email = '$Email', Phone_number ='$Number' where ID = $ID  ";
			$result = mysqli_query($my_connection, $updateQuery);
				if($result > 0){
					$Query = "update old_score_tbl set Fullname = '$Fullname', Section ='$Section' where ID = $ID";
					$update = mysqli_query($my_connection, $Query);
						if($update > 0){
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
		}else{
			$now = DateTime::createFromFormat('U.u', microtime(true));
			$id = $now->format('YmdHisu');
			$imageName = "cw_img/$id.jpg";
			$imagePath = "http://192.168.10.1/CodeWebScripts/$imageName";

			if(file_put_contents($imageName, base64_decode($Image)) != false){
				$updateQuery = "update old_users_tbl set Image ='$imagePath', Fullname = '$Fullname', Section ='$Section', Email = '$Email', Phone_number ='$Number' where ID = $ID ";
				$result = mysqli_query($my_connection, $updateQuery);
				if($result > 0){
					$Query = "update old_score_tbl set Fullname = '$Fullname', Section ='$Section' where ID = $ID  ";
					$update = mysqli_query($my_connection, $Query);
						if($update > 0){
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
			}else{
				echo "failed";
				exit;
			}
		}
		
	}


	//Update Lesson 
	if(isset($_POST['ID']) && isset($_POST['Tlesson']) && isset($_POST['Content']) && isset($_POST['Code']) && isset($_POST['Trivia']) && isset($_POST['Image'])){
        $ID = $_POST['ID'];
        $title = $_POST['Tlesson'];
        $content = $_POST['Content'];
        $code = $_POST['Code'];
        $trivia = $_POST['Trivia'];
        $Image = $_POST['Image'];

    	if($Image == ""){
			$updateQuery = "update cw_lessons set lsn_title = '$title', lsn_content ='$content', lsn_code_content = '$code', lsn_trivia ='$trivia' where lsn_id = $ID";
			$result = mysqli_query($my_connection, $updateQuery);
				if($result > 0){
					echo "success";
					exit;
				}else{
					echo "failed";
					exit;
				}
		}else{
			$now = DateTime::createFromFormat('U.u', microtime(true));
			$id = $now->format('YmdHisu');
			$imageName = "cw_img/$id.jpg";
			$imagePath = "http://192.168.10.1/CodeWebScripts/$imageName";

			if(file_put_contents($imageName, base64_decode($Image)) != false){
				$updateQuery = "update cw_lessons set lsn_output = '$imagePath', lsn_title = '$title', lsn_content ='$content', lsn_code_content = '$code', lsn_trivia ='$trivia' where lsn_id = $ID";
				$result = mysqli_query($my_connection, $updateQuery);
					if($result > 0){
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

	}

	if(isset($_POST['Category_Level']) && isset($_POST['id'])){
		$catLevel = $_POST['Category_Level'];
		$id = $_POST['id'];
// Points need Here
		$updateQuery = "update users_tbl set Category_Level = '$catLevel'  where ID = $id ";
		$result = mysqli_query($my_connection, $updateQuery);
			if($result > 0){
				echo "success";
				exit;
			}else{
				echo "failed";
				exit;
			}
	}

	if( isset($_POST['Crs_Level']) && isset($_POST['Cat_Level']) && isset($_POST['id']) ){
		$crsLevel = $_POST['Crs_Level'];
		$categoryLevel = $_POST['Cat_Level'];
		$id = $_POST['id'];
// Points need Here
		$updateQuery = "update users_tbl set Course_Level = '$crsLevel' , Category_Level = '$categoryLevel' , User_level = 1 where ID = $id ";
		$result = mysqli_query($my_connection, $updateQuery);
			if($result > 0){
				echo "success";
				exit;
			}else{
				echo "failed";
				exit;
			}
	}

	if(isset($_POST['App_Tutorial']) && isset($_POST['ID'])){
		$tutorial = $_POST['App_Tutorial'];
		$id = $_POST['ID'];

		$updateQuery = "update users_tbl set App_Tutorial = '$tutorial' where ID = $id";
		$result = mysqli_query($my_connection, $updateQuery);
			if($result > 0){
				echo "success";
				exit;
			}else{
				echo "failed";
				exit;
			}
	}

	//Update Correct Scores
	if( isset($_POST['id']) &&  isset($_POST['UserLevel']) && isset($_POST['Points']) && isset($_POST['CourseLevel']) && isset($_POST['CategoryLevel']) ){
		$id = $_POST['id'];
		$CategoryLevel = $_POST['CategoryLevel'];
		$CourseLevel = $_POST['CourseLevel'];
		$Points = $_POST['Points'];
		$Level = $_POST['UserLevel'];

		$loadQuery = "SELECT $CourseLevel , $CategoryLevel FROM score_tbl where  ID = $id "; 
		$result = mysqli_query($my_connection, $loadQuery);
		$row = mysqli_fetch_array($result);

		if($row != null){
			$Crsscore = $row[0] + 1;
			$Catscore = $row[1] + 1;

			$updQuery = "update score_tbl set $CourseLevel = $Crsscore , $CategoryLevel = $Catscore where ID = $id ";
			$upd = mysqli_query($my_connection, $updQuery);

			if($upd > 0){
				$updateQuery = "update users_tbl set User_level = $Level , Points = $Points where ID = $id ";
				$update = mysqli_query($my_connection, $updateQuery);

				if($update > 0){
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

	}

	//Update Wrong Scores
	if( isset($_POST['id']) && isset($_POST['UserLevel'])){
		$id = $_POST['id'];
		$Level = $_POST['UserLevel'];

		$loadQuery = "SELECT Wrong_ans FROM score_tbl where  ID = $id "; 
		$result = mysqli_query($my_connection, $loadQuery);
		$row = mysqli_fetch_array($result);

		if($row != null){
			$wrong_ans = $row[0] + 1;
			$WrongQuery = "update score_tbl set Wrong_ans = $wrong_ans where ID = $id ";
			$WrongResult = mysqli_query($my_connection, $WrongQuery);
			if($WrongResult > 0){
				$updateQuery = "update users_tbl set User_level = $Level where ID = $id ";
				$update = mysqli_query($my_connection, $updateQuery);
				if($update > 0){
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

		}else{
			echo "failed";
			exit;
		}
	}


	if(isset($_POST['quiz_Id']) && isset($_POST['quiz_Title']) && isset($_POST['quiz_Question']) && isset($_POST['quiz_OptA']) && isset($_POST['quiz_OptB']) && isset($_POST['quiz_OptC']) && isset($_POST['quiz_OptD']) && isset($_POST['quiz_Ans']) ){
		$quiz_Title = $_POST['quiz_Title'];
		$quiz_Question = $_POST['quiz_Question'];
		$quiz_OptA = $_POST['quiz_OptA'];
		$quiz_OptB = $_POST['quiz_OptB'];
		$quiz_OptC = $_POST['quiz_OptC'];
		$quiz_OptD = $_POST['quiz_OptD'];
		$quiz_Ans = $_POST['quiz_Ans'];
		$quiz_Date = date('Y-m-d');;
		$quiz_Id = $_POST['quiz_Id'];

			$updateQuery = "UPDATE cw_quiz SET quiz_Title= '$quiz_Title',quiz_Date= '$quiz_Date',quiz_Question='$quiz_Question',quiz_OptA='$quiz_OptA',quiz_OptB='$quiz_OptB',quiz_OptC= '$quiz_OptC',quiz_OptD='$quiz_OptD',quiz_Ans='$quiz_Ans' WHERE quiz_Id = $quiz_Id";
			$update = mysqli_query($my_connection, $updateQuery);

				if($update > 0){
					echo "success";
					exit;
				}else{
					echo "failed";
					exit;
				}
	}
	

	if(isset($_POST['id']) && isset($_POST['QuizType']) && isset($_POST['QuizSession']) ){
		$id = $_POST['id'];
		$QuizType = $_POST['QuizType'];
		$QuizSession = $_POST['QuizSession'];


		$loadQuery = "SELECT $QuizType FROM users_tbl where  ID = $id "; 
		$result = mysqli_query($my_connection, $loadQuery);
		$row = mysqli_fetch_array($result);

		if($row != null){
			$QuizScore = $row[0] + 1;

			$updQuery = "update users_tbl set $QuizType = $QuizScore , $QuizSession = 'Taken' where ID = $id ";
			$upd = mysqli_query($my_connection, $updQuery);

			if($upd > 0){
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


//update profile
	if(isset($_POST['ID']) && isset($_POST['Fullname']) && isset($_POST['Username']) && isset($_POST['Password']) && isset($_POST['Number']) && isset($_POST['Email']) && isset($_POST['Image']) ){
		$ID = $_POST['ID'];
		$Fullname = $_POST['Fullname'];
		$Username = $_POST['Username'];
		$Password = $_POST['Password'];
		$Email = $_POST['Email'];
		$Number = $_POST['Number'];
		$Image = $_POST['Image'];
		


		if($Image == ""){
			$updateQuery = "update users_tbl set Fullname = '$Fullname',  Password = '$Password' , Username = '$Username', Email = '$Email', Phone_number ='$Number' where ID = $ID ";
			$result = mysqli_query($my_connection, $updateQuery);
				if($result > 0){
					$Query = "update score_tbl set Fullname = '$Fullname' where ID = $ID ";
					$update = mysqli_query($my_connection, $Query);
						if($update > 0){
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
		}else{
			$now = DateTime::createFromFormat('U.u', microtime(true));
				$id = $now->format('YmdHisu');
				$imageName = "cw_img/$id.jpg";
				$imagePath = "http://192.168.10.1/CodeWebScripts/$imageName";

				if(file_put_contents($imageName, base64_decode($Image)) != false){
					$updateQuery = "update users_tbl set Image ='$imagePath', Fullname = '$Fullname',  Password = '$Password' , Username = '$Username', Email = '$Email', Phone_number ='$Number' where ID = $ID ";
					$result = mysqli_query($my_connection, $updateQuery);
						if($result > 0){
							$Query = "update score_tbl set Fullname = '$Fullname' where ID = $ID";
							$update = mysqli_query($my_connection, $Query);
								if($update > 0){
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
				}else{
					echo "failed";
					exit;
				}
			}
	}
	
?>