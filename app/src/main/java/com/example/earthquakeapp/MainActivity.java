package com.example.earthquakeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<EarthquakeC>> {
     private static final String LOG_TAG = MainActivity.class.getName();
     private static final int EARTHQUAKE_LOADER_ID = 1;
     private TextView mEmptyStateTextView;
     private ProgressBar mProgressBar;



    //API link
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=6&limit=10";
    private EarthquakeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // log massaging, afterwords remove these massages
        Log.i(LOG_TAG,"TEST: Earthquake Activity onCreate() is called");

        //find referance of the ListView in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        //create a new adapter that take the list of eartquakes as input
        mAdapter = new EarthquakeAdapter(this, new ArrayList<EarthquakeC>());
        earthquakeListView.setAdapter(mAdapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                //Find the current earthquake that was clicked on
                EarthquakeC currentEarthquake = mAdapter.getItem(position);

                // Convert the string URL  into URI object (to pass into he inten constructor)
                Uri earthquakeUri = Uri.parse(currentEarthquake.getURL());

                // Create a new intent to view the earthquake URI                                                                                        e
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);

            }
        });


        mEmptyStateTextView =(TextView) findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(mEmptyStateTextView);


        // Get a reference to he LoaderManager, in order to intract with loaders

        // log massaging, afterwords remove these massages
        Log.i(LOG_TAG,"TEST: calling initloader()...");

        // Get a reference to the ConnectivityManager to check state of network connectivity,
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection , fetch data
        if(networkInfo !=null && networkInfo.isConnected()){
            // Get a reference to the LoaderManager , in order to interect with loaders
            LoaderManager loaderManager = getLoaderManager();
            //Initilize the loader, pass in the int ID constant defined above and pass in null for
            //the bundle. pass in this activity for the LoaderCallback parameter (which is valid
            //beacuse this activity implements the LoaderCallbacks interface)
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID,null,this);
        }else{
            // otherwise, display error
            // First, hide loader indicator so error massage will be visible
            View loadingIndicator = findViewById(R.id.loading_spinner);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error massage
            mEmptyStateTextView.setText(R.string.no_internet);
        }



    }

    /*
    * Constan value for the earthquake loader ID, we can choose any integer
    * This realy only comes into play if you're using multiple loaders
    * */


    @NonNull
    @Override
    public Loader<List<EarthquakeC>> onCreateLoader(int id, Bundle bundle) {

        // log massaging, afterwords remove these massages
        Log.i(LOG_TAG,"TEST: onCreateLoader is called");




        // Create a new loader for the given URL
        return new EarthquakeLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<EarthquakeC>> loader, List<EarthquakeC> earthquakes) {

        // log massaging, afterwords remove these massages
        Log.i(LOG_TAG,"TEST: onLoadFinished() is called");

        // HIde loadingIndicator because the data has been loaded
        View loadingIndicator= findViewById(R.id.loading_spinner);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No earthquakes found"
        mEmptyStateTextView.setText(R.string.no_earthquakes);

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of EarthquakeC , then add them to the adapter's
        // data set. This will trigger the ListView to update
        if (earthquakes != null && !earthquakes.isEmpty()) {
            mAdapter.addAll(earthquakes);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<EarthquakeC>> loader) {
        // log massaging, afterwords remove these massages
        Log.i(LOG_TAG,"TEST: onLoadReset is called");

        // Loader reset, so we can clear out ur eisting data
        mAdapter.clear();
    }


}