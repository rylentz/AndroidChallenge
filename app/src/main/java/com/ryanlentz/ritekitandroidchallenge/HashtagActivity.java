package com.ryanlentz.ritekitandroidchallenge;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HashtagActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<String>>{

    /**
     * Constant ID for the DataLoader.
     */
    private static final int LOADER_ID = 1;

    /**
     * URL to query for hashtags
     */
    private static final String HASHTAG_URL = "https://api.ritekit.com/v1/search/trending/?green=0";

    /**
     * ProgressBar to let the user know that data is loading
     */
    private ProgressBar mProgressSpinner;

    /**
     * TextView that is displayed when the list is empty
     */

    private TextView mEmptyStateTextView;

    /**
     * ArrayAdapter for the list of hashtags
     */
    private ArrayAdapter<String> mArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hashtag);

        // Sets title to reflect current activity
        setTitle(getString(R.string.trending_hashtags));

        // Finds a reference to the ProgressBar in the layout
        mProgressSpinner = (ProgressBar) findViewById(R.id.hashtag_progress_spinner);

        // Finds a reference to the ListView in the layout
        ListView hashtagListView = (ListView) findViewById(R.id.hashtag_list_view);

        // Finds a reference to the TextView in the layout
        mEmptyStateTextView = (TextView) findViewById(R.id.hashtag_empty_view);

        // Calls setEmptyView on the hashtagListView
        hashtagListView.setEmptyView(mEmptyStateTextView);

        ArrayList<String> hashtags = new ArrayList<>();

        // Creates an ArrayAdapter for the hashtagListView
        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, hashtags);

        // Sets the adapter on the hashtagListView
        hashtagListView.setAdapter(mArrayAdapter);

        // Sets an onItemClickListener on the ListView
        hashtagListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Gets the hashtag from the current position
                String currentHashtag = mArrayAdapter.getItem(position);

                // Creates an intent to start the InfluencersActivity
                Intent intent = new Intent(HashtagActivity.this, InfluencersActivity.class);

                // Attaches the current hashtag to the intent
                intent.putExtra("hashtag", currentHashtag);

                // Starts the InfluencersActivity
                startActivity(intent);
            }
        });

        // Gets a reference to the ConnectivityManager, in order to check connectivity
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Uses NetworkInfo to check the connectivity
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        // Sets a boolean based on the connectivity
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        // Check the internet status. If true, initialize the loader, otherwise let the user know
        // that there is no internet.
        if (isConnected) {
            // Gets a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initializes the loader. Passes in the int ID constant defined above and pass in null
            // for the bundle. Passes in this activity for the LoaderCallbacks parameter (which is
            // valid because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(LOADER_ID, null, this);
        } else {
            // Removes the ProgressBar
            mProgressSpinner.setVisibility(View.GONE);

            // Sets the TextView
            mEmptyStateTextView.setText(R.string.no_internet);
        }
    }

    @Override
    public Loader<List<String>> onCreateLoader(int id, Bundle args) {
        // Returns a DataLoader for the given URL string and datatype
        return new DataLoader(this, HASHTAG_URL, "hashtags");
    }

    @Override
    public void onLoadFinished(Loader<List<String>> loader, List<String> data) {
        // Sets the ProgressBar to GONE when loading has finished
        mProgressSpinner.setVisibility(View.GONE);

        // Sets empty state text to inform the user that now hashtags were found
        mEmptyStateTextView.setText(R.string.no_hashtags);

        // Clears the adapter of previous news data
        mArrayAdapter.clear();

        // If there is a valid list, then adds it to the adapter's data set
        if (data != null && !data.isEmpty()) {
            mArrayAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<String>> loader) {
        // Clears out our existing data when loader resets
        mArrayAdapter.clear();
    }
}
