package com.acitivitytracker.kchan.activitytracker.ViewModel;

import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.acitivitytracker.kchan.activitytracker.R;
import com.acitivitytracker.kchan.activitytracker.SigninActivity;
import com.acitivitytracker.kchan.activitytracker.Utils.Constants;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.maps.GoogleMap;

import java.util.List;

public class MapsActivityViewModel extends ViewModel implements LifecycleObserver {

    private static final String TAG = "MAPSACTIVITYVIEWMODEL";
    private Context context;
    private String currentActivity;
    private Location currentLocation;
    private Location mLastKnownLocation;
    private List<Location> locations;

    public static MapsActivityViewModel getInstance() {
        return mInstance;
    }

    private static MapsActivityViewModel mInstance;

    private MapsActivityViewModel(Context context){
        this.context = context;
    }
    LocatedActivity la=new LocatedActivity();

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(Context context,List<Location> locations) {
        this.locations = locations;
    }
    public static void init(Context context) {
        mInstance = new MapsActivityViewModel(context);
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


    public void handleUserActivity(int type, int confidence) {
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


    public void onLogoutClicked(){
        Intent intent = new Intent(context, SigninActivity.class);
        context.startActivity(intent);
    }

}
