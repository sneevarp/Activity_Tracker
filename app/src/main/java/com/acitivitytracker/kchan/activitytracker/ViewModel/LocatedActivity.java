package com.acitivitytracker.kchan.activitytracker.ViewModel;
import java.util.Observable;

public class LocatedActivity extends Observable{
    private static String activity;
    private static Double latitude;
    private static Double longitude;

    public void setActivity(String activity) {
        this.activity = activity;

    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;

    }
    public String getActivity() {

        return activity;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
