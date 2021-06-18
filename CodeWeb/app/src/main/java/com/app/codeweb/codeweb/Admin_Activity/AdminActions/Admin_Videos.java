package com.app.codeweb.codeweb.Admin_Activity.AdminActions;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.amigold.fundapter.interfaces.DynamicImageLoader;
import com.app.codeweb.codeweb.Admin_Activity.AdminActions.ActionsCom.AddVideo;
import com.app.codeweb.codeweb.LessonActivity.VideoPanel;
import com.app.codeweb.codeweb.Others.MyImageLoader;
import com.app.codeweb.codeweb.Others.ResultObject;
import com.app.codeweb.codeweb.Others.Serialized.SrlLesson;
import com.app.codeweb.codeweb.Others.Serialized.Srl_CrsCat;
import com.app.codeweb.codeweb.Others.ThumbnailAdapter;
import com.app.codeweb.codeweb.Others.Upload;
import com.app.codeweb.codeweb.Others.VideoInterface;
import com.app.codeweb.codeweb.Others.VideoThumbnail;
import com.app.codeweb.codeweb.R;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.ImageLoader;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Admin_Videos extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{
    GridView gridview;
    SrlLesson item;
    FunDapter<SrlLesson> adp;
    Dialog addDialog;
    public static Animation animAlpha;
    private ArrayList<SrlLesson> load,loadSpLesson;
    Spinner spCrs, spCat, spLsn;
    Button chooseFile, upload;
    TextView path;
    ArrayList<Srl_CrsCat> crsLoad, catLoad;
    ArrayList<String> CrsString, CatString, LsnString;
    final int GALLERY_REQUEST = 22131;
    GalleryPhoto galleryPhoto;
    String selectedPath;
    private static final int SELECT_VIDEO =3;
    private static final String TAG = Admin_Videos.class.getSimpleName();
    private static final int REQUEST_VIDEO_CAPTURE = 300;
    private static final int READ_REQUEST_CODE = 200;
    private Uri uri;
    private String pathToStoredVideo;
    private VideoView displayRecordedVideo;
    private static final String SERVER_PATH = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_videos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Videos");
        animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
        galleryPhoto = new GalleryPhoto(Admin_Videos.this);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addVideos();
            }
        });

        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SrlLesson item = load.get(i);
                if(item.lsn_video.isEmpty()){
                    Toast.makeText(Admin_Videos.this, "This video may be deleted from the server", Toast.LENGTH_SHORT).show();
                }else{
                    Intent in = new Intent(Admin_Videos.this, VideoPanel.class);
                    in.putExtra("data", item);
                    startActivity(in);
                }

            }
        });


        AsyncLoad();


    }
    public void AsyncLoad(){
        HashMap postData = new HashMap();
        postData.put("Videos","Videos");
        PostResponseAsyncTask loadData = new PostResponseAsyncTask(Admin_Videos.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                load = new JsonConverter<SrlLesson>().toArrayList(s, SrlLesson.class);
                BindDictionary<SrlLesson> data = new BindDictionary<SrlLesson>();

                data.addStringField(R.id.lessontext, new StringExtractor<SrlLesson>() {
                    @Override
                    public String getStringValue(SrlLesson item, int position) {

                            return item.lsn_title;

                    }
                });
                data.addStringField(R.id.questiontext, new StringExtractor<SrlLesson>() {
                    @Override
                    public String getStringValue(SrlLesson item, int position) {

                            return item.lsn_course;

                    }
                });

                adp = new FunDapter<>(Admin_Videos.this, load, R.layout.custom_thumbnail_layout, data);

                gridview.setAdapter(adp);
//                initTextFilter(adp);
            }
        });
        loadData.execute("http://192.168.10.1/CodeWebScripts/load.php");
    }

    public void addVideos(){
        addDialog = new Dialog(this);
        addDialog.setTitle("Add Video");
        addDialog.setContentView(R.layout.admin_video_action);
        addDialog.setCanceledOnTouchOutside(false);
        addDialog.setCancelable(true);
        addDialog.show();
        displayRecordedVideo = (VideoView)findViewById(R.id.displayRecordedVideo);
        spCrs = (Spinner) addDialog.findViewById(R.id.sp_course);
        spCat = (Spinner) addDialog.findViewById(R.id.sp_category);
        spLsn = (Spinner) addDialog.findViewById(R.id.sp_lesson);
        chooseFile = (Button) addDialog.findViewById(R.id.Files);
        upload = (Button) addDialog.findViewById(R.id.upload);
        //path = (TextView) addDialog.findViewById(R.id.path);

        spLoad();

        chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                Intent in = new Intent();
                in.setType("video/*");
                in.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(in, "Select a Video"), SELECT_VIDEO);

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                uploadVideo();
                AsyncLoad();
            }
        });

    }
    public void spLoad(){
        HashMap postData = new HashMap();
        postData.put("sp_course", "sp_course");
        PostResponseAsyncTask task = new PostResponseAsyncTask(Admin_Videos.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                crsLoad = new JsonConverter<Srl_CrsCat>().toArrayList(s, Srl_CrsCat.class);
                CrsString = new ArrayList<>();
                CrsString.add("Select Course");
                for(Srl_CrsCat d:crsLoad){
                    CrsString.add(d.crs_title);
                }

                ArrayAdapter<String> adp = new ArrayAdapter<String>(Admin_Videos.this, R.layout.spinner_costum, CrsString);
                spCrs.setAdapter(adp);
            }
        });task.execute("http://192.168.10.1/CodeWebScripts/load.php");


        spCrs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final String title = CrsString.get(i);
                if (title.equalsIgnoreCase("Select Course")) {
                    CatString = new ArrayList<>();
                    CatString.add("Select Category");
                    ArrayAdapter<String> adp = new ArrayAdapter<String>(Admin_Videos.this, R.layout.spinner_costum, CatString);
                    spCat.setAdapter(adp);
                } else {
                    HashMap postData = new HashMap();
                    postData.put("category", title);
                    PostResponseAsyncTask load = new PostResponseAsyncTask(Admin_Videos.this, postData, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                            catLoad = new JsonConverter<Srl_CrsCat>().toArrayList(s, Srl_CrsCat.class);
                            CatString = new ArrayList<>();
                            CatString.add("Select Category");
                            for (Srl_CrsCat d : catLoad) {
                                CatString.add(d.cat_fun);
                            }
                            ArrayAdapter<String> adp = new ArrayAdapter<String>(Admin_Videos.this, R.layout.spinner_costum, CatString);
                            spCat.setAdapter(adp);
                        }
                    });
                    load.execute("http://192.168.10.1/CodeWebScripts/load.php");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final String cat = CatString.get(i);
                if (cat.equalsIgnoreCase("Select Category")) {
                    LsnString = new ArrayList<>();
                    LsnString.add("Select Topic");
                    ArrayAdapter<String> adp = new ArrayAdapter<String>(Admin_Videos.this, R.layout.spinner_costum, LsnString);
                    spLsn.setAdapter(adp);
                } else {
                    HashMap postData = new HashMap();
                    postData.put("topic", cat);
                    PostResponseAsyncTask load = new PostResponseAsyncTask(Admin_Videos.this, postData, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                            loadSpLesson = new JsonConverter<SrlLesson>().toArrayList(s, SrlLesson.class);
                            LsnString = new ArrayList<>();
                            LsnString.add("Select Topic");
                            for (SrlLesson d : loadSpLesson) {
                                LsnString.add(d.lsn_title);
                            }
                            ArrayAdapter<String> adp = new ArrayAdapter<String>(Admin_Videos.this, R.layout.spinner_costum, LsnString);
                            spLsn.setAdapter(adp);
                        }
                    });
                    load.execute("http://192.168.10.1/CodeWebScripts/load.php");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == SELECT_VIDEO){
            uri = data.getData();
            if(EasyPermissions.hasPermissions(Admin_Videos.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                displayRecordedVideo.setVideoURI(uri);
                displayRecordedVideo.start();

                pathToStoredVideo = getRealPathFromURIPath(uri, Admin_Videos.this);
                Log.d(TAG, " Video Path " + pathToStoredVideo);
                //Store the video to your server
                uploadVideoToServer(pathToStoredVideo);

            }else{
                EasyPermissions.requestPermissions(Admin_Videos.this, getString(R.string.read_file), READ_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }

    private String getFileDestinationPath(){
        String generatedFilename = String.valueOf(System.currentTimeMillis());
        String filePathEnvironment = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        File directoryFolder = new File(filePathEnvironment + "/video/");
        if(!directoryFolder.exists()){
            directoryFolder.mkdir();
        }
        Log.d(TAG, "Full path " + filePathEnvironment + "/video/" + generatedFilename + ".mp4");
        return filePathEnvironment + "/video/" + generatedFilename + ".mp4";
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, Admin_Videos.this);
    }
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if(uri != null){
            if(EasyPermissions.hasPermissions(Admin_Videos.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                displayRecordedVideo.setVideoURI(uri);
                displayRecordedVideo.start();

                pathToStoredVideo = getRealPathFromURIPath(uri, Admin_Videos.this);
                Log.d(TAG, "Video Path " + pathToStoredVideo);
                //Store the video to your server
                uploadVideoToServer(pathToStoredVideo);

            }
        }
    }
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "User has denied requested permission");
    }

    private void uploadVideoToServer(String pathToVideoFile){
        File videoFile = new File(pathToVideoFile);
        RequestBody videoBody = RequestBody.create(MediaType.parse("video/*"), videoFile);
        MultipartBody.Part vFile = MultipartBody.Part.createFormData("video", videoFile.getName(), videoBody);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_PATH)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        VideoInterface vInterface = retrofit.create(VideoInterface.class);
        Call<ResultObject> serverCom = vInterface.uploadVideoToServer(vFile);
        serverCom.enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                ResultObject result = response.body();
                if(!TextUtils.isEmpty(result.getSuccess())){
                    Toast.makeText(Admin_Videos.this, "Result " + result.getSuccess(), Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Result " + result.getSuccess());
                }
            }
            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                Log.d(TAG, "Error message " + t.getMessage());
            }
        });
    }
    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();

        return path;
    }

    private void uploadVideo() {
        class UploadVideo extends AsyncTask<Void, Void, String> {

            ProgressDialog uploading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                uploading = ProgressDialog.show(Admin_Videos.this, "Uploading File", "Please wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                uploading.dismiss();
                Toast.makeText(Admin_Videos.this, s+"", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                Upload u = new Upload();
                String msg = u.uploadVideo(selectedPath);
                return msg;
            }
        }
        UploadVideo uv = new UploadVideo();
        uv.execute();
    }
}
