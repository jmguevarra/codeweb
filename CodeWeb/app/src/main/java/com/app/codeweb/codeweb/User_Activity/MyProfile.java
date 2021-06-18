package com.app.codeweb.codeweb.User_Activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.codeweb.codeweb.Admin_Activity.AdminActions.Admin_MyProfile;
import com.app.codeweb.codeweb.Admin_Activity.AdminPanel;
import com.app.codeweb.codeweb.Admin_Activity.SectionsCom.StudentDetail;
import com.app.codeweb.codeweb.MainActivity;
import com.app.codeweb.codeweb.Others.CircleTransform;
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
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MyProfile extends AppCompatActivity {
    private SrlStudents userData;
    TextView fullname, id, username, email, contact;
    ImageView user_img, ic_edit;
    String html_size = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Profile");

        fullname = (TextView) findViewById(R.id.fullname);
        id = (TextView) findViewById(R.id.id);
        username = (TextView) findViewById(R.id.username);
        email = (TextView) findViewById(R.id.email);
        contact = (TextView) findViewById(R.id.contact);
        user_img = (ImageView) findViewById(R.id.user_img);
        ic_edit = (ImageView) findViewById(R.id.edit);
        if(getIntent().getSerializableExtra("data") != null){
            userData  = (SrlStudents) getIntent().getSerializableExtra("data");
            fullname.setText(userData.Fullname);
            id.setText(userData.Student_ID);
            username.setText(Html.fromHtml(userData.Username+"<br><b>Username</b>"));
            email.setText(Html.fromHtml(userData.Email+"<br><b>Email</font></b>"));
            contact.setText(Html.fromHtml(userData.Phone_number+"<br><b>Contact Number</b>"));
            Picasso.with(MyProfile.this)
                    .load(userData.Image)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.stat_sys_download)
                    .transform(new CircleTransform())
                    .into(user_img);
            QuizzesPerformance();
            OverallPer();
            BarHTML();
            BarCss();
            BarJs();

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(MyProfile.this, ProfileEdit.class);
                    in.putExtra("data", userData);
                    startActivity(in);
                }
            });

        }else{
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        toHome();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            toHome();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void QuizzesPerformance(){
        final BarChart barChart = (BarChart) findViewById(R.id.barquiz);
        HashMap postData = new HashMap();
        postData.put("Quiz_ID", ""+userData.ID);
        postData.put("Quiz_Fullname", userData.Fullname);
        PostResponseAsyncTask task = new PostResponseAsyncTask(MyProfile.this, postData, new AsyncResponse() {
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
        PostResponseAsyncTask task = new PostResponseAsyncTask(MyProfile.this, postData, new AsyncResponse() {
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
                        course.add("HTML 5 Not");
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
                    Toast.makeText(MyProfile.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                }
            }
        });task.execute("http://192.168.10.1/CodeWebScripts/load.php");
    }
    public void BarCss(){
        final BarChart barCss = (BarChart) findViewById(R.id.bar_css);
        HashMap postData = new HashMap();
        postData.put("Scr_ID", ""+userData.ID);
        postData.put("Scr_Fullname", userData.Fullname);
        PostResponseAsyncTask task = new PostResponseAsyncTask(MyProfile.this, postData, new AsyncResponse() {
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
                    Toast.makeText(MyProfile.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                }
            }
        });task.execute("http://192.168.10.1/CodeWebScripts/load.php");
    }
    public void BarJs(){
        final BarChart barJs = (BarChart) findViewById(R.id.bar_js);
            HashMap postData = new HashMap();
            postData.put("Scr_ID", ""+userData.ID);
            postData.put("Scr_Fullname", userData.Fullname);
            PostResponseAsyncTask task = new PostResponseAsyncTask(MyProfile.this, postData, new AsyncResponse() {
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
                    Toast.makeText(MyProfile.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                }
            }
        });task.execute("http://192.168.10.1/CodeWebScripts/load.php");
    }
    public void OverallPer(){
            final PieChart pieChart = (PieChart) findViewById(R.id.piegraph);
            HashMap postData = new HashMap();
            postData.put("Scr_ID", ""+userData.ID);
            postData.put("Scr_Fullname", userData.Fullname);
            PostResponseAsyncTask task = new PostResponseAsyncTask(MyProfile.this, postData, new AsyncResponse() {
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

                    int totalQuest =17;
                    float needToAttain = totalQuest - (totalHtml+Wrong_ans);
                    pieEntries.add(new PieEntry((totalHtml/totalQuest)*100, (totalHtml/totalQuest)*100+"% HTML"));
                    pieEntries.add(new PieEntry((totalCss/totalQuest)*100, (totalCss/totalQuest)*100+"% Css"));
                    pieEntries.add(new PieEntry((totalJs/totalQuest)*100, (totalJs/totalQuest)*100+"% Js"));
                    pieEntries.add(new PieEntry((Wrong_ans/totalQuest)*100,(Wrong_ans/totalQuest)*100+"% Mistakes"));
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

    public void toHome(){
        HashMap postData = new HashMap();
        postData.put("adminData", ""+userData.ID);
        PostResponseAsyncTask adminData = new PostResponseAsyncTask(MyProfile.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                ArrayList<SrlStudents> usr_data = new JsonConverter<SrlStudents>().toArrayList(s, SrlStudents.class);
                SrlStudents usrPos = usr_data.get(0);
                Intent in = new Intent(MyProfile.this, MainActivity.class);
                in.putExtra("data",usrPos);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        }); adminData.execute("http://192.168.10.1/CodeWebScripts/load.php");

    }
}
