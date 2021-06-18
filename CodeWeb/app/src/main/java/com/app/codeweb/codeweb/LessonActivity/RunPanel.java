package com.app.codeweb.codeweb.LessonActivity;

import android.annotation.SuppressLint;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.app.codeweb.codeweb.R;

public class RunPanel extends AppCompatActivity {
    WebView webView;
    ImageButton runBtn, refreshBtn, stopBtn;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lesson_coderun_panel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Test Panel");

        runBtn = (ImageButton) findViewById(R.id.btn_run);
        refreshBtn = (ImageButton) findViewById(R.id.btn_refresh);
        stopBtn = (ImageButton) findViewById(R.id.btn_stop);
        webView = (WebView) findViewById(R.id.webview);

        if(getIntent().getExtras().getString("code") != null){
            String code = getIntent().getExtras().getString("code");
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setNeedInitialFocus(true);
            //webView.getSettings().setUseWideViewPort(true);
            webView.loadData(code, "text/html", "UTF-8");


            runBtn.setEnabled(false);
            refreshBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RunPanel.super.recreate();
                    Toast.makeText(RunPanel.this, "Refreshed", Toast.LENGTH_SHORT).show();
                }
            });
            stopBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavUtils.navigateUpFromSameTask(RunPanel.this);
                }
            });

        }else{
            Toast.makeText(this, R.string.went_wrong, Toast.LENGTH_SHORT).show();
        }



    }
}
