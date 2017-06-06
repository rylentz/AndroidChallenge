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

public class InfluencersActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<String>>{

    /**
     * Constant ID for the DataLoader.
     */
    private static final int LOADER_ID = 2;

    /**
     * Base URL to query for influencers
     */
    private static final String BASE_INFLUENCERS_URL = "https://api.ritekit.com/v1/stats/influencers/";

    /**
     * ID to append to base URL
     */
    private static final String APPEND_CLIENT_ID = "?client_id=1578fa840b1df1b2ea56c49a1043c68e87c1e8450ce6";

    /**
     * ProgressBar to let the user know that data is loading
     */
    private ProgressBar mProgressSpinner;

    /**
     * TextView that is displayed when the list is empty
     */

    private TextView mEmptyStateTextView;

    /**
     * ArrayAdapter for the list of influencers
     */
    private ArrayAdapter<String> mArrayAdapter;

    /**
     * Variable to store the hashtag from the intent that started this activity
     */
    private String mHashtag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_influencers);

        // Gets the intent that started this activity
        Intent intent = getIntent();

        // Gets the hashtag that was clicked on in the parent activity
        mHashtag = intent.getStringExtra("hashtag");

        // Sets title to reflect current activity
        setTitle(getString(R.string.influencers));

        // Finds a reference to the ProgressBar in the layout
        mProgressSpinner = (ProgressBar) findViewById(R.id.influencers_progress_spinner);

        // Finds a reference to the ListView in the layout
        ListView influencersListView = (ListView) findViewById(R.id.influencers_list_view);

        // Finds a reference to the TextView in the layout
        mEmptyStateTextView = (TextView) findViewById(R.id.influencers_empty_view);

        // Calls setEmptyView on the influencersListView
        influencersListView.setEmptyView(mEmptyStateTextView);

        ArrayList<String> influencers = new ArrayList<>();

        // Creates an ArrayAdapter for the influencersListView
        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, influencers);

        // Sets the adapter on the influencersListView
        influencersListView.setAdapter(mArrayAdapter);

        // Sets an onItemClickListener on the ListView
        influencersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Displays a toast letting the user know that there is no point in clicking
                Toast.makeText(InfluencersActivity.this, R.string.unclickable, Toast.LENGTH_SHORT).show();
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

            // Initializes the loader. Passes in the int ID constant defined above and pass in null for
            // the bundle. Passes in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
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
        // Creates the URL string to query for the hashtags influencers
        final String influencersUrl = BASE_INFLUENCERS_URL + mHashtag + APPEND_CLIENT_ID;

        // Returns a DataLoader for the given URL string and datatype
        return new DataLoader(this, influencersUrl, "influencers");
    }

    @Override
    public void onLoadFinished(Loader<List<String>> loader, List<String> data) {
        // Sets the ProgressBar to GONE when loading has finished
        mProgressSpinner.setVisibility(View.GONE);

        // Sets empty state text to inform the user that now influencers were found
        mEmptyStateTextView.setText(R.string.no_influencers);

        // Clears the adapter of previous news data
        mArrayAdapter.clear();

        // If there is a valid list, then adds it to the adapter's
        // data set
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
