<?PHP
    include_once("connection.php");

//Add Account
    if(isset($_POST['Username']) && isset($_POST['Password']) && isset($_POST['ID'])){
    	$username = $_POST['Username'];
    	$password = $_POST['Password'];
    	$id = $_POST['ID'];

    	$selectQuery = "select * from users_tbl where Username = '$username' or Password = '$password'";
		$check = mysqli_query($my_connection, $selectQuery);
		$checkAccount = mysqli_fetch_array($check);

			if(isset($checkAccount)){
				echo "exist";
				exit;
			}else{
				$saveQuery = "update users_tbl set Username = '$username', Password = '$password' where ID = $id";
				$save  = mysqli_query($my_connection, $saveQuery);
					if($save > 0){
						echo "success";
						exit;
					}else{
						echo "failed";
						exit;
					}
			}


    	mysqli_close($my_connection);
    }



//Add Section
	if(isset($_POST['newSec'])){
		$yr_sec = $_POST['newSec'];
		$url = 'http://192.168.10.1/CodeWebScripts/cw_img/students.png';
		$batch = date('Y');

		$inQuery = "insert into cw_sections(`yr_sec`,`batch`,`yrsec_icon`) values ('$yr_sec','$batch','$url')";
		$result = mysqli_query($my_connection, $inQuery);
			if($result > 0){
				echo "success";
				exit;
			}else{
				echo "failed";
			}
	
	}
	

	//Add Students
	if(isset($_POST['Fullname']) && isset($_POST['Stud_ID']) && isset($_POST['Section']) && isset($_POST['Image']) && isset($_POST['Number']) && isset($_POST['Email'])){
		$Fullname = $_POST['Fullname'];
		$Stud_ID = $_POST['Stud_ID'];
		$Section = $_POST['Section'];
		$Image = $_POST['Image'];
		$Email = $_POST['Email'];
		$Number = $_POST['Number'];
		$batch = date('Y');


		if($Image == ""){
			$user_img = 'http://192.168.10.1/CodeWebScripts/cw_img/user_icon.png';
			$inQuery = "insert into users_tbl (`Batch`,`HtmlQuiz`,`CssQuiz`,`JsQuiz`,`User_level`,`Course_Level`,`Category_Level`,`App_tutorial`,`Type`,`Fullname`, `Student_ID`,`Section`, `Email`, `Phone_number`, `Image`) values
			('$batch','NotTaken','NotTaken','NotTaken',1,'HTML','Overview','True','student','$Fullname', '$Stud_ID', '$Section','$Email', '$Number','$user_img')";
			$result = mysqli_query($my_connection, $inQuery);
				if($result > 0){
					$scoreQuery = "insert into score_tbl (`Batch`,`Fullname`,`Student_ID`,`Section`) values
						('$batch','$Fullname', '$Stud_ID', '$Section')";
						$score_tbl = mysqli_query($my_connection, $scoreQuery);
							if($score_tbl > 0){
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
			$batch = date('Y');
			if(file_put_contents($imageName, base64_decode($Image)) != false){
				$inQuery = "insert into users_tbl (`Batch`,`HtmlQuiz`,`CssQuiz`,`JsQuiz`,`User_level`,`Course_Level`,`Category_Level`,`App_tutorial`,`Type`,`Fullname`, `Student_ID`,`Section`, `Email`, `Phone_number`, `Image`) values
				('$batch','NotTaken','NotTaken','NotTaken',1,'HTML','Overview','True','student','$Fullname', '$Stud_ID', '$Section','$Email', '$Number','$imagePath')";
				$result = mysqli_query($my_connection, $inQuery);
					if($result > 0){
						$scoreQuery = "insert into score_tbl (`Batch`,`Fullname`, `Student_ID`,`Section`) values
						('$batch','$Fullname', '$Stud_ID', '$Section')";
						$score_tbl = mysqli_query($my_connection, $scoreQuery);
							if($score_tbl > 0){
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

	//Add Admin
	if(isset($_POST['Fullname']) && isset($_POST['Stud_ID']) && isset($_POST['Number']) && isset($_POST['Email'])  && isset($_POST['Image'])){
		$Fullname = $_POST['Fullname'];
		$Stud_ID = $_POST['Stud_ID'];
		$Email = $_POST['Email'];
		$Number = $_POST['Number'];
		$Image = $_POST['Image'];
		$batch = date('Y');
		

		if($Image == ""){
			$user_img = 'http://192.168.10.1/CodeWebScripts/cw_img/user_icon.png';
			$inQuery = "insert into users_tbl (`Batch`,`Type`,`Fullname`, `Student_ID`,`Email`, `Phone_number`, `Image`) values
			('$batch','administrator','$Fullname', '$Stud_ID','$Email','$Number','$user_img')";
			$result = mysqli_query($my_connection, $inQuery);

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
			$batch = date('Y');

			if(file_put_contents($imageName, base64_decode($Image)) != false){
				$inQuery = "insert into users_tbl (`Batch`,`Type`,`Fullname`, `Student_ID`,`Email`, `Phone_number`, `Image`) values
				('$batch','administrator','$Fullname', '$Stud_ID','$Email','$Number','$imagePath')";
				$result = mysqli_query($my_connection, $inQuery);
				
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

	//Add Lesson 
	if(isset($_POST['quiz_Question']) && isset($_POST['quiz_OptA']) && isset($_POST['quiz_OptB']) && isset($_POST['quiz_OptC']) && isset($_POST['quiz_OptD']) && isset($_POST['quiz_Ans']) && isset($_POST['lsn_no']) && isset($_POST['lsn_ptCat']) && isset($_POST['Course']) && isset($_POST['Category']) && isset($_POST['Tlesson']) && isset($_POST['Content']) && isset($_POST['Code']) && isset($_POST['Trivia']) && isset($_POST['Image'])){
		$lsn_no = $_POST['lsn_no'];
		$lsn_ptCat = $_POST['lsn_ptCat'];
		$course = $_POST['Course'];
        $category = $_POST['Category'];
        $title = $_POST['Tlesson'];
        $content = $_POST['Content'];
        $code = $_POST['Code'];
        $trivia = $_POST['Trivia'];
        $Image = $_POST['Image'];
        $backpanel = "http://192.168.10.1/CodeWebScripts/cw_img/lesson_panel.png";
        $quiz_Question = $_POST['quiz_Question'];
		$quiz_OptA = $_POST['quiz_OptA'];
		$quiz_OptB = $_POST['quiz_OptB'];
		$quiz_OptC = $_POST['quiz_OptC'];
		$quiz_OptD = $_POST['quiz_OptD'];
		$quiz_Ans = $_POST['quiz_Ans'];

		if($quiz_Ans == '1st'){
				$answer = $quiz_OptA;
			}else if($quiz_Ans == '2nd'){
				$answer = $quiz_OptB;
			}else if($quiz_Ans == '3rd'){
				$answer = $quiz_OptC;
			}else if($quiz_Ans == '4th'){
				$answer = $quiz_OptD;
			}

		if($course == 'HTML'){
			$icon = "http://192.168.10.1/CodeWebScripts/cw_img/ic_html.jpg";
		}else if($course == 'Css'){
			$icon = "http://192.168.10.1/CodeWebScripts/cw_img/ic_css.jpg";
		}else if($course == 'Js'){
			$icon = "http://192.168.10.1/CodeWebScripts/cw_img/ic_js.jpg";
		}

		if($Image == ""){
			$inQuery = "insert into cw_lessons (`lsn_question`,`lsn_optA`, `lsn_optB`, `lsn_optC`, `lsn_optD`, `lsn_rightOpt`,`lsn_no`, `lsn_ptCat`,`lsn_output`,`lsn_icon`,`lsn_author`, `lsn_course`,`lsn_category`, `lsn_title`, `lsn_backpanel`,`lsn_content`, `lsn_code_content`, `lsn_trivia`) values
	        ('$quiz_Question','$quiz_OptA','$quiz_OptB','$quiz_OptC','$quiz_OptD','$answer',$lsn_no, '$lsn_ptCat','$icon','$icon', 'Admin', '$course', '$category', '$title', '$backpanel','$content', '$code', '$trivia')";
	        $result = mysqli_query($my_connection, $inQuery);

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
		    	$inQuery = "insert into cw_lessons (`lsn_question`, `lsn_optA`, `lsn_optB`, `lsn_optC`, `lsn_optD`, `lsn_rightOpt`,`lsn_no`, `lsn_ptCat`,`lsn_output`,`lsn_icon`,`lsn_author`, `lsn_course`,`lsn_category`, `lsn_title`, `lsn_backpanel`,`lsn_content`, `lsn_code_content`, `lsn_trivia`) values
		        ('$quiz_Question','$quiz_OptA','$quiz_OptB','$quiz_OptC','$quiz_OptD','$answer',$lsn_no, '$lsn_ptCat','$imagePath', '$icon', 'Admin', '$course', '$category', '$title', '$backpanel','$content', '$code', '$trivia')";
		        $result = mysqli_query($my_connection, $inQuery);
		            if($result > 0){
		                echo "success";
		                exit;
		            }else{
		                echo "failed";
		                exit;
		            }
	        }
	    }
	}

// quiz tex
	if(isset($_POST['quiz_Course']) && isset($_POST['quiz_Title']) && isset($_POST['quiz_Question']) && isset($_POST['quiz_OptA']) && isset($_POST['quiz_OptB']) && isset($_POST['quiz_OptC']) && isset($_POST['quiz_OptD']) && isset($_POST['quiz_Ans']) ){
		$quiz_Author = "Admin";
		$quiz_Course = $_POST['quiz_Course'];
		$quiz_Title = $_POST['quiz_Title'];
		$quiz_Type = "text";
		$quiz_Question = $_POST['quiz_Question'];
		$quiz_OptA = $_POST['quiz_OptA'];
		$quiz_OptB = $_POST['quiz_OptB'];
		$quiz_OptC = $_POST['quiz_OptC'];
		$quiz_OptD = $_POST['quiz_OptD'];
		$quiz_Ans = $_POST['quiz_Ans'];
		$quiz_Date = date('Y-m-d');

			if($quiz_Course == 'HTML'){
				$quiz_Icon = "http://192.168.10.1/CodeWebScripts/cw_img/ic_html.jpg";
			}else if($quiz_Course == 'Css'){
				$quiz_Icon = "http://192.168.10.1/CodeWebScripts/cw_img/ic_css.jpg";
			}else if($quiz_Course = 'Js'){
				$quiz_Icon = "http://192.168.10.1/CodeWebScripts/cw_img/ic_js.jpg";
			}

			if($quiz_Ans == '1st'){
				$answer = $quiz_OptA;
			}else if($quiz_Ans == '2nd'){
				$answer = $quiz_OptB;
			}else if($quiz_Ans == '3rd'){
				$answer = $quiz_OptC;
			}else if($quiz_Ans == '4th'){
				$answer = $quiz_OptD;
			}

		$inQuery = "INSERT INTO `cw_quiz`(`quiz_Type`, `quiz_Title`, `quiz_Course`, `quiz_Author`, `quiz_Icon`, `quiz_Date`, `quiz_Question`, `quiz_OptA`, `quiz_OptB`, `quiz_OptC`, `quiz_OptD`, `quiz_Ans`)
		VALUES ('$quiz_Type','$quiz_Title', '$quiz_Course', '$quiz_Author', '$quiz_Icon', '$quiz_Date','$quiz_Question','$quiz_OptA','$quiz_OptB','$quiz_OptC','$quiz_OptD','$answer')";
		$result = mysqli_query($my_connection, $inQuery);
            if($result > 0){
                echo "success";
                exit;
            }else{
                echo "failed";
                exit;
            }
	}

	// quiz img 
	if(isset($_POST['quiz_Course']) && isset($_POST['quiz_Title']) && isset($_POST['quiz_Question']) && isset($_POST['quiz_Opt1']) && isset($_POST['quiz_Opt2']) && isset($_POST['quiz_Opt3']) && isset($_POST['quiz_Opt4']) && isset($_POST['quiz_Ans']) ){
		$quiz_Author = "Admin";
		$quiz_Course = $_POST['quiz_Course'];
		$quiz_Title = $_POST['quiz_Title'];
		$quiz_Type = "img";
		$quiz_Question = $_POST['quiz_Question'];
		$quiz_OptA = $_POST['quiz_Opt1'];
		$quiz_OptB = $_POST['quiz_Opt2'];
		$quiz_OptC = $_POST['quiz_Opt3'];
		$quiz_OptD = $_POST['quiz_Opt4'];
		$quiz_Ans = $_POST['quiz_Ans'];
		$quiz_Date = date('Y-m-d');

			//Option a
			$now = DateTime::createFromFormat('U.u', microtime(true));
			$ida = $now->format('YmdHisu');
			$imageNameA = "cw_img/$ida.jpg";
			$imagePathA = "http://192.168.10.1/CodeWebScripts/$imageNameA";
	

			if($quiz_Course == 'HTML'){
				$quiz_Icon = "http://192.168.10.1/CodeWebScripts/cw_img/ic_html.jpg";
			}else if($quiz_Course == 'Css'){
				$quiz_Icon = "http://192.168.10.1/CodeWebScripts/cw_img/ic_css.jpg";
			}else if($quiz_Course = 'Js'){
				$quiz_Icon = "http://192.168.10.1/CodeWebScripts/cw_img/ic_js.jpg";
			}

			if($quiz_Ans == '1st'){
				$answer = $imagePathA;
			}else if($quiz_Ans == '2nd'){
				$answer = $imagePathB;
			}else if($quiz_Ans == '3rd'){
				$answer = $imagePathC;
			}else if($quiz_Ans == '4th'){
				$answer = $imagePathD;
			}

			if(file_put_contents($imageNameA, base64_decode($quiz_OptA)) != false){
				$idb = $now->format('YmdHis');
				$imageNameB = "cw_img/$idb.jpg";
				$imagePathB = "http://192.168.10.1/CodeWebScripts/$imageNameB";
				if(file_put_contents($imageNameB, base64_decode($quiz_OptB)) != false){
					$idc = $now->format('YmdHi');
					$imageNameC = "cw_img/$idc.jpg";
					$imagePathC = "http://192.168.10.1/CodeWebScripts/$imageNameC";
					if(file_put_contents($imageNameC, base64_decode($quiz_OptC)) != false){
						$idd = $now->format('YmdH');
						$imageNameD = "cw_img/$idd.jpg";
						$imagePathD = "http://192.168.10.1/CodeWebScripts/$imageNameD";					
						if(file_put_contents($imageNameD, base64_decode($quiz_OptD)) != false){
							$inQuery = "INSERT INTO `cw_quiz`(`quiz_Type`, `quiz_Title`, `quiz_Course`, `quiz_Author`, `quiz_Icon`, `quiz_Date`, `quiz_Question`, `quiz_OptA`, `quiz_OptB`, `quiz_OptC`, `quiz_OptD`, `quiz_Ans`)
							VALUES ('$quiz_Type','$quiz_Title', '$quiz_Course', '$quiz_Author', '$quiz_Icon', '$quiz_Date','$quiz_Question','$imagePathA','$imagePathB','$imagePathC','$imagePathD','$answer')";
							$result = mysqli_query($my_connection, $inQuery);
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
	
?> 