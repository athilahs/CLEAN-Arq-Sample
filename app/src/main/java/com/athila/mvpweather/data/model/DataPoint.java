package com.athila.mvpweather.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by athila on 17/03/16.
 */
public class DataPoint {

    // fields and default constructor are in package visibility because of StorIO (just in case)
    @SerializedName("time")
    long mTime;

    @SerializedName("summary")
    String mSummary;

    @SerializedName("icon")
    String mIcon;

    @SerializedName("temperatureMin")
    double mTemperatureMin;

    @SerializedName("temperatureMax")
    double mTemperatureMax;

    // only for current conditions
    @SerializedName("temperature")
    double mTemperature;

    public double getTemperatureMax() {
        return mTemperatureMax;
    }

    public double getTemperatureMin() {
        return mTemperatureMin;
    }

    public String getIcon() {
        return mIcon;
    }

    public String getSummary() {
        return mSummary;
    }

    public long getTime() {
        return mTime;
    }

    public double getTemperature() {
        return mTemperature;
    }

    DataPoint() {}


}
