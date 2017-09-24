package com.bhave.intellijent;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by USER on 24-09-2017.
 */

public class RecLoader extends AsyncTaskLoader<List<Song>> {

    public RecLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {forceLoad();}

    @Override
    public List<Song> loadInBackground() {
        List<Song> list = RecExtractor.extractData();
        return list;
    }
}
