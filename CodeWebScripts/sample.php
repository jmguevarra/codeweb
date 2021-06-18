<?php
	require "connection.php";
//	require('api/fpdf.php');

	$now = DateTime::createFromFormat('U.u', microtime(true));
	$idb = $now->format('YmdH');

	echo $idb;

?>


	<!-- 	$inQuery = "INSERT INTO `cw_quiz`(`quiz_Type`, `quiz_Title`, `quiz_Course`, `quiz_Author`, `quiz_Icon`, `quiz_Date`, `quiz_Question`, `quiz_OptA`, `quiz_OptB`, `quiz_OptC`, `quiz_OptD`, `quiz_Ans`)
		VALUES 
		('$quiz_Type','Nav Element', '$quiz_Course', '$quiz_Author', '$quiz_Icon', '$quiz_Date','The information below is usually provided in footer tag except one','Metadata','Copyright Information','Terms of Service','Privacy Policy','Metadata'),
		('$quiz_Type','', '$quiz_Course', '$quiz_Author', '$quiz_Icon', '$quiz_Date','','','','','',''),
		('$quiz_Type','', '$quiz_Course', '$quiz_Author', '$quiz_Icon', '$quiz_Date','','','','','',''),
		('$quiz_Type','', '$quiz_Course', '$quiz_Author', '$quiz_Icon', '$quiz_Date','','','','','',''),
		('$quiz_Type','', '$quiz_Course', '$quiz_Author', '$quiz_Icon', '$quiz_Date','','','','','',''),
		('$quiz_Type','', '$quiz_Course', '$quiz_Author', '$quiz_Icon', '$quiz_Date','','','','','',''),
		('$quiz_Type','', '$quiz_Course', '$quiz_Author', '$quiz_Icon', '$quiz_Date','','','','','',''),
		('$quiz_Type','', '$quiz_Course', '$quiz_Author', '$quiz_Icon', '$quiz_Date','','','','','',''),
		('$quiz_Type','', '$quiz_Course', '$quiz_Author', '$quiz_Icon', '$quiz_Date','','','','','',''),
		('$quiz_Type','', '$quiz_Course', '$quiz_Author', '$quiz_Icon', '$quiz_Date','','','','','',''),
		('$quiz_Type','', '$quiz_Course', '$quiz_Author', '$quiz_Icon', '$quiz_Date','','','','','',''),
		('$quiz_Type','', '$quiz_Course', '$quiz_Author', '$quiz_Icon', '$quiz_Date','','','','','',''),
		('$quiz_Type','', '$quiz_Course', '$quiz_Author', '$quiz_Icon', '$quiz_Date','','','','','',''),
		('$quiz_Type','', '$quiz_Course', '$quiz_Author', '$quiz_Icon', '$quiz_Date','','','','','',''),
		('$quiz_Type','', '$quiz_Course', '$quiz_Author', '$quiz_Icon', '$quiz_Date','','','','','',''),
		('$quiz_Type','', '$quiz_Course', '$quiz_Author', '$quiz_Icon', '$quiz_Date','','','','','',''),
		 ";

		$result = mysqli_query($my_connection, $inQuery);
            if($result > 0){
                echo "success"; 
                exit;
            }else{
                echo "failed";
                exit;
            }
 -->
