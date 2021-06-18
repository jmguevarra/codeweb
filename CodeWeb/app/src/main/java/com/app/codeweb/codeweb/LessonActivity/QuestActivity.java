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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.codeweb.codeweb.LessonActivity.CssLesson.CssFunActivity;
import com.app.codeweb.codeweb.LessonActivity.CssLesson.CssLessonPanel;
import com.app.codeweb.codeweb.LessonActivity.HtmlLesson.HtmlFunActivity;
import com.app.codeweb.codeweb.LessonActivity.HtmlLesson.HtmlLessonPanel;
import com.app.codeweb.codeweb.LessonActivity.Js_Activity.JsFunActivity;
import com.app.codeweb.codeweb.LessonActivity.Js_Activity.JsLessonPanel;
import com.app.codeweb.codeweb.Others.Serialized.SrlLesson;
import com.app.codeweb.codeweb.Others.Serialized.SrlQuiz;
import com.app.codeweb.codeweb.Others.Serialized.SrlStudents;
import com.app.codeweb.codeweb.Others.SoundPlayer;
import com.app.codeweb.codeweb.R;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.ArrayList;
import java.util.HashMap;

public class QuestActivity extends AppCompatActivity {
    SrlLesson data;
    SrlStudents userData, updateData;

    TextView questText;
    RadioButton rb_A,rb_B,rb_C,rb_D;
    Button next;
    ArrayList<SrlLesson> load;
    ArrayList<SrlStudents> loadUser;
    ArrayList<SrlQuiz> quiz;
    int lsn_id;
    SoundPlayer sound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lession_quest);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sound = new SoundPlayer(QuestActivity.this);

        questText = (TextView) findViewById(R.id.Question);
        rb_A = (RadioButton) findViewById(R.id.rb_A);
        rb_B = (RadioButton) findViewById(R.id.rb_B);
        rb_C = (RadioButton) findViewById(R.id.rb_C);
        rb_D = (RadioButton) findViewById(R.id.rb_D);
        next = (Button) findViewById(R.id.next);

        if(getIntent().getSerializableExtra("data") != null && getIntent().getSerializableExtra("student") != null) {
            data = (SrlLesson) getIntent().getSerializableExtra("data");
            userData = (SrlStudents) getIntent().getSerializableExtra("student");
            getSupportActionBar().setTitle(data.lsn_title);

            final Bundle bundle = getIntent().getExtras();
            lsn_id = bundle.getInt("id");

            questText.setText(data.lsn_question);
            rb_A.setText(data.lsn_optA);
            rb_B.setText(data.lsn_optB);
            rb_C.setText(data.lsn_optC);
            rb_D.setText(data.lsn_optD);

            AsyncLoad();
        }else{
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Leaving();
    }

    private void Leaving() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("You want to close this Question?");
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setMessage("You'll loss several points if you are not answering this question. Are you sure you want Leave this Question?");
        builder.setPositiveButton("No" , new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Yes", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateUserLevel();
            }
        });
        builder.show();
    }

    public void gotoNextLesson(View view){
        if(rb_A.isChecked() && rb_A.getText().toString().equalsIgnoreCase(data.lsn_rightOpt)){
            Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
            updateScore();
            sound.playCheck();
        }else if(rb_B.isChecked() && rb_B.getText().toString().equalsIgnoreCase(data.lsn_rightOpt)){
            Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
            sound.playCheck();
            updateScore();
        }else if(rb_C.isChecked() && rb_C.getText().toString().equalsIgnoreCase(data.lsn_rightOpt)){
            Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
            sound.playCheck();
            updateScore();
        }else if(rb_D.isChecked() && rb_D.getText().toString().equalsIgnoreCase(data.lsn_rightOpt)){
            Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
            sound.playCheck();
            updateScore();
        }else{
            Toast.makeText(this, "Wrong", Toast.LENGTH_SHORT).show();
            Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(200);
            sound.playWrong();
            updateUserLevel();
        }
    }

    public void updateScore(){
        if(userData.User_level == load.size()){
            if((userData.HtmlQuiz.equalsIgnoreCase("NotTaken") && userData.Category_Level.equalsIgnoreCase("html_HTML5") && userData.Course_Level.equalsIgnoreCase("HTML")) ||
                    (userData.CssQuiz.equalsIgnoreCase("NotTaken") && userData.Category_Level.equalsIgnoreCase("css_Transitions") && userData.Course_Level.equalsIgnoreCase("Css")) ||
                    (userData.JsQuiz.equalsIgnoreCase("NotTaken") && userData.Category_Level.equalsIgnoreCase("js_Events") && userData.Course_Level.equalsIgnoreCase("Js")) ) {
                AlertDialog.Builder builder = new AlertDialog.Builder(QuestActivity.this);
                builder.setTitle("Your Quiz is Ready");
                if(userData.Course_Level.equalsIgnoreCase("HTML")){
                    builder.setIcon(R.drawable.ic_html);
                }else if(userData.Course_Level.equalsIgnoreCase("Css")){
                    builder.setIcon(R.drawable.ic_css);
                }else if(userData.Course_Level.equalsIgnoreCase("Js")){
                    builder.setIcon(R.drawable.ic_js);
                }
                builder.setMessage("Are you sure you want to take your quiz now?");
                builder.setPositiveButton("I need to Review" , new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(QuestActivity.this, "Take your Review First...", Toast.LENGTH_SHORT).show();
                        if(userData.Course_Level.equalsIgnoreCase("HTML")){
                            Intent in = new Intent(QuestActivity.this, HtmlFunActivity.class);
                            in.putExtra("data", userData);
                            in.putExtra("id", lsn_id);
                            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); in.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(in);
                        }else if(userData.Course_Level.equalsIgnoreCase("Css")){
                            Intent in = new Intent(QuestActivity.this, CssFunActivity.class);
                            in.putExtra("data", userData);
                            in.putExtra("id", lsn_id);
                            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); in.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(in);
                        }else if(userData.Course_Level.equalsIgnoreCase("Js")){
                            Intent in = new Intent(QuestActivity.this, JsFunActivity.class);
                            in.putExtra("data", userData);
                            in.putExtra("id", lsn_id);
                            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); in.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(in);
                        }

                    }
                });
                builder.setNegativeButton("Yes", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int level = 1 ,tScore = userData.Points+1;
                        HashMap postData = new HashMap();
                        postData.put("id", userData.ID+"");
                        postData.put("CategoryLevel", userData.Category_Level);
                        postData.put("CourseLevel", userData.Course_Level);
                        postData.put("Points", tScore+"");
                        postData.put("UserLevel", level+""); PostResponseAsyncTask task = new PostResponseAsyncTask(QuestActivity.this, postData, new AsyncResponse() {
                            @Override
                            public void processFinish(String s) {
                                if(s.contains("success")){
                                    if(userData.Course_Level.equalsIgnoreCase("HTML")){
                                        GetQuizzes("HTML");
                                    }else if(userData.Course_Level.equalsIgnoreCase("Css")){
                                        GetQuizzes("Css");
                                    }else if(userData.Course_Level.equalsIgnoreCase("Js")){
                                        GetQuizzes("Js");
                                    }
                                }else if(s.contains("failed")){
                                    Toast.makeText(QuestActivity.this, R.string.scripts_error, Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(QuestActivity.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });task.execute("http://192.168.10.1/CodeWebScripts/update.php");
                    }

                });
                builder.show();
            }else{ //update the Category
                int level = 1 ,tScore = userData.Points+1;
                updateCategory();
                HashMap postData = new HashMap();
                postData.put("id", userData.ID+"");
                postData.put("CategoryLevel", userData.Category_Level);
                postData.put("CourseLevel", userData.Course_Level);
                postData.put("Points", tScore+"");
                postData.put("UserLevel", level+""); PostResponseAsyncTask task = new PostResponseAsyncTask(QuestActivity.this, postData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        if(s.contains("success")){
                            HashMap postData = new HashMap();
                            postData.put("ID", userData.ID+"");
                            PostResponseAsyncTask task = new PostResponseAsyncTask(QuestActivity.this, postData, new AsyncResponse() {
                                @Override
                                public void processFinish(String s) {
                                    if(!s.isEmpty()){
                                        loadUser = new JsonConverter<SrlStudents>().toArrayList(s, SrlStudents.class);
                                        SrlStudents studentsData = loadUser.get(0);
                                        if(userData.Course_Level.equalsIgnoreCase("HTML")){
                                            Intent in = new Intent(QuestActivity.this, HtmlFunActivity.class);
                                            in.putExtra("data", studentsData);
                                            startActivity(in);
                                        }else if(userData.Course_Level.equalsIgnoreCase("Css")){
                                            Intent in = new Intent(QuestActivity.this, CssFunActivity.class);
                                            in.putExtra("data", studentsData);
                                            startActivity(in);
                                        }else if(userData.Course_Level.equalsIgnoreCase("Js")){
                                            Intent in = new Intent(QuestActivity.this, JsFunActivity.class);
                                            in.putExtra("data", studentsData);
                                            startActivity(in);
                                        }
                                    }else{
                                        Toast.makeText(QuestActivity.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });task.execute("http://192.168.10.1/CodeWebScripts/load.php");
                        }else if(s.contains("failed")){
                            Toast.makeText(QuestActivity.this, R.string.scripts_error, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(QuestActivity.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                        }
                    }
                });task.execute("http://192.168.10.1/CodeWebScripts/update.php");
            }
        }else{// Update the Lesson
            CorrectAns(userData.Points+1, userData.User_level+1);
        }
    }
    public void CorrectAns(int Points, int level){
        HashMap postData = new HashMap();
        postData.put("id", userData.ID+"");
        postData.put("CategoryLevel", userData.Category_Level);
        postData.put("CourseLevel", userData.Course_Level);
        postData.put("Points", Points+"");
        postData.put("UserLevel", level+"");
        PostResponseAsyncTask task = new PostResponseAsyncTask(QuestActivity.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if(s.contains("success")){
                    HashMap postData = new HashMap();
                    postData.put("ID", userData.ID+"");
                    PostResponseAsyncTask task = new PostResponseAsyncTask(QuestActivity.this, postData, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                            loadUser = new JsonConverter<SrlStudents>().toArrayList(s, SrlStudents.class);
                            SrlStudents updateUser = loadUser.get(0);

                            if(userData.Course_Level.equalsIgnoreCase("HTML")){
                               Intent in = new Intent(QuestActivity.this, HtmlLessonPanel.class);
                                in.putExtra("data", updateUser);
                                in.putExtra("id", lsn_id);
                                startActivity(in);
                            }else if(userData.Course_Level.equalsIgnoreCase("Css")){
                                Intent in = new Intent(QuestActivity.this, CssLessonPanel.class);
                                in.putExtra("data", updateUser);
                                in.putExtra("id", lsn_id);
                                startActivity(in);
                            }else if(userData.Course_Level.equalsIgnoreCase("Js")){
                                Intent in = new Intent(QuestActivity.this, JsLessonPanel.class);
                                in.putExtra("data", updateUser);
                                in.putExtra("id", lsn_id);
                                startActivity(in);
                            }


                            //Need Notification for new Lesson
                            }
                    });task.execute("http://192.168.10.1/CodeWebScripts/load.php");
                }else if(s.contains("failed")){
                    Toast.makeText(QuestActivity.this, R.string.scripts_error, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(QuestActivity.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                }

            }
        });task.execute("http://192.168.10.1/CodeWebScripts/update.php");
    }

    public void updateUserLevel(){
        //Update the userLevel if Wrong
        if(userData.User_level == load.size()){
            if((userData.HtmlQuiz.equalsIgnoreCase("NotTaken") && userData.Category_Level.equalsIgnoreCase("html_HTML5") && userData.Course_Level.equalsIgnoreCase("HTML")) ||
                    (userData.CssQuiz.equalsIgnoreCase("NotTaken") && userData.Category_Level.equalsIgnoreCase("css_Transitions") && userData.Course_Level.equalsIgnoreCase("Css")) ||
                    (userData.JsQuiz.equalsIgnoreCase("NotTaken") && userData.Category_Level.equalsIgnoreCase("js_Events") && userData.Course_Level.equalsIgnoreCase("Js")) ) {

                AlertDialog.Builder builder = new AlertDialog.Builder(QuestActivity.this);
                builder.setTitle("Your Quiz is Ready");
                if(userData.Course_Level.equalsIgnoreCase("HTML")){
                    builder.setIcon(R.drawable.ic_html);
                }else if(userData.Course_Level.equalsIgnoreCase("Css")){
                    builder.setIcon(R.drawable.ic_css);
                }else if(userData.Course_Level.equalsIgnoreCase("Js")){
                    builder.setIcon(R.drawable.ic_js);
                }
                builder.setMessage("Are you sure you want to take your quiz now?");
                builder.setPositiveButton("I need to Review" , new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Toast.makeText(QuestActivity.this, "Take your Review First!..", Toast.LENGTH_SHORT).show();
                        if(userData.Course_Level.equalsIgnoreCase("HTML")){
                            Intent in = new Intent(QuestActivity.this, HtmlFunActivity.class);
                            in.putExtra("data", userData);
                            in.putExtra("id", lsn_id);
                            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); in.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(in);
                        }else if(userData.Course_Level.equalsIgnoreCase("Css")){
                            Intent in = new Intent(QuestActivity.this, CssFunActivity.class);
                            in.putExtra("data", userData);
                            in.putExtra("id", lsn_id);
                            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); in.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(in);
                        }else if(userData.Course_Level.equalsIgnoreCase("Js")){
                            Intent in = new Intent(QuestActivity.this, JsFunActivity.class);
                            in.putExtra("data", userData);
                            in.putExtra("id", lsn_id);
                            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); in.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(in);
                        }
                    }
                });
                builder.setNegativeButton("Yes", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int level = 1;
                        HashMap postData = new HashMap();
                        postData.put("id", userData.ID+"");
                        postData.put("UserLevel", level+"");
                        PostResponseAsyncTask task = new PostResponseAsyncTask(QuestActivity.this, postData, new AsyncResponse() {
                            @Override
                            public void processFinish(String s) {
                                if(s.contains("success")){
                                    if(userData.Course_Level.equalsIgnoreCase("HTML")){
                                        GetQuizzes("HTML");
                                    }else if(userData.Course_Level.equalsIgnoreCase("Css")){
                                        GetQuizzes("Css");
                                    }else if(userData.Course_Level.equalsIgnoreCase("Js")){
                                        GetQuizzes("Js");
                                    }
                                }else if(s.contains("failed")){
                                    Toast.makeText(QuestActivity.this, R.string.scripts_error, Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(QuestActivity.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });task.execute("http://192.168.10.1/CodeWebScripts/update.php");
                    }
                });builder.show();
            }else{
                int level = 1;
                updateCategory();
                HashMap postData = new HashMap();
                postData.put("id", userData.ID+"");
                postData.put("UserLevel", level+"");
                PostResponseAsyncTask task = new PostResponseAsyncTask(QuestActivity.this, postData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        if(s.contains("success")){
                            HashMap postData = new HashMap();
                            postData.put("ID", userData.ID+"");
                            PostResponseAsyncTask task = new PostResponseAsyncTask(QuestActivity.this, postData, new AsyncResponse() {
                                @Override
                                public void processFinish(String s) {
                                    if(!s.isEmpty()){
                                        loadUser = new JsonConverter<SrlStudents>().toArrayList(s, SrlStudents.class);
                                        SrlStudents studentsData = loadUser.get(0);
                                        if(userData.Course_Level.equalsIgnoreCase("HTML")){
                                            Intent in = new Intent(QuestActivity.this, HtmlFunActivity.class);
                                            in.putExtra("data", studentsData);
                                            startActivity(in);
                                        }else if(userData.Course_Level.equalsIgnoreCase("Css")){
                                            Intent in = new Intent(QuestActivity.this, CssFunActivity.class);
                                            in.putExtra("data", studentsData);
                                            startActivity(in);
                                        }else if(userData.Course_Level.equalsIgnoreCase("Js")){
                                            Intent in = new Intent(QuestActivity.this, JsFunActivity.class);
                                            in.putExtra("data", studentsData);
                                            startActivity(in);
                                        }
                                    }else{
                                        Toast.makeText(QuestActivity.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });task.execute("http://192.168.10.1/CodeWebScripts/load.php");
                        }else if(s.contains("failed")){
                            Toast.makeText(QuestActivity.this, R.string.scripts_error, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(QuestActivity.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                        }

                    }
                });task.execute("http://192.168.10.1/CodeWebScripts/update.php");
            }

        }else{
            wrongAns(userData.User_level+1);
            Toast.makeText(QuestActivity.this, "New Lesson Unlocked...", Toast.LENGTH_SHORT).show();
        }
    }
    public void wrongAns(int level){
        HashMap postData = new HashMap();
        postData.put("id", userData.ID+"");
        postData.put("UserLevel", level+"");
        PostResponseAsyncTask task = new PostResponseAsyncTask(QuestActivity.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if(s.contains("success")){
                    HashMap postData = new HashMap();
                    postData.put("ID", userData.ID+"");
                    PostResponseAsyncTask task = new PostResponseAsyncTask(QuestActivity.this, postData, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                            loadUser = new JsonConverter<SrlStudents>().toArrayList(s, SrlStudents.class);
                            SrlStudents updateUser = loadUser.get(0);

                            if(userData.Course_Level.equalsIgnoreCase("HTML")){
                               Intent in = new Intent(QuestActivity.this, HtmlLessonPanel.class);
                                in.putExtra("data", updateUser);
                                in.putExtra("id", lsn_id);
                                startActivity(in);
                            }else if(userData.Course_Level.equalsIgnoreCase("Css")){
                                Intent in = new Intent(QuestActivity.this, CssLessonPanel.class);
                                in.putExtra("data", updateUser);
                                in.putExtra("id", lsn_id);
                                startActivity(in);
                            }else if(userData.Course_Level.equalsIgnoreCase("Js")){
                                Intent in = new Intent(QuestActivity.this, JsLessonPanel.class);
                                in.putExtra("data", updateUser);
                                in.putExtra("id", lsn_id);
                                startActivity(in);
                            }

                        }
                    });task.execute("http://192.168.10.1/CodeWebScripts/load.php");

                }else if(s.contains("failed")){
                    Toast.makeText(QuestActivity.this, R.string.scripts_error, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(QuestActivity.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                }

            }
        });task.execute("http://192.168.10.1/CodeWebScripts/update.php");
    }

    public void GetQuizzes(String Crs){
        HashMap postData = new HashMap();
        postData.put("Quiz", "Quiz");
        postData.put("Course", Crs);
         PostResponseAsyncTask task = new PostResponseAsyncTask(QuestActivity.this, postData, new AsyncResponse() {
                @Override
                public void processFinish(String s) {
                    if(s != null){
                        quiz = new JsonConverter<SrlQuiz>().toArrayList(s, SrlQuiz.class);
                        final SrlQuiz quizData = quiz.get(0);
                        final String query = s;
                        HashMap postData = new HashMap();
                        postData.put("ID", userData.ID+"");
                        PostResponseAsyncTask task = new PostResponseAsyncTask(QuestActivity.this, postData, new AsyncResponse() {
                            @Override
                            public void processFinish(String s) {
                                if(!s.isEmpty()){
                                    loadUser = new JsonConverter<SrlStudents>().toArrayList(s, SrlStudents.class);
                                    SrlStudents studentsData = loadUser.get(0);
                                    Intent in = new Intent(QuestActivity.this, QuizActivity.class);
                                    in.putExtra("data", studentsData);
                                    in.putExtra("quiz", quizData);
                                    in.putExtra("quest", query);
                                    in.putExtra("pos", 0);
                                    startActivity(in);
                                }else{
                                    Toast.makeText(QuestActivity.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });task.execute("http://192.168.10.1/CodeWebScripts/load.php");
                    }else{
                        Toast.makeText(QuestActivity.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                    }
                }
            });task.execute("http://192.168.10.1/CodeWebScripts/load.php");
    }

    public void AsyncLoad(){
        HashMap postData = new HashMap();
        postData.put("Course", data.lsn_course);
        postData.put("Category", data.lsn_ptCat);
        PostResponseAsyncTask task = new PostResponseAsyncTask(QuestActivity.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                load = new JsonConverter<SrlLesson>().toArrayList(s, SrlLesson.class);
            }
        });task.execute("http://192.168.10.1/CodeWebScripts/load.php");
    }

    public void updateCategory(){
        if(userData.Course_Level.equalsIgnoreCase("HTML")){
            if(userData.Category_Level.equalsIgnoreCase("html_Overview")){
                HashMap postData = new HashMap();
                postData.put("Category_Level", "html_Basic");
                postData.put("id", userData.ID+"");
                PostResponseAsyncTask task = new PostResponseAsyncTask(QuestActivity.this, postData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        if(s.contains("success")){
                            Toast.makeText(QuestActivity.this, "You Unlocked new Category", Toast.LENGTH_SHORT).show();
                        }else if(s.contains("failed")){
                            Toast.makeText(QuestActivity.this, R.string.scripts_error, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(QuestActivity.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                        }
                    }
                });task.execute("http://192.168.10.1/CodeWebScripts/update.php");
            }else if(userData.Category_Level.equalsIgnoreCase("html_Basic")){
                HashMap postData = new HashMap();
                postData.put("Category_Level", "html_HTML5");
                postData.put("id", userData.ID+"");
                PostResponseAsyncTask task = new PostResponseAsyncTask(QuestActivity.this, postData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        if(s.contains("success")){
                            Toast.makeText(QuestActivity.this, "You Unlocked new Category", Toast.LENGTH_SHORT).show();
                        }else if(s.contains("failed")){
                            Toast.makeText(QuestActivity.this, R.string.scripts_error, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(QuestActivity.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                        }
                    }
                });task.execute("http://192.168.10.1/CodeWebScripts/update.php");
            }
        }else if(userData.Course_Level.equalsIgnoreCase("Css")){
            if(userData.Category_Level.equalsIgnoreCase("css_Basic")){
                UpdateCat("css_Text", userData.ID);
            }else if(userData.Category_Level.equalsIgnoreCase("css_Text")){
                UpdateCat("css_Properties", userData.ID);
            }else if(userData.Category_Level.equalsIgnoreCase("css_Properties")){
                UpdateCat("css_Layouts", userData.ID);
            }else if(userData.Category_Level.equalsIgnoreCase("css_Layouts")){
                UpdateCat("css_Css3", userData.ID);
            }else if(userData.Category_Level.equalsIgnoreCase("css_Css3")){
                UpdateCat("css_Backgrounds", userData.ID);
            }else if(userData.Category_Level.equalsIgnoreCase("css_Backgrounds")){
                UpdateCat("css_Transitions", userData.ID);
            }
        }else if(userData.Course_Level.equalsIgnoreCase("Js")){
            if(userData.Category_Level.equalsIgnoreCase("js_Overview")){
                UpdateCat("js_Basic", userData.ID);
            }else if(userData.Category_Level.equalsIgnoreCase("js_Basic")){
                UpdateCat("js_Conloops", userData.ID);
            }else if(userData.Category_Level.equalsIgnoreCase("js_Conloops")){
                UpdateCat("js_Functions", userData.ID);
            }else if(userData.Category_Level.equalsIgnoreCase("js_Functions")){
                UpdateCat("js_Objects", userData.ID);
            }else if(userData.Category_Level.equalsIgnoreCase("js_Objects")){
                UpdateCat("js_Core", userData.ID);
            }else if(userData.Category_Level.equalsIgnoreCase("js_Core")){
                UpdateCat("js_Events", userData.ID);
            }
        }
    }

    public void UpdateCat(String Cat, int id){
        HashMap postData = new HashMap();
        postData.put("Category_Level", Cat);
        postData.put("id", id+"");
        PostResponseAsyncTask task = new PostResponseAsyncTask(QuestActivity.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if(s.contains("success")){
                    Toast.makeText(QuestActivity.this, "You Unlocked new Category", Toast.LENGTH_SHORT).show();
                }else if(s.contains("failed")){
                    Toast.makeText(QuestActivity.this, R.string.scripts_error, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(QuestActivity.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                }
            }
        });task.execute("http://192.168.10.1/CodeWebScripts/update.php");
    }

}
