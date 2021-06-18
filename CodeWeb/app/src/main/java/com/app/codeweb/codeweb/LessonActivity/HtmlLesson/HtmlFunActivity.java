package com.app.codeweb.codeweb.LessonActivity.HtmlLesson;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;

import com.app.codeweb.codeweb.AboutUs;
import com.app.codeweb.codeweb.MainActivity;
import com.app.codeweb.codeweb.Others.Serialized.SrlStudents;
import com.app.codeweb.codeweb.R;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.ArrayList;
import java.util.HashMap;

public class HtmlFunActivity extends AppCompatActivity {
    ImageButton htmlbasics, challenges,overview, html5;
    Animation animAlpha;
    ArrayList<SrlStudents> usr_data;
    private SrlStudents ID ,userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lesson_html_fun);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("HTML Fun");

        animAlpha  = AnimationUtils.loadAnimation(this, R.anim.alpha);
        overview = (ImageButton) findViewById(R.id.overviewbtn);
        challenges = (ImageButton) findViewById(R.id.challengesbtn);
        htmlbasics = (ImageButton) findViewById(R.id.htmlbasicbtn);
        html5 = (ImageButton) findViewById(R.id.html5btn);

        if(getIntent().getSerializableExtra("data") != null){
            ID  = (SrlStudents) getIntent().getSerializableExtra("data");
            if(ID.Type.equalsIgnoreCase("student")) {
                HashMap postData = new HashMap();
                postData.put("ID", ""+ID.ID);
                PostResponseAsyncTask adminData = new PostResponseAsyncTask(HtmlFunActivity.this, postData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        usr_data = new JsonConverter<SrlStudents>().toArrayList(s, SrlStudents.class);
                        userData = usr_data.get(0);
                        String CatLevel = "";

                        for(SrlStudents d:usr_data){
                            CatLevel = d.Category_Level;
                        }
                        if(userData.Course_Level.equalsIgnoreCase("HTML") && userData.HtmlQuiz.equalsIgnoreCase("NotTaken")){
                            UserLevel(CatLevel);
                        }else if(userData.HtmlQuiz.equalsIgnoreCase("Taken")){
                            // Do tNothing For Admin
                        }
                    }
                }); adminData.execute("http://192.168.10.1/CodeWebScripts/load.php");
            }else{
                // Do tNothing For Admin
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

        if (id == R.id.action_about) {
            Intent in = new Intent(HtmlFunActivity.this, AboutUs.class);
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
        NavUtils.navigateUpFromSameTask(this);
    }

//    public void BackBtn(View view){
//        view.startAnimation(animAlpha);
//        Intent in = new Intent(HtmlFunActivity.this, MainActivity.class);
//        in.putExtra("data",userData);
//        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(in);
//    }

    public void UserLevel(String Cat){
        challenges.setEnabled(false);
        overview.setEnabled(false);
        htmlbasics.setEnabled(false);
        html5.setEnabled(false);
        if(Cat.equalsIgnoreCase("html_Overview")){
            overview.setEnabled(true);
            htmlbasics.setImageResource(R.drawable.ut_basic_btn);
            html5.setImageResource(R.drawable.ut_html5);
        }else if(Cat.equalsIgnoreCase("html_Basic")){
            overview.setEnabled(true);
            htmlbasics.setEnabled(true);
            html5.setImageResource(R.drawable.ut_html5);
        }else if(Cat.equalsIgnoreCase("html_HTML5")){
            overview.setEnabled(true);
            htmlbasics.setEnabled(true);
            html5.setEnabled(true);
        }
    }


    public void gotoOverviewbtn(View view){
        view.startAnimation(animAlpha);
        if(ID.Type.equalsIgnoreCase("student")){
            Intent in = new Intent(getApplicationContext(), HtmlLessonPanel.class);
            in.putExtra("data", userData);
            in.putExtra("id", 1);
            startActivity(in);
        }else{
            Intent in = new Intent(getApplicationContext(), HtmlLessonPanel.class);
            in.putExtra("id", 1);
            in.putExtra("data", ID);
            startActivity(in);
        }
    }

    public void gotoBasicbtn(View view) {
        view.startAnimation(animAlpha);
        if(ID.Type.equalsIgnoreCase("student")){
            Intent in = new Intent(getApplicationContext(), HtmlLessonPanel.class);
            in.putExtra("data", userData);
            in.putExtra("id", 2);
            startActivity(in);
        }else{
            Intent in = new Intent(getApplicationContext(), HtmlLessonPanel.class);
            in.putExtra("id", 2);
            in.putExtra("data", ID);
            startActivity(in);
        }
    }

    public void gotoHtml5(View view) {
        view.startAnimation(animAlpha);
        if(ID.Type.equalsIgnoreCase("student")){
            Intent in = new Intent(getApplicationContext(), HtmlLessonPanel.class);
            in.putExtra("data", userData);
            in.putExtra("id", 3);
            startActivity(in);
        }else{
            Intent in = new Intent(getApplicationContext(), HtmlLessonPanel.class);
            in.putExtra("id", 3);
            in.putExtra("data", ID);
            startActivity(in);
        }
    }

    public void gotoChallengesbtn(View view) {
        view.startAnimation(animAlpha);
//        Intent beat = new Intent(getApplicationContext(), HtmlLessonPanel.class);
//        beat.putExtra("beat", 3);
//        startActivity(beat);
    }

    public void toHome(){
        HashMap postData = new HashMap();
        postData.put("adminData", ""+ID.ID);
        PostResponseAsyncTask adminData = new PostResponseAsyncTask(HtmlFunActivity.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
               ArrayList<SrlStudents> data = new JsonConverter<SrlStudents>().toArrayList(s, SrlStudents.class);
               SrlStudents usrPos = data.get(0);
                Intent in = new Intent(HtmlFunActivity.this, MainActivity.class);
                in.putExtra("data",usrPos);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        }); adminData.execute("http://192.168.10.1/CodeWebScripts/load.php");
    }
}
