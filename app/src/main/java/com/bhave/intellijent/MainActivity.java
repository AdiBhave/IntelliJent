package com.bhave.intellijent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    private TextView info;

    SharedPreferences sp;
    SharedPreferences.Editor spe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        info = (TextView)findViewById(R.id.info);
        PackageInfo info;

    }


    public void listSongs(View view){
        Intent i = new Intent(this,SongList.class);
        startActivity(i);
    }

    public void songRecs(View view){
        Intent i = new Intent(this,UploadPhotoActivity.class);
        startActivity(i);

    }

}
