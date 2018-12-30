package com.example.kchan.activitytracker;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.kchan.activitytracker.Utils.Constants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

public class LocationServices extends Service {
    private static final String TAG = "LOCATIONSERVICES" ;
    private static final int ONGOING_NOTIFICATION_ID = 1;
    private FusedLocationProviderClient mfusedLocationProviderClient;
    HandlerThread handlerThread = new HandlerThread("MyHandlerThread");
    public LocationServices() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mfusedLocationProviderClient = new FusedLocationProviderClient(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "LOCATION SERVICES RUNNING", Toast.LENGTH_SHORT).show();

        Intent notificationIntent = new Intent(this, MapsActivity.class);

        PendingIntent pendingIntent = PendingIntent.getService(this, 0,
                notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Activity Tracekr")
                .setTicker("Activity tracking Location")
                .setContentText("Location Services")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();
        startForeground(1337,
                notification);
        handlerThread.start();
        Looper looper = handlerThread.getLooper();
        Handler handler = new Handler(looper);
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "getLocation: getting location information.");
                getLocation();
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    private void getLocation() {
        Toast.makeText(this, "getting Location", Toast.LENGTH_SHORT).show();
        // ---------------------------------- LocationRequest ------------------------------------
        // Create the location request to start receiving updates
        LocationRequest mLocationRequestHighAccuracy = new LocationRequest();
        mLocationRequestHighAccuracy.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequestHighAccuracy.setInterval(Constants.UPDATE_INTERVAL);
        mLocationRequestHighAccuracy.setFastestInterval(Constants.FASTEST_INTERVAL);


        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Log.d(TAG, "getLocation: getting location information.");
        mfusedLocationProviderClient.requestLocationUpdates(mLocationRequestHighAccuracy, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        Log.d(TAG, "onLocationResult: got location result.");
                        Location location = locationResult.getLastLocation();
                        broadcastActivity(location);
                    }
                },
                Looper.myLooper()); // Looper.myLooper tells this to repeat forever until thread is destroyed
    }

    private void broadcastActivity(Location location) {
        Toast.makeText(getApplicationContext(), location.toString(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Constants.BROADCAST_LOCATION_SERVICES);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        handlerThread.quit();
        super.onDestroy();
    }
}
