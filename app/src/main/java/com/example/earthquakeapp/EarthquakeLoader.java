package com.example.earthquakeapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;
/*
* Loads a list of earthquakes by using an AsyncTask to perform the
* network request to given URL*/

public class EarthquakeLoader extends AsyncTaskLoader<List<EarthquakeC>> {


    // Tag for log massage
private static final String LOG_TAG = EarthquakeLoader.class.getName();

//Query URL
    private  String mURL;

    public EarthquakeLoader(Context context, String url) {
        super(context);
        mURL = url;
    }

    @Override
    protected void onStartLoading(){
        // log massaging, afterwords remove these massages
        Log.i(LOG_TAG,"TEST: onStartLoading is called");
        forceLoad();
    }

    //this is on background thread

    @Override
    public List<EarthquakeC> loadInBackground() {

        // log massaging, afterwords remove these massages
        Log.i(LOG_TAG,"TEST: loadInBackground is called");


        if (mURL==null){
            return null;
        }

        //perform the network request , parse the response, and extract a list of earthquakes
        List<EarthquakeC> earthquakeAL = QueryUtils.fetchEarthquakeData(mURL);
        return earthquakeAL;
    }
}
