package com.athila.cleansample.data.datasource.database;

import com.athila.cleansample.BuildConfig;
import com.athila.cleansample.data.util.DatabaseUtils;
import com.athila.cleansample.infrastructure.CleanSampleApp;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

/**
 * Created by athila on 08/06/16.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, // Robolectric does not support 23
    packageName = "com.athila.cleansample")
public abstract class DatabaseTest {

  StorIOSQLite mStorIOSQLite;

  @Before
  public void setupStorIO() {
    mStorIOSQLite = ((CleanSampleApp) RuntimeEnvironment.application).getApplicationComponent()
        .storIO();
    DatabaseUtils.clearTables(mStorIOSQLite);
  }
}
