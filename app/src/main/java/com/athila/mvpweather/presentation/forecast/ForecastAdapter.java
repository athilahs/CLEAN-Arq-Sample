package com.athila.mvpweather.presentation.forecast;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.athila.mvpweather.R;
import com.athila.mvpweather.data.model.DataPoint;
import com.athila.mvpweather.data.model.Forecast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by athila on 18/03/16.
 */
public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {

    private Forecast mForecast;
    private Context mContext;

    public ForecastAdapter(Context context) {
        mContext = context;
    }
    @Override
    public ForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_row, parent, false);
        return new ForecastViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ForecastViewHolder holder, int position) {
        DataPoint forecastItem = mForecast.getDailyForecasts().get(position);
        if (forecastItem != null) {
            // Get the day from timestamp
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(mForecast.getTimezone()));
            // API returns time in seconds
            calendar.setTimeInMillis(forecastItem.getTime() * 1000);
            String dayOfWeek = new SimpleDateFormat("EEE", Locale.getDefault()).format(calendar.getTime());
            holder.forecastItemDay.setText(dayOfWeek);
            holder.forecastItemIcon.setImageDrawable(ContextCompat.getDrawable(mContext, ForecastUtils.getWeatherIcon(forecastItem.getIcon())));
            holder.forecastItemSummary.setText(forecastItem.getSummary());
            holder.forecastItemTemperatureMax.setText(mContext.getString(R.string.temperature, forecastItem.getTemperatureMax()));
            holder.forecastItemTemperatureMin.setText(mContext.getString(R.string.temperature, forecastItem.getTemperatureMin()));
        }
    }

    @Override
    public int getItemCount() {
        if (mForecast == null) {
            return 0;
        }
        return mForecast.getDailyForecasts().size();
    }

    public void setForecast(Forecast forecast) {
        mForecast = forecast;
    }

    public static class ForecastViewHolder extends RecyclerView.ViewHolder {
        public TextView forecastItemDay;
        public TextView forecastItemTemperatureMax;
        public TextView forecastItemTemperatureMin;
        public TextView forecastItemSummary;
        public ImageView forecastItemIcon;

        public ForecastViewHolder(View forecastItemView) {
            super(forecastItemView);
            forecastItemDay = (TextView) forecastItemView.findViewById(R.id.forecast_item_day);
            forecastItemTemperatureMax = (TextView) forecastItemView.findViewById(R.id.forecast_item_temperature_max);
            forecastItemTemperatureMin = (TextView) forecastItemView.findViewById(R.id.forecast_item_temperature_min);
            forecastItemSummary = (TextView) forecastItemView.findViewById(R.id.forecast_item_summary);
            forecastItemIcon = (ImageView) forecastItemView.findViewById(R.id.forecast_item_icon);
        }
    }
}