<!-- class PDF extends FPDF{
	// $rpt_ID = 12;
		// $rpt_avatar = 
		$rpt_Fullname = "Jaime Guevarra Jr";
		$rpt_Section ="BSIT-4C";
		$rpt_Stud_ID ="014A-4394";
		// $rpt_HTML = "Taken";
		// $rpt_html_Score = 0;
		// $rpt_Js = "notTaken"
		// $rpt_Js_Score = 0;
		// $rpt_Js = "Not Taken";
		// $rpt_js_Score = 0;

		function Header(){
		  if($this->page == 1){
		  	  global $title;
		    $w = $this->GetStringWidth($title)+6;
		    $this->SetX((210-$w)/2);
		    $this->Image('cw_img/ic_codeweb.png',10,6,30);
		    $this->SetFont('Arial','B',15);
		    $this->SetDrawColor(0,80,180);
		   	$this->SetFillColor(255,255,255);
		    $this->SetTextColor(0,0,0);
		    $this->SetLineWidth(1);
		    $this->Cell($w,10,$title,0,0,'C',true);
		    $this->Ln(32);
		  }
		}

		function Title(){
		    $this->SetFont('Arial','B',1);
		    $this->SetFillColor(47,47,47);
		    $this->Cell(0,1,"",0,1,'L',true);
		    $this->Ln(10);
		}


		function StudentProfile($fullname,$schoolId,$section){
			$this->setX(15);
			$this->SetFont('Arial','B',16);
		    $this->Cell(0,0, "Student Profile",0,0,'L',false);
		    $this->Ln(13);
			$this->Image('cw_img/ic_codeweb.png',25,59,25);
			$this->SetFillColor(255,255,255);
			$this->setX(53);
			$this->SetFont('Arial','B',16);
		    $this->Cell(0,0, $fullname,0,0,'L',false);
		    $this->Ln(3);
		    $sec_id = $schoolId. 
			"\n".$section."";
		    $this->setX(53);
			$this->SetFont('Arial','',12);
		    $this->MultiCell(0,6, $sec_id,0,1,'L',false);
		    $this->Ln(20);	  
		   
		}

		function StudentQuizzes($html, $html_score, $Js, $Js_score, $js, $js_score){
			$this->setX(15);
		    $this->SetFont('Arial','B',16);
		    $this->Cell(0,0, "Student Quizzes",0,0,'L',false);
		    $this->Ln(14);

		   
		    $htmlMistakes = 10 - $html_score;
		    $HTMlFormat = "Taken: ".$html.
		    "\nCorrect Answer: ".$html_score.
		    "\nMistakes: ".$htmlMistakes.
		    "\nAvarage: ".($html_score/10)*100;
		    
		    $this->setX(25);
		    $this->SetFont('Arial','B',14);
		    $this->Cell(0,0, "HTMl Quiz",0,0,'L',false);
		    $this->Ln(4);
		    $this->setX(32);
		    $this->SetFont('Arial','',12);
		    $this->MultiCell(0,6, $HTMlFormat,0,1,'C',false);
		    $this->Ln(7);

		    $JsMistakes = 10 - $Js_score;
		    $JsFormat = "Taken: ".$Js.
		    "\nCorrect Answer: ".$Js_score.
		    "\nMistakes: ".$JsMistakes.
		    "\nAvarage: ".($Js_score/10)*100;

		    $this->setX(25);
		    $this->SetFont('Arial','B',14);
		    $this->Cell(0,0, "Js Quiz",0,0,'L',false);
		    $this->Ln(4);
		    $this->setX(32);
			$this->SetFont('Arial','',12);
		    $this->MultiCell(0,6, $JsFormat,0,1,'C',false);
		    $this->Ln(7);

		    $jsMistakes = 10 - $js_score;
		    $JsFormat = "Taken: ".$js.
		    "\nCorrect Answer: ".$js_score.
		    "\nMistakes: ".$jsMistakes.
		    "\nAvarage: ".($js_score/10)*100;

		    $this->setX(25);
		    $this->SetFont('Arial','B',14);
		    $this->Cell(0,0, "Javascript Quiz",0,0,'L',false);
		    $this->Ln(4);
		    $this->setX(32);
			$this->SetFont('Arial','',12);
		    $this->MultiCell(0,6, $JsFormat,0,1,'C',false);
		    $this->Ln(20);	
		}
		
		function Excercises($html_score, $Js_score, $js_score){
			$this->setX(15);
		    $this->SetFont('Arial','B',16);
		    $this->Cell(0,0, "Student Excercises",0,0,'L',false);
		    $this->Ln(14);

		    $htmlMistakes = 29-$html_score;
		    $format_html = "Overall Score: ".$html_score.
		    "\nOverall Mistakes: ".$htmlMistakes.
		    "\nAvarage: ".number_format(($html_score/29)*100,2);
		    $this->setX(25);
		    $this->SetFont('Arial','B',14);
		    $this->Cell(0,0, "HTMl Excercises",0,0,'L',false);
		    $this->Ln(4);
		    $this->setX(32);
		    $this->SetFont('Arial','',12);
		    $this->MultiCell(0,6,   $format_html,0,1,'C',false);
		    $this->Ln(7);

		    $JsMistakes = 29-$Js_score;
		    $format_Js = "Overall Score: ".$Js_score.
		    "\nOverall Mistakes: ".$JsMistakes.
		    "\nAvarage: ".number_format(($Js_score/29)*100, 2);
		    $this->setX(25);
		    $this->SetFont('Arial','B',14);
		    $this->Cell(0,0, "Js Excercises",0,0,'L',false);
		    $this->Ln(4);
		    $this->setX(32);
		    $this->SetFont('Arial','',12);
		    $this->MultiCell(0,6, $format_Js,0,1,'C',false);
		    $this->Ln(7);

		    $jsMistakes = 30-$js_score;
		    $format_js = "Overall Score: ".$html_score.
		    "\nOverall Mistakes: ".$jsMistakes.
		    "\nAvarage: ".number_format(($js_score/30)*100);
		    $this->setX(25);
		    $this->SetFont('Arial','B',14);
		    $this->Cell(0,0, "Javascript Excercises",0,0,'L',false);
		    $this->Ln(4);
		    $this->setX(32);
		    $this->SetFont('Arial','',12);
		    $this->MultiCell(0,6,  $format_js,0,1,'C',false);
		    $this->Ln(7);

		}
	

		function Footer(){
		    $this->SetY(-15);
		    $this->SetFont('Arial','I',10);
		    $this->Cell(0,10,$this->PageNo(),0,0,'R');
		}

	}

		$pdf = new PDF();
		$title = "Dalubhasaan ng Lungsod ng Lucena";
		$pdf->setTitle($title);
		$pdf->AddPage();
		$pdf->Title();
		$pdf->StudentProfile($rpt_Fullname, $rpt_Stud_ID, $rpt_Section);
		$pdf->StudentQuizzes('Taken', 6, 'Taken', 9,'Taken',4);
		$pdf->Excercises(25,15,26);


		// $filename = $id+"";
		// $path  = "pdf_folder/".$filename.".pdf";
		$pdf->Output(); -->
	
	<!-- // $quiz_Date = date('Y-m-d');
	// $quiz_Author = "Admin";
	// $quiz_Type = "Multiple Choice";
	// $quiz_Course = "HTML";
	// //$quiz_backpanel = "http://192.168.10.1/CodeWebScripts/cw_img/lesson_panel.png";
	// $quiz_Icon = "http://192.168.10.1/CodeWebScripts/cw_img/ic_html.jpg";


	
 -->
  



