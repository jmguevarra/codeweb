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
import android.widget.TextView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.amigold.fundapter.interfaces.DynamicImageLoader;
import com.app.codeweb.codeweb.Others.CircleTransform;
import com.app.codeweb.codeweb.Others.MyImageLoader;
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

import static android.app.Activity.RESULT_OK;


public class AdminUsers extends Fragment {
    //effects
    Animation animAlpha;

    //Data
    private ArrayList<SrlStudents> load;
    FunDapter<SrlStudents> adp_admin;
    SrlStudents  adminSelected, user_profile;
    CameraPhoto cameraPhoto;
    GalleryPhoto galleryPhoto;
    final int CAMERA_REQUEST = 13323;
    final int GALLERY_REQUEST = 22131;
    String encode_Image = "";

    //Objects Initialization
    ListView listadmin;
    private  Dialog Add, user ,photo;
    EditText etFullname, etEmail, etNumber, etStud_ID;
    ImageView user_img, iv_camera, iv_gallery;

    public AdminUsers() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_profile, container, false);
        animAlpha = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);

        //Casting Objects
        listadmin = (ListView) view.findViewById(R.id.list_admin);
        cameraPhoto = new CameraPhoto(getActivity());
        galleryPhoto = new GalleryPhoto(getActivity());

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Add();
            }
        });

        AsyncLoad();
        listadmin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                user_profile = load.get(i);
                UserProfile();
            }
        });
        registerForContextMenu(listadmin);
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu stdMenu, View stdView, ContextMenu.ContextMenuInfo stdMenuInfo) {
        super.onCreateContextMenu(stdMenu, stdView, stdMenuInfo);
        MenuInflater StudentsInflater  = getActivity().getMenuInflater();
        StudentsInflater.inflate(R.menu.adminlist, stdMenu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem std_Item) {
        AdapterView.AdapterContextMenuInfo StudentsInfo = (AdapterView.AdapterContextMenuInfo) std_Item.getMenuInfo();

        if(std_Item.getItemId() == R.id.adm_delete){
            adminSelected = adp_admin.getItem(StudentsInfo.position);
            Delete();
        }

        return super.onContextItemSelected(std_Item);
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
                        Bitmap serverBitmap = ImageLoader.init().from(photoPath).requestSize(1024, 1024).getBitmap();
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
                        Bitmap serverBitmap = ImageLoader.init().from(photoPath).requestSize(1024, 1024).getBitmap();
                        encode_Image = ImageBase64.encode(serverBitmap);
                    }else{
                        encode_Image = "";
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

    private void Delete(){
        AlertDialog.Builder adBuilder = new AlertDialog.Builder(getActivity());
        adBuilder.setTitle("Delete Option");
        adBuilder.setMessage("Are sure you want to delete "+adminSelected.Fullname+" in admin section?");
        adBuilder.setPositiveButton("No", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        adBuilder.setNegativeButton("Yes", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HashMap postData = new HashMap();
                postData.put("ID", ""+adminSelected.ID);
                PostResponseAsyncTask deleteTask = new PostResponseAsyncTask(getActivity(), postData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        if(s.contains("success")){
                            Toast.makeText(getActivity(), adminSelected.Fullname+" has been removed ", Toast.LENGTH_SHORT).show();
                            AsyncLoad();
                        }else if(s.contains("failed")){
                            Toast.makeText(getActivity(), R.string.scripts_error, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(), R.string.went_wrong, Toast.LENGTH_SHORT).show();
                        }
                    }
                });deleteTask.execute("http://192.168.10.1/CodeWebScripts/remove.php");
                dialog.dismiss();
            }
        });
        adBuilder.show();
    }

    private void UserProfile(){
        user = new Dialog(getActivity());
        user.setTitle("Admin Profile");
        user.setContentView(R.layout.dialog_admin_detailed);
        user.setCanceledOnTouchOutside(false);

        TextView Fullname = (TextView) user.findViewById(R.id.fullname);
        TextView ID = (TextView) user.findViewById(R.id.id);
        TextView Username = (TextView) user.findViewById(R.id.username);
        TextView Email = (TextView) user.findViewById(R.id.email);
        TextView Number = (TextView) user.findViewById(R.id.contact);
        user_img = (ImageView) user.findViewById(R.id.user_img);

        Fullname.setText(user_profile.Fullname);
        ID.setText(user_profile.Student_ID);
        Username.setText(user_profile.Username);
        Email.setText(user_profile.Email);
        Number.setText(user_profile.Phone_number);
        Picasso.with(getActivity())
                .load(user_profile.Image)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.stat_sys_download)
                .transform(new CircleTransform())
                .fit()
                .into(user_img);
        user.show();
    }



    private void Add(){
        Add = new Dialog(getActivity());
        Add.setTitle("Add Administrator");
        Add.setContentView(R.layout.dialog_add_admin);
        Add.setCanceledOnTouchOutside(false);

        etFullname = (EditText) Add.findViewById(R.id.etFullname);
        etStud_ID = (EditText) Add.findViewById(R.id.etStud_ID);
        etEmail = (EditText) Add.findViewById(R.id.etEmail);
        etNumber = (EditText) Add.findViewById(R.id.etNumber);
        user_img = (ImageView) Add.findViewById(R.id.user_img);

        user_img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                view.startAnimation(animAlpha);
                getPhoto();
                return false;
            }
        });

        Button add = (Button) Add.findViewById(R.id.addbtn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                HashMap postData = new HashMap();
                postData.put("Fullname", etFullname.getText().toString());
                postData.put("Stud_ID", etStud_ID.getText().toString());
                postData.put("Email", etEmail.getText().toString());
                postData.put("Number", etNumber.getText().toString());
                postData.put("Image", encode_Image);
                PostResponseAsyncTask addTask = new PostResponseAsyncTask(getActivity(), postData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        if(s.contains("success")){
                            Add.dismiss();
                            Toast.makeText(getActivity(), "One user has been added as Admin", Toast.LENGTH_SHORT).show();
                            AddAlertMore();
                        }else if(s.contains("failed")){
                            Toast.makeText(getActivity(), R.string.scripts_error, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(), R.string.went_wrong, Toast.LENGTH_SHORT).show();
                        }
                    }
                });addTask.execute("http://192.168.10.1/CodeWebScripts/insert.php");
            }
        });
        encode_Image = "";
        Add.show();
    }

    private void AddAlertMore(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add more users as Admin?");
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
                Add();
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void AsyncLoad() {
        HashMap postData = new HashMap();
        postData.put("type_admin","administrator");
        PostResponseAsyncTask loadData = new PostResponseAsyncTask(this.getActivity(),postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                load = new JsonConverter<SrlStudents>().toArrayList(s, SrlStudents.class);
                BindDictionary<SrlStudents> data = new BindDictionary<SrlStudents>();

                data.addStringField(R.id.yr_sec, new StringExtractor<SrlStudents>() {
                    @Override
                    public String getStringValue(SrlStudents item, int position) {
                        return item.Fullname;
                    }
                });

                data.addStringField(R.id.num_of_Stud, new StringExtractor<SrlStudents>() {
                    @Override
                    public String getStringValue(SrlStudents item, int position) {
                        return item.Student_ID;
                    }
                });

                data.addDynamicImageField(R.id.iv_user_ic, new StringExtractor<SrlStudents>() {
                    @Override
                    public String getStringValue(SrlStudents item, int position) {
                        return item.Image;
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
                adp_admin = new FunDapter<>(getActivity(), load, R.layout.students_custom, data);
                listadmin.setAdapter(adp_admin);
            }
        });
        loadData.execute("http://192.168.10.1/CodeWebScripts/load.php");
    }
}
