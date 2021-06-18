<?php
	require "connection.php";


	if(isset($_POST['id']) && isset($_POST['batch']) ){
		$section = $_POST['id'];
		$batch = $_POST['batch'];

		$query = "INSERT INTO old_users_tbl(`ID`,`Type`,`Student_ID`,`Username`,`Password`,`Fullname`,`Section`,`Batch`,`Email`,`Phone_number`,`Image`,`User_badge`,`User_level`,`Course_Level`,`Category_Level`,`HtmlQuiz`,`HtmlQuiz_Score`,`CssQuiz`,`CssQuiz_Score`,`JsQuiz`,`JsQuiz_Score`,`Points`,`App_Tutorial`) SELECT * FROM users_tbl where Section = '$section' and Batch= '$batch';";
		$save = mysqli_query($my_connection, $query);
		if($save > 0 ){
			$move = "INSERT INTO old_cw_sections(`sec_id`,`yr_sec`,`batch`,`yrsec_icon`) SELECT * FROM cw_sections where  yr_sec = '$section' and batch = '$batch'; ";
			$sav = mysqli_query($my_connection, $move);
			if($sav > 0){
				$move_score = "INSERT INTO old_score_tbl(`ID`,`Student_ID`,`Fullname`,`Section`,`Batch`,`html_Overview`,`html_Basic`,`html_HTML5`,`HTML`,`css_Basic`,`css_Text`,`css_Properties`,`css_Layouts`,`css_Css3`,`css_Backgrounds`,`css_Transitions`,`Css`,`js_Overview`,`js_Basic`,`js_Conloops`,`js_Functions`,`js_Objects`,`js_Core`,`js_Events`,`Js`,`Wrong_ans`) SELECT * FROM score_tbl where  Section = '$section' and Batch = '$batch'; ";
				$sav_score = mysqli_query($my_connection, $move_score);
				if($sav_score > 0){
					$delete = "DELETE from users_tbl where Section = '$section' and Batch = '$batch';";
					$remove = mysqli_query($my_connection, $delete);
					if($remove > 0 ){
						$del = "DELETE from cw_sections where yr_sec = '$section' and batch = '$batch';";
						$rem = mysqli_query($my_connection, $del);
						if($rem > 0){
							$del_score = "DELETE from score_tbl where Section = '$section' and Batch = '$batch';";
							$rem_score = mysqli_query($my_connection, $del_score);
							if($rem_score > 0){
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
		}else{
			echo "failed";
			exit;
		}
	}


	if(isset($_POST['ret_id']) && isset($_POST['ret_batch']) ){
		$section = $_POST['ret_id'];
		$batch = $_POST['ret_batch'];

		$query = "INSERT INTO users_tbl(`Type`,`Student_ID`,`Username`,`Password`,`Fullname`,`Section`,`Batch`,`Email`,`Phone_number`,`Image`,`User_badge`,`User_level`,`Course_Level`,`Category_Level`,`HtmlQuiz`,`HtmlQuiz_Score`,`CssQuiz`,`CssQuiz_Score`,`JsQuiz`,`JsQuiz_Score`,`Points`,`App_Tutorial`) SELECT `Type`,`Student_ID`,`Username`,`Password`,`Fullname`,`Section`,`Batch`,`Email`,`Phone_number`,`Image`,`User_badge`,`User_level`,`Course_Level`,`Category_Level`,`HtmlQuiz`,`HtmlQuiz_Score`,`CssQuiz`,`CssQuiz_Score`,`JsQuiz`,`JsQuiz_Score`,`Points`,`App_Tutorial` FROM old_users_tbl where Section = '$section' and Batch= '$batch';";
		$save = mysqli_query($my_connection, $query);
		if($save > 0 ){
			$move = "INSERT INTO cw_sections(`yr_sec`,`batch`,`yrsec_icon`) SELECT `yr_sec`,`batch`,`yrsec_icon` FROM old_cw_sections where  yr_sec = '$section' and batch = '$batch'; ";
			$sav = mysqli_query($my_connection, $move);
			if($sav > 0){
				$move_score = "INSERT INTO score_tbl(`Student_ID`,`Fullname`,`Section`,`Batch`,`html_Overview`,`html_Basic`,`html_HTML5`,`HTML`,`css_Basic`,`css_Text`,`css_Properties`,`css_Layouts`,`css_Css3`,`css_Backgrounds`,`css_Transitions`,`Css`,`js_Overview`,`js_Basic`,`js_Conloops`,`js_Functions`,`js_Objects`,`js_Core`,`js_Events`,`Js`,`Wrong_ans`) SELECT `Student_ID`,`Fullname`,`Section`,`Batch`,`html_Overview`,`html_Basic`,`html_HTML5`,`HTML`,`css_Basic`,`css_Text`,`css_Properties`,`css_Layouts`,`css_Css3`,`css_Backgrounds`,`css_Transitions`,`Css`,`js_Overview`,`js_Basic`,`js_Conloops`,`js_Functions`,`js_Objects`,`js_Core`,`js_Events`,`Js`,`Wrong_ans` FROM old_score_tbl where  Section = '$section' and Batch = '$batch'; ";
				$sav_score = mysqli_query($my_connection, $move_score);
				if($sav_score > 0){
					$delete = "DELETE from old_users_tbl where Section = '$section' and Batch = '$batch';";
					$remove = mysqli_query($my_connection, $delete);
					if($remove > 0 ){
						$del = "DELETE from old_cw_sections where yr_sec = '$section' and batch = '$batch';";
						$rem = mysqli_query($my_connection, $del);
						if($rem > 0){
							$del_score = "DELETE from old_score_tbl where Section = '$section' and Batch = '$batch';";
							$rem_score = mysqli_query($my_connection, $del_score);
							if($rem_score > 0){
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
		}else{
			echo "failed";
			exit;
		}

	}

?>