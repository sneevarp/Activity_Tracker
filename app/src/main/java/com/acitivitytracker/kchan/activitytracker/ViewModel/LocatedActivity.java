package com.acitivitytracker.kchan.activitytracker.ViewModel;

public class LocatedActivity {
    private String activity;
    private Double latitude;
    private Double longitude;

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
