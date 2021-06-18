package com.app.codeweb.codeweb.LessonActivity.HtmlLesson;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.app.codeweb.codeweb.LessonActivity.LesAdapter;
import com.app.codeweb.codeweb.Others.Serialized.SrlStudents;
import com.app.codeweb.codeweb.R;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;


import com.app.codeweb.codeweb.AboutUs;
import com.app.codeweb.codeweb.Others.Serialized.SrlLesson;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.ArrayList;
import java.util.HashMap;

public class HtmlLessonPanel extends AppCompatActivity{
    GridView gridview;
   /* TextView titletext;*/
    Animation animAlpha;
    ArrayList<SrlStudents> usr_data;
    private ArrayList<SrlLesson> load, finish;
    private SrlStudents userData, updateUser;
    SrlLesson item;
    int lsn_id, position , UserLevel = 0 ;
    String course;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lesson_panel);
        animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Casting Objects
        final Bundle bundle = getIntent().getExtras();
        lsn_id = bundle.getInt("id");
      /*  titletext = (TextView) findViewById(R.id.titlename);*/
        gridview = (GridView) findViewById(R.id.gridview);

        if(getIntent().getSerializableExtra("data") != null){
            userData  = (SrlStudents) getIntent().getSerializableExtra("data");
            if(userData.Type.equalsIgnoreCase("student")){
                if(userData.Course_Level.equalsIgnoreCase("HTML") && userData.HtmlQuiz.equalsIgnoreCase("NotTaken")){
                    StudentLoad();
                }else if(userData.HtmlQuiz.equalsIgnoreCase("Taken")){
                    adminLoad(lsn_id);
                }
            }else if(userData.Type.equalsIgnoreCase("administrator")){
                adminLoad(lsn_id);
            }
        }else{
            Toast.makeText(this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cw_child_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_about) {
            Intent in = new Intent(HtmlLessonPanel.this, AboutUs.class);
            startActivity(in);
            return true;
        }else if(id == android.R.id.home){
            toHome();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        toHome();
    }

//    public void BackBtn(View view){
//        Intent in = new Intent(HtmlLessonPanel.this, HtmlFunActivity.class);
//        in.putExtra("data",updateUser);
//        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(in);
//    }

    public void adminLoad(int i){
        if(i == 1){
            getSupportActionBar().setTitle("Overview");
            HashMap postData = new HashMap();
            postData.put("Course", "HTML");
            postData.put("Category", "html_Overview");
            PostResponseAsyncTask loadData = new PostResponseAsyncTask(HtmlLessonPanel.this, postData, new AsyncResponse() {
                @Override
                public void processFinish(String s) {
                    finish = new JsonConverter<SrlLesson>().toArrayList(s, SrlLesson.class);
                    LesAdapter adp = new LesAdapter(HtmlLessonPanel.this, 0, finish, 0);
                    gridview.setAdapter(adp);
                    gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                          SrlLesson lesson = finish.get(pos);
                            Intent in = new Intent(HtmlLessonPanel.this, HtmlDetailedLesson.class);
                            in.putExtra("data", lesson);
                            in.putExtra("id", lsn_id);
                            in.putExtra("finish", "finish");
                            in.putExtra("position", pos+1);
                            in.putExtra("student",  userData);
                            startActivity(in);
                        }
                    });
                }
            });loadData.execute("http://192.168.10.1/CodeWebScripts/load.php");
        }else if(i == 2){
            getSupportActionBar().setTitle("The Basics");
            HashMap postData = new HashMap();
            postData.put("Course", "HTML");
            postData.put("Category", "html_Basic");
            PostResponseAsyncTask loadData = new PostResponseAsyncTask(HtmlLessonPanel.this, postData, new AsyncResponse() {
                @Override
                public void processFinish(String s) {
                    finish = new JsonConverter<SrlLesson>().toArrayList(s, SrlLesson.class);
                    LesAdapter adp = new LesAdapter(HtmlLessonPanel.this, 0, finish, 0);
                    gridview.setAdapter(adp);
                    gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                            SrlLesson lesson = finish.get(pos);
                            Intent in = new Intent(HtmlLessonPanel.this, HtmlDetailedLesson.class);
                            in.putExtra("data", lesson);
                            in.putExtra("id", lsn_id);
                            in.putExtra("position", pos+1);
                            in.putExtra("finish", "finish");
                            in.putExtra("student",  userData);
                            startActivity(in);
                        }
                    });
                }
            });loadData.execute("http://192.168.10.1/CodeWebScripts/load.php");
        }else if(i == 3){
            getSupportActionBar().setTitle("HTML 5");
            HashMap postData = new HashMap();
            postData.put("Course", "HTML");
            postData.put("Category", "html_HTML5");
            PostResponseAsyncTask loadData = new PostResponseAsyncTask(HtmlLessonPanel.this, postData, new AsyncResponse() {
                @Override
                public void processFinish(String s) {
                    finish = new JsonConverter<SrlLesson>().toArrayList(s, SrlLesson.class);
                    LesAdapter adp = new LesAdapter(HtmlLessonPanel.this, 0, finish, 0);
                    gridview.setAdapter(adp);
                    gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                            SrlLesson lesson = finish.get(pos);
                            Intent in = new Intent(HtmlLessonPanel.this, HtmlDetailedLesson.class);
                            in.putExtra("data", lesson);
                            in.putExtra("id", lsn_id);
                            in.putExtra("finish", "finish");
                            in.putExtra("position", pos+1);
                            in.putExtra("student",  userData);
                            startActivity(in);
                        }
                    });
                }
            });loadData.execute("http://192.168.10.1/CodeWebScripts/load.php");
        }else{
            Toast.makeText(this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
        }
    }

    public void StudentLoad(){
        if(lsn_id == 1){
            if(userData.Category_Level.equalsIgnoreCase("html_Overview")){
                getSupportActionBar().setTitle("Overview");
                loadCourse();
            }else{
                adminLoad(lsn_id);
            }
        }else if(lsn_id == 2){
            if(userData.Category_Level.equalsIgnoreCase("html_Basic")){
                getSupportActionBar().setTitle("The Basic");
                loadCourse();
            }else{
                adminLoad(lsn_id);
            }
        }else if(lsn_id == 3){
            if(userData.Category_Level.equalsIgnoreCase("html_HTML5")){
                getSupportActionBar().setTitle("HTML 5");
                loadCourse();
            }else{
                adminLoad(lsn_id);
            }
        }
    }

    public void loadCourse(){
        HashMap postData = new HashMap();
        postData.put("Course", userData.Course_Level);
        postData.put("Category", userData.Category_Level);
        PostResponseAsyncTask loadData = new PostResponseAsyncTask(HtmlLessonPanel.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                load = new JsonConverter<SrlLesson>().toArrayList(s, SrlLesson.class);
                    HashMap postData = new HashMap();
                    postData.put("ID", ""+userData.ID);
                    PostResponseAsyncTask adminData = new PostResponseAsyncTask(HtmlLessonPanel.this, postData, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                            usr_data = new JsonConverter<SrlStudents>().toArrayList(s, SrlStudents.class);
                            updateUser = usr_data.get(0);

                            for(SrlStudents d:usr_data){
                                UserLevel = d.User_level;
                            }

                            LesAdapter adp = new LesAdapter(HtmlLessonPanel.this, 0, load,UserLevel);
                            gridview.setAdapter(adp);
                            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    item = load.get(i);
                                    position = i;
                                    if(lsn_id == 1 && userData.Course_Level.equalsIgnoreCase("HTML") && userData.Category_Level.equalsIgnoreCase("html_Overview")){
                                        if(i >= UserLevel) {
                                            Toast.makeText(HtmlLessonPanel.this, "Read the unlocked Lesson First", Toast.LENGTH_SHORT).show();
                                        }else{
                                            lessonDefault();
                                        }
                                    }else if(lsn_id == 2 && userData.Course_Level.equalsIgnoreCase("HTML") && userData.Category_Level.equalsIgnoreCase("html_Basic")){
                                        if(i >= UserLevel) {
                                            Toast.makeText(HtmlLessonPanel.this, "Read the unlocked Lesson First", Toast.LENGTH_SHORT).show();
                                        }else{
                                            lessonDefault();
                                        }
                                    }else if(lsn_id == 3 && userData.Course_Level.equalsIgnoreCase("HTML") && userData.Category_Level.equalsIgnoreCase("html_HTML5") ){
                                        if(i >= UserLevel) {
                                            Toast.makeText(HtmlLessonPanel.this, "Read the unlocked Lesson First", Toast.LENGTH_SHORT).show();
                                        }else{
                                            lessonDefault();
                                        }
                                    }


                                }
                            });
                        }
                    }); adminData.execute("http://192.168.10.1/CodeWebScripts/load.php");
            }
        });loadData.execute("http://192.168.10.1/CodeWebScripts/load.php");
    }

    public void lessonDefault(){
        SrlLesson lesson = load.get(position);
        Intent in = new Intent(HtmlLessonPanel.this, HtmlDetailedLesson.class);
        in.putExtra("data", lesson);
        in.putExtra("id", lsn_id);
        in.putExtra("position", position+1);
        in.putExtra("finish", "not");
        in.putExtra("student",  updateUser);
        startActivity(in);
    }

    public void toHome(){
        HashMap postData = new HashMap();
        postData.put("adminData", ""+userData.ID);
        PostResponseAsyncTask adminData = new PostResponseAsyncTask(HtmlLessonPanel.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                ArrayList<SrlStudents> usr_data = new JsonConverter<SrlStudents>().toArrayList(s, SrlStudents.class);
                SrlStudents usrPos = usr_data.get(0);
                Intent in = new Intent(HtmlLessonPanel.this, HtmlFunActivity.class);
                in.putExtra("data",usrPos);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        }); adminData.execute("http://192.168.10.1/CodeWebScripts/load.php");

    }
}