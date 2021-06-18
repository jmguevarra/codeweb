package com.app.codeweb.codeweb.Admin_Activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.amigold.fundapter.interfaces.DynamicImageLoader;
import com.app.codeweb.codeweb.Others.CircleTransform;
import com.app.codeweb.codeweb.Others.MyImageLoader;
import com.app.codeweb.codeweb.Admin_Activity.SectionsCom.StudentList;
import com.app.codeweb.codeweb.Others.Serialized.SrlSection;
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

import static android.app.Activity.RESULT_OK;


public class AdminSections extends Fragment{
    ListView list;
    private Dialog addSec, addStudents, photo;
    Animation animAlpha;
    FunDapter<SrlSection> adp;
    private ArrayList<SrlSection> load;
    CameraPhoto cameraPhoto;
    GalleryPhoto galleryPhoto;
    final int CAMERA_REQUEST = 13323;
    final int GALLERY_REQUEST = 22131;
    String encode_Image = "";

    EditText etSection;
    SrlSection selectedSec, Secdata;

    //AddStudentCom
    EditText etFullname,etAddSection, etEmail, etNumber, etStud_ID;
    ImageView user_img, iv_camera, iv_gallery;
    public AdminSections() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sections, container, false);
        animAlpha = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);

        //Casting
        list = (ListView) view.findViewById(R.id.list_stud);
        cameraPhoto = new CameraPhoto(getActivity());
        galleryPhoto = new GalleryPhoto(getActivity());

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddSec();
            }
        });

        AysncLoad();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SrlSection item = load.get(i);
                Intent in = new Intent(getActivity(), StudentList.class);
                in.putExtra("studList", item);
                startActivity(in);
            }
        });
        registerForContextMenu(list);

        return view;

    }

    @Override
    public void onCreateContextMenu(ContextMenu adminMenu, View adminView, ContextMenu.ContextMenuInfo adminMenuInfo) {
        super.onCreateContextMenu(adminMenu, adminView, adminMenuInfo);
        MenuInflater AdminInflater  = getActivity().getMenuInflater();
        AdminInflater.inflate(R.menu.listmenu, adminMenu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem admin_Item) {
        AdapterView.AdapterContextMenuInfo adminInfo = (AdapterView.AdapterContextMenuInfo) admin_Item.getMenuInfo();
            if(admin_Item.getItemId() == R.id.update){
                selectedSec = adp.getItem(adminInfo.position);
                UpdateSec();
            }else if(admin_Item.getItemId() == R.id.delete){
                selectedSec = adp.getItem(adminInfo.position);
                DeleteSec();
            }
                return super.onContextItemSelected(admin_Item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == CAMERA_REQUEST){
                String photoPath = cameraPhoto.getPhotoPath();
                try {
                    Bitmap bitmap = MyImageLoader.init().from(photoPath).getCircleBitmap();
                    user_img.setImageBitmap(bitmap);

                    if(!photoPath.isEmpty()) {
                        Bitmap serverBitmap = ImageLoader.init().from(photoPath).requestSize(1023, 1024).getBitmap();
                        encode_Image = ImageBase64.encode(serverBitmap);
                    }else{
                        encode_Image = "";
                    }
                } catch (FileNotFoundException e) {
                    Toast.makeText(getActivity(), "Something Wrong while loading photos", Toast.LENGTH_SHORT).show();
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
                        Bitmap serverBitmap = ImageLoader.init().from(photoPath).requestSize(1023, 1024).getBitmap();
                        encode_Image = ImageBase64.encode(serverBitmap);
                    }else{
                        encode_Image = "none";
                    }
                } catch (FileNotFoundException e) {
                    Toast.makeText(getActivity(), "Something Wrong while choosing photos", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /*My Method*/

    public void getPhoto(){
        photo = new Dialog(getActivity());
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
                    Toast.makeText(getActivity(), "Something Wrong while taking photos", Toast.LENGTH_SHORT).show();
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

    private void AysncLoad(){
        HashMap postData = new HashMap();
        postData.put("sections","sections");
        PostResponseAsyncTask loadData = new PostResponseAsyncTask(this.getActivity(),postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                load = new JsonConverter<SrlSection>().toArrayList(s, SrlSection.class);
                BindDictionary<SrlSection>  data = new BindDictionary<SrlSection>();

                data.addStringField(R.id.yr_sec, new StringExtractor<SrlSection>() {
                    @Override
                    public String getStringValue(SrlSection item, int position) {
                        return item.yr_sec;
                    }
                });

                data.addStringField(R.id.num_of_Stud, new StringExtractor<SrlSection>() {
                    @Override
                    public String getStringValue(SrlSection item, int position) {
                        return "Students";
                    }
                });

                data.addDynamicImageField(R.id.iv_user_ic, new StringExtractor<SrlSection>() {
                    @Override
                    public String getStringValue(SrlSection item, int position) {
                        return item.yrsec_icon;
                    }
                }, new DynamicImageLoader() {
                    @Override
                    public void loadImage(String url, ImageView view) {
                        Picasso.with(getActivity())
                                .load(url)
                                .placeholder(android.R.drawable.ic_menu_gallery)
                                .error(android.R.drawable.stat_sys_download)
                                .transform(new CircleTransform())
                                .fit()
                                .into(view);
                    }
                });
                adp = new FunDapter<>(getActivity(), load, R.layout.students_custom, data);
                list.setAdapter(adp);
            }
        });
        loadData.execute("http://192.168.10.1/CodeWebScripts/load.php");
    }

    private void AddSec(){
        addSec = new Dialog(getActivity());
        addSec.setTitle("New Section");
        addSec.setContentView(R.layout.dialog_addsec);
        addSec.setCanceledOnTouchOutside(false);

        //casting
        etSection = (EditText) addSec.findViewById(R.id.etadd);
        etSection.setHint("Add Section");

        //coding
        Button add = (Button) addSec.findViewById(R.id.addbtn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                if(!etSection.getText().toString().isEmpty()){
                    HashMap postData = new HashMap();
                    postData.put("yr_sec", etSection.getText().toString());
                    postData.put("addAlert", "addAlert");
                    PostResponseAsyncTask addTask = new PostResponseAsyncTask(getActivity(), postData, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                           if(s.contains("same")) {
                                Toast.makeText(getActivity(), etSection.getText().toString()+" already existed try another one!", Toast.LENGTH_SHORT).show();
                            }else{
                               AddAlert();
                            }
                        }
                    });addTask.execute("http://192.168.10.1/CodeWebScripts/alert.php");
                }else{
                    // etSection.setTextColor(getResources().getColor(R.color.cpb_error_state_selector));
                    Toast.makeText(getActivity(), "TextBox is Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addSec.show();
    }

    private void AddAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Alert");
        builder.setMessage("To add this "+etSection.getText().toString()+" in section list, you need to add atleast one or more students!.");
        builder.setPositiveButton("Cancel", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Add", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AddStudents();
                dialog.cancel();
            }
        });
        builder.show();

    }
    private void AddAlertMore(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add More Students?");
        builder.setMessage("Just press ADD AGAIN to add more student in section of "+etSection.getText().toString()+".");
        builder.setPositiveButton("No, thanks", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addSec.dismiss();
                HashMap postData = new HashMap();
                postData.put("newSec", etSection.getText().toString());
                PostResponseAsyncTask addTask = new PostResponseAsyncTask(getActivity(), postData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        if(s.contains("success")){
                            Toast.makeText(getActivity(), "Section has been Added", Toast.LENGTH_SHORT).show();
                            AysncLoad();
                        }else if(s.contains("same")) {
                            Toast.makeText(getActivity(), etSection.getText().toString()+" already existed try another one!", Toast.LENGTH_SHORT).show();
                        }else if(s.contains("failed")){
                            Toast.makeText(getActivity(), R.string.scripts_error, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(), R.string.went_wrong, Toast.LENGTH_SHORT).show();
                        }
                    }
                });addTask.execute("http://192.168.10.1/CodeWebScripts/insert.php");
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Add Again", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AddStudents();
                dialog.dismiss();
            }
        });
        builder.show();

    }

    private void AddStudents(){
        addStudents = new Dialog(getActivity());
        addStudents.setTitle("Add Students");
        addStudents.setContentView(R.layout.dialog_addstud);
        addStudents.setCanceledOnTouchOutside(false);

        etFullname = (EditText) addStudents.findViewById(R.id.etFullname);
        etStud_ID = (EditText) addStudents.findViewById(R.id.etStud_ID);
        etAddSection = (EditText) addStudents.findViewById(R.id.etSection);
        etEmail = (EditText) addStudents.findViewById(R.id.etEmail);
        etNumber = (EditText) addStudents.findViewById(R.id.etNumber);
        user_img = (ImageView) addStudents.findViewById(R.id.user_img);

        etAddSection.setText(etSection.getText().toString());
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
                    Toast.makeText(getActivity(), "Make sure that there's no empty boxes. You need to fill it all!", Toast.LENGTH_SHORT).show();
                }else{
                    HashMap postData = new HashMap();
                    postData.put("Fullname", etFullname.getText().toString());
                    postData.put("Stud_ID", etStud_ID.getText().toString());
                    postData.put("Section", etAddSection.getText().toString());
                    postData.put("Email", etEmail.getText().toString());
                    postData.put("Number", etNumber.getText().toString());
                    postData.put("Image", encode_Image);
                    PostResponseAsyncTask addTask = new PostResponseAsyncTask(getActivity(), postData, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                            if(s.contains("success")){
                                addStudents.dismiss();
                                Toast.makeText(getActivity(), "Students has been added", Toast.LENGTH_SHORT).show();
                                AddAlertMore();
                            }else if(s.contains("failed")){
                                Toast.makeText(getActivity(), R.string.scripts_error, Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity(), R.string.went_wrong, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });addTask.execute("http://192.168.10.1/CodeWebScripts/insert.php");
                }

            }

        });
        encode_Image = "";
        addStudents.show();
    }

    private void UpdateSec(){
        addSec = new Dialog(getActivity());
        addSec.setTitle("Update Option");
        addSec.setContentView(R.layout.dialog_addsec);
        addSec.setCanceledOnTouchOutside(false);

        //casting
        etSection = (EditText) addSec.findViewById(R.id.etadd);
        etSection.setHint("Update Section");
        etSection.setText(selectedSec.yr_sec);

        //coding
        Button add = (Button) addSec.findViewById(R.id.addbtn);
        add.setText("Update");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                if(!etSection.getText().toString().isEmpty()){
                    updateAlert();
                }else{
                    Toast.makeText(getActivity(), "TextBox is Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        addSec.show();
    }

    private void updateAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Update Alert");
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setMessage("Are you sure you want to change "+selectedSec.yr_sec+" to "+etSection.getText().toString()+"?");
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
                postData.put("newSec", etSection.getText().toString());
                postData.put("slct_sec", selectedSec.yr_sec);
                postData.put("sec_id", ""+selectedSec.sec_id);
                PostResponseAsyncTask updateTask = new PostResponseAsyncTask(getActivity(), postData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        if(s.contains("success")){
                            addSec.dismiss();
                            Toast.makeText(getActivity(), "Update Successfully", Toast.LENGTH_SHORT).show();
                            AysncLoad();
                        }else if(s.contains("same")) {
                            Toast.makeText(getActivity(), etSection.getText().toString()+" already existed try another one!", Toast.LENGTH_SHORT).show();
                        }else if(s.contains("failed")){
                            Toast.makeText(getActivity(), R.string.scripts_error, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(), R.string.went_wrong, Toast.LENGTH_SHORT).show();
                        }
                    }
                });updateTask.execute("http://192.168.10.1/CodeWebScripts/update.php");
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void DeleteSec(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete Option");
        builder.setIcon(android.R.drawable.ic_delete);
        builder.setMessage("Are sure you want to delete "+selectedSec.yr_sec+", the entire section?");
        builder.setPositiveButton("No", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Yes", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                VrfyDelete();
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void VrfyDelete(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Terms of Condition");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage("The students data in this section of "+selectedSec.yr_sec+" will be deleted and we're not Obligated in any risk if you press OK. Otherwise you can press CANCEL to avoid risk.");
        builder.setPositiveButton("Cancel", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNegativeButton("OK, I understand ", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HashMap postData = new HashMap();
                postData.put("sec_id", ""+selectedSec.sec_id);
                postData.put("yr_sec", selectedSec.yr_sec);
                PostResponseAsyncTask deleteTask = new PostResponseAsyncTask(getActivity(), postData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        if(s.contains("success")){
                            Toast.makeText(getActivity(), "Section Deleted", Toast.LENGTH_SHORT).show();
                            AysncLoad();
                        }else if(s.contains("failed")){
                            Toast.makeText(getActivity(), R.string.scripts_error, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(), R.string.went_wrong, Toast.LENGTH_SHORT).show();
                        }
                    }
                });deleteTask.execute("http://192.168.10.1/CodeWebScripts/remove.php");
            }
        });
        builder.show();
    }


}
