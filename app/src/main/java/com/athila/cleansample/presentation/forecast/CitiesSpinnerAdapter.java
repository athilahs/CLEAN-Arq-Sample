package com.athila.cleansample.presentation.forecast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.athila.cleansample.R;
import com.athila.cleansample.data.model.City;

import java.util.List;

/**
 * Created by athila on 18/03/16.
 */
public class CitiesSpinnerAdapter extends ArrayAdapter<City> {

    private List<City> mCities;

    public CitiesSpinnerAdapter(Context context, List<City> cities) {
        super(context, R.layout.cities_spinner_row, cities);
        mCities = cities;
    }

    public void setCities(List<City> cities) {
        mCities.clear();
        mCities.addAll(cities);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        City city = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.cities_spinner_row, parent, false);
            viewHolder.cityName = (TextView) convertView.findViewById(R.id.cities_spinner_city_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.cityName.setText(city.getName());
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    private static class ViewHolder {
        TextView cityName;
    }
}
