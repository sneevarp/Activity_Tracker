package com.example.kchan.activitytracker.Utils;

public class Constants {
    public static final String BROADCAST_DETECTED_ACTIVITY = "activity_intent";
    public static final String BROADCAST_LOCATION_SERVICES = "location_intent";
    public static final float DEFAULT_ZOOM = 5.0F;
    public static final long DETECTION_INTERVAL_IN_MILLISECONDS = 20 * 1000;
    public static final int CONFIDENCE = 60;
    public static final int ERROR_DIALOG_REQUEST = 9001;
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 9002;
    public static final int PERMISSIONS_REQUEST_ENABLE_GPS = 9003;
    public final static long UPDATE_INTERVAL = 4 * 1000;  /* 4 secs */
    public final static long FASTEST_INTERVAL = 2000; /* 2 sec */
    public final static long MAXIMUM_WAITTIME = 5000;
}
