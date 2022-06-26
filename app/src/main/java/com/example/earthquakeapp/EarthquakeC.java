package com.example.earthquakeapp;

public class EarthquakeC {
    private Double mMagnitude;

    private String mLocation;

    private long mTimeInMilliSeconds;

    private String mURL;

    public EarthquakeC(Double Magnitude, String Location,long TimeInMilliSeconds, String url) {
        mMagnitude = Magnitude;
        mLocation = Location;
        mTimeInMilliSeconds = TimeInMilliSeconds;
        mURL = url;
    }

    public Double getMagnitude() {
        return mMagnitude;
    }

    public String getLocation() {
        return mLocation;
    }

    public long getTimeInMilliSeconds() {
        return mTimeInMilliSeconds;
    }

    public String getURL() {
        return mURL;
    }
}
