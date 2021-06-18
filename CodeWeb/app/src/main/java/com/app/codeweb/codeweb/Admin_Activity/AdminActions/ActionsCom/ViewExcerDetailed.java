package com.app.codeweb.codeweb.Admin_Activity.AdminActions.ActionsCom;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.codeweb.codeweb.Others.CircleTransform;
import com.app.codeweb.codeweb.Others.Serialized.SrlLesson;
import com.app.codeweb.codeweb.R;
import com.squareup.picasso.Picasso;

public class ViewExcerDetailed extends AppCompatActivity {
    ImageView lsn_icon;
    SrlLesson lessonData;
    ImageButton edit;
    TextView lsn_title, lsn_course, lsn_type, lsn_author, lsn_published, lsn_question, lsn_opta, lsn_optb, lsn_optc, lsn_optd, lsn_ans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_view_excer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(getIntent().getSerializableExtra("data") != null){
            lessonData = (SrlLesson) getIntent().getSerializableExtra("data");
            getSupportActionBar().setTitle(lessonData.lsn_title);

            lsn_icon = (ImageView) findViewById(R.id.quiz_icon);
            lsn_title = (TextView) findViewById(R.id.quiz_title);
            lsn_course = (TextView) findViewById(R.id.quiz_course);
            lsn_type = (TextView) findViewById(R.id.quiz_type);
            lsn_author = (TextView) findViewById(R.id.quiz_author);
            lsn_published = (TextView) findViewById(R.id.quiz_published);
            lsn_question = (TextView) findViewById(R.id.quiz_question);
            lsn_opta = (TextView) findViewById(R.id.quiz_opta);
            lsn_optb = (TextView) findViewById(R.id.quiz_optb);
            lsn_optc = (TextView) findViewById(R.id.quiz_optc);
            lsn_optd = (TextView) findViewById(R.id.quiz_optd);
            lsn_ans = (TextView) findViewById(R.id.quiz_ans);
            edit = (ImageButton) toolbar.findViewById(R.id.edit);
            Picasso.with(ViewExcerDetailed.this)
                    .load(lessonData.lsn_icon)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.stat_sys_download)
                    .transform(new CircleTransform())
                    .into(lsn_icon);
            lsn_title.setText(lessonData.lsn_title);
            lsn_course.setText(lessonData.lsn_course);
            lsn_type.setText(Html.fromHtml("<b>Type of Question</b><br> Multiple Choice"));
            lsn_author.setText(Html.fromHtml("<b>Author</b><br>"+ lessonData.lsn_author));
            lsn_published.setText(Html.fromHtml("<b>Date Published</b><br> Not Available"));
            lsn_question.setText(lessonData.lsn_question+"?");
            lsn_opta.setText(Html.fromHtml("<b>1st Option</b><br>")+ lessonData.lsn_optA);
            lsn_optb.setText(Html.fromHtml("<b>2nd Option</b><br>")+ lessonData.lsn_optB);
            lsn_optc.setText(Html.fromHtml("<b>3rd Option</b><br>")+ lessonData.lsn_optC);
            lsn_optd.setText(Html.fromHtml("<b>4th Option</b><br>")+ lessonData.lsn_optD);
            lsn_ans.setText(lessonData.lsn_rightOpt);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(ViewExcerDetailed.this, AddDel_Quiz.class);
                    in.putExtra("excer", lessonData);
                    startActivity(in);
                }
            });

        }else{
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }
}

