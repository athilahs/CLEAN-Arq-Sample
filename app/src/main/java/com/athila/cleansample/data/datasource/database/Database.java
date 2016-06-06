package com.athila.cleansample.data.datasource.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.athila.cleansample.R;
import com.athila.cleansample.data.datasource.database.table.CitiesTable;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mvpwheather.db";
    private static final int DATABASE_VERSION = 1;
    private Context mContext;

    public Database(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public final void onCreate(final SQLiteDatabase db) {
        db.execSQL(CitiesTable.SQL_CREATE);

        prePopulateDatabase(db);
    }

    @Override
    public final void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        // Remember to update mappings for StorIO
    }

    private void prePopulateDatabase(SQLiteDatabase db) {

        // Built-in cities
        String sqlBuiltInDublin = "INSERT INTO " + CitiesTable.TABLE_NAME + " VALUES('cbeaac75-6cb2-4eb5-a708-fec8881d7f6b', '"+mContext.getString(R.string.city_dublin)+"', 53.349805, -6.26031);";
        String sqlBuiltInLondon = "INSERT INTO " + CitiesTable.TABLE_NAME + " VALUES('9dbebcab-1aef-4b6d-a7b4-5e0d45e0b4fa', '"+mContext.getString(R.string.city_london)+"', 51.507351, -0.127758);";
        String sqlBuiltInNewYork = "INSERT INTO " + CitiesTable.TABLE_NAME + " VALUES('ca6c3f94-0f1d-462c-b88b-ef0a2fbe9006', '"+mContext.getString(R.string.city_new_york)+"', 40.712784, -74.005941);";
        String sqlBuiltInBarcelona = "INSERT INTO " + CitiesTable.TABLE_NAME + " VALUES('7d6bfb40-ebf3-493f-bc3a-61573fc3f6ba', '"+mContext.getString(R.string.city_barcelona)+"', 41.385064, 2.173403);";
        db.execSQL(sqlBuiltInDublin);
        db.execSQL(sqlBuiltInLondon);
        db.execSQL(sqlBuiltInNewYork);
        db.execSQL(sqlBuiltInBarcelona);
    }
}
