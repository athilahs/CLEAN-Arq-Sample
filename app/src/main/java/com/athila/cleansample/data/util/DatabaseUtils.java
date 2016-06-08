package com.athila.cleansample.data.util;

import com.athila.cleansample.data.datasource.database.table.CitiesTable;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.RawQuery;

/**
 * Created by athila on 08/06/16.
 */

public class DatabaseUtils {
    /**
     * Clear the data from all tables without dropping them.
     *
     * @param storIOSQLite the storIO reference responsible for managing the database
     */
    public static void clearTables(StorIOSQLite storIOSQLite) {
        storIOSQLite.executeSQL()
                .withQuery(RawQuery.builder()
                        .query("delete from "+ CitiesTable.TABLE_NAME)
                        // TODO: add delete statements for other tables
                        .build())
                .prepare()
                .executeAsBlocking();
    }
}
