package com.app.codeweb.codeweb.LessonActivity.CssLesson;

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
import com.app.codeweb.codeweb.LessonActivity.HtmlLesson.HtmlFunActivity;
import com.app.codeweb.codeweb.MainActivity;
import com.app.codeweb.codeweb.Others.Serialized.SrlStudents;
import com.app.codeweb.codeweb.R;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.ArrayList;
import java.util.HashMap;

public class CssFunActivity extends AppCompatActivity {
    ImageView css_basic, css_text,css_properties,css_layout,css3,css_image,css_transition;
    Animation animAlpha;
    ArrayList<SrlStudents> usr_data;
    private SrlStudents ID ,userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lesson_css_fun);
        animAlpha  = AnimationUtils.loadAnimation(this, R.anim.alpha);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Css Fun");

        css_basic = (ImageButton) findViewById(R.id.css_basic);
        css_text = (ImageButton) findViewById(R.id.css_text);
        css_properties = (ImageButton) findViewById(R.id.css_properties);
        css_layout = (ImageButton) findViewById(R.id.css_layout);
        css3 = (ImageButton) findViewById(R.id.css3);
        css_image = (ImageButton) findViewById(R.id.css_image);
        css_transition = (ImageButton) findViewById(R.id.css_transition);


        if(getIntent().getSerializableExtra("data") != null){
            ID  = (SrlStudents) getIntent().getSerializableExtra("data");
            if(ID.Type.equalsIgnoreCase("student")) {
                HashMap postData = new HashMap();
                postData.put("ID", ""+ID.ID);
                PostResponseAsyncTask adminData = new PostResponseAsyncTask(CssFunActivity.this, postData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        usr_data = new JsonConverter<SrlStudents>().toArrayList(s, SrlStudents.class);
                        userData = usr_data.get(0);
                        String CatLevel = "";

                        for(SrlStudents d:usr_data){
                            CatLevel = d.Category_Level;
                        }
                        if(userData.Course_Level.equalsIgnoreCase("Css") && userData.CssQuiz.equalsIgnoreCase("NotTaken")){
                            UserLevel(CatLevel);
                        }else if(userData.CssQuiz.equalsIgnoreCase("Taken")){
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
           Intent in = new Intent(CssFunActivity.this, AboutUs.class);
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
        css_basic.setEnabled(false);
        css_text.setEnabled(false);
        css_properties.setEnabled(false);
        css_layout.setEnabled(false);
        css3.setEnabled(false);
        css_image.setEnabled(false);
        css_transition.setEnabled(false);

        if(Cat.equalsIgnoreCase("css_Basic")){
            css_basic.setEnabled(true);
            css_text.setImageResource(R.drawable.ut_text);
            css_properties.setImageResource(R.drawable.ut_properties);
            css_layout.setImageResource(R.drawable.ut_layouts);
            css3.setImageResource(R.drawable.ut_css3);
            css_image.setImageResource(R.drawable.ut_image_background);
            css_transition.setImageResource(R.drawable.ut_transition);
        }else if(Cat.equalsIgnoreCase("css_Text")){
            css_basic.setEnabled(true);
            css_text.setEnabled(true);
            css_properties.setImageResource(R.drawable.ut_properties);
            css_layout.setImageResource(R.drawable.ut_layouts);
            css3.setImageResource(R.drawable.ut_css3);
            css_image.setImageResource(R.drawable.ut_image_background);
            css_transition.setImageResource(R.drawable.ut_transition);
        }else if(Cat.equalsIgnoreCase("css_Properties")){
            css_basic.setEnabled(true);
            css_text.setEnabled(true);
            css_properties.setEnabled(true);
            css_layout.setImageResource(R.drawable.ut_layouts);
            css3.setImageResource(R.drawable.ut_css3);
            css_image.setImageResource(R.drawable.ut_image_background);
            css_transition.setImageResource(R.drawable.ut_transition);
        }else if(Cat.equalsIgnoreCase("css_Layouts")){
            css_basic.setEnabled(true);
            css_text.setEnabled(true);
            css_properties.setEnabled(true);
            css_layout.setEnabled(true);
            css3.setImageResource(R.drawable.ut_css3);
            css_image.setImageResource(R.drawable.ut_image_background);
            css_transition.setImageResource(R.drawable.ut_transition);
        }else if(Cat.equalsIgnoreCase("css_Css3")){
            css_basic.setEnabled(true);
            css_text.setEnabled(true);
            css_properties.setEnabled(true);
            css_layout.setEnabled(true);
            css3.setEnabled(true);
            css_image.setImageResource(R.drawable.ut_image_background);
            css_transition.setImageResource(R.drawable.ut_transition);
        }else if(Cat.equalsIgnoreCase("css_Backgrounds")){
            css_basic.setEnabled(true);
            css_text.setEnabled(true);
            css_properties.setEnabled(true);
            css_layout.setEnabled(true);
            css3.setEnabled(true);
            css_image.setEnabled(true);
            css_transition.setImageResource(R.drawable.ut_transition);
        }else if(Cat.equalsIgnoreCase("css_Transitions")){
            css_basic.setEnabled(true);
            css_text.setEnabled(true);
            css_properties.setEnabled(true);
            css_layout.setEnabled(true);
            css3.setEnabled(true);
            css_image.setEnabled(true);
            css_transition.setEnabled(true);
        }
    }

    //Buttons
    public void gotoCssBasic(View v){
        v.startAnimation(animAlpha);
        if(ID.Type.equalsIgnoreCase("student")){
            Intent in = new Intent(getApplicationContext(), CssLessonPanel.class);
            in.putExtra("data", userData);
            in.putExtra("id", 1);
            startActivity(in);
        }else{
            Intent in = new Intent(getApplicationContext(), CssLessonPanel.class);
            in.putExtra("id", 1);
            in.putExtra("data", ID);
            startActivity(in);
        }
    }
    public void gotoCssText(View v){
        v.startAnimation(animAlpha);
        if(ID.Type.equalsIgnoreCase("student")){
            Intent in = new Intent(getApplicationContext(), CssLessonPanel.class);
            in.putExtra("data", userData);
            in.putExtra("id", 2);
            startActivity(in);
        }else{
            Intent in = new Intent(getApplicationContext(), CssLessonPanel.class);
            in.putExtra("id", 2);
            in.putExtra("data", ID);
            startActivity(in);
        }
    }
    public void gotoCssProperties(View v){
        v.startAnimation(animAlpha);
        if(ID.Type.equalsIgnoreCase("student")){
            Intent in = new Intent(getApplicationContext(), CssLessonPanel.class);
            in.putExtra("data", userData);
            in.putExtra("id", 3);
            startActivity(in);
        }else{
            Intent in = new Intent(getApplicationContext(), CssLessonPanel.class);
            in.putExtra("id", 3);
            in.putExtra("data", ID);
            startActivity(in);
        }
    }
    public void gotoCssLayouts(View v){
        v.startAnimation(animAlpha);
        if(ID.Type.equalsIgnoreCase("student")){
            Intent in = new Intent(getApplicationContext(), CssLessonPanel.class);
            in.putExtra("data", userData);
            in.putExtra("id", 4);
            startActivity(in);
        }else{
            Intent in = new Intent(getApplicationContext(), CssLessonPanel.class);
            in.putExtra("id", 4);
            in.putExtra("data", ID);
            startActivity(in);
        }
    }
    public void gotoCss3(View v){
        v.startAnimation(animAlpha);
        if(ID.Type.equalsIgnoreCase("student")){
            Intent in = new Intent(getApplicationContext(), CssLessonPanel.class);
            in.putExtra("data", userData);
            in.putExtra("id", 5);
            startActivity(in);
        }else{
            Intent in = new Intent(getApplicationContext(), CssLessonPanel.class);
            in.putExtra("id", 5);
            in.putExtra("data", ID);
            startActivity(in);
        }
    }
    public void gotoCssImage(View v){
        v.startAnimation(animAlpha);
        if(ID.Type.equalsIgnoreCase("student")){
            Intent in = new Intent(getApplicationContext(), CssLessonPanel.class);
            in.putExtra("data", userData);
            in.putExtra("id", 6);
            startActivity(in);
        }else{
            Intent in = new Intent(getApplicationContext(), CssLessonPanel.class);
            in.putExtra("id", 6);
            in.putExtra("data", ID);
            startActivity(in);
        }
    }
    public void gotoCssTransition(View v){
        v.startAnimation(animAlpha);
        if(ID.Type.equalsIgnoreCase("student")){
            Intent in = new Intent(getApplicationContext(), CssLessonPanel.class);
            in.putExtra("data", userData);
            in.putExtra("id", 7);
            startActivity(in);
        }else{
            Intent in = new Intent(getApplicationContext(), CssLessonPanel.class);
            in.putExtra("id", 7);
            in.putExtra("data", ID);
            startActivity(in);
        }
    }


}
