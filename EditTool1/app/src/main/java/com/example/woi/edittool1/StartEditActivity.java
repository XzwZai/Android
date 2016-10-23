package com.example.woi.edittool1;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StartEditActivity extends AppCompatActivity {
    public static String SAVE_PATH = "KEY_SAVE_PATH";
    public static  final String apppath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/down/";
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_edit);

    }


    public void startedit(View view) {
        Intent intent = new Intent(this,EditTextActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(SAVE_PATH,apppath);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
