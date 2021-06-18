package com.app.codeweb.codeweb.Admin_Activity.AdminActions.ActionsCom;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.app.codeweb.codeweb.Admin_Activity.AdminActions.Admin_Quizzes;
import com.app.codeweb.codeweb.Others.CircleTransform;
import com.app.codeweb.codeweb.Others.Serialized.SrlQuiz;
import com.app.codeweb.codeweb.R;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ViewQuizDetailed extends AppCompatActivity {
    ImageView quiz_icon;
    SrlQuiz quizData;
    Toolbar toolbar;
    ImageButton edit, delete;
    TextView quiz_title, quiz_course,quiz_type,quiz_author,quiz_published,quiz_question,quiz_opta, quiz_optb,quiz_optc,quiz_optd,quiz_ans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Quizzes");

        if(getIntent().getSerializableExtra("data") != null){
            quizData = (SrlQuiz) getIntent().getSerializableExtra("data");

            quiz_icon = (ImageView) findViewById(R.id.quiz_icon);
            quiz_title = (TextView) findViewById(R.id.quiz_title);
            quiz_course = (TextView) findViewById(R.id.quiz_course);
            quiz_type = (TextView) findViewById(R.id.quiz_type);
            quiz_author = (TextView) findViewById(R.id.quiz_author);
            quiz_published = (TextView) findViewById(R.id.quiz_published);
            quiz_question = (TextView) findViewById(R.id.quiz_question);
            quiz_opta = (TextView) findViewById(R.id.quiz_opta);
            quiz_optb = (TextView) findViewById(R.id.quiz_optb);
            quiz_optc = (TextView) findViewById(R.id.quiz_optc);
            quiz_optd = (TextView) findViewById(R.id.quiz_optd);
            quiz_ans = (TextView) findViewById(R.id.quiz_ans);
            edit = (ImageButton) toolbar.findViewById(R.id.edit);
            delete = (ImageButton) toolbar.findViewById(R.id.delete);
            Picasso.with(ViewQuizDetailed.this)
                    .load(quizData.quiz_Icon)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.stat_sys_download)
                    .transform(new CircleTransform())
                    .into(quiz_icon);
            quiz_title.setText(quizData.quiz_Title);
            quiz_course.setText(quizData.quiz_Course);
            quiz_type.setText(Html.fromHtml("<b>Quiz Type</b><br>"+quizData.quiz_Type));
            quiz_author.setText(Html.fromHtml("<b>Author</b><br>"+quizData.quiz_Author));
            quiz_published.setText(Html.fromHtml("<b>Date Published</b><br>"+quizData.quiz_Date));
            quiz_question.setText(quizData.quiz_Question+"?");
            quiz_opta.setText(Html.fromHtml("<b>1st Option</b><br>")+quizData.quiz_OptA);
            quiz_optb.setText(Html.fromHtml("<b>2nd Option</b><br>")+quizData.quiz_OptB);
            quiz_optc.setText(Html.fromHtml("<b>3rd Option</b><br>")+quizData.quiz_OptC);
            quiz_optd.setText(Html.fromHtml("<b>4th Option</b><br>")+quizData.quiz_OptD);
            quiz_ans.setText(quizData.quiz_Ans);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(ViewQuizDetailed.this, AddDel_Quiz.class);
                    in.putExtra("data", quizData);
                    startActivity(in);
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Delete();
                }
            });

        }else{
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void Delete(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Option");
        builder.setIcon(android.R.drawable.ic_delete);
        builder.setMessage("Are sure you want to delete "+quizData.quiz_Title+" in question list of Quizzes?");
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
                postData.put("quiz_id", quizData.quiz_Id+"");
                PostResponseAsyncTask task = new PostResponseAsyncTask(ViewQuizDetailed.this, postData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        if(s.contains("success")){
                            Intent in = new Intent(ViewQuizDetailed.this, Admin_Quizzes.class);
                            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(in);
                            Toast.makeText(ViewQuizDetailed.this, "Delete Successful", Toast.LENGTH_SHORT).show();
                        }else if(s.contains("failed")){
                            Toast.makeText(ViewQuizDetailed.this, R.string.scripts_error, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(ViewQuizDetailed.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                        }
                    }
                });task.execute("http://192.168.10.1/CodeWebScripts/remove.php");
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
