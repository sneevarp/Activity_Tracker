package com.acitivitytracker.kchan.activitytracker.BackgroundService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.acitivitytracker.kchan.activitytracker.MapsActivity;
import com.acitivitytracker.kchan.activitytracker.ResultHelper;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.List;

public class ActivityBroadcastReceiver extends BroadcastReceiver {

    public static final String ACTIVITY_PROCESS_UPDATES =
            "com.google.android.gms.location.sample.backgroundlocationupdates.action" +
                    ".ACTIVITY_UPDATES";
    private static final String TAG = "ACTIVITYBROADCASTRECEIVER";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"onReceive");
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTIVITY_PROCESS_UPDATES.equals(action)) {
                ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
                if (result != null) {
                    List<DetectedActivity> locations = result.getProbableActivities();
                    ResultHelper locationResultHelper = ResultHelper.getRhInstance();
                    // Show notification with the location data.
                    locationResultHelper.showNotificationActivity(locations.get(locations.size()-1).getType(), locations.get(locations.size()-1).getConfidence());
                    Log.i(TAG, result.getMostProbableActivity().toString());
                }
            }
        }
    }
}
