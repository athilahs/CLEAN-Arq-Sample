package com.athila.mvpweather.presentation.forecast;

import android.support.annotation.DrawableRes;

import com.athila.mvpweather.R;

/**
 * Created by athila on 18/03/16.
 */
public class ForecastUtils {
    // These values are in compliance with the API
    private static final String CLEAR_DAY = "clear-day";
    private static final String CLEAR_NIGHT = "clear-night";
    private static final String RAIN = "rain";
    private static final String SNOW = "snow";
    private static final String CLOUDY = "cloudy";
    private static final String PARTLY_CLOUDY_DAY = "partly-cloudy-day";
    private static final String PARTLY_CLOUDY_NIGHT = "partly-cloudy-night";

    public static @DrawableRes int getWeatherIcon(String weatherCondition) {
        switch(weatherCondition) {
            case CLEAR_DAY:
            case CLEAR_NIGHT:
                return R.drawable.ic_weather_clear_day;

            case RAIN:
                return R.drawable.ic_weather_rain;

            case SNOW:
                return R.drawable.ic_weather_snow;

            case CLOUDY:
                return R.drawable.ic_weather_cloudy;

            case PARTLY_CLOUDY_DAY:
            case PARTLY_CLOUDY_NIGHT:
                return R.drawable.ic_weather_partly_cloudy;

            default:
                // TODO: get a default icon
                return R.drawable.ic_weather_clear_day;
        }
    }
}
