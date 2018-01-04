package com.example.amrez.newsappproject;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsObjects>> {

    private TextView mTextMessage;


    private String requestURL = "https://content.guardianapis.com/search?q=";

    private LoaderManager loaderManager;
    private static int BOOK_LOADER_ID = 1;
    private NewsAdapter Adapter;
    private ProgressBar progressBar;
    private TextView mEmptyStateTextView;
    private Button b_search;
    private EditText search;
    private ListView bookListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.emptyNetworkState);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        // find view for widgets
        b_search = (Button) findViewById(R.id.b_search);
        bookListView = (ListView) findViewById(R.id.list);
        search = (EditText) findViewById(R.id.editText);
        //progress Visibility
        progressBar = (ProgressBar) findViewById(R.id.progressPar);
        progressBar.setVisibility(View.GONE);
        //at empty state
        mEmptyStateTextView = (TextView) findViewById(R.id.emptyNetworkState);
        bookListView.setEmptyView(mEmptyStateTextView);

        //add Books to list
        Adapter = new NewsAdapter(this, new ArrayList<NewsObjects>());
        bookListView.setAdapter(Adapter);

        //for network connectivity
        final ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        //check network connectivity
        if (networkInfo != null && networkInfo.isConnected()) {
            //give any data to list
            requestURL = requestURL + "football";
            loaderManager = getLoaderManager();
            loaderManager.initLoader(BOOK_LOADER_ID, null, MainActivity.this);
        } else {
            mEmptyStateTextView.setText(R.string.networkState);
        }

        // Set onClickListener on the b_search button
        b_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userQuery = search.getText().toString();
                //if EditText empty
                if (search.length() == 0) {
                    search.setHint("enter data to search");
                }
                //if EditText not empty
                else {
                    BOOK_LOADER_ID += 1;
                    requestURL = "https://content.guardianapis.com/search?q=";
                    //if network connection
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {
                        //to hide bookListView while searching
                        bookListView.setVisibility(View.GONE);
                        // Remove  spaces from userQuery
                        String userQueryMain = userQuery.replaceAll("\\s+", "");
                        // Update Url request
                        requestURL = requestURL + userQueryMain;
                        // Fetch QueryUtils data from loaderManager
                        loaderManager = getLoaderManager();
                        // Init loader with eny with BOOK_LOADER_ID counter
                        loaderManager.initLoader(BOOK_LOADER_ID, null, MainActivity.this);
                    }
                    //if No connection
                    else {
                        Adapter.clear();
                        // hide progressBar
                        progressBar.setVisibility(View.GONE);
                        //tell user No internet connection
                        mEmptyStateTextView.setText(R.string.networkState);
                    }

                }
            }
        });

        //onclick on any data on list
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NewsObjects currentBook = Adapter.getItem(i);
                Uri bookUri = Uri.parse(currentBook.getPreviewLink());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                // to launch a new activity
                startActivity(websiteIntent);
            }
        });

    }

    @Override
    public Loader<List<NewsObjects>> onCreateLoader(int id, Bundle args) {
        progressBar.setVisibility(View.VISIBLE);
        Uri baseUri = Uri.parse(requestURL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("api-key", "19439b4e-d566-4de1-988a-5a49fb4cca54");
        uriBuilder.appendQueryParameter("show-tags", "contributor");

        return new NewsTask(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsObjects>> loader, List<NewsObjects> data) {
        progressBar.setVisibility(View.GONE);
        mEmptyStateTextView.setText("No News founded");
        Adapter.clear();

        //if have data update list
        if (data != null && !data.isEmpty()) {
            Adapter.addAll(data);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<NewsObjects>> loader) {
        Adapter.clear();
    }

}
