package com.example.earthquakeapp;


import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    private static final String LOG_TAG = MainActivity.class.getName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }




    /*
    * Ruturns new URL object from the given string URL
    * */
    private  static URL createUrl(String stringUrl){

        URL url= null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();

        }
        return  url;

    }

    /*
    *Make an HTTP request to the given URL and return a string as the response.
    */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if(url == null){
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        //InputStream is used for read data from a source,
        //OutputStream for writing data to the destination
        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000); // milliseconds
            urlConnection.setConnectTimeout(15000); // millisecnds
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successfull {response code 200}
            if(urlConnection.getResponseCode()==200){
                inputStream = urlConnection.getInputStream();
                //readFromStream is manually build method
                jsonResponse = readFromStream(inputStream);
            }else{
                Log.e(null,"Error response code "+urlConnection.getResponseCode());
            }

        }catch (IOException e){
            Log.e(null,"Problem in retrieving the earthquake JSON results",e);
        }finally {
            if(urlConnection !=null){
                urlConnection.disconnect();
            }
            if(inputStream !=null){
                // closing the input stream could throw an IOExecption, which is why
                // the makeHttpRequest(URL url) method signatute specifies then an IOEecption
                // could be thrown
                inputStream.close();
            }
        }

        return jsonResponse;

    }


    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if(inputStream!=null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line!=null){
                output.append(line);
                line = reader.readLine();
            }
        }
         return output.toString();

    }








    /**
     * Return a list of {@link EarthquakeC} objects that has been built up from
     * parsing a JSON response.
     */
    private static List<EarthquakeC> extractFeatureFromJson(String earthquakeJSON) {

        // If the JSON string is empty or null, then return early
        if(TextUtils.isEmpty(earthquakeJSON)){
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<EarthquakeC> earthquakeAL2 = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

            //create a JSONObject from the SAMPLE_JSON_RESPONSE string
            JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);

            //Extract the JSONArray associated wih the key called "features"
            //Which represents a list of features (or earthquakes)
            JSONArray earthquakeArray = baseJsonResponse.getJSONArray("features");
            for (int i=0;i<earthquakeArray.length();i++){
                JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);
                JSONObject properties = currentEarthquake.getJSONObject("properties");
                Double magnitude = properties.getDouble("mag");
                String location = properties.getString("place");
               // String time = properties.getString("time");

                //Extract the value for the key called "time"
                long miliSecTime = properties.getLong("time");


                //Extract the value for the key called "url"
                String url = properties.getString("url");

                EarthquakeC earthquakeC_Object = new EarthquakeC(magnitude,location,miliSecTime,url);
                earthquakeAL2.add(earthquakeC_Object);

            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakeAL2;
    }

    //Query the USGS dataset and return the list of {@link Earthquakec} objects
    public static List<EarthquakeC> fetchEarthquakeData(String requesrUrl){


        Log.i(LOG_TAG,"TEST: fetchEarthquakeData() is called...");


        // Create URL object
        URL url=createUrl(requesrUrl);

        //perform HTTP request to the URL and recieve a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(null,"Problem in making the http request",e);
            e.printStackTrace();
        }

        //Extract relevent fielids from the json response and a list of EarthquakeC
        List<EarthquakeC> earthquakesAL = extractFeatureFromJson(jsonResponse);

        //return the list of EarthquakesC
        return earthquakesAL;
    }

}