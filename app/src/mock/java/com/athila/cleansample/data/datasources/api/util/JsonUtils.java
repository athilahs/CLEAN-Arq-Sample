package com.athila.cleansample.data.datasources.api.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.athila.cleansample.infrastructure.CleanSampleLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by athila on 15/06/16.
 */

public class JsonUtils {
    public static @Nullable String getFromFile(@NonNull Context context, @NonNull String filePath) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getResources().getAssets().open(filePath)));

            StringBuilder sb = new StringBuilder();

            // do reading, usually loop until end of file reading
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            return sb.toString();
        } catch (IOException e) {
            CleanSampleLog.error("Could not open file: "+filePath);
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                    // silent
                }
            }
        }
    }
}
