package com.acitivitytracker.kchan.activitytracker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.preference.PreferenceManager;

import com.acitivitytracker.kchan.activitytracker.BackgroundService.ObservableObject;
import com.acitivitytracker.kchan.activitytracker.ViewModel.LocatedActivity;
import com.google.android.gms.location.DetectedActivity;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class ResultHelper {

    final static String KEY_LOCATION_UPDATES_RESULT = "location-update-result";

    final private static String PRIMARY_CHANNEL = "default";

    private String activityname;

    private static ResultHelper rhInstance;

    public  static ResultHelper getRhInstance() {
        return rhInstance;
    }


    public void setLocationstring(String locationstring) {
        this.locationstring = locationstring;
    }

    private String locationstring;
    private Context mContext;
    private List<Location> mLocations;


    public String getActivityname() {
        return activityname;
    }

    public String getLocationstring() {
        return locationstring;
    }

    public ResultHelper() {
    }

    private NotificationManager mNotificationManager;

    public static void init(Context context){
        rhInstance = new ResultHelper(context);
    }

    private ResultHelper(Context context) {
        mContext = context;

        NotificationChannel channel = new NotificationChannel(PRIMARY_CHANNEL,
                context.getString(R.string.default_channel), NotificationManager.IMPORTANCE_DEFAULT);
        channel.setLightColor(Color.GREEN);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getNotificationManager().createNotificationChannel(channel);
    }
    /**
     * Returns the title for reporting about a list of {@link Location} objects.
     */
    public String getLocationResultTitle() {
        String numLocationsReported = mContext.getResources().getQuantityString(
                R.plurals.num_locations_reported, mLocations.size(), mLocations.size());
        return numLocationsReported + ": " + DateFormat.getDateTimeInstance().format(new Date());
    }

    public String getLocationResultText() {
        if (mLocations.isEmpty()) {
            return mContext.getString(R.string.unknown_location);
        }

        StringBuilder sb = new StringBuilder();
        for (Location location : mLocations) {
            sb.append("(");
            sb.append(location.getLatitude());
            sb.append(", ");
            sb.append(location.getLongitude());
            sb.append(")");
            sb.append("\n");
        }
        return sb.toString();

    }

    /**
     * Saves location result as a string to {@link android.content.SharedPreferences}.
     */
    public void saveResults() {

        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putString(KEY_LOCATION_UPDATES_RESULT, getLocationResultTitle() + "\n" +
                        getLocationResultText())
                .apply();
    }

    /**
     * Fetches location results from {@link android.content.SharedPreferences}.
     */
    public static String getSavedLocationResult(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_LOCATION_UPDATES_RESULT, "");
    }

    /**
     * Get the notification mNotificationManager.
     * <p>
     * Utility method as this helper works with it a lot.
     *
     * @return The system service NotificationManager
     */
    private NotificationManager getNotificationManager() {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) mContext.getSystemService(
                    Context.NOTIFICATION_SERVICE);
        }
        return mNotificationManager;
    }

    /**
     * Displays a notification with the location results.
     */
    public void showNotification(List<Location> locations) {
        this.mLocations = locations;
        Intent notificationIntent = new Intent(mContext, MapsActivity.class);

        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(MapsActivity.class);

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        setLocationstring(getLocationResultText());
        Notification.Builder notificationBuilder = new Notification.Builder(mContext,
                PRIMARY_CHANNEL)
                .setContentTitle(getLocationResultTitle())
                .setContentText(getLocationResultText())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(notificationPendingIntent);

        getNotificationManager().notify(0, notificationBuilder.build());
    }

    public void setActivityname(String activityname) {
        this.activityname = activityname;
    }

    public void showNotificationActivity(int update, int confidence) {
        if(confidence > 20){
            setActivityname(getActivity(update));
            LocatedActivity la=new LocatedActivity();
            la.setActivity(getActivityname());
            la.setLatitude(mLocations.get(0).getLatitude());
            la.setLongitude(mLocations.get(0).getLongitude());

            ObservableObject.getInstance().updateValue(la);
        }
    }

    public String getActivity(int update) {
        switch (update) {
            case DetectedActivity.IN_VEHICLE: {
                return "DRIVING";
            }
            case DetectedActivity.ON_BICYCLE: {
                return "Riding BICYCLE";
            }
            case DetectedActivity.ON_FOOT: {
                return "WALKING";
            }
            case DetectedActivity.RUNNING: {
                return "RUNNING";
            }
            case DetectedActivity.STILL: {
                return "STILL";
            }
            case DetectedActivity.TILTING: {
                return "Tilted";
            }
            case DetectedActivity.WALKING: {
                return "Walking";
            }
            case DetectedActivity.UNKNOWN: {
                return "DRIVING";
            }
        }
        return "NOT FOUND";
    }
}
