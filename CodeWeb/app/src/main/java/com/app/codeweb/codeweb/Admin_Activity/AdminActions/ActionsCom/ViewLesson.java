package com.app.codeweb.codeweb.Admin_Activity.AdminActions.ActionsCom;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.codeweb.codeweb.Admin_Activity.AdminActions.Admin_Lessons;
import com.app.codeweb.codeweb.Others.Serialized.SrlLesson;
import com.app.codeweb.codeweb.R;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ViewLesson extends AppCompatActivity {
    public static Animation animAlpha;
    SrlLesson data;

    TextView  lsn_content, lsn_trivia,lsn_code;
    ImageView lsn_output;
    Button conBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_view_lesson);
        animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lsn_content = (TextView) findViewById(R.id.lsn_lesson);
        lsn_code = (TextView) findViewById(R.id.lsn_code);
        lsn_trivia = (TextView) findViewById(R.id.lsn_trivia);
        lsn_output = (ImageView) findViewById(R.id.lsn_result);

        if(getIntent().getSerializableExtra("data") != null) {
            data = (SrlLesson) getIntent().getSerializableExtra("data");

            getSupportActionBar().setTitle(data.lsn_title);
            lsn_content.setText(Html.fromHtml(data.lsn_content));
            lsn_code.setText(Html.fromHtml(data.lsn_code_content));
            lsn_trivia.setText(Html.fromHtml(data.lsn_trivia));
            Picasso.with(ViewLesson.this)
                    .load(data.lsn_output)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.stat_sys_download)
                    .into(lsn_output);


        }else{
            Toast.makeText(this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
        }
    }


    public void Edit(View view){
        view.startAnimation(animAlpha);
        Intent in = new Intent(ViewLesson.this, AddLesson.class);
        in.putExtra("data", data);
        startActivity(in);
    }
    public void Delete(View view){
        view.startAnimation(animAlpha);
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewLesson.this);
        builder.setTitle("Delete Option");
        builder.setIcon(android.R.drawable.ic_delete);
        builder.setMessage("Are sure you want to delete this lesson '"+data.lsn_title+"' from "+data.lsn_category+" of "+data.lsn_course+" course?");
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
                postData.put("Lesson_ID", ""+data.lsn_id);
                PostResponseAsyncTask task = new PostResponseAsyncTask(ViewLesson.this, postData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        if(s.contains("success")){
                            Toast.makeText(ViewLesson.this, "Lesson Deleted", Toast.LENGTH_SHORT).show();
                            Intent in = new Intent(ViewLesson.this, Admin_Lessons.class);
                            startActivity(in);
                        }else if(s.contains("failed")){
                            Toast.makeText(ViewLesson.this, R.string.scripts_error, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(ViewLesson.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                        }
                    }
                });task.execute("http://192.168.10.1/CodeWebScripts/remove.php");
                dialog.cancel();
            }
        });
        builder.show();
    }


}
