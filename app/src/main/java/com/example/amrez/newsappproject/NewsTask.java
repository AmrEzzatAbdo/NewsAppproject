package com.example.amrez.newsappproject;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by amrez on 10/20/2017.
 */

public class NewsTask extends AsyncTaskLoader<List<NewsObjects>> {

    private String mUrl;

    public NewsTask(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<NewsObjects> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // extract list books
        List<NewsObjects> books = QueryUtils.fetchNewsData(mUrl);
        return books;
    }
}
