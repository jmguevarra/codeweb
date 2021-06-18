package com.app.codeweb.codeweb.Admin_Activity.AdminActions.ActionsCom;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.codeweb.codeweb.Admin_Activity.AdminActions.Admin_Excercises;
import com.app.codeweb.codeweb.Admin_Activity.AdminActions.Admin_Lessons;
import com.app.codeweb.codeweb.Admin_Activity.AdminActions.Admin_Quizzes;
import com.app.codeweb.codeweb.Others.Serialized.SrlLesson;
import com.app.codeweb.codeweb.Others.Serialized.SrlQuiz;
import com.app.codeweb.codeweb.Others.Serialized.Srl_CrsCat;
import com.app.codeweb.codeweb.R;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.ArrayList;
import java.util.HashMap;

public class AddDel_Quiz extends AppCompatActivity {
    EditText quiz_title, quiz_question,quiz_opta, quiz_optb,quiz_optc,quiz_optd;
    Spinner quiz_course, quiz_ans;
    ArrayList<String> crs, opt;
    ArrayList<Srl_CrsCat> crsLoad;
    Button addUpd ;
    String sp_ans;
    SrlQuiz data;
    SrlLesson excer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_add_del);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Quizzes");

        quiz_title = (EditText) findViewById(R.id.et_quiz_title);
        quiz_course = (Spinner) findViewById(R.id.sp_course);
        quiz_question = (EditText) findViewById(R.id.et_quiz_question);
        quiz_opta = (EditText) findViewById(R.id.et_quiz_opta);
        quiz_optb = (EditText) findViewById(R.id.et_quiz_optb);
        quiz_optc = (EditText) findViewById(R.id.et_quiz_optc);
        quiz_optd = (EditText) findViewById(R.id.et_quiz_optd);
        quiz_ans = (Spinner) findViewById(R.id.sp_ans);
        addUpd = (Button) findViewById(R.id.btn_add_del);

        if(getIntent().getSerializableExtra("data") != null){
            data =(SrlQuiz) getIntent().getSerializableExtra("data");
            crs = new ArrayList<>();
            quiz_course.setEnabled(false);
            crs.add(data.quiz_Course);
            ArrayAdapter<String> crsadp = new ArrayAdapter<String>(AddDel_Quiz.this, R.layout.spinner_costum, crs);
            quiz_course.setAdapter(crsadp);

            quiz_title.setText(data.quiz_Title);
            quiz_opta.setText(data.quiz_OptA);
            quiz_optb.setText(data.quiz_OptB);
            quiz_optc.setText(data.quiz_OptC);
            quiz_optd.setText(data.quiz_OptD);
            quiz_question.setText(data.quiz_Question);
            CorrectOption();
            quiz_ans.setEnabled(false);
            opt = new ArrayList<>();
            opt.add(data.quiz_Ans);
            ArrayAdapter<String> optadp = new ArrayAdapter<String>(AddDel_Quiz.this, R.layout.spinner_costum, opt);
            quiz_ans.setAdapter(optadp);
            addUpd.setText("Save Changes");

            addUpd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UpdateQuiz();
                }
            });

        }else if(getIntent().getSerializableExtra("excer") != null){
            excer =(SrlLesson) getIntent().getSerializableExtra("excer");
            crs = new ArrayList<>();
            quiz_course.setEnabled(false);
            crs.add(excer.lsn_course);
            ArrayAdapter<String> crsadp = new ArrayAdapter<String>(AddDel_Quiz.this, R.layout.spinner_costum, crs);
            quiz_course.setAdapter(crsadp);

            quiz_title.setText(excer.lsn_title);
            quiz_opta.setText(excer.lsn_optA);
            quiz_optb.setText(excer.lsn_optB);
            quiz_optc.setText(excer.lsn_optC);
            quiz_optd.setText(excer.lsn_optD);
            quiz_question.setText(excer.lsn_rightOpt);
            ExcerCorrectOption();
            quiz_ans.setEnabled(false);
            opt = new ArrayList<>();
            opt.add(excer.lsn_rightOpt);
            ArrayAdapter<String> optadp = new ArrayAdapter<String>(AddDel_Quiz.this, R.layout.spinner_costum, opt);
            quiz_ans.setAdapter(optadp);
            addUpd.setText("Save Changes");

            addUpd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UpdateQuiz();
                }
            });

        }else{
            SpLoad();
            addUpd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddQuiz();
                }
            });
        }

    }

    @Override
    public void onBackPressed(){
        if(getIntent().getSerializableExtra("data") != null){
            NavUtils.navigateUpFromSameTask(this);
        }else if(getIntent().getSerializableExtra("excer") != null){
            Intent in = new Intent(AddDel_Quiz.this, Admin_Excercises.class);
            startActivity(in);
        }
    }
