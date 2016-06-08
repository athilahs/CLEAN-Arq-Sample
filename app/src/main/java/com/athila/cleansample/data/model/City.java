package com.athila.cleansample.data.model;

import com.athila.cleansample.data.datasource.database.table.CitiesTable;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

import java.util.UUID;

/**
 * Created by athila on 17/03/16.
 */
@StorIOSQLiteType(table = CitiesTable.TABLE_NAME)
public class City {

    // fields and default constructor are in package visibility because of StorIO
    @StorIOSQLiteColumn(name = CitiesTable.UUID, key = true)
    String mUUID;

    @StorIOSQLiteColumn(name = CitiesTable.NAME)
    String mName;

    @StorIOSQLiteColumn(name = CitiesTable.LATITUDE)
    double mLatitude;

    @StorIOSQLiteColumn(name = CitiesTable.LONGITUDE)
    double mLongitude;

    City() {
        mUUID = UUID.randomUUID().toString();
    }

    public City(String name, double latitude, double longitude) {
        this();
        mName = name;
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public String getId() {
        return mUUID;
    }

    public void setId(String id) {
        mUUID = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        City city = (City) o;

        return Double.compare(city.mLatitude, mLatitude) == 0
                && Double.compare(city.mLongitude, mLongitude) == 0
                && (mName != null ? mName.equals(city.mName) : city.mName == null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = mName != null ? mName.hashCode() : 0;
        temp = Double.doubleToLongBits(mLatitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(mLongitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "City{" +
                "mUUID='" + mUUID + '\'' +
                ", mName='" + mName + '\'' +
                ", mLatitude=" + mLatitude +
                ", mLongitude=" + mLongitude +
                "}\n";
    }
}
