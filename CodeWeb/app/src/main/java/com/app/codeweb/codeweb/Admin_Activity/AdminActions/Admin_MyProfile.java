package com.app.codeweb.codeweb.Admin_Activity.AdminActions;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.codeweb.codeweb.Admin_Activity.AdminPanel;
import com.app.codeweb.codeweb.MainActivity;
import com.app.codeweb.codeweb.Others.CircleTransform;
import com.app.codeweb.codeweb.Others.Serialized.SrlStudents;
import com.app.codeweb.codeweb.R;
import com.app.codeweb.codeweb.User_Activity.MyProfile;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class Admin_MyProfile extends AppCompatActivity {

    private SrlStudents userData;
    TextView fullname, id, username, email, contact;
    ImageView user_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_my_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Profile");

        fullname = (TextView) findViewById(R.id.fullname);
        id = (TextView) findViewById(R.id.id);
        username = (TextView) findViewById(R.id.username);
        email = (TextView) findViewById(R.id.email);
        contact = (TextView) findViewById(R.id.contact);
        user_img = (ImageView) findViewById(R.id.user_img);
        if(getIntent().getSerializableExtra("data") != null){
           userData  = (SrlStudents) getIntent().getSerializableExtra("data");
            fullname.setText(userData.Fullname);
            id.setText(userData.Student_ID);
            username.setText(Html.fromHtml(userData.Username+"<br><b>Username</b>"));
            email.setText(Html.fromHtml(userData.Email+"<br><b>Email</font></b>"));
            contact.setText(Html.fromHtml(userData.Phone_number+"<br><b>Contact Number</b>"));
            Picasso.with(Admin_MyProfile.this)
                    .load(userData.Image)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.stat_sys_download)
                    .transform(new CircleTransform())
                    .into(user_img);
        }else{
            Toast.makeText(this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        toHome();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            toHome();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void toHome(){
        HashMap postData = new HashMap();
        postData.put("adminData", ""+userData.ID);
        PostResponseAsyncTask adminData = new PostResponseAsyncTask(Admin_MyProfile.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                ArrayList<SrlStudents> usr_data = new JsonConverter<SrlStudents>().toArrayList(s, SrlStudents.class);
                SrlStudents usrPos = usr_data.get(0);
                Intent in = new Intent(Admin_MyProfile.this, AdminPanel.class);
                in.putExtra("data",usrPos);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        }); adminData.execute("http://192.168.10.1/CodeWebScripts/load.php");

    }
}
