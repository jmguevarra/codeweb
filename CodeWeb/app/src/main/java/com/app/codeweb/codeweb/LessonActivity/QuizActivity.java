package com.app.codeweb.codeweb.LessonActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.codeweb.codeweb.MainActivity;
import com.app.codeweb.codeweb.Others.Serialized.SrlQuiz;
import com.app.codeweb.codeweb.Others.Serialized.SrlStudents;
import com.app.codeweb.codeweb.Others.SoundPlayer;
import com.app.codeweb.codeweb.R;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.ArrayList;
import java.util.HashMap;

public class QuizActivity extends AppCompatActivity {
    SrlStudents userData;
    SrlQuiz quiz;
    ArrayList<SrlQuiz> Questions;
    ArrayList<SrlStudents> loadUser;
    Chronometer chrono;
    int pos;

    TextView questText;
    RadioButton rb_A,rb_B,rb_C,rb_D;
    Button next;
    SoundPlayer sound;
    String questions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_panel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sound = new SoundPlayer(QuizActivity.this);

        chrono = (Chronometer) findViewById(R.id.chronometer);
        questText = (TextView) findViewById(R.id.Question);
        rb_A = (RadioButton) findViewById(R.id.rb_A);
        rb_B = (RadioButton) findViewById(R.id.rb_B);
        rb_C = (RadioButton) findViewById(R.id.rb_C);
        rb_D = (RadioButton) findViewById(R.id.rb_D);
        next = (Button) findViewById(R.id.next);

