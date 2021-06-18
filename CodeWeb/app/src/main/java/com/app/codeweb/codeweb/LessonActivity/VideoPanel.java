package com.app.codeweb.codeweb.LessonActivity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.MediaController;
import android.widget.VideoView;

import com.app.codeweb.codeweb.Others.Serialized.SrlLesson;
import com.app.codeweb.codeweb.R;

public class VideoPanel extends AppCompatActivity {
    SrlLesson data;
    VideoView  videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lesson_video);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

         videoView = (VideoView) findViewById(R.id.videoView);

        if(getIntent().getSerializableExtra("data") != null ) {
            data = (SrlLesson) getIntent().getSerializableExtra("data");
            getSupportActionBar().setTitle(data.lsn_title);
            StreamVideo();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void StreamVideo(){
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        Uri uri = Uri.parse(data.lsn_video);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();
    }
}
