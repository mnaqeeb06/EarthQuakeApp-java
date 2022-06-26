package com.example.earthquakeapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EarthquakeAdapter extends ArrayAdapter<EarthquakeC> {
    private static final String LOCAION_SEPARATOR = " of";

    public EarthquakeAdapter(Context contact, List<EarthquakeC> earthquakes){
        super(contact,0,earthquakes);

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {



        // Check if there is an existing list item view(called convert view) hen we can reuse
        // otherwise, if convertview is null, then inflate a new list item layout
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list_item,parent,false);
        }

        // Find the earthquake at the given position in the list of earthquakes
        EarthquakeC currentEarthquake = getItem(position);

        // Find the textview with view ID magnitude
        TextView magnitudeView = (TextView) listItemView.findViewById(R.id.magnitude);
        // Format the magnitude to show 1 decimal place
        String formattedMagnitude = formatMagnitudeM(currentEarthquake.getMagnitude());
        //Display the magnitude of the current earthquake in the Textview
        magnitudeView.setText(formattedMagnitude);



        //Spliting Original Location into two halfs
        String orignalLocation = currentEarthquake.getLocation();
        String primaryLocation;
        String locationOffset;
        if(orignalLocation.contains(LOCAION_SEPARATOR)){
            String [] parts = orignalLocation.split(LOCAION_SEPARATOR);
            locationOffset = parts[0];
            primaryLocation = parts[1];

        }else{
            locationOffset = getContext().getString(R.string.app_name);
            primaryLocation = orignalLocation;
        }



        // Find the textview with view ID location_offsetTW
        TextView locationOffsetView = (TextView) listItemView.findViewById(R.id.location_offsetTW);
        //Display the half (location_offsetTW) of the current earthquake in the Textview
        locationOffsetView.setText(locationOffset);

        // Find the textview with view ID location_offsetTW
        TextView primaryLocationView = (TextView) listItemView.findViewById(R.id.primary_locationTW);
        //Display the half (location_offsetTW) of the current earthquake in the Textview
        primaryLocationView.setText(primaryLocation);


        // Create a new Date object from the tme in milliseconds of the earthquake
        Date dateObject = new Date(currentEarthquake.getTimeInMilliSeconds());

        /*---For date*---*/
        // Find the textview with view ID date
        TextView dateView = (TextView) listItemView.findViewById(R.id.dateTW);
        // format the date string  (i.e. "Mar 3, 1984") // manually create formatDate
        String formatedDate = formatDate(dateObject);
        //Display the date  of the current earthquake in that textview
        dateView.setText(formatedDate);


        /*---For time----*/
        // Find the textview with view ID time
        TextView timeView = (TextView) listItemView.findViewById(R.id.timeTW);
        // format the date string  (i.e. "Mar 3, 1984") // manually create formatDate
        String formatedTime = formatTime(dateObject);
        //Display the date  of the current earthquake in that textview
        timeView.setText(formatedTime);


        // magnitude color
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();

        // Get appropiate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getMagnitude());
        //set the color on the magnitue circle
        magnitudeCircle.setColor(magnitudeColor);







        //Return the list item view that is now showing the appropriate data
        return listItemView;
    }


    //Return the formatted date string (i.e. "Mar 3, 1984")
    private String formatDate(Date dateObject){
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);

    }

    //Return the formatted time
    private String formatTime(Date dateObject){
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);

    }


    private String formatMagnitudeM(double magnitude){
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    private int getMagnitudeColor(double magnitude){
        int magnitudeColorResourceId;
       int magnituideFloor = (int) Math.floor(magnitude);
        switch (magnituideFloor){
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;


            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;

            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;

            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;

            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;

            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;

            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;

            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;

            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;

            default:
                magnitudeColorResourceId = R.color.magnitude10Plus;
                break;



        }
        return ContextCompat.getColor(getContext(),magnitudeColorResourceId);
    }


}
