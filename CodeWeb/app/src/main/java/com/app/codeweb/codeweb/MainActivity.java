package com.app.codeweb.codeweb;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.audiofx.BassBoost;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.codeweb.codeweb.Admin_Activity.AdminActions.Admin_MyProfile;
import com.app.codeweb.codeweb.Admin_Activity.AdminActions.Feedbacks;
import com.app.codeweb.codeweb.LessonActivity.CodeEditor;
import com.app.codeweb.codeweb.LessonActivity.CssLesson.CssFunActivity;
import com.app.codeweb.codeweb.LessonActivity.Js_Activity.JsFunActivity;
import com.app.codeweb.codeweb.LessonActivity.HtmlLesson.HtmlFunActivity;
import com.app.codeweb.codeweb.Others.CircleTransform;
import com.app.codeweb.codeweb.Admin_Activity.AdminActions.Admin_Excercises;
import com.app.codeweb.codeweb.Admin_Activity.AdminActions.Admin_Lessons;
import com.app.codeweb.codeweb.Admin_Activity.AdminActions.Admin_Quizzes;
import com.app.codeweb.codeweb.Admin_Activity.AdminActions.Admin_Videos;
import com.app.codeweb.codeweb.Others.Connection;
import com.app.codeweb.codeweb.Others.Serialized.SrlStudents;
import com.app.codeweb.codeweb.User_Activity.MyProfile;
import com.app.codeweb.codeweb.User_Activity.SendFeedBacks;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String cId;
    Animation animAlpha;
    SrlStudents usrData, usrPos;
    ImageButton gt_admin, htmlbtn,cssbtn, jsbtn;
    ImageView userImg,home;
    TextView user_name, navName, navSec;
    ArrayList<SrlStudents> usr_data;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // this.registerReceiver(receiver, new IntentFilter(Settings.ACTION_WIFI_SETTINGS));
        usrData = (SrlStudents) getIntent().getSerializableExtra("data");
        if(usrData.Type.equalsIgnoreCase("administrator")){
            setContentView(R.layout.admin_students_view);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Home");

            //Casting Objects
            animAlpha  = AnimationUtils.loadAnimation(this, R.anim.alpha);
            gt_admin = (ImageButton) findViewById(R.id.gt_admin);
            home= (ImageButton) toolbar.findViewById(R.id.home);
            user_name = (TextView) findViewById(R.id.studname);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            View header = navigationView.getHeaderView(0);
            navName = (TextView) header.findViewById(R.id.nav_name);
            navSec = (TextView) header.findViewById(R.id.nav_sec);
            userImg = (ImageView) header.findViewById(R.id.user_img);

            gt_admin.setVisibility(View.VISIBLE);
            user_name.setText(usrData.Fullname+"");
            navName.setText(usrData.Fullname);
            Picasso.with(MainActivity.this)
                    .load(usrData.Image)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.stat_sys_download)
                    .transform(new CircleTransform())
                    .into(userImg);
        }else{
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Home");

            //Casting Objects
            animAlpha  = AnimationUtils.loadAnimation(this, R.anim.alpha);
            gt_admin = (ImageButton) findViewById(R.id.gt_admin);
            user_name = (TextView) findViewById(R.id.studname);
            navName = (TextView) findViewById(R.id.nav_name);
            navSec = (TextView) findViewById(R.id.nav_sec);
            userImg = (ImageView) findViewById(R.id.user_img);
            home= (ImageButton) toolbar.findViewById(R.id.home);
            home.setVisibility(View.INVISIBLE);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            View header = navigationView.getHeaderView(0);
            navName = (TextView) header.findViewById(R.id.nav_name);
            navSec = (TextView) header.findViewById(R.id.nav_sec);
            userImg = (ImageView) header.findViewById(R.id.user_img);

            gt_admin.setVisibility(View.INVISIBLE);
            user_name.setText(usrData.Fullname);
            navName.setText(usrData.Fullname);
            navSec.setText(usrData.Section);
            Picasso.with(MainActivity.this)
                    .load(usrData.Image)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.stat_sys_download)
                    .transform(new CircleTransform())
                    .into(userImg);
            userImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(MainActivity.this, "Edit Profile", Toast.LENGTH_SHORT).show();
                }
            });

            CourseLevel();
        }

    }

   /* private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Connection con = new Connection(getApplicationContext());
            if(!con.isWifiConnected(getApplicationContext())){
                con.ConnectionDialog(getApplicationContext()).setPositiveButton("Refresh", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                con.ConnectionDialog(getApplicationContext()).show();
            }else{

            }
        }
    };*/


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(usrData.Type.equalsIgnoreCase("administrator")){
                MainActivity.super.onBackPressed();
            }else {
                LogoutDialog();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(usrData.Type.equalsIgnoreCase("administrator")){
            getMenuInflater().inflate(R.menu.admin_menu, menu);
        }else {
            getMenuInflater().inflate(R.menu.main, menu);
        }
        return true;
    }

    public void Home(View view){
        view.startAnimation(animAlpha);
        if(usrData.Type.equalsIgnoreCase("administrator")){
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(usrData.Type.equalsIgnoreCase("administrator")){
            if(id == R.id.about_me){
                Toast.makeText(this, "UnderConstruction", Toast.LENGTH_SHORT).show();
            }
        }else {
            if (id == R.id.action_about) {
                Intent in = new Intent(MainActivity.this, AboutUs.class);
                startActivity(in);
                return true;
            } else if (id == R.id.action_logout) {
                LogoutDialog();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(usrData.Type.equalsIgnoreCase("administrator")){
            if(id == R.id.nav_myprofile ){
                Intent in  = new Intent(MainActivity.this, Admin_MyProfile.class);
                in.putExtra("data", usrData);
                startActivity(in);
            }else if (id == R.id.nav_lesson) {
                Intent in  = new Intent(MainActivity.this, Admin_Lessons.class);
                startActivity(in);
            } else if (id == R.id.nav_videos) {
                Intent in  = new Intent(MainActivity.this, Admin_Videos.class);
                startActivity(in);
            } else if (id == R.id.nav_quizzes) {
                Intent in  = new Intent(MainActivity.this, Admin_Quizzes.class);
                startActivity(in);
            } else if (id == R.id.nav_excercises) {
                Intent in  = new Intent(MainActivity.this, Admin_Excercises.class);
                startActivity(in);
            } else if (id == R.id.nav_editor) {
                Intent in  = new Intent(MainActivity.this, CodeEditor.class);
                startActivity(in);
            }else if (id == R.id.nav_feedbacks) {
                Intent in  = new Intent(MainActivity.this, Feedbacks.class);
                startActivity(in);
            }
        }else{
            if(id == R.id.nav_myprofile ){
                Intent in = new Intent(MainActivity.this, MyProfile.class);
                in.putExtra("data", usrData);
                startActivity(in);
            }else if (id == R.id.nav_html) {
                Intent in = new Intent(MainActivity.this, HtmlFunActivity.class);
                in.putExtra("data", usrData);
                startActivity(in);
            } else if (id == R.id.nav_css) {
               if(usrData.Course_Level.equalsIgnoreCase("Css")){
                   Intent in = new Intent(MainActivity.this, CssFunActivity.class);
                   in.putExtra("data", usrData);
                   startActivity(in);
               }else{
                   Toast.makeText(this, "Finish the HTML Course to Unlocked this", Toast.LENGTH_SHORT).show();
               }
            }else if(id == R.id.nav_js){
                if(usrData.Course_Level.equalsIgnoreCase("Js")){
                    Intent in = new Intent(MainActivity.this, JsFunActivity.class);
                    in.putExtra("data", usrData);
                    startActivity(in);
                }else{
                    Toast.makeText(this, "Finish the Css Course to Unlocked this", Toast.LENGTH_SHORT).show();
                }
            } else if (id == R.id.nav_editor) {
                Intent in  = new Intent(MainActivity.this, CodeEditor.class);
                startActivity(in);
            } else if (id == R.id.nav_feedbacks) {
                Intent in = new Intent(MainActivity.this, SendFeedBacks.class);
                in.putExtra("data", usrData);
                startActivity(in);
            }else if(id == R.id.nav_instructor){
                Intent in = new Intent(MainActivity.this, AboutUs.class);
                startActivity(in);
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void CourseLevel(){
        htmlbtn = (ImageButton) findViewById(R.id.htmlbtn);
        cssbtn = (ImageButton) findViewById(R.id.cssbtn);
        jsbtn = (ImageButton) findViewById(R.id.jsbtn);
            htmlbtn.setEnabled(false);
            cssbtn.setEnabled(false);
            jsbtn.setEnabled(false);
        if(usrData.Course_Level.equalsIgnoreCase("HTML")){
            htmlbtn.setEnabled(true);
            cssbtn.setImageResource(R.drawable.ut_ic_css);
            jsbtn.setImageResource(R.drawable.ut_ic_js);
        }else if(usrData.Course_Level.equalsIgnoreCase("Css")){
            htmlbtn.setEnabled(true);
            cssbtn.setEnabled(true);
            jsbtn.setImageResource(R.drawable.ut_ic_js);
        }else if(usrData.Course_Level.equalsIgnoreCase("Js")){
            htmlbtn.setEnabled(true);
            cssbtn.setEnabled(true);
            jsbtn.setEnabled(true);
        }
    }
    private void LogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sign Out");
        builder.setMessage("Are sure you want to sign-out?");
        builder.setPositiveButton("No", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Yes", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                NavUtils.navigateUpFromSameTask(MainActivity.this);
            }
        });
        builder.show();
    }

    public void gotoHtmlFun(View view){
        view.startAnimation(animAlpha);
        if(usrData.Type.equalsIgnoreCase("administrator")){
            Intent in = new Intent(MainActivity.this, HtmlFunActivity.class);
            in.putExtra("data", usrData);
            startActivity(in);
        }else{
            Intent in = new Intent(MainActivity.this, HtmlFunActivity.class);
            in.putExtra("data", usrData);
            startActivity(in);
        }
    }
    public void gotoCssFun(View view){
        view.startAnimation(animAlpha);
        if(usrData.Type.equalsIgnoreCase("administrator")) {
            Intent in = new Intent(MainActivity.this, CssFunActivity.class);
            in.putExtra("data", usrData);
            startActivity(in);
        }else{
            Intent in = new Intent(MainActivity.this, CssFunActivity.class);
            in.putExtra("data", usrData);
            startActivity(in);
        }
    }
    public void gotoJsFun(View view){
        view.startAnimation(animAlpha);
        if(usrData.Type.equalsIgnoreCase("administrator")){
            Intent in = new Intent(MainActivity.this, JsFunActivity.class);
            in.putExtra("data", usrData);
            startActivity(in);
        }else{
            Intent in = new Intent(MainActivity.this, JsFunActivity.class);
            in.putExtra("data", usrData);
            startActivity(in);
        }
    }
    public void gotoAdmin(View view){
        view.startAnimation(animAlpha);
        super.onBackPressed();
    }
}
