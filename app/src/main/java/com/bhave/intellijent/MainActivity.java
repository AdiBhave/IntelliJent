package com.bhave.intellijent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void listSongs(View view){
        Intent i = new Intent(this,SongList.class);
        startActivity(i);
    }

    public void songRecs(View view){
        Intent i = new Intent(this,Recommendations.class);
        startActivity(i);

    }

    public void about(View view){

    }
}
