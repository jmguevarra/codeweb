package com.app.codeweb.codeweb.Admin_Activity.SectionsCom;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.amigold.fundapter.interfaces.DynamicImageLoader;
import com.amigold.fundapter.interfaces.FunDapterFilter;
import com.app.codeweb.codeweb.Others.CircleTransform;
import com.app.codeweb.codeweb.Others.MyImageLoader;
import com.app.codeweb.codeweb.Others.Serialized.SrlStudents;
import com.app.codeweb.codeweb.Others.Serialized.SrlSection;
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

public class StudentList extends AppCompatActivity {

    ListView listStud;
    Animation animAlpha;
    Dialog addStudents, update, photo;
    SearchView srch;

    private ArrayList<SrlStudents> load;
    FunDapter<SrlStudents> adp;
    SrlStudents  selected;
    SrlSection list;
    CameraPhoto cameraPhoto;
    GalleryPhoto galleryPhoto;
    final int CAMERA_REQUEST = 13323;
    final int GALLERY_REQUEST = 22131;
    String encode_Image = "";

    //AddStudentCom
    EditText etFullname,etAddSection, etEmail, etNumber, etStud_ID;
    ImageView user_img, iv_camera, iv_gallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(getIntent().getSerializableExtra("studList")!=null){
            list = (SrlSection) getIntent().getSerializableExtra("studList");
            AsyncLoad();
            getSupportActionBar().setTitle(list.yr_sec);
        }else if(getIntent().getSerializableExtra("section")!=null){
            String sec =  getIntent().getExtras().getString("section");
            SectionLoad(sec);
            getSupportActionBar().setTitle(sec);
        }