//For Quiz
    public void CorrectOption(){
        if(data.quiz_Ans.equalsIgnoreCase(data.quiz_OptA)){
            quiz_opta.setAlpha(0.5f);
            if(quiz_opta.getAlpha() == 0.5f){
                quiz_opta.setFocusable(false);
                quiz_opta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view){
                        if(quiz_opta.getAlpha() == 0.5f){
                            toEditCorrectOption(quiz_opta);
                        }
                    }
                });

            }

        }else if(data.quiz_Ans.equalsIgnoreCase(data.quiz_OptB)){
            quiz_optb.setAlpha(0.5f);
            if(quiz_optb.getAlpha() == 0.5f){
                quiz_optb.setFocusable(false);
                quiz_optb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view){
                        if(quiz_optb.getAlpha() == 0.5f){
                            toEditCorrectOption(quiz_optb);
                        }
                    }
                });

            }
        }else if(data.quiz_Ans.equalsIgnoreCase(data.quiz_OptC)){
            quiz_optc.setAlpha(0.5f);
            if(quiz_optc.getAlpha() == 0.5f){
                quiz_optc.setFocusable(false);
                quiz_optc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view){
                        if(quiz_optc.getAlpha() == 0.5f){
                            toEditCorrectOption(quiz_optc);
                        }
                    }
                });

            }
        }else if(data.quiz_Ans.equalsIgnoreCase(data.quiz_OptD)){
            quiz_optd.setAlpha(0.5f);
            if(quiz_optd.getAlpha() == 0.5f){
                quiz_optd.setFocusable(false);
                quiz_optd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view){
                        if(quiz_optd.getAlpha() == 0.5f){
                            toEditCorrectOption(quiz_optd);
                        }
                    }
                });

            }
        }
    }
    public void toEditCorrectOption(final EditText editOption){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Correct Option");
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setMessage("Are you sure you want to edit this Correct Option?");
        builder.setPositiveButton("No", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Yes", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(editOption.getAlpha() == 0.5f && quiz_opta.getAlpha() == 1f && quiz_optb.getAlpha() == 1f && quiz_optc.getAlpha() == 1f){
                    editOption.setAlpha(1f);
                    editOption.setFocusable(true);
                    editOption.setFocusableInTouchMode(true);
                    editOption.requestFocus();
                    quiz_opta.setFocusable(false);
                    quiz_optb.setFocusable(false);
                    quiz_optc.setFocusable(false);
                    quiz_opta.setAlpha(0.5f);
                    quiz_optb.setAlpha(0.5f);
                    quiz_optc.setAlpha(0.5f);
                    EditListener(editOption, quiz_opta,quiz_optb,quiz_optc);
                }else if(editOption.getAlpha() == 0.5f && quiz_optb.getAlpha() == 1f && quiz_optc.getAlpha() == 1f && quiz_optd.getAlpha() == 1f){
                    editOption.setAlpha(1f);
                    editOption.setFocusable(true);
                    editOption.setFocusableInTouchMode(true);
                    editOption.requestFocus();
                    quiz_optb.setFocusable(false);
                    quiz_optc.setFocusable(false);
                    quiz_optd.setFocusable(false);
                    quiz_optb.setAlpha(0.5f);
                    quiz_optc.setAlpha(0.5f);
                    quiz_optd.setAlpha(0.5f);
                    EditListener(editOption, quiz_optb,quiz_optc,quiz_optd);
                }else if(editOption.getAlpha() == 0.5f && quiz_optc.getAlpha() == 1f && quiz_optd.getAlpha() == 1f && quiz_opta.getAlpha() == 1f){
                    editOption.setAlpha(1f);
                    editOption.setFocusable(true);
                    editOption.setFocusableInTouchMode(true);
                    editOption.requestFocus();
                    quiz_optc.setFocusable(false);
                    quiz_optd.setFocusable(false);
                    quiz_opta.setFocusable(false);
                    quiz_optc.setAlpha(0.5f);
                    quiz_optd.setAlpha(0.5f);
                    quiz_opta.setAlpha(0.5f);
                    EditListener(editOption, quiz_optc,quiz_optd,quiz_opta);
                }else if(editOption.getAlpha() == 0.5f && quiz_optd.getAlpha() == 1f && quiz_opta.getAlpha() == 1f && quiz_optb.getAlpha() == 1f){
                    editOption.setAlpha(1f);
                    editOption.setFocusable(true);
                    editOption.setFocusableInTouchMode(true);
                    editOption.requestFocus();
                    quiz_optd.setFocusable(false);
                    quiz_opta.setFocusable(false);
                    quiz_optb.setFocusable(false);
                    quiz_optd.setAlpha(0.5f);
                    quiz_opta.setAlpha(0.5f);
                    quiz_optb.setAlpha(0.5f);
                    EditListener(editOption, quiz_opta,quiz_optb,quiz_optd);

                }
            }
        });
        builder.show();
    }
    public void EditListener(final EditText optCorrect, final EditText opt1, final EditText opt2, final EditText opt3){

        opt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (opt1.getAlpha() == 0.5f) {
                    toEditWrongOption(optCorrect);
                }
            }
        });
        opt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (opt2.getAlpha() == 0.5f) {
                    toEditWrongOption(optCorrect);

                }
            }
        });
        opt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (opt3.getAlpha() == 0.5f) {
                    toEditWrongOption(optCorrect);
                }
            }
        });
    }
    public void toEditWrongOption(final EditText Editcorrect){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Finish Editing");
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setMessage("Are sure to the new Correct Option?");
        builder.setPositiveButton("No", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Yes", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                quiz_ans.setEnabled(false);
                opt = new ArrayList<>();
                opt.add(Editcorrect.getText().toString());
                ArrayAdapter<String> optadp = new ArrayAdapter<String>(AddDel_Quiz.this, R.layout.spinner_costum, opt);
                quiz_ans.setAdapter(optadp);
                Toast.makeText(AddDel_Quiz.this, "Correct Answer is Updated", Toast.LENGTH_SHORT).show();
                if(Editcorrect.getAlpha() == 1f && quiz_opta.getAlpha() == 0.5f && quiz_optb.getAlpha() == 0.5f && quiz_optc.getAlpha() == 0.5f){
                    Editcorrect.setAlpha(0.5f);
                    Editcorrect.setFocusable(false);
                    quiz_opta.setFocusable(true);
                    quiz_optb.setFocusable(true);
                    quiz_optc.setFocusable(true);
                    quiz_opta.setFocusableInTouchMode(true);
                    quiz_optb.setFocusableInTouchMode(true);
                    quiz_optc.setFocusableInTouchMode(true);
                    quiz_opta.setAlpha(1f);
                    quiz_optb.setAlpha(1f);
                    quiz_optc.setAlpha(1f);
                }else if(Editcorrect.getAlpha() == 1f && quiz_optb.getAlpha() == 0.5f && quiz_optc.getAlpha() == 0.5f && quiz_optd.getAlpha() == 0.5f){
                    Editcorrect.setAlpha(0.5f);
                    Editcorrect.setFocusable(false);
                    quiz_optb.setFocusable(true);
                    quiz_optc.setFocusable(true);
                    quiz_optd.setFocusable(true);
                    quiz_optb.setFocusableInTouchMode(true);
                    quiz_optc.setFocusableInTouchMode(true);
                    quiz_optd.setFocusableInTouchMode(true);
                    quiz_optb.setAlpha(1f);
                    quiz_optc.setAlpha(1f);
                    quiz_optd.setAlpha(1f);
                }else if(Editcorrect.getAlpha() == 1f && quiz_optc.getAlpha() == 0.5f && quiz_optd.getAlpha() == 0.5f && quiz_opta.getAlpha() == 0.5f){
                    Editcorrect.setAlpha(0.5f);
                    Editcorrect.setFocusable(false);
                    quiz_optc.setFocusable(true);
                    quiz_optd.setFocusable(true);
                    quiz_opta.setFocusable(true);
                    quiz_optc.setFocusableInTouchMode(true);
                    quiz_optd.setFocusableInTouchMode(true);
                    quiz_opta.setFocusableInTouchMode(true);
                    quiz_optc.setAlpha(1f);
                    quiz_optd.setAlpha(1f);
                    quiz_opta.setAlpha(1f);
                }else if(Editcorrect.getAlpha() == 1f && quiz_optd.getAlpha() == 0.5f && quiz_opta.getAlpha() == 0.5f && quiz_optb.getAlpha() == 0.5f){
                    Editcorrect.setAlpha(0.5f);
                    Editcorrect.setFocusable(false);
                    quiz_optd.setFocusable(true);
                    quiz_opta.setFocusable(true);
                    quiz_optb.setFocusable(true);
                    quiz_optd.setFocusableInTouchMode(true);
                    quiz_opta.setFocusableInTouchMode(true);
                    quiz_optb.setFocusableInTouchMode(true);
                    quiz_optd.setAlpha(1f);
                    quiz_opta.setAlpha(1f);
                    quiz_optb.setAlpha(1f);
                }
            }
        });
        builder.show();
    }

    public void AddQuiz(){
        if(!quiz_title.getText().toString().isEmpty() &&  !quiz_opta.getText().toString().isEmpty()&& !quiz_optb.getText().toString().isEmpty()&& !quiz_optc.getText().toString().isEmpty()&& !quiz_optd.getText().toString().isEmpty()){
            HashMap postData = new HashMap();
            postData.put("quiz_Course", quiz_course.getSelectedItem().toString());
            postData.put("quiz_Title", quiz_title.getText().toString());
            postData.put("quiz_Question", quiz_question.getText().toString());
            postData.put("quiz_OptA", quiz_opta.getText().toString());
            postData.put("quiz_OptB", quiz_optb.getText().toString());
            postData.put("quiz_OptC", quiz_optc.getText().toString());
            postData.put("quiz_OptD", quiz_optd.getText().toString());
            postData.put("quiz_Ans", quiz_ans.getSelectedItem().toString());
            PostResponseAsyncTask task = new PostResponseAsyncTask(AddDel_Quiz.this, postData, new AsyncResponse() {
                @Override
                public void processFinish(String s) {
                    if(s.contains("success")){
                        Toast.makeText(AddDel_Quiz.this, "One Question Has Been Added", Toast.LENGTH_SHORT).show();
                        Intent in = new Intent(AddDel_Quiz.this, Admin_Quizzes.class);
                        startActivity(in);
                    }else if (s.contains("failed")) {
                        Toast.makeText(AddDel_Quiz.this, R.string.scripts_error, Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(AddDel_Quiz.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                    }
                }
            });task.execute("http://192.168.10.1/CodeWebScripts/insert.php");
        }else{
            Toast.makeText(this, "Fill the Empty Boxes", Toast.LENGTH_SHORT).show();
        }


    }
    public void UpdateQuiz(){
        HashMap postData = new HashMap();
        if(getIntent().getSerializableExtra("data") != null){
            postData.put("quiz_Id", data.quiz_Id+"");
        }else if(getIntent().getSerializableExtra("excer") != null){
            postData.put("quiz_Id", excer.lsn_id+"");
        }
        postData.put("quiz_Title", quiz_title.getText().toString());
        postData.put("quiz_Question", quiz_question.getText().toString());
        postData.put("quiz_OptA", quiz_opta.getText().toString());
        postData.put("quiz_OptB", quiz_optb.getText().toString());
        postData.put("quiz_OptC", quiz_optc.getText().toString());
        postData.put("quiz_OptD", quiz_optd.getText().toString());
        postData.put("quiz_Ans", quiz_ans.getSelectedItem().toString());
        PostResponseAsyncTask task = new PostResponseAsyncTask(AddDel_Quiz.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if(s.contains("success")){
                    Toast.makeText(AddDel_Quiz.this, "One Question Has Been Updated", Toast.LENGTH_SHORT).show();
                    if(getIntent().getSerializableExtra("data") != null){
                        Intent in = new Intent(AddDel_Quiz.this, Admin_Quizzes.class);
                        startActivity(in);
                    }else if(getIntent().getSerializableExtra("excer") != null){
                        Intent in = new Intent(AddDel_Quiz.this, Admin_Excercises.class);
                        startActivity(in);
                    }
                }else if (s.contains("failed")) {
                    Toast.makeText(AddDel_Quiz.this, R.string.scripts_error, Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(AddDel_Quiz.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                }
            }
        });task.execute("http://192.168.10.1/CodeWebScripts/update.php");
    }
    public void SpLoad(){
        HashMap postData = new HashMap();
        postData.put("sp_course", "sp_course");
        PostResponseAsyncTask task = new PostResponseAsyncTask(AddDel_Quiz.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                crsLoad = new JsonConverter<Srl_CrsCat>().toArrayList(s, Srl_CrsCat.class);
                crs = new ArrayList<>();
                for(Srl_CrsCat d:crsLoad){
                    crs.add(d.crs_title);
                }
                ArrayAdapter<String> adp = new ArrayAdapter<String>(AddDel_Quiz.this, R.layout.spinner_costum, crs);
                quiz_course.setAdapter(adp);
            }
        });task.execute("http://192.168.10.1/CodeWebScripts/load.php");

        opt = new ArrayList<>();
        opt.add("1st");
        opt.add("2nd");
        opt.add("3rd");
        opt.add("4th");
        ArrayAdapter<String> crsadp = new ArrayAdapter<String>(AddDel_Quiz.this, R.layout.spinner_costum, opt);
        quiz_ans.setAdapter(crsadp);
    }
