package com.example.kchan.activitytracker.viewModel;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.example.kchan.activitytracker.SigninActivity;
import com.google.android.gms.maps.GoogleMap;

public class MapsActivityViewModel {

    private static int REQUEST_LOCATION_PERMISSION = 1234;

    public MapsActivityViewModel() { }

    public void enableMyLocation(Context context, Activity activity, @NonNull GoogleMap mMap) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    public void onLogoutClicked(Context context){
        Intent intent = new Intent(context, SigninActivity.class);
        context.startActivity(intent);
    }

}
