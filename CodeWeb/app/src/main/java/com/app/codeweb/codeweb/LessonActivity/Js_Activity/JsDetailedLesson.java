package com.app.codeweb.codeweb.LessonActivity.Js_Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.codeweb.codeweb.AboutUs;
import com.app.codeweb.codeweb.LessonActivity.CodeEditor;
import com.app.codeweb.codeweb.LessonActivity.QuestActivity;
import com.app.codeweb.codeweb.LessonActivity.VideoPanel;
import com.app.codeweb.codeweb.Others.Serialized.SrlLesson;
import com.app.codeweb.codeweb.Others.Serialized.SrlStudents;
import com.app.codeweb.codeweb.R;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class JsDetailedLesson extends AppCompatActivity {
    TextView lsn_lesson, lsn_trivia,lsn_code;
    ImageView lsn_result;
    SrlLesson data;
    SrlStudents userData;
    Button conBtn;
    ArrayList<SrlLesson> load;
    int lsn_id,position;
    /*TextView titletext;*/
    String finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lesson_detailed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*titletext = (TextView) findViewById(R.id.titlename);*/
        lsn_lesson = (TextView) findViewById(R.id.lsn_lesson);
        lsn_code = (TextView) findViewById(R.id.lsn_code);
        lsn_trivia = (TextView) findViewById(R.id.lsn_trivia);
        lsn_result = (ImageView) findViewById(R.id.lsn_result);
        conBtn = (Button) findViewById(R.id.continueBtn);

        if(getIntent().getSerializableExtra("data") != null && getIntent().getSerializableExtra("student") != null){
            data = (SrlLesson) getIntent().getSerializableExtra("data");
            userData = (SrlStudents) getIntent().getSerializableExtra("student");
            getSupportActionBar().setTitle(data.lsn_title);
            AsyncLoad();

            final Bundle bundle = getIntent().getExtras();
            lsn_id = bundle.getInt("id");
            position = bundle.getInt("position");
            finish = bundle.getString("finish");

            lsn_lesson.setText(Html.fromHtml(data.lsn_content));
            lsn_code.setText(Html.fromHtml(data.lsn_code_content));
            lsn_trivia.setText(Html.fromHtml(data.lsn_trivia));
            Picasso.with(JsDetailedLesson.this)
                    .load(data.lsn_output)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.stat_sys_download)
                    .into(lsn_result);
        }else{
            Toast.makeText(this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
        }

        conBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(finish.equalsIgnoreCase("finish")){
                    if(data.lsn_no == load.size()){
                        if(lsn_id == 7){
                            Toast.makeText(JsDetailedLesson.this, "There's no more", Toast.LENGTH_SHORT).show();
                            Intent in = new Intent(JsDetailedLesson.this, JsFunActivity.class);
                            in.putExtra("data", userData);
                            startActivity(in);
                        }else{
                            Intent in = new Intent(JsDetailedLesson.this, JsLessonPanel.class);
                            in.putExtra("data", userData);
                            in.putExtra("id", lsn_id + 1);
                            startActivity(in);
                        }
                    }else{
                        SrlLesson item = load.get(data.lsn_no);
                        Intent in = new Intent(JsDetailedLesson.this, JsDetailedLesson2.class);
                        in.putExtra("data", item);
                        in.putExtra("id", lsn_id);
                        in.putExtra("position", item.lsn_no+1);
                        in.putExtra("finish", "finish");
                        in.putExtra("student", userData);
                        JsDetailedLesson.super.onBackPressed();
                        startActivity(in);
                    }
                }else if(finish.equalsIgnoreCase("not")){
                    if(data.lsn_no == userData.User_level || (data.lsn_no == load.size() && data.lsn_no == userData.User_level)) {
                        Intent in = new Intent(JsDetailedLesson.this, QuestActivity.class);
                        in.putExtra("student", userData);
                        in.putExtra("id", lsn_id);
                        in.putExtra("data", data);
                        JsDetailedLesson.super.onBackPressed();
                        startActivity(in);
                    }else{
                        SrlLesson item = load.get(data.lsn_no);
                        Intent in = new Intent(JsDetailedLesson.this, JsDetailedLesson2.class);
                        in.putExtra("data", item);
                        in.putExtra("id", lsn_id);
                        in.putExtra("finish", "not");
                        in.putExtra("position",  item.lsn_no+1);
                        in.putExtra("student", userData);
                        JsDetailedLesson.super.onBackPressed();
                        startActivity(in);
                    }
                }

            }
        });
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
            Intent in = new Intent(JsDetailedLesson.this, AboutUs.class);
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

   /* public void BackBtn(View view){
        Intent in = new Intent(JsDetailedLesson.this, JsLessonPanel.class);
        in.putExtra("data", userData);
        in.putExtra("id",  lsn_id);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(in);
    }*/

    public void gotoVideoSite(View view){
        if(data.lsn_video.isEmpty()){
            Toast.makeText(this, "There's no Video for this Lesson", Toast.LENGTH_SHORT).show();
        }else{
            Intent in  = new Intent(JsDetailedLesson.this, VideoPanel.class);
            in.putExtra("data", data);
            startActivity(in);
        }
    }

    public void gotoCodeEditor(View view){
        Intent in  = new Intent(JsDetailedLesson.this, CodeEditor.class);
        startActivity(in);
    }

    public void AsyncLoad(){
        HashMap postData = new HashMap();
        postData.put("Course", data.lsn_course);
        postData.put("Category", data.lsn_ptCat);
        PostResponseAsyncTask task = new PostResponseAsyncTask(JsDetailedLesson.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                load = new JsonConverter<SrlLesson>().toArrayList(s, SrlLesson.class);
            }
        });task.execute("http://192.168.10.1/CodeWebScripts/load.php");
    }

    public void toHome(){
        HashMap postData = new HashMap();
        postData.put("adminData", ""+userData.ID);
        PostResponseAsyncTask adminData = new PostResponseAsyncTask(JsDetailedLesson.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                ArrayList<SrlStudents> usr_data = new JsonConverter<SrlStudents>().toArrayList(s, SrlStudents.class);
                SrlStudents usrPos = usr_data.get(0);
                Intent in = new Intent(JsDetailedLesson.this, JsLessonPanel.class);
                in.putExtra("data",usrPos);
                in.putExtra("id",  lsn_id);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        }); adminData.execute("http://192.168.10.1/CodeWebScripts/load.php");

    }
}
