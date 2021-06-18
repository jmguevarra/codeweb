package com.app.codeweb.codeweb;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.app.AlertDialog;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.codeweb.codeweb.Admin_Activity.AdminPanel;
import com.app.codeweb.codeweb.Others.CircleTransform;
import com.app.codeweb.codeweb.Others.Connectivity;
import com.app.codeweb.codeweb.Others.Serialized.SrlStudents;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    Animation animAlpha;
    SliderLayout sliderShow;
    Toolbar toolbar;
    Dialog signInDialog, signIdDialog, signUpDialog;
    Button  showLoginDialog;

    //login dialog
    EditText etUsername, etPassword;
    Button  showIdDialog;

    //Id Dialog
    EditText etId, signUsername, signPassword;
    Button signUp_btn;
    TextView fullname, textID;
    ImageView usr_img;
    static String usr_id = "";
    static String usr_type = "";
    ArrayList<SrlStudents> usr_data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Connectivity connectivity = new Connectivity();
        if(connectivity.isMobileConnected(LoginActivity.this)){
            connectivity.mobileBuildDialog(LoginActivity.this).show();
        }else if(!connectivity.isWifiConnected(LoginActivity.this)){
            connectivity.buildDialog(LoginActivity.this).show();
        }else {
                setContentView(R.layout.activity_login);
                animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
                toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                getSupportActionBar().setTitle("CodeWeb");
                //getSupportActionBar().setLogo(getResources().getDrawable(R.drawable.ic_codeweb));


                sliderShow = (SliderLayout) findViewById(R.id.slider);
                    HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
                    file_maps.put("Let's Code the Web",R.drawable.ic_codeweb);
                    file_maps.put("Js Interact Yourself", R.drawable.js_tag);
                    file_maps.put("Css, Cascading Your Style",R.drawable.css_tag);
                    file_maps.put("Learn and Play HTML",R.drawable.html_tag);


                    for(String name : file_maps.keySet()){
                        TextSliderView textSliderView = new TextSliderView(this);
                        // initialize a SliderLayout
                        textSliderView
                                .description(name)
                                .image(file_maps.get(name))
                                .setScaleType(BaseSliderView.ScaleType.Fit);
                        //add your extra information
                        textSliderView.bundle(new Bundle());
                        textSliderView.getBundle()
                                .putString("extra",name);

                        sliderShow.addSlider(textSliderView);
                    }

                sliderShow.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
               // sliderShow.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));
                sliderShow.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                sliderShow.setCustomAnimation(new DescriptionAnimation());
                sliderShow.setDuration(4000);
                sliderShow.addOnPageChangeListener(this);


                showLoginDialog = (Button) findViewById(R.id.login);
                showLoginDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.startAnimation(animAlpha);
                        SigninDialog();
                    }
                });


        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cw_child_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            Intent in = new Intent(LoginActivity.this, AboutUs.class);
            startActivity(in);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        ExitDialog();
    }



