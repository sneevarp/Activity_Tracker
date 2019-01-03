package com.acitivitytracker.kchan.activitytracker;

import android.content.Context;
import android.preference.PreferenceManager;

public class LocationRequestHelper {
    final static String KEY_LOCATION_UPDATES_REQUESTED = "location-updates-requested";

    static void setRequesting(Context context, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_LOCATION_UPDATES_REQUESTED, value)
                .apply();
    }

    static boolean getRequesting(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_LOCATION_UPDATES_REQUESTED, false);
    }
}
