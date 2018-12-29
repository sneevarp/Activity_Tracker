package com.example.kchan.activitytracker;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.kchan.activitytracker.BackgroundService.LocationUpdateBroadcastReceiver;
import com.example.kchan.activitytracker.Fragment.ProfileFragment;
import com.example.kchan.activitytracker.Singleton.User;
import com.example.kchan.activitytracker.Utils.Constants;
import com.example.kchan.activitytracker.ViewModel.MapsActivityViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private static final String TAG = "MapActivity";
    private GoogleMap mMap;
    private GoogleSignInClientValue googleSigninClientValue;
    private MapsActivityViewModel mapsActivityViewModel;
    private FusedLocationProviderClient mfusedLocationProviderClient;
    private Location mLastKnownLocation;
    private Marker marker;
    private NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    private String personName;
    private String personGivenName;
    private String personFamilyName;
    private String personEmail;
    private String personId;
    private Uri personPhoto;
    private boolean isProfileFragmentEnabled;
    private User currentUser;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mapsActivityViewModel.updateLocationUI(mMap);
        mapsActivityViewModel.getDeviceLocation(mfusedLocationProviderClient, mMap);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                requestLocationUpdates();
                if(isProfileFragmentEnabled) {
                    currentUser = User.getInstance();
                    Toast.makeText(MapsActivity.this,"Hi " + currentUser.getAccount().getDisplayName(), Toast.LENGTH_SHORT).show();
                    isProfileFragmentEnabled = false;
                    closeProfileFragment();
                }
            }
        });
        }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Toolbar toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        googleSigninClientValue = new GoogleSignInClientValue(this);
        mapsActivityViewModel= new MapsActivityViewModel(this);
        mfusedLocationProviderClient = new FusedLocationProviderClient(this);
      //  mapsActivityViewModel.startTracking();
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.drawer_view);
        buildGoogleApiClient();

        initMap();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(MapsActivity.this);
        if (acct != null) {
            personName = acct.getDisplayName();
            personGivenName = acct.getGivenName();
            personFamilyName = acct.getFamilyName();
            personEmail = acct.getEmail();
            personId = acct.getId();
            personPhoto = acct.getPhotoUrl();
        }

    }

    private void buildGoogleApiClient() {
        if (mGoogleApiClient != null) {
            return;
        }
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapsActivityViewModel.registerReceiver();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeLocationUpdates();
       /* mapsActivityViewModel.unregisterReceiver();
        mapsActivityViewModel.stopTracking();
        mapsActivityViewModel.stopLocationServices();*/
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);
     /*   TextView gname=(TextView)findViewById(R.id.name);
        gname.setText(personGivenName+" "+personFamilyName);
        TextView gemail=(TextView)findViewById(R.id.email);
        gemail.setText(personEmail);
        ImageView gphoto=findViewById(R.id.profilepic);
        Glide.with(this).load(personPhoto).apply(RequestOptions.circleCropTransform()).into(gphoto);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.signout:
                googleSigninClientValue.getInstance().signOut()
                        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(MapsActivity.this, "Lets logout", Toast.LENGTH_SHORT).show();
                            }
                        });
                /*mapsActivityViewModel.unregisterReceiver();
                mapsActivityViewModel.stopLocationServices();
                mapsActivityViewModel.onLogoutClicked();*/
                return true;
            case R.id.profile:
                 displayProfileFragment();
                 isProfileFragmentEnabled = true;
                 mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.normal_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.hybrid_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.satellite_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.terrain_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void displayProfileFragment(){
        ProfileFragment profileFragment = ProfileFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.profile_fragment, profileFragment);
        fragmentTransaction.commit();
    }

    private void closeProfileFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ProfileFragment profileFragment = (ProfileFragment) fragmentManager.findFragmentById(R.id.profile_fragment);
        if (profileFragment != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(profileFragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onBackPressed() {
        if (isProfileFragmentEnabled) {
            closeProfileFragment();
            isProfileFragmentEnabled = false;
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Exit");
            builder.setMessage("Are You Sure?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intent);
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(Constants.UPDATE_INTERVAL);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(Constants.FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Sets the maximum time when batched location updates are delivered. Updates may be
        // delivered sooner than this interval.
        mLocationRequest.setMaxWaitTime(Constants.MAXIMUM_WAITTIME);
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, LocationUpdateBroadcastReceiver.class);
        intent.setAction(LocationUpdateBroadcastReceiver.ACTION_PROCESS_UPDATES);
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void requestLocationUpdates() {
        try {
            com.google.android.gms.location.LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, getPendingIntent());
        } catch (SecurityException e) {

        }
    }

    public void removeLocationUpdates() {
        Log.i(TAG, "Removing location updates");
        LocationRequestHelper.setRequesting(this, false);
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
                getPendingIntent());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

