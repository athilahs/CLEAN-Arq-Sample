package com.athila.cleansample.presentation.citieslist;

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
public class CitiesListAdapter extends ArrayAdapter<City> {

    private List<City> mCities;
    private Context mContext;

    public CitiesListAdapter(Context context, List<City> cities) {
        super(context, R.layout.cities_list_row, cities);
        mCities = cities;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        City city = getItem(position);
        CitiesListViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new CitiesListViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.cities_list_row, parent, false);
            viewHolder.cityName = (TextView) convertView.findViewById(R.id.cities_list_item_city_name);
            viewHolder.cityCoordinates = (TextView) convertView.findViewById(R.id.cities_list_item_city_coordinates);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CitiesListViewHolder) convertView.getTag();
        }

        viewHolder.cityName.setText(city.getName());
        viewHolder.cityCoordinates.setText(mContext.getString(R.string.coordinates_representation,
                city.getLatitude(), city.getLongitude()));

        return convertView;
    }

    public void setCities(List<City> cities) {
        mCities.clear();
        mCities.addAll(cities);
    }

    public static class CitiesListViewHolder {
        TextView cityName;
        TextView cityCoordinates;
    }
}
