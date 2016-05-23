package com.athila.mvpweather.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by athila on 17/03/16.
 */
public class Forecast {

    // fields are in package visibility because of StorIO (just in case)
    @SerializedName("timezone")
    String mTimezone;

    @SerializedName("latitude")
    double mLatitude;

    @SerializedName("longitude")
    double mLongitude;

    @SerializedName("currently")
    DataPoint mCurrentConditions;

    @SerializedName("daily")
    DailyForecast mDailyForecast;

    // public for tests
    public Forecast() {}

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public DataPoint getCurrentConditions() {
        return mCurrentConditions;
    }

    public String getTimezone() {
        return mTimezone;
    }

    // This will ignore the other properties of DailyForecast object. It they are needed, expose a method to get it
    public List<DataPoint> getDailyForecasts() {
        return mDailyForecast.getDailyForecasts();
    }

    public static class DailyForecast {
        @SerializedName("summary")
        String mSummary;

        @SerializedName("icon")
        String mIcon;

        @SerializedName("data")
        List<DataPoint> mDailyForecasts;

        public String getSummary() {
            return mSummary;
        }

        public String getIcon() {
            return mIcon;
        }

        public List<DataPoint> getDailyForecasts() {
            return mDailyForecasts;
        }
    }
}
