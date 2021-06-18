package com.app.codeweb.codeweb.Admin_Activity.AdminActions.ActionsCom;

import android.app.Dialog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.codeweb.codeweb.Admin_Activity.AdminActions.Admin_Lessons;
import com.app.codeweb.codeweb.Others.MyImageLoader;
import com.app.codeweb.codeweb.Others.Serialized.SrlLesson;
import com.app.codeweb.codeweb.Others.Serialized.Srl_CrsCat;
import com.app.codeweb.codeweb.R;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.ImageLoader;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class AddLesson extends AppCompatActivity {
    public static Animation animAlpha;
    ArrayList<String> crs, cat,opt;
    ArrayList<Srl_CrsCat> crsLoad, catLoad;
    SrlLesson data;

    //Objects
    CameraPhoto cameraPhoto;
    GalleryPhoto galleryPhoto;
    final int CAMERA_REQUEST = 13323;
    final int GALLERY_REQUEST = 22131;
    Dialog photo;
    String encode_Image = "";
    Spinner sp_course, sp_category;
    ImageView iv_output,iv_camera, iv_gallery;
    EditText etTitle, etContent, etCodeContent, etTrivia;
    EditText quiz_question,quiz_opta, quiz_optb,quiz_optc,quiz_optd;
    Spinner  quiz_ans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
        cameraPhoto = new CameraPhoto(AddLesson.this);
        galleryPhoto = new GalleryPhoto(AddLesson.this);

        if(getIntent().getSerializableExtra("data") != null){
            UpdateActivity();
        }else {
            AddActivity();
        }

    }
    public void UpdateActivity(){

        setContentView(R.layout.admin_update_lesson);
        data = (SrlLesson) getIntent().getSerializableExtra("data");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sp_course = (Spinner) findViewById(R.id.sp_course);
        sp_category = (Spinner) findViewById(R.id.sp_category);
        etTitle = (EditText) findViewById(R.id.et_title);
        etContent = (EditText) findViewById(R.id.et_content);
        etCodeContent = (EditText) findViewById(R.id.et_code_content);
        etTrivia = (EditText) findViewById(R.id.et_trivia);
        iv_output = (ImageView) findViewById(R.id.iv_output);

        iv_output.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                getPhoto();
            }
        });

        getSupportActionBar().setTitle(data.lsn_title);
        crs = new ArrayList<>();
        sp_course.setEnabled(false);
        crs.add(data.lsn_course);
        ArrayAdapter<String> crsadp = new ArrayAdapter<String>(AddLesson.this, R.layout.spinner_costum, crs);
        sp_course.setAdapter(crsadp);
        cat = new ArrayList<>();
        sp_category.setEnabled(false);
        cat.add(data.lsn_category);
        ArrayAdapter<String> catadp = new ArrayAdapter<String>(AddLesson.this, R.layout.spinner_costum, cat);
        sp_category.setAdapter(catadp);
        etTitle.setText(data.lsn_title);
        etContent.setText(Html.fromHtml(data.lsn_content));
        etCodeContent.setText(Html.fromHtml(data.lsn_code_content));
        etTrivia.setText(Html.fromHtml(data.lsn_trivia));
        Picasso.with(AddLesson.this)
                .load(data.lsn_output)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.stat_sys_download)
                .into(iv_output);

        Button btnLesson = (Button) findViewById(R.id.btn_lesson);
        btnLesson.setText("Update Lesson");
        btnLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                HashMap postData = new HashMap();
                postData.put("ID", data.lsn_id+"");
                postData.put("Tlesson", etTitle.getText().toString());
                postData.put("Content", etContent.getText().toString());
                postData.put("Code", etCodeContent.getText().toString());
                postData.put("Trivia", etTrivia.getText().toString());
                postData.put("Image", encode_Image);
                PostResponseAsyncTask task = new PostResponseAsyncTask(AddLesson.this, postData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        if(s.contains("success")){
                            Toast.makeText(AddLesson.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                            Intent in = new Intent(AddLesson.this , Admin_Lessons.class);
                            startActivity(in);
                        }else if(s.contains("failed")){
                            Toast.makeText(AddLesson.this, R.string.scripts_error, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(AddLesson.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                        }
                    }
                });task.execute("http://192.168.10.1/CodeWebScripts/update.php");

            }
        });
          /*  sp_course.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(AddLesson.this, "You cannot change the Course of this lesson to another Course", Toast.LENGTH_SHORT).show();
                }
            });

            sp_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(AddLesson.this, "You cannot change the Category of this Lesson to another Category", Toast.LENGTH_SHORT).show();
                }
            });
*/
    }

    public void AddActivity(){
        setContentView(R.layout.admin_add_lesson);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Lessons");

        sp_course = (Spinner) findViewById(R.id.sp_course);
        sp_category = (Spinner) findViewById(R.id.sp_category);
        etTitle = (EditText) findViewById(R.id.et_title);
        etContent = (EditText) findViewById(R.id.et_content);
        etCodeContent = (EditText) findViewById(R.id.et_code_content);
        etTrivia = (EditText) findViewById(R.id.et_trivia);
        iv_output = (ImageView) findViewById(R.id.iv_output);
        quiz_question = (EditText) findViewById(R.id.et_quiz_question);
        quiz_opta = (EditText) findViewById(R.id.et_quiz_opta);
        quiz_optb = (EditText) findViewById(R.id.et_quiz_optb);
        quiz_optc = (EditText) findViewById(R.id.et_quiz_optc);
        quiz_optd = (EditText) findViewById(R.id.et_quiz_optd);
        quiz_ans = (Spinner) findViewById(R.id.sp_ans);

        iv_output.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                getPhoto();
            }
        });
        SpLoad();
        Button btnLesson = (Button) findViewById(R.id.btn_lesson);
        btnLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                if(sp_course.getSelectedItem().toString().equalsIgnoreCase("Select Course")) {
                    Toast.makeText(AddLesson.this, "Select Course Before Add This Lesson", Toast.LENGTH_SHORT).show();
                }else if(sp_course.getSelectedItem().toString().equalsIgnoreCase("Choose Category")) {
                    Toast.makeText(AddLesson.this, "Your category is empty. Choose from  listed category of " + sp_course.getSelectedItem().toString() + " to add this Lesson", Toast.LENGTH_SHORT).show();
                }else if(etTitle.getText().toString().isEmpty() || etContent.getText().toString().isEmpty() || etCodeContent.getText().toString().isEmpty() || etTrivia.getText().toString().isEmpty()) {
                    Toast.makeText(AddLesson.this, "Make sure that there's no empty boxes. You need to fill it all!", Toast.LENGTH_SHORT).show();
                }else if(quiz_question.getText().toString().isEmpty() || quiz_opta.getText().toString().isEmpty() || quiz_optb.getText().toString().isEmpty() || quiz_optc.getText().toString().isEmpty()|| quiz_optd.getText().toString().isEmpty()) {
                    Toast.makeText(AddLesson.this, "Make sure that there's no empty boxes. You need to fill it all!", Toast.LENGTH_SHORT).show();
                }else{
                    InsertLesson();
                }
            }
        });

    }

    public void InsertLesson(){
        HashMap postData = new HashMap();
        postData.put("mxCourse", sp_course.getSelectedItem().toString());
        postData.put("mxCategory", sp_category.getSelectedItem().toString());
        PostResponseAsyncTask task = new PostResponseAsyncTask(AddLesson.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s){
                if(!s.isEmpty()){
                    int lsn_no = Integer.parseInt(s);
                    HashMap postData = new HashMap();
                    if(sp_course.getSelectedItem().toString().equalsIgnoreCase("HTML")){
                        if(sp_category.getSelectedItem().toString().equalsIgnoreCase("Overview")){
                            postData.put("lsn_ptCat", "html_Overview");
                        }else if(sp_category.getSelectedItem().toString().equalsIgnoreCase("The Basic")){
                            postData.put("lsn_ptCat", "html_Basic");
                        }else if(sp_category.getSelectedItem().toString().equalsIgnoreCase("HTML 5")){
                            postData.put("lsn_ptCat", "html_HTML5");
                        }
                    }else if(sp_course.getSelectedItem().toString().equalsIgnoreCase("Css")){
                        if(sp_category.getSelectedItem().toString().equalsIgnoreCase("The Basic")){
                            postData.put("lsn_ptCat", "css_Basic");
                        }else if(sp_category.getSelectedItem().toString().equalsIgnoreCase("Text Editing")){
                            postData.put("lsn_ptCat", "css_Text");
                        }else if(sp_category.getSelectedItem().toString().equalsIgnoreCase("Properties")){
                            postData.put("lsn_ptCat", "css_Properties");
                        }else if(sp_category.getSelectedItem().toString().equalsIgnoreCase("Layouts")){
                            postData.put("lsn_ptCat", "css_Layouts");
                        }else if(sp_category.getSelectedItem().toString().equalsIgnoreCase("Css 3")){
                            postData.put("lsn_ptCat", "css_Css3");
                        }else if(sp_category.getSelectedItem().toString().equalsIgnoreCase("Backgrounds")){
                            postData.put("lsn_ptCat", "css_Backgrounds");
                        }else if(sp_category.getSelectedItem().toString().equalsIgnoreCase("Transitions")){
                            postData.put("lsn_ptCat", "css_Transitions");
                        }
                    }else if(sp_course.getSelectedItem().toString().equalsIgnoreCase("Js")){
                        if(sp_category.getSelectedItem().toString().equalsIgnoreCase("Overview")){
                            postData.put("lsn_ptCat", "js_Overview");
                        }else if(sp_category.getSelectedItem().toString().equalsIgnoreCase("Basic Concepts")){
                            postData.put("lsn_ptCat", "js_Basic");
                        }else if(sp_category.getSelectedItem().toString().equalsIgnoreCase("Conditional and Loops")){
                            postData.put("lsn_ptCat", "js_Conloops");
                        }else if(sp_category.getSelectedItem().toString().equalsIgnoreCase("Functions")){
                            postData.put("lsn_ptCat", "js_Functions");
                        }else if(sp_category.getSelectedItem().toString().equalsIgnoreCase("Objects")){
                            postData.put("lsn_ptCat", "js_Objects");
                        }else if(sp_category.getSelectedItem().toString().equalsIgnoreCase("Core Objects")){
                            postData.put("lsn_ptCat", "js_Core");
                        }else if(sp_category.getSelectedItem().toString().equalsIgnoreCase("Dom & Events")){
                            postData.put("lsn_ptCat", "js_Events");
                        }
                    }
                    postData.put("lsn_no",lsn_no+"");
                    postData.put("Course", sp_course.getSelectedItem().toString());
                    postData.put("Category", sp_category.getSelectedItem().toString());
                    postData.put("Tlesson", etTitle.getText().toString());
                    postData.put("Content", etContent.getText().toString());
                    postData.put("Code", etCodeContent.getText().toString());
                    postData.put("Trivia", etTrivia.getText().toString());
                    postData.put("Image", encode_Image);
                    postData.put("quiz_Question", quiz_question.getText().toString());
                    postData.put("quiz_OptA", quiz_opta.getText().toString());
                    postData.put("quiz_OptB", quiz_optb.getText().toString());
                    postData.put("quiz_OptC", quiz_optc.getText().toString());
                    postData.put("quiz_OptD", quiz_optd.getText().toString());
                    postData.put("quiz_Ans", quiz_ans.getSelectedItem().toString());
                    PostResponseAsyncTask task = new PostResponseAsyncTask(AddLesson.this, postData, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                            if (s.contains("success")) {
                                Toast.makeText(AddLesson.this, "One Lesson Has Been Added", Toast.LENGTH_SHORT).show();
                                Intent in = new Intent(AddLesson.this, Admin_Lessons.class);
                                startActivity(in);
                            } else if (s.contains("failed")) {
                                Toast.makeText(AddLesson.this, R.string.scripts_error, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AddLesson.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    task.execute("http://192.168.10.1/CodeWebScripts/insert.php");

                }
            }
        });task.execute("http://192.168.10.1/CodeWebScripts/load.php");





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == CAMERA_REQUEST){
                String photoPath = cameraPhoto.getPhotoPath();
                try {
                    Bitmap bitmap = MyImageLoader.init().from(photoPath).requestSize(1024, 1024).getBitmap();
                    iv_output.setImageBitmap(bitmap);

                    if(!photoPath.isEmpty()) {
                        Bitmap serverBitmap = ImageLoader.init().from(photoPath).requestSize(1024, 1024).getBitmap();
                        encode_Image = ImageBase64.encode(serverBitmap);
                    }else{
                        encode_Image = "";
                    }
                } catch (FileNotFoundException e) {
                    Toast.makeText(AddLesson.this, "Something Wrong while loading photos", Toast.LENGTH_SHORT).show();
                }

            }
            else if(requestCode == GALLERY_REQUEST){
                Uri uri = data.getData();
                galleryPhoto.setPhotoUri(uri);
                String photoPath = galleryPhoto.getPath();
                try {
                    Bitmap bitmap = MyImageLoader.init().from(photoPath).getBitmap();
                    iv_output.setImageBitmap(bitmap);

                    if(!photoPath.isEmpty()) {
                        Bitmap serverBitmap = ImageLoader.init().from(photoPath).requestSize(1024, 1024).getBitmap();
                        encode_Image = ImageBase64.encode(serverBitmap);
                    }else{
                        encode_Image = "";
                    }
                } catch (FileNotFoundException e) {
                    Toast.makeText(AddLesson.this, "Something Wrong while choosing photos", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void getPhoto(){
        photo = new Dialog(AddLesson.this);
        photo.setTitle("Choose Action");
        photo.setContentView(R.layout.dialog_photo);
        photo.setCanceledOnTouchOutside(false);

        iv_camera = (ImageView) photo.findViewById(R.id.iv_camera);
        iv_gallery = (ImageView) photo.findViewById(R.id.iv_gallery);

        iv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                try {
                    startActivityForResult(cameraPhoto.takePhotoIntent(), CAMERA_REQUEST);
                    cameraPhoto.addToGallery();
                    photo.dismiss();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        iv_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);
                photo.dismiss();
            }
        });

        photo.show();
    }

    public void SpLoad(){
        HashMap postData = new HashMap();
        postData.put("sp_course", "sp_course");
        PostResponseAsyncTask task = new PostResponseAsyncTask(AddLesson.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                crsLoad = new JsonConverter<Srl_CrsCat>().toArrayList(s, Srl_CrsCat.class);
                crs = new ArrayList<>();
                crs.add("Select Course");
                for(Srl_CrsCat d:crsLoad){
                    crs.add(d.crs_title);
                }

                ArrayAdapter<String> adp = new ArrayAdapter<String>(AddLesson.this, R.layout.spinner_costum, crs);
                sp_course.setAdapter(adp);
            }
        });task.execute("http://192.168.10.1/CodeWebScripts/load.php");


        sp_course.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final String title = crs.get(i);
                if(title.equalsIgnoreCase("Select Course")){
                    cat= new ArrayList<>();
                    cat.add("Select Category");
                    ArrayAdapter<String> adp = new ArrayAdapter<String>(AddLesson.this, R.layout.spinner_costum, cat);
                    sp_category.setAdapter(adp);
                }else{
                    HashMap postData = new HashMap();
                    postData.put("category", title);
                    PostResponseAsyncTask load = new PostResponseAsyncTask(AddLesson.this, postData, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                            catLoad = new JsonConverter<Srl_CrsCat>().toArrayList(s, Srl_CrsCat.class);
                            cat= new ArrayList<>();
                            for(Srl_CrsCat d:catLoad){
                                cat.add(d.cat_fun);
                            }
                            ArrayAdapter<String> adp = new ArrayAdapter<String>(AddLesson.this, R.layout.spinner_costum, cat);
                            sp_category.setAdapter(adp);
                        }
                    });load.execute("http://192.168.10.1/CodeWebScripts/load.php");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        opt = new ArrayList<>();
        opt.add("1st");
        opt.add("2nd");
        opt.add("3rd");
        opt.add("4th");
        ArrayAdapter<String> crsadp = new ArrayAdapter<String>(AddLesson.this, R.layout.spinner_costum, opt);
        quiz_ans.setAdapter(crsadp);

    }
}