// For Excercise
    public void ExcerCorrectOption(){
        if(excer.lsn_rightOpt.equalsIgnoreCase(excer.lsn_optA)){
            quiz_opta.setAlpha(0.5f);
            if(quiz_opta.getAlpha() == 0.5f){
                quiz_opta.setFocusable(false);
                quiz_opta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view){
                        if(quiz_opta.getAlpha() == 0.5f){
                            toEditExcerExcerCorrectOption(quiz_opta);
                        }
                    }
                });

            }

        }else if(excer.lsn_rightOpt.equalsIgnoreCase(excer.lsn_optB)){
            quiz_optb.setAlpha(0.5f);
            if(quiz_optb.getAlpha() == 0.5f){
                quiz_optb.setFocusable(false);
                quiz_optb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view){
                        if(quiz_optb.getAlpha() == 0.5f){
                            toEditExcerExcerCorrectOption(quiz_optb);
                        }
                    }
                });

            }
        }else if(excer.lsn_rightOpt.equalsIgnoreCase(excer.lsn_optC)){
            quiz_optc.setAlpha(0.5f);
            if(quiz_optc.getAlpha() == 0.5f){
                quiz_optc.setFocusable(false);
                quiz_optc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view){
                        if(quiz_optc.getAlpha() == 0.5f){
                            toEditExcerExcerCorrectOption(quiz_optc);
                        }
                    }
                });

            }
        }else if(excer.lsn_rightOpt.equalsIgnoreCase(excer.lsn_optD)){
            quiz_optd.setAlpha(0.5f);
            if(quiz_optd.getAlpha() == 0.5f){
                quiz_optd.setFocusable(false);
                quiz_optd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view){
                        if(quiz_optd.getAlpha() == 0.5f){
                            toEditExcerExcerCorrectOption(quiz_optd);
                        }
                    }
                });

            }
        }
    }
    public void toEditExcerExcerCorrectOption(final EditText editOption){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Correct Option");
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setMessage("Are you sure you want to edit this Correct Option?");
        builder.setPositiveButton("No", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Yes", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(editOption.getAlpha() == 0.5f && quiz_opta.getAlpha() == 1f && quiz_optb.getAlpha() == 1f && quiz_optc.getAlpha() == 1f){
                    editOption.setAlpha(1f);
                    editOption.setFocusable(true);
                    editOption.setFocusableInTouchMode(true);
                    editOption.requestFocus();
                    quiz_opta.setFocusable(false);
                    quiz_optb.setFocusable(false);
                    quiz_optc.setFocusable(false);
                    quiz_opta.setAlpha(0.5f);
                    quiz_optb.setAlpha(0.5f);
                    quiz_optc.setAlpha(0.5f);
                    ExcerEditListener(editOption, quiz_opta,quiz_optb,quiz_optc);
                }else if(editOption.getAlpha() == 0.5f && quiz_optb.getAlpha() == 1f && quiz_optc.getAlpha() == 1f && quiz_optd.getAlpha() == 1f){
                    editOption.setAlpha(1f);
                    editOption.setFocusable(true);
                    editOption.setFocusableInTouchMode(true);
                    editOption.requestFocus();
                    quiz_optb.setFocusable(false);
                    quiz_optc.setFocusable(false);
                    quiz_optd.setFocusable(false);
                    quiz_optb.setAlpha(0.5f);
                    quiz_optc.setAlpha(0.5f);
                    quiz_optd.setAlpha(0.5f);
                    ExcerEditListener(editOption, quiz_optb,quiz_optc,quiz_optd);
                }else if(editOption.getAlpha() == 0.5f && quiz_optc.getAlpha() == 1f && quiz_optd.getAlpha() == 1f && quiz_opta.getAlpha() == 1f){
                    editOption.setAlpha(1f);
                    editOption.setFocusable(true);
                    editOption.setFocusableInTouchMode(true);
                    editOption.requestFocus();
                    quiz_optc.setFocusable(false);
                    quiz_optd.setFocusable(false);
                    quiz_opta.setFocusable(false);
                    quiz_optc.setAlpha(0.5f);
                    quiz_optd.setAlpha(0.5f);
                    quiz_opta.setAlpha(0.5f);
                    ExcerEditListener(editOption, quiz_optc,quiz_optd,quiz_opta);
                }else if(editOption.getAlpha() == 0.5f && quiz_optd.getAlpha() == 1f && quiz_opta.getAlpha() == 1f && quiz_optb.getAlpha() == 1f){
                    editOption.setAlpha(1f);
                    editOption.setFocusable(true);
                    editOption.setFocusableInTouchMode(true);
                    editOption.requestFocus();
                    quiz_optd.setFocusable(false);
                    quiz_opta.setFocusable(false);
                    quiz_optb.setFocusable(false);
                    quiz_optd.setAlpha(0.5f);
                    quiz_opta.setAlpha(0.5f);
                    quiz_optb.setAlpha(0.5f);
                    ExcerEditListener(editOption, quiz_opta,quiz_optb,quiz_optd);

                }
            }
        });
        builder.show();
    }
    public void ExcerEditListener(final EditText optCorrect, final EditText opt1, final EditText opt2, final EditText opt3){

        opt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (opt1.getAlpha() == 0.5f) {
                    toEditExcerWrongOption(optCorrect);
                }
            }
        });
        opt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (opt2.getAlpha() == 0.5f) {
                    toEditExcerWrongOption(optCorrect);

                }
            }
        });
        opt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (opt3.getAlpha() == 0.5f) {
                    toEditExcerWrongOption(optCorrect);
                }
            }
        });
    }
    public void toEditExcerWrongOption(final EditText Editcorrect){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Finish Editing");
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setMessage("Are sure to the new Correct Option?");
        builder.setPositiveButton("No", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Yes", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                quiz_ans.setEnabled(false);
                opt = new ArrayList<>();
                opt.add(Editcorrect.getText().toString());
                ArrayAdapter<String> optadp = new ArrayAdapter<String>(AddDel_Quiz.this, R.layout.spinner_costum, opt);
                quiz_ans.setAdapter(optadp);
                Toast.makeText(AddDel_Quiz.this, "Correct Answer is Updated", Toast.LENGTH_SHORT).show();
                if(Editcorrect.getAlpha() == 1f && quiz_opta.getAlpha() == 0.5f && quiz_optb.getAlpha() == 0.5f && quiz_optc.getAlpha() == 0.5f){
                    Editcorrect.setAlpha(0.5f);
                    Editcorrect.setFocusable(false);
                    quiz_opta.setFocusable(true);
                    quiz_optb.setFocusable(true);
                    quiz_optc.setFocusable(true);
                    quiz_opta.setFocusableInTouchMode(true);
                    quiz_optb.setFocusableInTouchMode(true);
                    quiz_optc.setFocusableInTouchMode(true);
                    quiz_opta.setAlpha(1f);
                    quiz_optb.setAlpha(1f);
                    quiz_optc.setAlpha(1f);
                }else if(Editcorrect.getAlpha() == 1f && quiz_optb.getAlpha() == 0.5f && quiz_optc.getAlpha() == 0.5f && quiz_optd.getAlpha() == 0.5f){
                    Editcorrect.setAlpha(0.5f);
                    Editcorrect.setFocusable(false);
                    quiz_optb.setFocusable(true);
                    quiz_optc.setFocusable(true);
                    quiz_optd.setFocusable(true);
                    quiz_optb.setFocusableInTouchMode(true);
                    quiz_optc.setFocusableInTouchMode(true);
                    quiz_optd.setFocusableInTouchMode(true);
                    quiz_optb.setAlpha(1f);
                    quiz_optc.setAlpha(1f);
                    quiz_optd.setAlpha(1f);
                }else if(Editcorrect.getAlpha() == 1f && quiz_optc.getAlpha() == 0.5f && quiz_optd.getAlpha() == 0.5f && quiz_opta.getAlpha() == 0.5f){
                    Editcorrect.setAlpha(0.5f);
                    Editcorrect.setFocusable(false);
                    quiz_optc.setFocusable(true);
                    quiz_optd.setFocusable(true);
                    quiz_opta.setFocusable(true);
                    quiz_optc.setFocusableInTouchMode(true);
                    quiz_optd.setFocusableInTouchMode(true);
                    quiz_opta.setFocusableInTouchMode(true);
                    quiz_optc.setAlpha(1f);
                    quiz_optd.setAlpha(1f);
                    quiz_opta.setAlpha(1f);
                }else if(Editcorrect.getAlpha() == 1f && quiz_optd.getAlpha() == 0.5f && quiz_opta.getAlpha() == 0.5f && quiz_optb.getAlpha() == 0.5f){
                    Editcorrect.setAlpha(0.5f);
                    Editcorrect.setFocusable(false);
                    quiz_optd.setFocusable(true);
                    quiz_opta.setFocusable(true);
                    quiz_optb.setFocusable(true);
                    quiz_optd.setFocusableInTouchMode(true);
                    quiz_opta.setFocusableInTouchMode(true);
                    quiz_optb.setFocusableInTouchMode(true);
                    quiz_optd.setAlpha(1f);
                    quiz_opta.setAlpha(1f);
                    quiz_optb.setAlpha(1f);
                }
            }
        });
        builder.show();
    }
}
