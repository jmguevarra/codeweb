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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.amigold.fundapter.interfaces.FunDapterFilter;
import com.app.codeweb.codeweb.Admin_Activity.AdminActions.ActionsCom.AddLesson;
import com.app.codeweb.codeweb.Admin_Activity.AdminActions.ActionsCom.ViewLesson;
import com.app.codeweb.codeweb.Others.Serialized.SrlLesson;
import com.app.codeweb.codeweb.Others.Serialized.SrlLesson;
import com.app.codeweb.codeweb.R;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class Admin_Lessons extends AppCompatActivity {
    private ArrayList<SrlLesson> load;
    FunDapter<SrlLesson> adp;
    SrlLesson selectedItem;
    ListView listLesson;
    SearchView srch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_lessons);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Lessons");

        listLesson = (ListView) findViewById(R.id.lesson_list);

        srch = (SearchView) findViewById(R.id.srch);
        srch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                srch.requestFocus();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Admin_Lessons.this, AddLesson.class);
                startActivity(in);
            }
        });

        AsyncLoad();
        listLesson.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SrlLesson item = load.get(i);
                Intent in = new Intent(Admin_Lessons.this, ViewLesson.class);
                in.putExtra("data", item);
                startActivity(in);
            }
        });
        registerForContextMenu(listLesson);

    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }

    public void initTextFilter(final FunDapter<SrlLesson> adapter){
        adapter.initFilter(new FunDapterFilter<SrlLesson>() {
            @Override
            public ArrayList<SrlLesson> filter(String filterConstraint, ArrayList<SrlLesson> originalList) {
                ArrayList<SrlLesson> filtered = new ArrayList<SrlLesson>();
                for(int i = 0; i < originalList.size(); i++){
                    SrlLesson quiz = originalList.get(i);
                    String lowerCase =   filterConstraint.toLowerCase();
                    if(quiz.lsn_title.toLowerCase().matches(lowerCase) || quiz.lsn_title.toLowerCase().startsWith(lowerCase)){
                        filtered.add(quiz);
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


    @Override
    public void onCreateContextMenu(ContextMenu adminMenu, View adminView, ContextMenu.ContextMenuInfo adminMenuInfo) {
        super.onCreateContextMenu(adminMenu, adminView, adminMenuInfo);
        MenuInflater AdminInflater  = Admin_Lessons.this.getMenuInflater();
        AdminInflater.inflate(R.menu.listmenu, adminMenu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem admin_Item) {
        AdapterView.AdapterContextMenuInfo adminInfo = (AdapterView.AdapterContextMenuInfo) admin_Item.getMenuInfo();
        if(admin_Item.getItemId() == R.id.view){
            selectedItem = adp.getItem(adminInfo.position);
            ViewLesson();
        }else if(admin_Item.getItemId() == R.id.update){
            selectedItem = adp.getItem(adminInfo.position);
            UpdateLesson();
        }else if(admin_Item.getItemId() == R.id.delete){
            selectedItem = adp.getItem(adminInfo.position);
            DeleteLesson();
        }
        return super.onContextItemSelected(admin_Item);
    }

    public void ViewLesson(){
        Intent in = new Intent(Admin_Lessons.this, ViewLesson.class);
        in.putExtra("data", selectedItem);
        startActivity(in);
    }

    public void UpdateLesson(){
        Intent in = new Intent(Admin_Lessons.this, AddLesson.class);
        in.putExtra("data", selectedItem);
        startActivity(in);
    }

    public void DeleteLesson(){
            AlertDialog.Builder builder = new AlertDialog.Builder(Admin_Lessons.this);
            builder.setTitle("Delete Option");
            builder.setIcon(android.R.drawable.ic_delete);
            builder.setMessage("Are sure you want to delete this lesson '"+selectedItem.lsn_title+"' from "+selectedItem.lsn_category+" of "+selectedItem.lsn_course+" course?");
            builder.setPositiveButton("No", new Dialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setNegativeButton("Yes", new Dialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    HashMap postData = new HashMap();
                    postData.put("Lesson_ID", ""+selectedItem.lsn_id);
                    PostResponseAsyncTask task = new PostResponseAsyncTask(Admin_Lessons.this, postData, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                            if(s.contains("success")){
                                Toast.makeText(Admin_Lessons.this, "Lesson Deleted", Toast.LENGTH_SHORT).show();
                                AsyncLoad();
                            }else if(s.contains("failed")){
                                Toast.makeText(Admin_Lessons.this, R.string.scripts_error, Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(Admin_Lessons.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });task.execute("http://192.168.10.1/CodeWebScripts/remove.php");
                    dialog.cancel();
                }
            });
            builder.show();
    }


    public void AsyncLoad(){
        HashMap postData = new HashMap();
        postData.put("Courses","courses");
        PostResponseAsyncTask loadData = new PostResponseAsyncTask(Admin_Lessons.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                load = new JsonConverter<SrlLesson>().toArrayList(s, SrlLesson.class);
                BindDictionary<SrlLesson> data = new BindDictionary<SrlLesson>();

                data.addStringField(R.id.lsn_title, new StringExtractor<SrlLesson>() {
                    @Override
                    public String getStringValue(SrlLesson item, int position) {
                        return item.lsn_title;
                    }
                });

                data.addStringField(R.id.lsn_category, new StringExtractor<SrlLesson>() {
                    @Override
                    public String getStringValue(SrlLesson item, int position) {
                        return item.lsn_category;
                    }
                });

                data.addDynamicImageField(R.id.ic_courses, new StringExtractor<SrlLesson>() {
                    @Override
                    public String getStringValue(SrlLesson item, int position) {
                        return item.lsn_icon;
                    }
                }, new DynamicImageLoader() {
                    @Override
                    public void loadImage(String url, ImageView view) {
                        Picasso.with(Admin_Lessons.this)
                                .load(url)
                                .placeholder(android.R.drawable.ic_menu_gallery)
                                .error(android.R.drawable.stat_sys_download)
                                .into(view);
                    }
                });
                adp = new FunDapter<>(Admin_Lessons.this, load, R.layout.slessons_custom, data);
                listLesson.setAdapter(adp);
                initTextFilter(adp);
            }
        });
        loadData.execute("http://192.168.10.1/CodeWebScripts/load.php");
    }

}
