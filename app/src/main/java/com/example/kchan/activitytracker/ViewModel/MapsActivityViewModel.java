package com.example.kchan.activitytracker.ViewModel;

import android.app.Activity;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.ViewModel;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.kchan.activitytracker.BackgroundService.BackgroundDetectedActivitiesService;
import com.example.kchan.activitytracker.LocationServices;
import com.example.kchan.activitytracker.R;
import com.example.kchan.activitytracker.SigninActivity;
import com.example.kchan.activitytracker.Utils.Constants;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static com.example.kchan.activitytracker.Utils.Constants.DEFAULT_ZOOM;

public class MapsActivityViewModel extends ViewModel implements LifecycleObserver {

    private static final String TAG = "MAPSACTIVITYVIEWMODEL";
    private BroadcastReceiver broadcastActivityReceiver, broadcastLocationReceiver;
    private Context context;
    private String currentActivity;
    private Location currentLocation;
    private Location mLastKnownLocation;
    private List<Location> locations;

    public MapsActivityViewModel(){}

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(Context context,List<Location> locations) {
        this.locations = locations;
        Toast.makeText(context, getLocations().toString(), Toast.LENGTH_SHORT).show();
    }

    public MapsActivityViewModel(Context context) {
        this.context = context;
        locations = new ArrayList<Location>();
        broadcastActivityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constants.BROADCAST_DETECTED_ACTIVITY)) {
                    Log.d(TAG, "Broadcast Check");
                    int type = intent.getIntExtra("type", -1);
                    int confidence = intent.getIntExtra("confidence", 0);
                    handleUserActivity(type, confidence);
                }
            }
        };

        broadcastLocationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constants.BROADCAST_LOCATION_SERVICES)) {
                    Toast.makeText(context, "received Broadcast", Toast.LENGTH_SHORT).show();
                    //\setCurrentLocation((Location)intent.getSerializableExtra("location"));
                }
            }
        };
    }

    public void updateLocationUI(GoogleMap mMap) {
        if (mMap == null) {
            return;
        }
        try {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /*public void addMarker(Location location){
        marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                .title(mapsActivityViewModel.getCurrentActivityText()));
    }*/

    public void getDeviceLocation(final FusedLocationProviderClient mfusedLocationProviderClient, final GoogleMap mMap) {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            Task locationResult = mfusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener((Activity) context, new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                         mLastKnownLocation = (Location)task.getResult();
                         mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(mLastKnownLocation.getLatitude(),
                                        mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                         //startLocationServices(mfusedLocationProviderClient);
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.");
                        Log.e(TAG, "Exception: %s", task.getException());
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                }
            });
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    private void handleUserActivity(int type, int confidence) {
        String label = context.getString(R.string.activity_unknown);
        switch (type) {
            case DetectedActivity.IN_VEHICLE: {
                label = context.getString(R.string.activity_in_vehicle);
                break;
            }
            case DetectedActivity.ON_BICYCLE: {
                label = context.getString(R.string.activity_on_bicycle);
                break;
            }
            case DetectedActivity.ON_FOOT: {
                label = context.getString(R.string.activity_on_foot);
                break;
            }
            case DetectedActivity.RUNNING: {
                label = context.getString(R.string.activity_running);
                break;
            }
            case DetectedActivity.STILL: {
                label = context.getString(R.string.activity_still);
                break;
            }
            case DetectedActivity.TILTING: {
                label = context.getString(R.string.activity_tilting);
                break;
            }
            case DetectedActivity.WALKING: {
                label = context.getString(R.string.activity_walking);
                break;
            }
            case DetectedActivity.UNKNOWN: {
                label = context.getString(R.string.activity_unknown);
                break;
            }
        }

        if (confidence > Constants.CONFIDENCE) {
            setCurrentActivityText(label);
            if(getCurrentLocation() != null)
                Toast.makeText(context, "Setting Location", Toast.LENGTH_SHORT).show(); // *** Write a callBack when there is change in data ****
        }

    }


    public String getCurrentActivityText() {
        return currentActivity;
    }

    public void setCurrentActivityText(String currentActivity) {
        this.currentActivity = currentActivity;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
        Toast.makeText(context, getCurrentLocation().toString(), Toast.LENGTH_SHORT).show();
    }

    public void startTracking() {
        Intent intent1 = new Intent(context, BackgroundDetectedActivitiesService.class);
        context.startService(intent1);
    }

    public void stopTracking() {
        Intent intent1 = new Intent(context, BackgroundDetectedActivitiesService.class);
        context.stopService(intent1);
    }



    public void startLocationServices(FusedLocationProviderClient mfusedLocationProviderClient){
        Intent intent = new Intent(context, LocationServices.class);
        context.startService(intent);
    }

    public void stopLocationServices(){
        Intent intent = new Intent(context, LocationServices.class);
        context.stopService(intent);
    }

    public void onLogoutClicked(){
        Intent intent = new Intent(context, SigninActivity.class);
        context.startActivity(intent);
    }

    public void registerReceiver(){
        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastActivityReceiver,
                new IntentFilter(Constants.BROADCAST_DETECTED_ACTIVITY));
        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastLocationReceiver,
                new IntentFilter(Constants.BROADCAST_LOCATION_SERVICES));
    }

    public void unregisterReceiver(){
        LocalBroadcastManager.getInstance(context).unregisterReceiver(broadcastActivityReceiver);
        LocalBroadcastManager.getInstance(context).unregisterReceiver(broadcastLocationReceiver);
    }
}
