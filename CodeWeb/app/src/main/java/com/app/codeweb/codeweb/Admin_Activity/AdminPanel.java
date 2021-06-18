package com.app.codeweb.codeweb.Admin_Activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.codeweb.codeweb.Admin_Activity.AdminActions.Admin_MyProfile;
import com.app.codeweb.codeweb.Admin_Activity.AdminActions.Feedbacks;
import com.app.codeweb.codeweb.LessonActivity.CodeEditor;
import com.app.codeweb.codeweb.Others.CircleTransform;
import com.app.codeweb.codeweb.Admin_Activity.AdminActions.Admin_Excercises;
import com.app.codeweb.codeweb.Admin_Activity.AdminActions.Admin_Lessons;
import com.app.codeweb.codeweb.Admin_Activity.AdminActions.Admin_Quizzes;
import com.app.codeweb.codeweb.Admin_Activity.AdminActions.Admin_Videos;
import com.app.codeweb.codeweb.Admin_Activity.TabCom.MyAdapter;
import com.app.codeweb.codeweb.Admin_Activity.TabCom.SlidingTabLayout;
import com.app.codeweb.codeweb.Others.Serialized.SrlStudents;
import com.app.codeweb.codeweb.MainActivity;
import com.app.codeweb.codeweb.R;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminPanel extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static Animation animAlpha;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewpager;
    SrlStudents usrData,usrPos;
    ArrayList<SrlStudents> usr_data;
    NavigationView navigationView;


    ImageView user_img;
    TextView nav_sec, nav_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Admin Panel");

        //Getting Data
        usrData = (SrlStudents) getIntent().getSerializableExtra("data");

        viewpager = (ViewPager) findViewById(R.id.vp_tabs);
        viewpager.setAdapter(new MyAdapter(getSupportFragmentManager(), this));

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tab);
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        slidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.textColor));
        slidingTabLayout.setCustomTabView(R.layout.tabview, R.id.tv_tab);
        slidingTabLayout.setViewPager(viewpager);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        nav_name = (TextView) header.findViewById(R.id.nav_name);
        user_img = (ImageView) header.findViewById(R.id.user_img);

        nav_name.setText(usrData.Fullname);
        Picasso.with(AdminPanel.this)
                .load(usrData.Image)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.stat_sys_download)
                .transform(new CircleTransform())
                .fit()
                .into(user_img);

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            ExitDialog();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_myprofile ){
            Intent in  = new Intent(AdminPanel.this, Admin_MyProfile.class);
            in.putExtra("data", usrData);
            startActivity(in);
        }else if (id == R.id.nav_lesson) {
            Intent in  = new Intent(AdminPanel.this, Admin_Lessons.class);
            startActivity(in);
        } else if (id == R.id.nav_videos) {
            Intent in  = new Intent(AdminPanel.this, Admin_Videos.class);
            startActivity(in);
        } else if (id == R.id.nav_quizzes) {
            Intent in  = new Intent(AdminPanel.this, Admin_Quizzes.class);
            startActivity(in);
        } else if (id == R.id.nav_excercises) {
            Intent in  = new Intent(AdminPanel.this, Admin_Excercises.class);
            startActivity(in);
        } else if (id == R.id.nav_editor) {
            Intent in  = new Intent(AdminPanel.this, CodeEditor.class);
            startActivity(in);
        }else if (id == R.id.nav_feedbacks) {
            Intent in  = new Intent(AdminPanel.this, Feedbacks.class);
            startActivity(in);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);

        MenuItem getItem = menu.findItem(R.id.home);
            if(getItem != null){
                AppCompatButton btn = (AppCompatButton) getItem.getActionView();
                btn.setBackgroundResource(R.drawable.ic_home);
                btn.setWidth(20);
                btn.setHeight(20);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toHome();
                    }
                });
            }

        return super.onCreateOptionsMenu(menu);
    }*/

    public void Home(View view){
        view.startAnimation(animAlpha);
        toHome();
    }


    private void ExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Leaving");
        builder.setMessage("Are sure you want to leave in this Admin Panel?");
        builder.setPositiveButton("No", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Yes", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AdminPanel.super.onBackPressed();
            }
        });
        builder.show();

    }

    public void toHome(){
        HashMap postData = new HashMap();
        postData.put("adminData", ""+usrData.ID);
        PostResponseAsyncTask adminData = new PostResponseAsyncTask(AdminPanel.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                usr_data = new JsonConverter<SrlStudents>().toArrayList(s, SrlStudents.class);
                usrPos = usr_data.get(0);
                Intent in = new Intent(AdminPanel.this, MainActivity.class);
                in.putExtra("data",usrPos);
                startActivity(in);
            }
        }); adminData.execute("http://192.168.10.1/CodeWebScripts/load.php");
    }


}

