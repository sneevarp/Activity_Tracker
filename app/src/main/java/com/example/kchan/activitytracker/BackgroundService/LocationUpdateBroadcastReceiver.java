package com.example.kchan.activitytracker.BackgroundService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.example.kchan.activitytracker.ResultHelper;
import com.example.kchan.activitytracker.ViewModel.MapsActivityViewModel;
import com.google.android.gms.location.LocationResult;

import java.util.List;

public class LocationUpdateBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "LUBroadcastReceiver";
    private MapsActivityViewModel mapsActivityViewModel;

    public static final String ACTION_PROCESS_UPDATES =
            "com.google.android.gms.location.sample.backgroundlocationupdates.action" +
                    ".PROCESS_UPDATES";

    public LocationUpdateBroadcastReceiver() {
        super();
        mapsActivityViewModel = MapsActivityViewModel.getInstance();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_PROCESS_UPDATES.equals(action)) {
                LocationResult result = LocationResult.extractResult(intent);
                if (result != null) {
                    List<Location> locations = result.getLocations();
                    mapsActivityViewModel.setLocations(context,locations);
                    ResultHelper locationResultHelper = new ResultHelper(
                            context, locations);
                    // Save the location data to SharedPreferences.
                    locationResultHelper.saveResults();
                    // Show notification with the location data.
                    locationResultHelper.showNotification();
                    Log.i(TAG, ResultHelper.getSavedLocationResult(context));
                }
            }
        }
    }

}
