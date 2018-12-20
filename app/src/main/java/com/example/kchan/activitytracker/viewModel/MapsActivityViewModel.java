package com.example.kchan.activitytracker.viewModel;

import android.content.Context;
import android.content.Intent;
import android.location.Location;

import com.example.kchan.activitytracker.SigninActivity;

public class MapsActivityViewModel {

    public MapsActivityViewModel() { }

    public void onLogoutClicked(Context context){
        Intent intent = new Intent(context, SigninActivity.class);
        context.startActivity(intent);
    }

    public void onLocationUpdated(Location location){
    }

}
