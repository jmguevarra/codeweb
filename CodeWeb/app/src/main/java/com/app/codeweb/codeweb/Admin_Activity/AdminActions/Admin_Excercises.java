package com.app.codeweb.codeweb.Admin_Activity.AdminActions;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.amigold.fundapter.interfaces.DynamicImageLoader;
import com.amigold.fundapter.interfaces.FunDapterFilter;
import com.app.codeweb.codeweb.Admin_Activity.AdminActions.ActionsCom.AddDel_Quiz;
import com.app.codeweb.codeweb.Admin_Activity.AdminActions.ActionsCom.ViewExcerDetailed;
import com.app.codeweb.codeweb.Admin_Activity.AdminActions.ActionsCom.ViewQuizDetailed;
import com.app.codeweb.codeweb.Others.Serialized.SrlLesson;
import com.app.codeweb.codeweb.Others.Serialized.SrlLesson;
import com.app.codeweb.codeweb.R;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class Admin_Excercises extends AppCompatActivity {
    private ArrayList<SrlLesson> load;
    FunDapter<SrlLesson> adp;
    SrlLesson selectedItem;
    ListView list;
    SearchView srch;
    Animation fadeleft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_excercises);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Exercises");

        fadeleft = AnimationUtils.loadAnimation(this, R.anim.show_to_left);
        list = (ListView) findViewById(R.id.lesson_list);
        srch = (SearchView) findViewById(R.id.srch);
        srch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                srch.requestFocus();
            }
        });
        AsyncLoad();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SrlLesson item = load.get(i);
                Intent in = new Intent(Admin_Excercises.this, ViewExcerDetailed.class);
                in.putExtra("data", item);
                startActivity(in);
            }
        });
        registerForContextMenu(list);

    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater  = getMenuInflater();
        inflater.inflate(R.menu.listmenu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        selectedItem = adp.getItem(info.position);
        if(item.getItemId() == R.id.update){
            Intent in = new Intent(Admin_Excercises.this, AddDel_Quiz.class);
            in.putExtra("excer", selectedItem);
            startActivity(in);
        }else if(item.getItemId() == R.id.delete){
            Toast.makeText(this, "Delete Option is not Required on this Activity", Toast.LENGTH_SHORT).show();
        }else if(item.getItemId() == R.id.view){
            Intent in = new Intent(Admin_Excercises.this, ViewExcerDetailed.class);
            in.putExtra("data", selectedItem);
            startActivity(in);
        }
        return super.onContextItemSelected(item);
    }


    public void AsyncLoad(){
        HashMap postData = new HashMap();
        postData.put("Courses","courses");
        PostResponseAsyncTask loadData = new PostResponseAsyncTask(Admin_Excercises.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                load = new JsonConverter<SrlLesson>().toArrayList(s, SrlLesson.class);
                BindDictionary< SrlLesson> data = new BindDictionary< SrlLesson>();

                data.addStringField(R.id.lsn_title, new StringExtractor< SrlLesson>() {
                    @Override
                    public String getStringValue( SrlLesson item, int position) {
                        return item.lsn_title;
                    }
                });

                data.addStringField(R.id.lsn_category, new StringExtractor< SrlLesson>() {
                    @Override
                    public String getStringValue( SrlLesson item, int position) {
                        return item.lsn_course;
                    }
                });

                data.addDynamicImageField(R.id.ic_courses, new StringExtractor< SrlLesson>() {
                    @Override
                    public String getStringValue( SrlLesson item, int position) {
                        return item.lsn_icon;
                    }
                }, new DynamicImageLoader() {
                    @Override
                    public void loadImage(String url, ImageView view) {
                        Picasso.with(Admin_Excercises.this)
                                .load(url)
                                .placeholder(android.R.drawable.ic_menu_gallery)
                                .error(android.R.drawable.stat_sys_download)
                                .into(view);
                    }
                });
                adp = new FunDapter<>(Admin_Excercises.this, load, R.layout.slessons_custom, data);
                list.setAdapter(adp);
                initTextFilter(adp);
            }
        });
        loadData.execute("http://192.168.10.1/CodeWebScripts/load.php");
    }

    public void initTextFilter(final FunDapter<SrlLesson> adapter){
        adapter.initFilter(new FunDapterFilter<SrlLesson>() {
            @Override
            public ArrayList<SrlLesson> filter(String filterConstraint, ArrayList<SrlLesson> originalList) {
                ArrayList<SrlLesson> filtered = new ArrayList<SrlLesson>();
                for(int i = 0; i < originalList.size(); i++){
                    SrlLesson list = originalList.get(i);
                    String lowerCase =   filterConstraint.toLowerCase();
                    if(list.lsn_title.toLowerCase().matches(lowerCase) || list.lsn_title.toLowerCase().startsWith(lowerCase)){
                        filtered.add(list);
                    }
                }

                return filtered;
            }
        });

        srch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                adapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });

    }
}
