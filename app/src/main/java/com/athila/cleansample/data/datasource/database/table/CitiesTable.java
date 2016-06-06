package com.athila.cleansample.data.datasource.database.table;

public interface CitiesTable {
    String TABLE_NAME = "Cities";

    String UUID = "uuid";
    String NAME = "name";
    String LATITUDE = "latitude";
    String LONGITUDE = "longitude";

    String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " ( "
            + UUID + " TEXT PRIMARY KEY"
            + "," +NAME + " TEXT"
            + "," + LATITUDE + " REAL"
            + "," + LONGITUDE + " REAL" + ")";
}
