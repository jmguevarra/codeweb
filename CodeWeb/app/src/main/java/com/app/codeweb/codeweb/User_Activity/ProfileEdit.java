package com.app.codeweb.codeweb.User_Activity;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.codeweb.codeweb.Others.CircleTransform;
import com.app.codeweb.codeweb.Others.MyImageLoader;
import com.app.codeweb.codeweb.Others.Serialized.SrlStudents;
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

public class ProfileEdit extends AppCompatActivity {
    CameraPhoto cameraPhoto;
    GalleryPhoto galleryPhoto;
    final int CAMERA_REQUEST = 13323;
    final int GALLERY_REQUEST = 22131;
    String encode_Image = "";
    Dialog photo;
    Animation animAlpha;

    private SrlStudents userData;
    TextView id, section;
    EditText fullname, username, password, contact, email;
    ImageView user_img,iv_camera, iv_gallery;
    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_profile_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Info");
        animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);

        user_img = (ImageView) findViewById(R.id.user_img);
        id = (TextView) findViewById(R.id.id);
        section = (TextView) findViewById(R.id.section);
        fullname = (EditText) findViewById(R.id.et_fullname);
        username = (EditText) findViewById(R.id.et_username);
        password = (EditText) findViewById(R.id.et_password);
        contact = (EditText) findViewById(R.id.et_number);
        email = (EditText) findViewById(R.id.et_email);
        saveBtn = (Button) findViewById(R.id.save);

        if(getIntent().getSerializableExtra("data") != null){
            userData  = (SrlStudents) getIntent().getSerializableExtra("data");

            Picasso.with(ProfileEdit.this)
                    .load(userData.Image)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.stat_sys_download)
                    .transform(new CircleTransform())
                    .into(user_img);
            id.setText(userData.Student_ID);
            section.setText(userData.Section);
            fullname.setText(userData.Fullname);
            username.setText(userData.Username);
            password.setText(userData.Password);
            email.setText(userData.Email);
            contact.setText(userData.Phone_number);

            cameraPhoto = new CameraPhoto(getApplicationContext());
            galleryPhoto = new GalleryPhoto(getApplicationContext());

            user_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.startAnimation(animAlpha);
                    getPhoto();
                }
            });

            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.startAnimation(animAlpha);
                    save();
                }
            });

        }else{
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void save(){
        HashMap postData = new HashMap();
        postData.put("ID", ""+userData.ID);
        postData.put("Fullname", fullname.getText().toString());
        postData.put("Username", username.getText().toString());
        postData.put("Password", password.getText().toString());
        postData.put("Number", contact.getText().toString());
        postData.put("Email", email.getText().toString());
        postData.put("Image", encode_Image);
        PostResponseAsyncTask updateTask = new PostResponseAsyncTask(ProfileEdit.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if(s.contains("success")){
                    HashMap postData = new HashMap();
                    postData.put("adminData", ""+userData.ID);
                    PostResponseAsyncTask task = new PostResponseAsyncTask(ProfileEdit.this, postData, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                            ArrayList<SrlStudents> loadUser = new JsonConverter<SrlStudents>().toArrayList(s, SrlStudents.class);
                            SrlStudents updateUser = loadUser.get(0);
                            Intent in = new Intent(ProfileEdit.this, MyProfile.class);
                            in.putExtra("data", updateUser);
                            startActivity(in);
                            Toast.makeText(ProfileEdit.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                        }
                    });task.execute("http://192.168.10.1/CodeWebScripts/load.php");

                }else if(s.contains("failed")){
                    Toast.makeText(ProfileEdit.this, R.string.scripts_error, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ProfileEdit.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                }
            }
        });updateTask.execute("http://192.168.10.1/CodeWebScripts/update.php");


        encode_Image = "";
    }

    public void getPhoto(){
        photo = new Dialog(ProfileEdit.this);
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
                }catch (IOException e) {
                    Toast.makeText(getApplicationContext(),
                            "Something Wrong while taking photos", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == CAMERA_REQUEST){
                String photoPath = cameraPhoto.getPhotoPath();
                try {
                    Bitmap bitmap = MyImageLoader.init().from(photoPath).getCircleBitmap();
                    user_img.setImageBitmap(bitmap);

                    if(!photoPath.isEmpty()) {
                        Bitmap serverBitmap = ImageLoader.init().from(photoPath).requestSize(1024, 1024).getBitmap();
                        encode_Image = ImageBase64.encode(serverBitmap);
                    }else{
                        encode_Image = "";
                    }
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(),
                            "Something Wrong while loading photos", Toast.LENGTH_SHORT).show();
                }

            }
            else if(requestCode == GALLERY_REQUEST){
                Uri uri = data.getData();
                galleryPhoto.setPhotoUri(uri);
                String photoPath = galleryPhoto.getPath();

                try {
                    Bitmap bitmap = MyImageLoader.init().from(photoPath).getCircleBitmap();
                    user_img.setImageBitmap(bitmap);

                    if(!photoPath.isEmpty()) {
                        Bitmap serverBitmap = ImageLoader.init().from(photoPath).requestSize(1024, 1024).getBitmap();
                        encode_Image = ImageBase64.encode(serverBitmap);
                    }else{
                        encode_Image = "";
                    }
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(),
                            "Something Wrong while choosing photos", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
