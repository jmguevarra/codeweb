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
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.amigold.fundapter.interfaces.DynamicImageLoader;
import com.amigold.fundapter.interfaces.FunDapterFilter;
import com.app.codeweb.codeweb.Admin_Activity.AdminActions.ActionsCom.AddDel_Quiz;
import com.app.codeweb.codeweb.Admin_Activity.AdminActions.ActionsCom.AddLesson;
import com.app.codeweb.codeweb.Admin_Activity.AdminActions.ActionsCom.ViewLesson;
import com.app.codeweb.codeweb.Admin_Activity.AdminActions.ActionsCom.ViewQuizDetailed;
import com.app.codeweb.codeweb.Admin_Activity.AdminPanel;
import com.app.codeweb.codeweb.Others.Serialized. SrlQuiz;
import com.app.codeweb.codeweb.Others.Serialized.SrlStudents;
import com.app.codeweb.codeweb.R;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.HashMap;

public class Admin_Quizzes extends AppCompatActivity {
    private ArrayList<SrlQuiz> load;
    FunDapter<SrlQuiz> adp;
    SrlQuiz selectedItem;
    ListView list;
    SearchView srch;
    Animation fadeleft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_quizzes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Quizzes");

        AsyncLoad();
        fadeleft = AnimationUtils.loadAnimation(this, R.anim.show_to_left);
        list = (ListView) findViewById(R.id.lesson_list);
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
                Intent in = new Intent(Admin_Quizzes.this, AddDel_Quiz.class);
                startActivity(in);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SrlQuiz item = load.get(i);
                Intent in = new Intent(Admin_Quizzes.this, ViewQuizDetailed.class);
                in.putExtra("data", item);
                startActivity(in);
            }
        });
        registerForContextMenu(list);


    }

    public void initTextFilter(final FunDapter<SrlQuiz> adapter){
        adapter.initFilter(new FunDapterFilter<SrlQuiz>() {
            @Override
            public ArrayList<SrlQuiz> filter(String filterConstraint, ArrayList<SrlQuiz> originalList) {
                ArrayList<SrlQuiz> filtered = new ArrayList<SrlQuiz>();
                for(int i = 0; i < originalList.size(); i++){
                    SrlQuiz quiz = originalList.get(i);
                    if(quiz.quiz_Title.startsWith(filterConstraint)  || quiz.quiz_Title.matches(filterConstraint)  || quiz.quiz_Title.equalsIgnoreCase(filterConstraint)){
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
            Intent in = new Intent(Admin_Quizzes.this, AddDel_Quiz.class);
            in.putExtra("data", selectedItem);
            startActivity(in);
        }else if(item.getItemId() == R.id.delete){
            Delete();
        }else if(item.getItemId() == R.id.view){
            Intent in = new Intent(Admin_Quizzes.this, ViewQuizDetailed.class);
            in.putExtra("data", selectedItem);
            startActivity(in);
        }
        return super.onContextItemSelected(item);
    }

    public void Delete(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Option");
        builder.setIcon(android.R.drawable.ic_delete);
        builder.setMessage("Are sure you want to delete "+selectedItem.quiz_Title+" in question list of Quizzes?");
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
                postData.put("quiz_id", selectedItem.quiz_Id+"");
                PostResponseAsyncTask task = new PostResponseAsyncTask(Admin_Quizzes.this, postData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        if(s.contains("success")){
                            AsyncLoad();
                            Toast.makeText(Admin_Quizzes.this, "Delete Successful", Toast.LENGTH_SHORT).show();
                        }else if(s.contains("failed")){
                            Toast.makeText(Admin_Quizzes.this, R.string.scripts_error, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Admin_Quizzes.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                        }
                    }
                });task.execute("http://192.168.10.1/CodeWebScripts/remove.php");
                 dialog.dismiss();
            }
        });
        builder.show();
    }

    public void AsyncLoad(){
        HashMap postData = new HashMap();
        postData.put("Quizzes","Quizzes");
        PostResponseAsyncTask loadData = new PostResponseAsyncTask(Admin_Quizzes.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                load = new JsonConverter< SrlQuiz>().toArrayList(s, SrlQuiz.class);
                BindDictionary< SrlQuiz> data = new BindDictionary< SrlQuiz>();

                data.addStringField(R.id.lsn_title, new StringExtractor< SrlQuiz>() {
                    @Override
                    public String getStringValue( SrlQuiz item, int position) {
                        return item.quiz_Title;
                    }
                });

                data.addStringField(R.id.lsn_category, new StringExtractor< SrlQuiz>() {
                    @Override
                    public String getStringValue( SrlQuiz item, int position) {
                        return item.quiz_Type;
                    }
                });

                data.addDynamicImageField(R.id.ic_courses, new StringExtractor< SrlQuiz>() {
                    @Override
                    public String getStringValue( SrlQuiz item, int position) {
                        return item.quiz_Icon;
                    }
                }, new DynamicImageLoader() {
                    @Override
                    public void loadImage(String url, ImageView view) {
                        Picasso.with(Admin_Quizzes.this)
                                .load(url)
                                .placeholder(android.R.drawable.ic_menu_gallery)
                                .error(android.R.drawable.stat_sys_download)
                                .into(view);
                    }
                });
                adp = new FunDapter<>(Admin_Quizzes.this, load, R.layout.slessons_custom, data);
                list.setAdapter(adp);
                initTextFilter(adp);
            }
        });
        loadData.execute("http://192.168.10.1/CodeWebScripts/load.php");
    }
}