        if(getIntent().getSerializableExtra("data")!= null && getIntent().getSerializableExtra("quiz")!= null) {
            userData = (SrlStudents) getIntent().getSerializableExtra("data");
            quiz = (SrlQuiz) getIntent().getSerializableExtra("quiz");
            pos = getIntent().getExtras().getInt("pos");
            questions = getIntent().getExtras().getString("quest");
            if(userData.Course_Level.equalsIgnoreCase("HTML")){
                getSupportActionBar().setTitle("HTML Quiz");
            }else if(userData.Course_Level.equalsIgnoreCase("Css")){
                getSupportActionBar().setTitle("Css Quiz");
            }else if(userData.Course_Level.equalsIgnoreCase("Js")){
                getSupportActionBar().setTitle("Js Quiz");
            }


            Questions = new JsonConverter<SrlQuiz>().toArrayList(questions, SrlQuiz.class);

            questText.setText(quiz.quiz_Question);
            rb_A.setText(quiz.quiz_OptA);
            rb_B.setText(quiz.quiz_OptB);
            rb_C.setText(quiz.quiz_OptC);
            rb_D.setText(quiz.quiz_OptD);

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (Questions.size() == pos + 1) {
                        LoadUser();
                        SrlStudents updateUser = loadUser.get(0);
                        Intent in = new Intent(QuizActivity.this, MainActivity.class);
                        in.putExtra("data", updateUser);
                        startActivity(in);
                        // notify with animation
                    }else {
                        if (!rb_A.isChecked() && !rb_B.isChecked() && !rb_C.isChecked() && !rb_D.isChecked()) {
                            Toast.makeText(QuizActivity.this, "Please Tap Your Desired Answer! ", Toast.LENGTH_SHORT).show();
                        } else {
                            if (rb_A.isChecked() && rb_A.getText().toString().equalsIgnoreCase(quiz.quiz_Ans)) {
                                Toast.makeText(QuizActivity.this, "Correct", Toast.LENGTH_SHORT).show();
                                sound.playCheck();
                                updateScore();
                            } else if (rb_B.isChecked() && rb_B.getText().toString().equalsIgnoreCase(quiz.quiz_Ans)) {
                                Toast.makeText(QuizActivity.this, "Correct", Toast.LENGTH_SHORT).show();
                                sound.playCheck();
                                updateScore();
                            } else if (rb_C.isChecked() && rb_C.getText().toString().equalsIgnoreCase(quiz.quiz_Ans)) {
                                Toast.makeText(QuizActivity.this, "Correct", Toast.LENGTH_SHORT).show();
                                sound.playCheck();
                                updateScore();
                            } else if (rb_D.isChecked() && rb_D.getText().toString().equalsIgnoreCase(quiz.quiz_Ans)) {
                                Toast.makeText(QuizActivity.this, "Correct", Toast.LENGTH_SHORT).show();
                                sound.playCheck();
                                updateScore();
                            } else {
                                Toast.makeText(QuizActivity.this, "Wrong", Toast.LENGTH_SHORT).show();
                                Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                                v.vibrate(200);
                                sound.playWrong();
                                if (Questions.size() == pos + 1) {
                                    getCourse();
                                }else{
                                    LoadNewQues();
                                }
                            }
                        }
                    }
                }
            });
        }else{
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    public void onBackPressed() {
        Leaving();
    }

    public void updateScore(){
        HashMap  postData = new HashMap();
        postData.put("id", userData.ID+"");
        if(userData.Course_Level.equalsIgnoreCase("HTML")){
            postData.put("QuizType", "HTMLQuiz_Score");
            postData.put("QuizSession", "HTMLQuiz");
        }else if(userData.Course_Level.equalsIgnoreCase("Css")){
            postData.put("QuizType", "CssQuiz_Score");
            postData.put("QuizSession", "CssQuiz");
        }else if(userData.Course_Level.equalsIgnoreCase("Js")){
            postData.put("QuizType", "JsQuiz_Score");
            postData.put("QuizSession", "JsQuiz");
        }
        PostResponseAsyncTask task = new PostResponseAsyncTask(QuizActivity.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if(s.contains("success")){
                    if (Questions.size() == pos + 1) {
                        getCourse();
                    }else{
                        LoadNewQues();
                    }
                }else if(s.contains("failed")){
                    Toast.makeText(QuizActivity.this, R.string.scripts_error, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(QuizActivity.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                }
            }
        });task.execute("http://192.168.10.1/CodeWebScripts/update.php");

    }

    private void Leaving() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are sure you want to Leave this Quiz?");
        builder.setIcon(android.R.drawable.ic_delete);
        builder.setMessage("This is only Once!, If you leave you lose your points.");
        builder.setPositiveButton("Continue" , new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Exit", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent in = new Intent(QuizActivity.this, MainActivity.class);
                in.putExtra("data", userData);
                startActivity(in);

            }
        });
        builder.show();
    }

    public void LoadUser(){
        HashMap postData = new HashMap();
        postData.put("ID", userData.ID+"");
        PostResponseAsyncTask task = new PostResponseAsyncTask(QuizActivity.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                loadUser = new JsonConverter<SrlStudents>().toArrayList(s, SrlStudents.class);
            }
        });task.execute("http://192.168.10.1/CodeWebScripts/load.php");
    }

    public void LoadNewQues(){
        HashMap postData = new HashMap();
        postData.put("ID", userData.ID+"");
        PostResponseAsyncTask task = new PostResponseAsyncTask(QuizActivity.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if(!s.isEmpty()) {
                    loadUser = new JsonConverter<SrlStudents>().toArrayList(s, SrlStudents.class);
                    SrlQuiz nextQuest = Questions.get(pos + 1);
                    SrlStudents updateStud = loadUser.get(0);
                    Intent in = new Intent(QuizActivity.this, QuizActivity1.class);
                    in.putExtra("data", updateStud);
                    in.putExtra("quiz", nextQuest);
                    in.putExtra("quest", questions);
                    in.putExtra("pos", pos + 1);
                    startActivity(in);
                }

            }
        });task.execute("http://192.168.10.1/CodeWebScripts/load.php");
    }

    public void getCourse(){
        if(userData.Course_Level.equalsIgnoreCase("HTML")){
            if(userData.Category_Level.equalsIgnoreCase("html_HTML5")){
                UpdateCrs("Css", "css_Basic", userData.ID);
            }
        }else if(userData.Course_Level.equalsIgnoreCase("Css")){
            if(userData.Category_Level.equalsIgnoreCase("css_Transitions")){
                UpdateCrs("Js", "js_Overview", userData.ID);
            }
        }else if(userData.Course_Level.equalsIgnoreCase("Js")){
            if(userData.Category_Level.equalsIgnoreCase("js_Events")){
                UpdateCrs("Js", "finish", userData.ID);
            }
        }
    }

    public void UpdateCrs(String nxtCourse, String nxtCat, int id){
        HashMap postData = new HashMap();
        postData.put("Crs_Level", nxtCourse);
        postData.put("Cat_Level", nxtCat);
        postData.put("id", id+"");
        PostResponseAsyncTask task = new PostResponseAsyncTask(QuizActivity.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if(s.contains("success")){
                    HashMap postData = new HashMap();
                    postData.put("ID", userData.ID+"");
                    PostResponseAsyncTask task = new PostResponseAsyncTask(QuizActivity.this, postData, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                            if(!s.isEmpty()){
                                loadUser = new JsonConverter<SrlStudents>().toArrayList(s, SrlStudents.class);
                                SrlStudents updateUser = loadUser.get(0);
                                    if(userData.Category_Level.equalsIgnoreCase("js_Events")){
                                        Toast.makeText(QuizActivity.this, "Yehhheeyyy", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(QuizActivity.this, "New Course Unlocked!.", Toast.LENGTH_SHORT).show();

                                    }
                                Intent in = new Intent(QuizActivity.this, MainActivity.class);
                                in.putExtra("data", updateUser);
                                startActivity(in);
                                // notify with animation
                            }else{
                                Toast.makeText(QuizActivity.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });task.execute("http://192.168.10.1/CodeWebScripts/load.php");
                }else if(s.contains("failed")){
                    Toast.makeText(QuizActivity.this, R.string.scripts_error, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(QuizActivity.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                }
            }
        });task.execute("http://192.168.10.1/CodeWebScripts/update.php");

    }

}