// Dialog And OnClick method for EveryDialog

    private void ExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Are sure you want to exit?");
        builder.setPositiveButton("No", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Yes", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LoginActivity.super.onBackPressed();
            }
        });
        builder.show();

    }

    public void SigninDialog() {
        signInDialog = new Dialog(this);
        signInDialog.setTitle("Sign in");
        signInDialog.setContentView(R.layout.dialog_login);
        signInDialog.setCanceledOnTouchOutside(false);
        signInDialog.setCancelable(true);

        showIdDialog = (Button) signInDialog.findViewById(R.id.sign_upbtn);
        showIdDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                SignidDialog();
            }
        });
        signInDialog.show();
    }

    public void onLogin(View view){
        view.startAnimation(animAlpha);
        etUsername = (EditText) signInDialog.findViewById(R.id.etUsername);
        etPassword = (EditText) signInDialog.findViewById(R.id.etPassword);
        if(etUsername.getText().toString().isEmpty() &&  etPassword.getText().toString().isEmpty()){
            Toast.makeText(LoginActivity.this, "Username and Password is empty",Toast.LENGTH_SHORT).show();
        }else if(etUsername.getText().toString().isEmpty()){
            Toast.makeText(LoginActivity.this, "Username is empty",Toast.LENGTH_SHORT).show();
            etUsername.setError("Username is Empty");
            etUsername.requestFocus();
        }else if(etPassword.getText().toString().isEmpty()){
            Toast.makeText(LoginActivity.this, "Password is empty",Toast.LENGTH_SHORT).show();
            etPassword.setError("Password is Empty");
            etPassword.requestFocus();
        }else{
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            HashMap postData = new HashMap();
            postData.put("username",username);
            postData.put("password",password);
            PostResponseAsyncTask login = new PostResponseAsyncTask(LoginActivity.this, postData, new AsyncResponse() {
                @Override
                public void processFinish(String s) {
                    if(s.contains("invalid")){
                        Toast.makeText(LoginActivity.this, "Invalid", Toast.LENGTH_SHORT).show();
                    }else{
                        usr_data = new JsonConverter<SrlStudents>().toArrayList(s, SrlStudents.class);
                        for(SrlStudents d: usr_data){
                            usr_type = d.Type;
                        }
                            if(usr_type.equalsIgnoreCase("administrator")){
                                SrlStudents data =  usr_data.get(0);
                                Intent i = new Intent(LoginActivity.this, AdminPanel.class);
                                i.putExtra("data", data);
                                startActivity(i);
                                Toast.makeText(LoginActivity.this, "Welcome "+data.Fullname, Toast.LENGTH_LONG).show();
                                signInDialog.dismiss();
                            }else if(usr_type.equalsIgnoreCase("student")){
                                SrlStudents data =  usr_data.get(0);
                                 Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                i.putExtra("data", data);
                                startActivity(i);
                                Toast.makeText(LoginActivity.this, "Welcome "+data.Fullname, Toast.LENGTH_LONG).show();
                                signInDialog.dismiss();
                            }else{
                                Toast.makeText(LoginActivity.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                            }
                    }
                }
            });login.execute("http://192.168.10.1/CodeWebScripts/LoginScript.php");

        }
    }

    public void SignidDialog() {
        signIdDialog = new Dialog(this);
        signIdDialog.setTitle("Students ID");
        signIdDialog.setContentView(R.layout.dialog_id);
        signIdDialog.setCanceledOnTouchOutside(false);
        signIdDialog.setCancelable(true);
        signIdDialog.show();
    }

    public void onIdLogin(View view){
        view.startAnimation(animAlpha);
        etId = (EditText) signIdDialog.findViewById(R.id.Id_signUp);

        if(etId.getText().toString().isEmpty()) {
            Toast.makeText(LoginActivity.this, "Students ID is empty", Toast.LENGTH_SHORT).show();
        }else {
            String textId = etId.getText().toString();

            HashMap postData = new HashMap();
            postData.put("Student_ID", textId);
            PostResponseAsyncTask idLogin = new PostResponseAsyncTask(LoginActivity.this, postData, new AsyncResponse() {
                @Override
                public void processFinish(String s) {
                    if(s.contains("success")){
                        SignUpDialog();
                    }else if(s.contains("failed")){
                        Toast.makeText(LoginActivity.this, "Your not a Student of DLL", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(LoginActivity.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                    }
                }
            }); idLogin.execute("http://192.168.10.1/CodeWebScripts/LoginScript.php");

        }
    }

    public void SignUpDialog() {
        signUpDialog = new Dialog(this);
        signUpDialog.setTitle("Sign Up");
        signUpDialog.setContentView(R.layout.dialog_signup1);
        signUpDialog.setCanceledOnTouchOutside(false);
        signUpDialog.setCancelable(true);

        fullname = (TextView) signUpDialog.findViewById(R.id.fullname);
        usr_img = (ImageView) signUpDialog.findViewById(R.id.usr_img);
        textID = (TextView) signUpDialog.findViewById(R.id.textID);
        signUsername = (EditText) signUpDialog.findViewById(R.id.signUsername);
        signPassword = (EditText) signUpDialog.findViewById(R.id.signPassword);
        signUp_btn = (Button) signUpDialog.findViewById(R.id.signUp_btn);

        HashMap postData = new HashMap();
        postData.put("Student_ID", etId.getText().toString());
        PostResponseAsyncTask loadData = new PostResponseAsyncTask(LoginActivity.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if(s.contains("created")) {
                    Toast.makeText(LoginActivity.this, "You have Already an Acoount", Toast.LENGTH_SHORT).show();
                }else{
                    signIdDialog.dismiss();
                    ArrayList<SrlStudents> data = new JsonConverter<SrlStudents>().toArrayList(s, SrlStudents.class);
                    Toast.makeText(LoginActivity.this, "Student ID Granted", Toast.LENGTH_LONG).show();

                    for(SrlStudents d: data){
                        usr_id = ""+d.ID;
                        fullname.setText(d.Fullname);
                        textID.setText(d.Student_ID);
                        Picasso.with(LoginActivity.this)
                                .load(d.Image)
                                .placeholder(android.R.drawable.ic_menu_gallery)
                                .error(android.R.drawable.stat_sys_download)
                                .transform(new CircleTransform())
                                .into(usr_img);

                    }
                    signUpDialog.show();
                }
            }
        });loadData.execute("http://192.168.10.1/CodeWebScripts/load.php");

        signUp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                if(!signUsername.getText().toString().isEmpty() && !signPassword.getText().toString().isEmpty()){
                    HashMap postData = new HashMap();
                    postData.put("ID", ""+usr_id);
                    postData.put("Username", signUsername.getText().toString());
                    postData.put("Password", signPassword.getText().toString());
                    PostResponseAsyncTask ins =  new PostResponseAsyncTask(LoginActivity.this, postData, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                            if(s.contains("success")){
                                Toast.makeText(LoginActivity.this, "Your account has been Created", Toast.LENGTH_SHORT).show();
                                signUpDialog.dismiss();
                            }else if(s.contains("exist")){
                                Toast.makeText(LoginActivity.this,"Username or Password already existed", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(LoginActivity.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });ins.execute("http://192.168.10.1/CodeWebScripts/insert.php");

                }else if(signUsername.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "Username is Empty", Toast.LENGTH_SHORT).show();
                }else if(signPassword.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "Password is Empty", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(LoginActivity.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
