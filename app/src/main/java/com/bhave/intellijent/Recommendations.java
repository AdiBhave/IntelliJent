package com.bhave.intellijent;

import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Recommendations extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Song>>{

    private ArrayList<Song> songList;
    private ListView songView;
    private SongAdapter x;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendations);

        songView = (ListView)findViewById(R.id.recList);
        songList = new ArrayList<Song>();




        LoaderManager lm = getLoaderManager();
        lm.initLoader(0, null, this);

    }


    @Override
    public Loader<List<Song>> onCreateLoader(int i, Bundle bundle) {
        return new RecLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Song>> loader, List<Song> songs) {
        x = new SongAdapter(this,songList);
        songView.setAdapter(x);
    }

    @Override
    public void onLoaderReset(Loader<List<Song>> loader) {

    }
}
