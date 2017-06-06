package com.ryanlentz.ritekitandroidchallenge;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;


public class DataLoader extends AsyncTaskLoader<List<String>> {

    private String mUrl;
    private String mDataDesired;

    public DataLoader(Context context, String url, String dataDesired) {
        super(context);
        mUrl = url;
        mDataDesired = dataDesired;
    }

    /**
     * Forces the loader to start loading
     */
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * Completes task on the background thread
     *
     * @return a list of String objects
     */
    @Override
    public List<String> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        return Utils.fetchHashtags(mUrl, mDataDesired);
    }
}
