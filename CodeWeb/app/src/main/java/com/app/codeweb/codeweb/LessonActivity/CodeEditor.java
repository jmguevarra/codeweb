package com.app.codeweb.codeweb.LessonActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.app.codeweb.codeweb.Others.CodeWebEditor;
import com.app.codeweb.codeweb.R;

public class CodeEditor extends AppCompatActivity {
    CodeWebEditor editText;
    ImageButton  runBtn, refreshBtn, stopBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lesson_code_editor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        runBtn = (ImageButton) findViewById(R.id.btn_run);
        refreshBtn = (ImageButton) findViewById(R.id.btn_refresh);
        stopBtn = (ImageButton) findViewById(R.id.btn_stop);
        editText = (CodeWebEditor) findViewById(R.id.edit_text);
        editText.requestFocus();
        runBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(!editText.getText().toString().isEmpty()){
                   Intent in = new Intent(CodeEditor.this, RunPanel.class);
                   in.putExtra("code", editText.getText().toString());
                   startActivity(in);
               }else{
                   Toast.makeText(CodeEditor.this, "EditText is Empty", Toast.LENGTH_SHORT).show();
                   editText.requestFocus();
               }
            }
        });

        refreshBtn.setEnabled(false);
        stopBtn.setEnabled(false);

    }
}
