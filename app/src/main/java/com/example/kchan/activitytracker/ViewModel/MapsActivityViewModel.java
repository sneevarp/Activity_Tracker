package com.example.kchan.activitytracker.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;

import com.example.kchan.activitytracker.SigninActivity;

public class MapsActivityViewModel extends ViewModel {


    public MapsActivityViewModel() { }

    public void onLogoutClicked(Context context){
        Intent intent = new Intent(context, SigninActivity.class);
        context.startActivity(intent);
    }

}
