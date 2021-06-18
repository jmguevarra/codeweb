package com.app.codeweb.codeweb.User_Activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;


import com.app.codeweb.codeweb.Others.Serialized.SrlStudents;
import com.app.codeweb.codeweb.R;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.HashMap;

public class SendFeedBacks extends AppCompatActivity {
    SrlStudents userData;
    EditText message;
    Animation animAlpha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedbacks);
        animAlpha  = AnimationUtils.loadAnimation(this, R.anim.alpha);
        if(getIntent().getSerializableExtra("data") != null){
            userData  = (SrlStudents) getIntent().getSerializableExtra("data");
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Send Feedbacks");
        }else{
            Toast.makeText(this,"Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onBackPressed() {
        if(message.getText().toString().isEmpty()){
            NavUtils.navigateUpFromSameTask(this);
        }else{
            leaveDialog();
        }

    }

    public void Send(View view){
        view.startAnimation(animAlpha);
        message = (EditText) findViewById(R.id.message);
        if(!message.getText().toString().isEmpty()){
            HashMap postData = new HashMap();
            postData.put("ID", userData.ID+"");
            postData.put("Image",userData.Image);
            postData.put("Fullname",userData.Fullname);
            postData.put("Stud_ID",userData.Student_ID);
            postData.put("Section",userData.Section);
            postData.put("Message", message.getText().toString());
            PostResponseAsyncTask task = new PostResponseAsyncTask(SendFeedBacks.this, postData, new AsyncResponse() {
                @Override
                public void processFinish(String s) {
                    if(s.contains("success")){
                        NavUtils.navigateUpFromSameTask(SendFeedBacks.this);
                    }else  if(s.contains("failed")){
                        Toast.makeText(SendFeedBacks.this, R.string.scripts_error, Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(SendFeedBacks.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                    }
                }
            });task.execute("http://192.168.10.1/CodeWebScripts/feedbacks.php");


        }else{
            message.requestFocus();
            Toast.makeText(this, "Please type your Message first before sending it.", Toast.LENGTH_SHORT).show();
        }
    }
    private void leaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Back to Home");
        builder.setMessage("Are sure you want to leave this panel without Sending your Concerns?");
        builder.setPositiveButton("No", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Yes", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                NavUtils.navigateUpFromSameTask(SendFeedBacks.this);
            }
        });
        builder.show();
    }
}
