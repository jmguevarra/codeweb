package com.app.codeweb.codeweb.Admin_Activity.AdminActions;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.amigold.fundapter.interfaces.DynamicImageLoader;
import com.app.codeweb.codeweb.Admin_Activity.AdminActions.ActionsCom.ViewLesson;
import com.app.codeweb.codeweb.Others.Serialized.SrlFeedbacks;
import com.app.codeweb.codeweb.Others.Serialized.SrlLesson;
import com.app.codeweb.codeweb.R;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class Feedbacks extends AppCompatActivity {
    private ArrayList<SrlFeedbacks> load;
    FunDapter<SrlFeedbacks> adp;
    SrlFeedbacks selectedItem;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_feedbacks);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Feedbacks");

        list = (ListView) findViewById(R.id.list);
        AsyncLoad();

    }
    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }

    public void AsyncLoad(){
        HashMap postData = new HashMap();
        postData.put("feedbacks","feedbacks");
        PostResponseAsyncTask loadData = new PostResponseAsyncTask(Feedbacks.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if(!s.isEmpty()){
                    load = new JsonConverter<SrlFeedbacks>().toArrayList(s, SrlFeedbacks.class);
                    BindDictionary<SrlFeedbacks> data = new BindDictionary<SrlFeedbacks>();

                    data.addStringField(R.id.name, new StringExtractor<SrlFeedbacks>() {
                        @Override
                        public String getStringValue(SrlFeedbacks item, int position) {
                            return item.Fullname;
                        }
                    });

                    data.addStringField(R.id.sec, new StringExtractor<SrlFeedbacks>() {
                        @Override
                        public String getStringValue(SrlFeedbacks item, int position) {
                            return item.Section;
                        }
                    });

                    data.addStringField(R.id.date, new StringExtractor<SrlFeedbacks>() {
                        @Override
                        public String getStringValue(SrlFeedbacks item, int position) {
                            return item.Date;
                        }
                    });
                    data.addStringField(R.id.con, new StringExtractor<SrlFeedbacks>() {
                        @Override
                        public String getStringValue(SrlFeedbacks item, int position) {
                            return item.Message;
                        }
                    });

                    data.addDynamicImageField(R.id.iv_user_ic, new StringExtractor<SrlFeedbacks>() {
                        @Override
                        public String getStringValue(SrlFeedbacks item, int position) {
                            return item.Image;
                        }
                    }, new DynamicImageLoader() {
                        @Override
                        public void loadImage(String url, ImageView view) {
                            Picasso.with(Feedbacks.this)
                                    .load(url)
                                    .placeholder(android.R.drawable.ic_menu_gallery)
                                    .error(android.R.drawable.stat_sys_download)
                                    .into(view);
                        }
                    });
                    adp = new FunDapter<>(Feedbacks.this, load, R.layout.custom_feedbacks, data);
                    list.setAdapter(adp);

                }else{
                    Toast.makeText(Feedbacks.this, "Connection Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        loadData.execute("http://192.168.10.1/CodeWebScripts/feedbacks.php");
    }
}
