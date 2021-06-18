<?php
	include_once("connection.php");
	require('api/fpdf.php');


	if(isset($_POST['rpt_js_Score']) && isset($_POST['rpt_Js']) && isset($_POST['rpt_css_Score']) && isset($_POST['rpt_Css']) && isset($_POST['rpt_html_Score']) && isset($_POST['rpt_HTML']) && isset($_POST['rpt_Section']) && isset($_POST['rpt_Type']) && isset($_POST['rpt_Stud_ID']) && isset($_POST['rpt_Fullname']) && isset($_POST['rpt_avatar']) && isset($_POST['rpt_ID']) ){
		
		$rpt_ID = $_POST['rpt_ID'];
		$rpt_avatar = $_POST['rpt_avatar'];
		$rpt_Fullname = $_POST['rpt_Fullname'];
		$rpt_Stud_ID = $_POST['rpt_Stud_ID'];
		$rpt_Type = $_POST['rpt_Type'];
		$rpt_Section = $_POST['rpt_Section'];
		$rpt_HTML = $_POST['rpt_HTML'];
		$rpt_html_Score = $_POST['rpt_html_Score'];
		$rpt_Css = $_POST['rpt_Css'];
		$rpt_css_Score = $_POST['rpt_css_Score'];
		$rpt_Js = $_POST['rpt_Js'];
		$rpt_js_Score = $_POST['rpt_js_Score'];

		class PDF extends FPDF{
	
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


		function StudentProfile($image, $fullname,$schoolId,$section){
			$this->setX(15);
			$this->SetFont('Arial','B',16);
		    $this->Cell(0,0, "Student Profile",0,0,'L',false);
		    $this->Ln(13);
			$this->Image($image,25,59,25);
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

		function StudentQuizzes($html, $html_score, $css, $css_score, $js, $js_score){
			$this->setX(15);
		    $this->SetFont('Arial','B',16);
		    $this->Cell(0,0, "Student Quizzes",0,0,'L',false);
		    $this->Ln(14);

		   
		   if($html_score == 0){
		   	 $htmlMistakes = 0;
		   }else{
		   		 $htmlMistakes = 10 - $html_score;
		   }
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

		    if($css_score == 0){
		   	 $cssMistakes = 0;
		   }else{
		   		 $cssMistakes = 10 - css_score;
		   }
		    $CssFormat = "Taken: ".$css.
		    "\nCorrect Answer: ".$css_score.
		    "\nMistakes: ".$cssMistakes.
		    "\nAvarage: ".($css_score/10)*100;

		    $this->setX(25);
		    $this->SetFont('Arial','B',14);
		    $this->Cell(0,0, "Css Quiz",0,0,'L',false);
		    $this->Ln(4);
		    $this->setX(32);
			$this->SetFont('Arial','',12);
		    $this->MultiCell(0,6, $CssFormat,0,1,'C',false);
		    $this->Ln(7);

		    if($js_score == 0){
		   	 $jsMistakes = 0;
		   }else{
		   		 $jsMistakes = 10 - $js_score;
		   }
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
		
		function Excercises($html_score, $css_score, $js_score){
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

		    $cssMistakes = 29-$css_score;
		    $format_css = "Overall Score: ".$css_score.
		    "\nOverall Mistakes: ".$cssMistakes.
		    "\nAvarage: ".number_format(($css_score/29)*100, 2);
		    $this->setX(25);
		    $this->SetFont('Arial','B',14);
		    $this->Cell(0,0, "Css Excercises",0,0,'L',false);
		    $this->Ln(4);
		    $this->setX(32);
		    $this->SetFont('Arial','',12);
		    $this->MultiCell(0,6, $format_css,0,1,'C',false);
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
		$pdf->StudentProfile($rpt_avatar,$rpt_Fullname, $rpt_Stud_ID, $rpt_Section);
		$pdf->StudentQuizzes($rpt_HTML, $rpt_html_Score, $rpt_Css, $rpt_css_Score,$rpt_Js,$rpt_js_Score);
		$pdf->Excercises(25,15,26);


		$path  = "pdf_folder/".$rpt_Stud_ID.".pdf";
		$pdf->Output($path,'F');
		$pdfpath = "http://192.168.10.1/CodeWebScripts/".$path;

		echo $pdfpath;

		
	}

?>