        srch = (SearchView) findViewById(R.id.srch);
        srch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                srch.requestFocus();
            }
        });

        //Casting of objects
        listStud = (ListView) findViewById(R.id.list);
        cameraPhoto = new CameraPhoto(getApplicationContext());
        galleryPhoto = new GalleryPhoto(getApplicationContext());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddStudents();
            }
        });

        listStud.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SrlStudents item = load.get(i);
                Intent in = new Intent(StudentList.this, StudentDetail.class);
                in.putExtra("data", item);
                startActivity(in);
            }
        });
        registerForContextMenu(listStud);
    }

    public void initTextFilter(final FunDapter<SrlStudents> adapter){
        adapter.initFilter(new FunDapterFilter<SrlStudents>() {
            @Override
            public ArrayList<SrlStudents> filter(String filterConstraint, ArrayList<SrlStudents> originalList) {
                ArrayList<SrlStudents> filtered = new ArrayList<SrlStudents>();
                for(int i = 0; i < originalList.size(); i++){
                    SrlStudents quiz = originalList.get(i);
                    String lowerCase = filterConstraint.toLowerCase();
                    if(quiz.Fullname.toLowerCase().startsWith(lowerCase) || quiz.Fullname.toLowerCase().matches(lowerCase) ){
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
        if(getIntent().getSerializableExtra("studList")!=null){
           super.onBackPressed();
        }else if(getIntent().getSerializableExtra("section")!=null){

        }
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
        selected = adp.getItem(info.position);
        if(item.getItemId() == R.id.update){
            UpdateSec();
        }else if(item.getItemId() == R.id.delete){
            DeleteSec();
        }else if(item.getItemId() == R.id.view){
            Intent in = new Intent(StudentList.this, StudentDetail.class);
            in.putExtra("data", selected);
            startActivity(in);
        }

        return super.onContextItemSelected(item);
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


    /*My Methods*/

    public void getPhoto(){
        photo = new Dialog(StudentList.this);
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


    public void UpdateSec(){
        update = new Dialog(StudentList.this);
        update.setTitle("Edit Info");
        update.setContentView(R.layout.dialog_addstud);
        update.setCanceledOnTouchOutside(false);

        etFullname = (EditText) update.findViewById(R.id.etFullname);
        etStud_ID = (EditText) update.findViewById(R.id.etStud_ID);
        etAddSection = (EditText) update.findViewById(R.id.etSection);
        etEmail = (EditText) update.findViewById(R.id.etEmail);
        etNumber = (EditText) update.findViewById(R.id.etNumber);
        user_img = (ImageView) update.findViewById(R.id.user_img);

        etFullname.setText(selected.Fullname);
        etStud_ID.setText(selected.Student_ID);
        etStud_ID.setEnabled(false);
        etAddSection.setText(selected.Section);
        etAddSection.setEnabled(false);
        etEmail.setText(selected.Email);
        etNumber.setText(selected.Phone_number);
        Picasso.with(StudentList.this)
                .load(selected.Image)
                .transform(new CircleTransform())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.stat_sys_download)
                .into(user_img);

        user_img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                view.startAnimation(animAlpha);
                getPhoto();
                return false;
            }
        });

        Button add = (Button) update.findViewById(R.id.addbtn);
        add.setText("Update");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                HashMap postData = new HashMap();
                postData.put("ID", ""+selected.ID);
                postData.put("Fullname", etFullname.getText().toString());
                postData.put("Section", etAddSection.getText().toString());
                postData.put("Email", etEmail.getText().toString());
                postData.put("Number", etNumber.getText().toString());
                postData.put("Image", encode_Image);

                PostResponseAsyncTask updateTask = new PostResponseAsyncTask(StudentList.this, postData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        if(s.contains("success")){
                            update.dismiss();
                            Toast.makeText(StudentList.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                            AsyncLoad();
                        }else if(s.contains("failed")){
                            Toast.makeText(StudentList.this, R.string.scripts_error, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(StudentList.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                        }
                    }
                });updateTask.execute("http://192.168.10.1/CodeWebScripts/update.php");

            }
        });
        encode_Image = "";
        update.show();
    }

    //delete
    public void DeleteSec(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Option");
        builder.setMessage("Are sure you want to delete "+selected.Fullname+" in section list of "+list.yr_sec+"?");
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
                postData.put("ID", ""+selected.ID);
                PostResponseAsyncTask deleteTask = new PostResponseAsyncTask(StudentList.this, postData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        if(s.contains("success")){
                            Toast.makeText(StudentList.this, selected.Fullname+" has been removed ", Toast.LENGTH_SHORT).show();
                            AsyncLoad();
                        }else if(s.contains("failed")){
                            Toast.makeText(StudentList.this, R.string.scripts_error, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(StudentList.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                        }
                    }
                });deleteTask.execute("http://192.168.10.1/CodeWebScripts/remove.php");
                dialog.dismiss();
            }
        });
        builder.show();
    }

    //Add item
    public void AddStudents(){
        addStudents = new Dialog(this);
        addStudents.setTitle("Add Students");
        addStudents.setContentView(R.layout.dialog_addstud);
        addStudents.setCanceledOnTouchOutside(false);

        etFullname = (EditText) addStudents.findViewById(R.id.etFullname);
        etStud_ID = (EditText) addStudents.findViewById(R.id.etStud_ID);
        etAddSection = (EditText) addStudents.findViewById(R.id.etSection);
        etEmail = (EditText) addStudents.findViewById(R.id.etEmail);
        etNumber = (EditText) addStudents.findViewById(R.id.etNumber);
        user_img = (ImageView) addStudents.findViewById(R.id.user_img);

        etAddSection.setText(list.yr_sec);
        etAddSection.setEnabled(false);

        user_img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                view.startAnimation(animAlpha);
                getPhoto();
                return false;
            }
        });

        Button add = (Button) addStudents.findViewById(R.id.addbtn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                if(etFullname.getText().toString().isEmpty()|| etNumber.getText().toString().isEmpty()||etEmail.getText().toString().isEmpty()|| etAddSection.getText().toString().isEmpty() || etStud_ID.getText().toString().isEmpty() ){
                    Toast.makeText(StudentList.this, "Make sure that there's no empty boxes. You need to fill it all!", Toast.LENGTH_SHORT).show();
                }else {
                    HashMap postData = new HashMap();
                    postData.put("Fullname", etFullname.getText().toString());
                    postData.put("Stud_ID", etStud_ID.getText().toString());
                    postData.put("Section", etAddSection.getText().toString());
                    postData.put("Email", etEmail.getText().toString());
                    postData.put("Number", etNumber.getText().toString());
                    postData.put("Image", encode_Image);
                    PostResponseAsyncTask addTask = new PostResponseAsyncTask(StudentList.this, postData, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                            if (s.contains("success")) {
                                addStudents.dismiss();
                                Toast.makeText(StudentList.this, "Students has been added", Toast.LENGTH_SHORT).show();
                                AddAlertMore();
                            } else if (s.contains("failed")) {
                                Toast.makeText(StudentList.this, R.string.scripts_error, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(StudentList.this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    addTask.execute("http://192.168.10.1/CodeWebScripts/insert.php");
                }
            }
        });
        encode_Image = "";
        addStudents.show();
    }

    public void AddAlertMore(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add More Students in "+list.yr_sec+"?");
        builder.setMessage("Just press Add");
        builder.setPositiveButton("No, thanks", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               dialog.dismiss();
                AsyncLoad();
            }
        });
        builder.setNegativeButton("Add", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AddStudents();
                dialog.dismiss();
            }
        });
        builder.show();

    }

    //Async Load
    public void AsyncLoad(){
        HashMap postData = new HashMap();
        postData.put("list", list.yr_sec);
        PostResponseAsyncTask listLoad = new PostResponseAsyncTask(StudentList.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                load = new JsonConverter<SrlStudents>().toArrayList(s, SrlStudents.class);
                BindDictionary<SrlStudents> d = new BindDictionary<>();

                d.addStringField(R.id.yr_sec, new StringExtractor<SrlStudents>() {
                    @Override
                    public String getStringValue(SrlStudents item, int position) {
                        return item.Fullname;
                    }
                });

                d.addStringField(R.id.num_of_Stud, new StringExtractor<SrlStudents>() {
                    @Override
                    public String getStringValue(SrlStudents item, int position) {
                        return item.Student_ID;
                    }
                });

                d.addDynamicImageField(R.id.iv_user_ic, new StringExtractor<SrlStudents>() {
                    @Override
                    public String getStringValue(SrlStudents item, int position) {
                        return item.Image;
                    }
                }, new DynamicImageLoader() {
                    @Override
                    public void loadImage(String url, ImageView view) {
                        Picasso.with(StudentList.this)
                                .load(url)
                                .placeholder(android.R.drawable.ic_menu_gallery)
                                .error(android.R.drawable.stat_sys_download)
                                .transform(new CircleTransform())
                                .fit()
                                .into(view);
                    }
                });

                adp = new FunDapter<>(StudentList.this, load, R.layout.students_custom, d);
                listStud.setAdapter(adp);
                initTextFilter(adp);
            }
        });
        listLoad.execute("http://192.168.10.1/CodeWebScripts/load.php");
    }

    public void SectionLoad(String Section){
        HashMap postData = new HashMap();
        postData.put("list", Section);
        PostResponseAsyncTask listLoad = new PostResponseAsyncTask(StudentList.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                load = new JsonConverter<SrlStudents>().toArrayList(s, SrlStudents.class);
                BindDictionary<SrlStudents> d = new BindDictionary<>();

                d.addStringField(R.id.yr_sec, new StringExtractor<SrlStudents>() {
                    @Override
                    public String getStringValue(SrlStudents item, int position) {
                        return item.Fullname;
                    }
                });

                d.addStringField(R.id.num_of_Stud, new StringExtractor<SrlStudents>() {
                    @Override
                    public String getStringValue(SrlStudents item, int position) {
                        return item.Student_ID;
                    }
                });

                d.addDynamicImageField(R.id.iv_user_ic, new StringExtractor<SrlStudents>() {
                    @Override
                    public String getStringValue(SrlStudents item, int position) {
                        return item.Image;
                    }
                }, new DynamicImageLoader() {
                    @Override
                    public void loadImage(String url, ImageView view) {
                        Picasso.with(StudentList.this)
                                .load(url)
                                .placeholder(android.R.drawable.ic_menu_gallery)
                                .error(android.R.drawable.stat_sys_download)
                                .transform(new CircleTransform())
                                .fit()
                                .into(view);
                    }
                });

                adp = new FunDapter<>(StudentList.this, load, R.layout.students_custom, d);
                listStud.setAdapter(adp);
            }
        });
        listLoad.execute("http://192.168.10.1/CodeWebScripts/load.php");
    }

}
