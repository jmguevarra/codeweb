package com.app.codeweb.codeweb.Admin_Activity.SectionsCom;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.codeweb.codeweb.Others.CircleTransform;
import com.app.codeweb.codeweb.Others.MyImageLoader;
import com.app.codeweb.codeweb.Others.Serialized.SrlLesson;
import com.app.codeweb.codeweb.Others.Serialized.SrlScore;
import com.app.codeweb.codeweb.Others.Serialized.SrlStudents;
import com.app.codeweb.codeweb.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ColorFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.ImageLoader;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class StudentDetail extends AppCompatActivity {

    SrlStudents userData;
    Animation animAlpha;
    TextView fullname, id, username, email, contact, section;
    ImageView user_img;
    Dialog update, photo;
    ArrayList<SrlStudents> load;
    ArrayList<SrlScore> loadScore;
   ArrayList<SrlLesson> loadCourse,loadCategory;


    CameraPhoto cameraPhoto;
    GalleryPhoto galleryPhoto;
    final int CAMERA_REQUEST = 13323;
    final int GALLERY_REQUEST = 22131;
    String encode_Image = "";
    EditText etFullname,etAddSection, etEmail, etNumber, etStud_ID;
    ImageView user_img2, iv_camera, iv_gallery;

    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_student_detail);
        //Casting
        animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Student Info");

        fullname = (TextView) findViewById(R.id.fullname);
        id = (TextView) findViewById(R.id.id);
        username = (TextView) findViewById(R.id.username);
        section = (TextView) findViewById(R.id.section);
        email = (TextView) findViewById(R.id.email);
        contact = (TextView) findViewById(R.id.contact);
        user_img = (ImageView) findViewById(R.id.user_img);
        cameraPhoto = new CameraPhoto(getApplicationContext());
        galleryPhoto = new GalleryPhoto(getApplicationContext());

        if(getIntent().getSerializableExtra("data")!= null){
            userData = (SrlStudents) getIntent().getSerializableExtra("data");
            fullname.setText(userData.Fullname);
            id.setText(userData.Student_ID);
            username.setText(Html.fromHtml(userData.Username+"<br><b>Username</b>"));
            email.setText(Html.fromHtml(userData.Email+"<br><b>Email</b>"));
            section.setText(Html.fromHtml(userData.Section+"<br><b>Yr & Sec</b>"));
            contact.setText(Html.fromHtml(userData.Phone_number+"<br><b>Contact Number</b>"));
            Picasso.with(StudentDetail.this)
                    .load(userData.Image)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.stat_sys_download)
                    .transform(new CircleTransform())
                    .into(user_img);
            QuizzesPerformance();
            BarHTML();
            BarCss();
            BarJs();
            OverallPer();

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            //fab.setBackgroundColor(R.color.textColor);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CreateReportPdf();
                }
            });
        }else{
            Toast.makeText(this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
        }
    }
    public void CreateReportPdf(){
        HashMap postData = new HashMap();
        postData.put("rpt_ID", ""+userData.ID);
        postData.put("rpt_avatar", ""+userData.Image);
        postData.put("rpt_Fullname",userData.Fullname);
        postData.put("rpt_Stud_ID", ""+userData.Student_ID);
        postData.put("rpt_Type", ""+userData.Type);
        postData.put("rpt_Section", ""+userData.Section);
        postData.put("rpt_HTML", ""+userData.HtmlQuiz);
        postData.put("rpt_html_Score", ""+userData.HtmlQuiz_Score);
        postData.put("rpt_Css", ""+userData.CssQuiz);
        postData.put("rpt_css_Score", ""+userData.CssQuiz_Score);
        postData.put("rpt_Js", ""+userData.JsQuiz);
        postData.put("rpt_js_Score", ""+userData.JsQuiz_Score);
        PostResponseAsyncTask task = new PostResponseAsyncTask(StudentDetail.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if(!s.isEmpty()) {
                    DownloadManager download = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse(s);
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    long reference = download.enqueue(request);
                }else{
                    Toast.makeText(StudentDetail.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                }
             }
        });task.execute("http://192.168.10.1/CodeWebScripts/report.php");
    }
    public void QuizzesPerformance(){
        final BarChart barChart = (BarChart) findViewById(R.id.barquiz);
        HashMap postData = new HashMap();
        postData.put("Quiz_ID", ""+userData.ID);
        postData.put("Quiz_Fullname", userData.Fullname);
        PostResponseAsyncTask task = new PostResponseAsyncTask(StudentDetail.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                ArrayList<SrlStudents> loadquiz = new JsonConverter<SrlStudents>().toArrayList(s, SrlStudents.class);
                float TotalScore = 10, totalHtmlQuiz = 0,totalCssQuiz = 0,totalJsQuiz=0;
                ArrayList<BarEntry> CorrectEntries = new ArrayList<>();
                ArrayList<String> course = new ArrayList<>();

                for(SrlStudents d: loadquiz){
                     totalHtmlQuiz = d.HtmlQuiz_Score;
                     totalCssQuiz = d.CssQuiz_Score;
                     totalJsQuiz = d.JsQuiz_Score;
                }
                CorrectEntries.add(new BarEntry(0f, (totalHtmlQuiz/TotalScore)*100));
                CorrectEntries.add(new BarEntry(1f, (totalCssQuiz/TotalScore)*100));
                CorrectEntries.add(new BarEntry(2f, (totalJsQuiz/TotalScore)*100));
                int htmlColor = 0, cssColor = 0, jsColor = 0;

                if(userData.HtmlQuiz.equalsIgnoreCase("NotTaken")){
                    course.add("HTML Not Already Taken");
                    course.add("Css Not Already Taken");
                    course.add("Js Not Already Taken");
                    htmlColor = R.color.red_200;
                    cssColor = R.color.red_500;
                    jsColor = R.color.red_700;
                }else if(userData.CssQuiz.equalsIgnoreCase("NotTaken")&& userData.HtmlQuiz.equalsIgnoreCase("Taken")){
                    if(60 <= (totalHtmlQuiz/TotalScore)*100){
                        htmlColor = R.color.green_200;
                        course.add("HTML Passed");
                    }else{
                        htmlColor = R.color.red_200;
                        course.add("HTML Failed");
                    }
                    course.add("Css Not Already Taken");
                    course.add("Js Not Already Taken");
                    cssColor = R.color.red_500;
                    jsColor = R.color.red_700;
                }else if(userData.JsQuiz.equalsIgnoreCase("NotTaken") && userData.CssQuiz.equalsIgnoreCase("Taken")){
                    if(60 <= (totalHtmlQuiz/TotalScore)*100 && 60 <= (totalCssQuiz/TotalScore)*100 ){
                        htmlColor = R.color.green_200;
                        course.add("HTML Passed");
                        course.add("Css Passed");
                        cssColor = R.color.green_500;
                    }else if(60 <= (totalHtmlQuiz/TotalScore)*100 && 60 >= (totalCssQuiz/TotalScore)*100 ){
                        cssColor = R.color.red_500;
                        course.add("HTML Passed");
                        course.add("Css Failed");
                        htmlColor = R.color.green_200;
                    }else if(60 >= (totalHtmlQuiz/TotalScore)*100 && 60 <= (totalCssQuiz/TotalScore)*100 ){
                        cssColor = R.color.green_500;
                        htmlColor = R.color.red_200;
                        course.add("HTML Failed");
                        course.add("Css Passed");
                    }else{
                        htmlColor = R.color.red_200;
                        cssColor = R.color.red_500;
                        course.add("HTML Failed");
                        course.add("Css Failed");
                    }
                    course.add("Js Not Already Taken");
                    jsColor = R.color.red_700;
                }else{
                    if(60 <= (totalHtmlQuiz/TotalScore)*100 && 60 <= (totalJsQuiz/TotalScore)*100 && 60 <= (totalCssQuiz/TotalScore)*100){
                        course.add("HTML Passed");
                        course.add("Css Passed");
                        course.add("Js Passed");
                        htmlColor = R.color.green_200;
                        cssColor = R.color.green_500;
                        jsColor = R.color.green_700;
                    }else if(60 <= (totalHtmlQuiz/TotalScore)*100 && 60 >= (totalHtmlQuiz/TotalScore)*100 && 60 >= (totalHtmlQuiz/TotalScore)*100){
                        course.add("HTML Passed");
                        htmlColor = R.color.green_200;
                        course.add("Css Failed");
                        course.add("Js Failed");
                        cssColor = R.color.red_500;
                        jsColor = R.color.red_700;
                    }else if(60 >= (totalHtmlQuiz/TotalScore)*100 && 60 <= (totalHtmlQuiz/TotalScore)*100 && 60 >= (totalHtmlQuiz/TotalScore)*100){
                        course.add("HTML Failed");
                        htmlColor = R.color.red_200;
                        course.add("Css Passed");
                        cssColor = R.color.green_500;
                        course.add("Js Failed");
                        jsColor = R.color.red_700;
                    }else if(60 >= (totalHtmlQuiz/TotalScore)*100 && 60 >= (totalHtmlQuiz/TotalScore)*100 && 60 <= (totalHtmlQuiz/TotalScore)*100){
                        course.add("HTML Failed");
                        htmlColor = R.color.red_200;
                        course.add("Css Failed");
                        cssColor = R.color.red_500;
                        course.add("Js Passed");
                        jsColor = R.color.green_700;
                    }else if(60 <= (totalHtmlQuiz/TotalScore)*100 && 60 <= (totalHtmlQuiz/TotalScore)*100 && 60 >= (totalHtmlQuiz/TotalScore)*100){
                        course.add("HTML Passed");
                        htmlColor = R.color.green_200;
                        course.add("Css Passed");
                        cssColor = R.color.green_500;
                        course.add("Js Failed");
                        jsColor = R.color.red_700;
                    }else if(60 >= (totalHtmlQuiz/TotalScore)*100 && 60 <= (totalHtmlQuiz/TotalScore)*100 && 60 <= (totalHtmlQuiz/TotalScore)*100){
                        course.add("HTML Failed");
                        htmlColor = R.color.red_200;
                        course.add("Css Passed");
                        cssColor = R.color.green_500;
                        course.add("Js Passed");
                        jsColor = R.color.green_700;
                    }else if(60 <= (totalHtmlQuiz/TotalScore)*100 && 60 >= (totalHtmlQuiz/TotalScore)*100 && 60 <= (totalHtmlQuiz/TotalScore)*100){
                        course.add("HTML Passed");
                        htmlColor = R.color.green_200;
                        course.add("Css Failed");
                        cssColor = R.color.red_500;
                        course.add("Js Passed");
                        jsColor = R.color.green_700;
                    }else{
                        course.add("HTML Failed");
                        course.add("Css Failed");
                        course.add("Js Failed");
                        htmlColor = R.color.red_200;
                        cssColor = R.color.red_500;
                        jsColor = R.color.red_700;
                    }

                }
                String[] legendString = course.toArray(new String[course.size()]);
                int[] color = {htmlColor, cssColor, jsColor};

                BarDataSet correctSet = new BarDataSet(CorrectEntries, "Quizzes");
                correctSet.setColors(ColorTemplate.createColors(getResources(), color));
                BarData data = new BarData(correctSet);
                data.setDrawValues(true);
                data.setValueFormatter(new PercentFormatter());
                data.setValueTextColor(R.color.colorPrimary);
                barChart.getXAxis().setEnabled(false);
                barChart.getAxisLeft().setAxisMaximum(100);
                barChart.getAxisLeft().setAxisMinimum(0);
                barChart.getAxisRight().setEnabled(false);
                barChart.getLegend().setExtra(ColorTemplate.createColors(getResources(), color), Arrays.asList(legendString));
                barChart.getLegend().setTextSize(10f);
                barChart.getLegend().setXEntrySpace(20f);
                barChart.getLegend().setYEntrySpace(0.5f);
                barChart.getLegend().setWordWrapEnabled(true);
                Description d = new Description();
                d.setEnabled(false);
                barChart.setDescription(d);
                barChart.setData(data);
                barChart.setTouchEnabled(true);
                barChart.setScaleEnabled(true);
                barChart.setHighlightPerTapEnabled(true);
                barChart.setDragEnabled(true);
                barChart.setFitBars(true);
            }
        });task.execute("http://192.168.10.1/CodeWebScripts/load.php");


    }
    public void BarHTML(){
        final BarChart barHTML = (BarChart) findViewById(R.id.bar_html);
        HashMap postData = new HashMap();
        postData.put("Scr_ID", ""+userData.ID);
        postData.put("Scr_Fullname", userData.Fullname);
        PostResponseAsyncTask task = new PostResponseAsyncTask(StudentDetail.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if(!s.isEmpty()){
                    ArrayList<SrlScore> loadScore = new JsonConverter<SrlScore>().toArrayList(s, SrlScore.class);
                    float html_Overview =0 , html_Basic = 0, html_HTML5= 0;
                    ArrayList<BarEntry> Entries = new ArrayList<>();
                    for(SrlScore d: loadScore){
                        html_Overview = d.html_Overview;
                        html_Basic = d.html_Basic;
                        html_HTML5 = d.html_HTML5;
                    }
                    ArrayList<String> course = new ArrayList<>();
                        course.add("Overview");
                        course.add("The Basic");
                        course.add("HTML 5");
                    Entries.add(new BarEntry(0f, (html_Overview/3)*100));
                    Entries.add(new BarEntry(1f, (html_Basic/15)*100));
                    Entries.add(new BarEntry(2f, (html_HTML5/11)*100));

                    String[] legendString = course.toArray(new String[course.size()]);
                    int[] color = {R.color.orange_1, R.color.orange_2, R.color.orange_3};

                    BarDataSet correctSet = new BarDataSet(Entries, "HTML Course");
                    correctSet.setColors(ColorTemplate.createColors(getResources(), color));
                    BarData data = new BarData(correctSet);
                    data.setDrawValues(true);
                    data.setValueFormatter(new PercentFormatter());
                    data.setValueTextColor(R.color.colorPrimary);
                    barHTML.getXAxis().setEnabled(false);
                    barHTML.getAxisLeft().setAxisMaximum(100);
                    barHTML.getAxisLeft().setAxisMinimum(0);
                    barHTML.getAxisRight().setEnabled(false);
                    barHTML.getLegend().setExtra(ColorTemplate.createColors(getResources(), color), Arrays.asList(legendString));
                    barHTML.getLegend().setTextSize(10f);
                    barHTML.getLegend().setXEntrySpace(20f);
                    barHTML.getLegend().setYEntrySpace(0.5f);
                    barHTML.getLegend().setWordWrapEnabled(true);
                    Description d = new Description();
                    d.setEnabled(false);
                    barHTML.setDescription(d);
                    barHTML.setData(data);
                    barHTML.setTouchEnabled(true);
                    barHTML.setScaleEnabled(true);
                    barHTML.setHighlightPerTapEnabled(true);
                    barHTML.setDragEnabled(true);
                    barHTML.setFitBars(true);
                }else{
                    Toast.makeText(StudentDetail.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                }
            }
        });task.execute("http://192.168.10.1/CodeWebScripts/load.php");
    }
    public void BarCss(){
        final BarChart barCss = (BarChart) findViewById(R.id.bar_css);
        HashMap postData = new HashMap();
        postData.put("Scr_ID", ""+userData.ID);
        postData.put("Scr_Fullname", userData.Fullname);
        PostResponseAsyncTask task = new PostResponseAsyncTask(StudentDetail.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if(!s.isEmpty()){
                    ArrayList<SrlScore> loadScore = new JsonConverter<SrlScore>().toArrayList(s, SrlScore.class);
                    float css_Basic =0 , css_Text= 0, css_Properties= 0, css_Layouts= 0,css_Css3= 0,css_Backgrounds= 0,css_Transitions= 0;
                    ArrayList<BarEntry> Entries = new ArrayList<>();
                    for(SrlScore d: loadScore){
                        css_Basic = d.css_Basic;
                        css_Text = d.css_Text;
                        css_Properties = d.css_Properties;
                        css_Layouts = d.css_Layouts;
                        css_Css3 = d.css_Css3;
                        css_Backgrounds = d.css_Backgrounds;
                        css_Transitions = d.css_Transitions;

                    }
                    ArrayList<String> course = new ArrayList<>();
                        course.add("The Basic ");
                        course.add("Text Editing ");
                        course.add("Properties");
                        course.add("Layouts");
                        course.add("Css3");
                        course.add("Background ");
                        course.add("Transitions");


                    //need correct Data for here
                    Entries.add(new BarEntry(0f, (css_Basic/5)*100));
                    Entries.add(new BarEntry(1f, (css_Text/15)*100));
                    Entries.add(new BarEntry(2f, (css_Properties/11)*100));
                    Entries.add(new BarEntry(3f, (css_Layouts/7)*100));
                    Entries.add(new BarEntry(4f, (css_Css3/2)*100));
                    Entries.add(new BarEntry(5f, (css_Backgrounds/2)*100));
                    Entries.add(new BarEntry(6f, (css_Transitions/2)*100));

                    String[] legendString = course.toArray(new String[course.size()]);
                    int[] color = {R.color.gray_1, R.color.gray_2, R.color.gray_3,R.color.gray_4,R.color.gray_5,R.color.gray_6,R.color.gray_7};

                    BarDataSet correctSet = new BarDataSet(Entries, "Css Course");
                    correctSet.setColors(ColorTemplate.createColors(getResources(), color));
                    BarData data = new BarData(correctSet);
                    data.setDrawValues(true);
                    data.setValueFormatter(new PercentFormatter());
                    data.setValueTextColor(R.color.colorPrimary);
                    barCss.getXAxis().setEnabled(false);
                    barCss.getAxisLeft().setAxisMaximum(100);
                    barCss.getAxisLeft().setAxisMinimum(0);
                    barCss.getAxisRight().setEnabled(false);
                    barCss.getLegend().setExtra(ColorTemplate.createColors(getResources(), color), Arrays.asList(legendString));
                    barCss.getLegend().setTextSize(10f);
                    barCss.getLegend().setXEntrySpace(20f);
                    barCss.getLegend().setYEntrySpace(0.5f);
                    barCss.getLegend().setWordWrapEnabled(true);
                    Description d = new Description();
                    d.setEnabled(false);
                    barCss.setDescription(d);
                    barCss.setData(data);
                    barCss.setTouchEnabled(true);
                    barCss.setScaleEnabled(true);
                    barCss.setHighlightPerTapEnabled(true);
                    barCss.setDragEnabled(true);
                    barCss.setFitBars(true);
                }else{
                    Toast.makeText(StudentDetail.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                }
            }
        });task.execute("http://192.168.10.1/CodeWebScripts/load.php");
    }
    public void BarJs(){
        final BarChart barJs = (BarChart) findViewById(R.id.bar_js);
        HashMap postData = new HashMap();
        postData.put("Scr_ID", ""+userData.ID);
        postData.put("Scr_Fullname", userData.Fullname);
        PostResponseAsyncTask task = new PostResponseAsyncTask(StudentDetail.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if(!s.isEmpty()){
                    ArrayList<SrlScore> loadScore = new JsonConverter<SrlScore>().toArrayList(s, SrlScore.class);
                    float js_Overview =0 , js_Basic= 0, js_ConLoops= 0, js_Functions= 0,js_Objects= 0,js_CoreObjects= 0,js_Events= 0;
                    ArrayList<BarEntry> Entries = new ArrayList<>();
                    for(SrlScore d: loadScore){
                        js_Overview = d.js_Overview;
                        js_Basic = d.js_Basic;
                        js_ConLoops = d.js_Conloops;
                        js_Functions = d.js_Functions;
                        js_Objects = d.js_Objects;
                        js_CoreObjects = d.js_Core;
                        js_Events = d.js_Events;

                    }
                    ArrayList<String> course = new ArrayList<>();
                        course.add("Overview ");
                        course.add("The Basic ");
                        course.add("ConLoops");
                        course.add("Functions");
                        course.add("Objects");
                        course.add("Core Objects ");
                        course.add("Events");


                    //need correct Data for here
                    Entries.add(new BarEntry(0f, (js_Overview/5)*100));
                    Entries.add(new BarEntry(1f, (js_Basic/6)*100));
                    Entries.add(new BarEntry(2f, (js_ConLoops/9)*100));
                    Entries.add(new BarEntry(3f, (js_Functions/5)*100));
                    Entries.add(new BarEntry(4f, (js_Objects/4)*100));
                    Entries.add(new BarEntry(5f, (js_CoreObjects/6)*100));
                    Entries.add(new BarEntry(6f, (js_Events/9)*100));

                    String[] legendString = course.toArray(new String[course.size()]);
                    int[] color = {R.color.blue_1, R.color.blue_2, R.color.blue_3,R.color.blue_4,R.color.blue_5,R.color.blue_6,R.color.blue_7};

                    BarDataSet correctSet = new BarDataSet(Entries, "Js Course");
                    correctSet.setColors(ColorTemplate.createColors(getResources(), color));
                    BarData data = new BarData(correctSet);
                    data.setDrawValues(true);
                    data.setValueFormatter(new PercentFormatter());
                    data.setValueTextColor(R.color.colorPrimary);
                    barJs.getXAxis().setEnabled(false);
                    barJs.getAxisLeft().setAxisMaximum(100);
                    barJs.getAxisLeft().setAxisMinimum(0);
                    barJs.getAxisRight().setEnabled(false);
                    barJs.getLegend().setExtra(ColorTemplate.createColors(getResources(), color), Arrays.asList(legendString));
                    barJs.getLegend().setTextSize(10f);
                    barJs.getLegend().setXEntrySpace(20f);
                    barJs.getLegend().setYEntrySpace(0.5f);
                    barJs.getLegend().setWordWrapEnabled(true);
                    Description d = new Description();
                    d.setEnabled(false);
                    barJs.setDescription(d);
                    barJs.setData(data);
                    barJs.setTouchEnabled(true);
                    barJs.setScaleEnabled(true);
                    barJs.setHighlightPerTapEnabled(true);
                    barJs.setDragEnabled(true);
                    barJs.setFitBars(true);
                }else{
                    Toast.makeText(StudentDetail.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                }
            }
        });task.execute("http://192.168.10.1/CodeWebScripts/load.php");
    }
    public void OverallPer(){
        final PieChart pieChart = (PieChart) findViewById(R.id.piegraph);
        HashMap postData = new HashMap();
        postData.put("Scr_ID", ""+userData.ID);
        postData.put("Scr_Fullname", userData.Fullname);
        PostResponseAsyncTask task = new PostResponseAsyncTask(StudentDetail.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                ArrayList<SrlScore>  loadScore = new JsonConverter<SrlScore>().toArrayList(s, SrlScore.class);
                float totalHtml = 0,totalCss = 0, totalJs = 0, Wrong_ans = 0;
                ArrayList<PieEntry> pieEntries = new ArrayList<>();
                for(SrlScore d: loadScore){
                    totalHtml = d.HTML;
                    totalCss = d.Css;
                    totalJs = d.Js;
                    Wrong_ans = d.Wrong_ans;
                }

                int totalQuest =117;
                float needToAttain = totalQuest - (totalHtml+Wrong_ans);
                pieEntries.add(new PieEntry((totalHtml/totalQuest)*100, (totalHtml/totalQuest)*100+"% HTML"));
                pieEntries.add(new PieEntry((totalCss/totalQuest)*100, (totalCss/totalQuest)*100+"% Css"));
                pieEntries.add(new PieEntry((totalJs/totalQuest)*100, (totalJs/totalQuest)*100+"% Js"));
                pieEntries.add(new PieEntry((Wrong_ans/totalQuest)*100, (Wrong_ans/totalQuest)*100+"% Mistakes"));
                pieEntries.add(new PieEntry((needToAttain/totalQuest)*100, (needToAttain/totalQuest)*100+"% Need to Attain"));
                int[] Colors = {R.color.orange_2, R.color.gray_1, R.color.colorPrimary, R.color.red_500, R.color.colorPrimaryLight};
                PieDataSet dataset =  new PieDataSet(pieEntries,"");
                dataset.setColors(ColorTemplate.createColors(getResources(),Colors));
                PieData data = new PieData(dataset);
                data.setDrawValues(true);
                data.setValueFormatter(new PercentFormatter());
                Legend l = pieChart.getLegend();
                l.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);
                l.setOrientation(Legend.LegendOrientation.VERTICAL);
                l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                Description d = new Description();
                d.setEnabled(false);
                pieChart.setDescription(d);
                pieChart.setData(data);
                pieChart.setTouchEnabled(true);
                pieChart.setHighlightPerTapEnabled(true);
            }
        });task.execute("http://192.168.10.1/CodeWebScripts/load.php");

    }

    public void AsyncLoadLesson(String Course){
        HashMap postData = new HashMap();
        postData.put("excerCourse", Course);
        PostResponseAsyncTask task = new PostResponseAsyncTask(StudentDetail.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s){
                loadCourse= new JsonConverter<SrlLesson>().toArrayList(s, SrlLesson.class);
            }
        });task.execute("http://192.168.10.1/CodeWebScripts/load.php");
    }

    public void loadCategory(String Course, String Category){
        HashMap postData = new HashMap();
        postData.put("Course", Course);
        postData.put("Category", Category);
        PostResponseAsyncTask task = new PostResponseAsyncTask(StudentDetail.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s){
                loadCategory= new JsonConverter<SrlLesson>().toArrayList(s, SrlLesson.class);
            }
        });task.execute("http://192.168.10.1/CodeWebScripts/load.php");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edt_dlt_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.update){
            Update();
        }else if(id == R.id.delete){
            Delete();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == CAMERA_REQUEST){
                String photoPath = cameraPhoto.getPhotoPath();
                try {
                    Bitmap bitmap = MyImageLoader.init().from(photoPath).getCircleBitmap();
                    user_img2.setImageBitmap(bitmap);

                    if(!photoPath.isEmpty()) {
                        Bitmap serverBitmap = ImageLoader.init().from(photoPath).requestSize(1024, 1024).getBitmap();
                        encode_Image = ImageBase64.encode(serverBitmap);
                    }else{
                        encode_Image = "";
                    }
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(),
                            "Something Wrong while loading photos", Toast.LENGTH_SHORT).show();
                }

            }
            else if(requestCode == GALLERY_REQUEST){
                Uri uri = data.getData();
                galleryPhoto.setPhotoUri(uri);
                String photoPath = galleryPhoto.getPath();

                try {
                    Bitmap bitmap = MyImageLoader.init().from(photoPath).getCircleBitmap();
                    user_img2.setImageBitmap(bitmap);

                    if(!photoPath.isEmpty()) {
                        Bitmap serverBitmap = ImageLoader.init().from(photoPath).requestSize(1024, 1024).getBitmap();
                        encode_Image = ImageBase64.encode(serverBitmap);
                    }else{
                        encode_Image = "";
                    }
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(),
                            "Something Wrong while choosing photos", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void Update(){
        update = new Dialog(StudentDetail.this);
        update.setTitle("Edit Info");
        update.setContentView(R.layout.dialog_addstud);
        update.setCanceledOnTouchOutside(false);

        etFullname = (EditText) update.findViewById(R.id.etFullname);
        etStud_ID = (EditText) update.findViewById(R.id.etStud_ID);
        etAddSection = (EditText) update.findViewById(R.id.etSection);
        etEmail = (EditText) update.findViewById(R.id.etEmail);
        etNumber = (EditText) update.findViewById(R.id.etNumber);
        user_img2 = (ImageView) update.findViewById(R.id.user_img);

        etFullname.setText(userData.Fullname);
        etStud_ID.setText(userData.Student_ID);
        etStud_ID.setEnabled(false);
        etAddSection.setText(userData.Section);
        etAddSection.setEnabled(false);
        etEmail.setText(userData.Email);
        etNumber.setText(userData.Phone_number);
        Picasso.with(StudentDetail.this)
                .load(userData.Image)
                .transform(new CircleTransform())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.stat_sys_download)
                .into(user_img2);

        user_img2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                view.startAnimation(animAlpha);
                getPhoto();
                return false;
            }
        });

        Button add = (Button) update.findViewById(R.id.addbtn);
        add.setText("Update");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                HashMap postData = new HashMap();
                postData.put("ID", ""+userData.ID);
                postData.put("Fullname", etFullname.getText().toString());
                postData.put("Section", etAddSection.getText().toString());
                postData.put("Email", etEmail.getText().toString());
                postData.put("Number", etNumber.getText().toString());
                postData.put("Image", encode_Image);

                PostResponseAsyncTask updateTask = new PostResponseAsyncTask(StudentDetail.this, postData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        if(s.contains("success")){
                            Toast.makeText(StudentDetail.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                            update.dismiss();
                            AsyncLoad();
                        }else if(s.contains("failed")){
                            Toast.makeText(StudentDetail.this, R.string.scripts_error, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(StudentDetail.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                        }
                    }
                });updateTask.execute("http://192.168.10.1/CodeWebScripts/update.php");

            }
        });
        encode_Image = "";
        update.show();
    }

    public void getPhoto(){
        photo = new Dialog(StudentDetail.this);
        photo.setTitle("Choose Action");
        photo.setContentView(R.layout.dialog_photo);
        photo.setCanceledOnTouchOutside(false);

        iv_camera = (ImageView) photo.findViewById(R.id.iv_camera);
        iv_gallery = (ImageView) photo.findViewById(R.id.iv_gallery);

        iv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                try {
                    startActivityForResult(cameraPhoto.takePhotoIntent(), CAMERA_REQUEST);
                    cameraPhoto.addToGallery();
                    photo.dismiss();
                }catch (IOException e) {
                    Toast.makeText(getApplicationContext(),
                            "Something Wrong while taking photos", Toast.LENGTH_SHORT).show();

                }
            }
        });

        iv_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);
                photo.dismiss();
            }
        });
        photo.show();
    }

    public void Delete(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Option");
        builder.setMessage("Are sure you want to delete "+userData.Fullname+" in section list of "+userData.Section+"?");
        builder.setPositiveButton("No", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Yes", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HashMap postData = new HashMap();
                postData.put("ID", ""+userData.ID);
                PostResponseAsyncTask deleteTask = new PostResponseAsyncTask(StudentDetail.this, postData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        if(s.contains("success")){
                            Toast.makeText(StudentDetail.this, userData.Fullname+" has been removed ", Toast.LENGTH_SHORT).show();
                            Intent in = new Intent(StudentDetail.this, StudentList.class);
                            in.putExtra("section", userData.Section);
                            startActivity(in);
                        }else if(s.contains("failed")){
                            Toast.makeText(StudentDetail.this, R.string.scripts_error, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(StudentDetail.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                        }
                    }
                });deleteTask.execute("http://192.168.10.1/CodeWebScripts/remove.php");
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void AsyncLoad() {
        HashMap postData = new HashMap();
        postData.put("ID", ""+userData.ID);
        PostResponseAsyncTask listLoad = new PostResponseAsyncTask(StudentDetail.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                load = new JsonConverter<SrlStudents>().toArrayList(s, SrlStudents.class);
                for(SrlStudents d:load) {
                    fullname.setText(d.Fullname);
                    id.setText(d.Student_ID);
                    username.setText(Html.fromHtml(d.Username + "<br><b>Username</b>"));
                    email.setText(Html.fromHtml(d.Email + "<br><b>Email</b>"));
                    section.setText(Html.fromHtml(d.Section + "<br><b>Yr & Sec</b>"));
                    contact.setText(Html.fromHtml(d.Phone_number + "<br><b>Contact Number</b>"));
                    Picasso.with(StudentDetail.this)
                            .load(d.Image)
                            .placeholder(android.R.drawable.ic_menu_gallery)
                            .error(android.R.drawable.stat_sys_download)
                            .transform(new CircleTransform())
                            .into(user_img);
                }
            }
        });listLoad.execute("http://192.168.10.1/CodeWebScripts/load.php");
    }

}
