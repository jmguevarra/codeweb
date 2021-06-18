package com.app.codeweb.codeweb.LessonActivity.Js_Activity;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.codeweb.codeweb.AboutUs;
import com.app.codeweb.codeweb.LessonActivity.CssLesson.CssFunActivity;
import com.app.codeweb.codeweb.MainActivity;
import com.app.codeweb.codeweb.Others.Serialized.SrlStudents;
import com.app.codeweb.codeweb.R;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.ArrayList;
import java.util.HashMap;

public class JsFunActivity extends AppCompatActivity {
    ImageView js_overview, js_basic ,js_loop, js_function, js_objects, js_core, js_events;
    Animation animAlpha;
    ArrayList<SrlStudents> usr_data;
    private SrlStudents ID ,userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lesson_js_fun);
        animAlpha  = AnimationUtils.loadAnimation(this, R.anim.alpha);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Js Fun");

        js_overview = (ImageButton) findViewById(R.id.js_overview);
        js_basic  = (ImageButton) findViewById(R.id.js_basic);
        js_loop = (ImageButton) findViewById(R.id.js_loop);
        js_function = (ImageButton) findViewById(R.id.js_function);
        js_objects = (ImageButton) findViewById(R.id.js_objects);
        js_core = (ImageButton) findViewById(R.id.js_core);
        js_events = (ImageButton) findViewById(R.id.js_events);

        if(getIntent().getSerializableExtra("data") != null){
            ID  = (SrlStudents) getIntent().getSerializableExtra("data");
            if(ID.Type.equalsIgnoreCase("student")) {
                HashMap postData = new HashMap();
                postData.put("ID", ""+ID.ID);
                PostResponseAsyncTask adminData = new PostResponseAsyncTask(JsFunActivity.this, postData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        usr_data = new JsonConverter<SrlStudents>().toArrayList(s, SrlStudents.class);
                        userData = usr_data.get(0);
                        String CatLevel = "";

                        for(SrlStudents d:usr_data){
                            CatLevel = d.Category_Level;
                        }
                        if(userData.Course_Level.equalsIgnoreCase("Js") && userData.JsQuiz.equalsIgnoreCase("NotTaken")){
                            UserLevel(CatLevel);
                        }else if(userData.JsQuiz.equalsIgnoreCase("Taken")){
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
             Intent in = new Intent(JsFunActivity.this, AboutUs.class);
             startActivity(in);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }


    public void UserLevel(String Cat){
        js_overview.setEnabled(false);
        js_basic.setEnabled(false);
        js_loop.setEnabled(false);
        js_function.setEnabled(false);
        js_objects.setEnabled(false);
        js_core.setEnabled(false);
        js_events.setEnabled(false);

        if(Cat.equalsIgnoreCase("js_Overview")){
            js_overview.setEnabled(true);
            js_basic.setImageResource(R.drawable.ut_basic_btn);
            js_loop.setImageResource(R.drawable.ut_loop_icon);
            js_function.setImageResource(R.drawable.ut_functions);
            js_objects.setImageResource(R.drawable.ut_objects);
            js_core.setImageResource(R.drawable.ut_core_objects);
            js_events.setImageResource(R.drawable.ut_events);
        }else if(Cat.equalsIgnoreCase("js_Basic")){
            js_overview.setEnabled(true);
            js_basic.setEnabled(true);
            js_loop.setImageResource(R.drawable.ut_loop_icon);
            js_function.setImageResource(R.drawable.ut_functions);
            js_objects.setImageResource(R.drawable.ut_objects);
            js_core.setImageResource(R.drawable.ut_core_objects);
            js_events.setImageResource(R.drawable.ut_events);
        }else if(Cat.equalsIgnoreCase("js_ConLoops")){
            js_overview.setEnabled(true);
            js_basic.setEnabled(true);
            js_loop.setEnabled(true);
            js_function.setImageResource(R.drawable.ut_functions);
            js_objects.setImageResource(R.drawable.ut_objects);
            js_core.setImageResource(R.drawable.ut_core_objects);
            js_events.setImageResource(R.drawable.ut_events);
        }else if(Cat.equalsIgnoreCase("js_Functions")){
            js_overview.setEnabled(true);
            js_basic.setEnabled(true);
            js_loop.setEnabled(true);
            js_function.setEnabled(true);
            js_objects.setImageResource(R.drawable.ut_objects);
            js_core.setImageResource(R.drawable.ut_core_objects);
            js_events.setImageResource(R.drawable.ut_events);
        }else if(Cat.equalsIgnoreCase("js_Objects")){
            js_overview.setEnabled(true);
            js_basic.setEnabled(true);
            js_loop.setEnabled(true);
            js_function.setEnabled(true);
            js_objects.setEnabled(true);
            js_core.setImageResource(R.drawable.ut_core_objects);
            js_events.setImageResource(R.drawable.ut_events);
        }else if(Cat.equalsIgnoreCase("js_Core")){
            js_overview.setEnabled(true);
            js_basic.setEnabled(true);
            js_loop.setEnabled(true);
            js_function.setEnabled(true);
            js_objects.setEnabled(true);
            js_core.setEnabled(true);
            js_events.setImageResource(R.drawable.ut_events);
        }else if(Cat.equalsIgnoreCase("js_Events")){
            js_overview.setEnabled(true);
            js_basic.setEnabled(true);
            js_loop.setEnabled(true);
            js_function.setEnabled(true);
            js_objects.setEnabled(true);
            js_core.setEnabled(true);
            js_events.setEnabled(true);
        }
    }

//Buttons
    public void gotoJsOverview(View v){
        v.startAnimation(animAlpha);
        if(ID.Type.equalsIgnoreCase("student")){
            Intent in = new Intent(getApplicationContext(), JsLessonPanel.class);
            in.putExtra("data", userData);
            in.putExtra("id", 1);
            startActivity(in);
        }else{
            Intent in = new Intent(getApplicationContext(), JsLessonPanel.class);
            in.putExtra("id", 1);
            in.putExtra("data", ID);
            startActivity(in);
        }
    }
    public void gotoJsBasic(View v){
        v.startAnimation(animAlpha);
        if(ID.Type.equalsIgnoreCase("student")){
            Intent in = new Intent(getApplicationContext(), JsLessonPanel.class);
            in.putExtra("data", userData);
            in.putExtra("id", 2);
            startActivity(in);
        }else{
            Intent in = new Intent(getApplicationContext(), JsLessonPanel.class);
            in.putExtra("id", 2);
            in.putExtra("data", ID);
            startActivity(in);
        }
    }
    public void gotoJsLoop_Con(View v){
        v.startAnimation(animAlpha);
        if(ID.Type.equalsIgnoreCase("student")){
            Intent in = new Intent(getApplicationContext(), JsLessonPanel.class);
            in.putExtra("data", userData);
            in.putExtra("id", 3);
            startActivity(in);
        }else{
            Intent in = new Intent(getApplicationContext(), JsLessonPanel.class);
            in.putExtra("id", 3);
            in.putExtra("data", ID);
            startActivity(in);
        }
    }
    public void gotoJsFunctions(View v){
        v.startAnimation(animAlpha);
        if(ID.Type.equalsIgnoreCase("student")){
            Intent in = new Intent(getApplicationContext(), JsLessonPanel.class);
            in.putExtra("data", userData);
            in.putExtra("id", 4);
            startActivity(in);
        }else{
            Intent in = new Intent(getApplicationContext(), JsLessonPanel.class);
            in.putExtra("id", 4);
            in.putExtra("data", ID);
            startActivity(in);
        }
    }
    public void gotoJsObjects(View v){
        v.startAnimation(animAlpha);
        if(ID.Type.equalsIgnoreCase("student")){
            Intent in = new Intent(getApplicationContext(), JsLessonPanel.class);
            in.putExtra("data", userData);
            in.putExtra("id", 5);
            startActivity(in);
        }else{
            Intent in = new Intent(getApplicationContext(), JsLessonPanel.class);
            in.putExtra("id", 5);
            in.putExtra("data", ID);
            startActivity(in);
        }
    }
    public void gotoJsCore(View v){
        v.startAnimation(animAlpha);
        if(ID.Type.equalsIgnoreCase("student")){
            Intent in = new Intent(getApplicationContext(), JsLessonPanel.class);
            in.putExtra("data", userData);
            in.putExtra("id", 6);
            startActivity(in);
        }else{
            Intent in = new Intent(getApplicationContext(), JsLessonPanel.class);
            in.putExtra("id", 6);
            in.putExtra("data", ID);
            startActivity(in);
        }
    }
    public void gotoJsEvents(View v){
        v.startAnimation(animAlpha);
        if(ID.Type.equalsIgnoreCase("student")){
            Intent in = new Intent(getApplicationContext(), JsLessonPanel.class);
            in.putExtra("data", userData);
            in.putExtra("id", 7);
            startActivity(in);
        }else{
            Intent in = new Intent(getApplicationContext(), JsLessonPanel.class);
            in.putExtra("id", 7);
            in.putExtra("data", ID);
            startActivity(in);
        }
    }

}
