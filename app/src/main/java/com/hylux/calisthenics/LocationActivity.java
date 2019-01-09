package com.hylux.calisthenics;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Debug;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LocationActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private final int ALL_PERMISSIONS_RESULT = 0;
    private final int PLAY_SERVICES_RESOLUTION_REQUEST = 10;

    private ArrayList<String> permissions;
    private GoogleApiClient apiClient; //TODO If there is a fusedClient, able to remove this?
    private FusedLocationProviderClient fusedClient;

    //TODO Implement GoogleMaps
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Location lastLocation;
    private List<Location> locations;
    private TextView currentLocation, updateTime;
    private long updateInterval;
    private final long FASTEST_INTERVAL = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        Log.d("STATE", "onCreate");

        //Request permissions to use device location
        this.permissions = new ArrayList<>();
        this.permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        this.permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        requestPerms(this.permissions);

        //Build Google Api Client
        this.apiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        this.fusedClient = LocationServices.getFusedLocationProviderClient(this);

        this.locationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult result) {
                Log.d("STATE", "onLocationResult");
                if (result == null) {
                    return;
                }

                Calendar calendar = Calendar.getInstance();

                updateTime.setText(String.valueOf(calendar.getTime()));

                lastLocation = result.getLastLocation();
                currentLocation.setText(lastLocation.toString());
                locations.add(lastLocation);
                Log.d("LOCATIONS", locations.toString());
            }
        };

        this.currentLocation = findViewById(R.id.currentLocation);
        this.updateTime = findViewById(R.id.updateTime);

        this.updateInterval = 5000;
        this.locations = new ArrayList<>();
    }

    private void requestPerms(ArrayList<String> permissions) {

        ArrayList<String>permissionsToRequest = new ArrayList<>();

        for (String permission : permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }

        if (permissionsToRequest.size() > 0) {
            requestPermissions(permissionsToRequest.toArray(new String[0]), ALL_PERMISSIONS_RESULT);
        }
    }

    private boolean havePermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                &&  ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You need to enable permissions to display location !", Toast.LENGTH_SHORT).show();

            return false;
        }
        return true;
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                finish();
            }
            return false;
        }
        return true;
    }

    private void startLocationUpdates() {

        Log.d("STATE", "startLocationUpdates");

        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(this.updateInterval)
                .setFastestInterval(this.FASTEST_INTERVAL);
        Log.d("STATE", locationRequest.toString());

        try {
            Log.d("STATE", "try requestLocationUpdates");
            Log.d("STATE", locationCallback.toString());
            fusedClient.requestLocationUpdates(locationRequest, locationCallback, null /* Looper */);

            /*fusedClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    lastLocation = location;
                    currentLocation.setText(lastLocation.toString());
                    locations.add(location);
                    Log.d("LOCATIONS", locations.toString());
                }
            });*/
        } catch (SecurityException e) {
            havePermissions();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("STATE", "onStart");

        if (apiClient != null) {
            apiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("STATE", "onResume");

        if (!checkPlayServices()) {
            currentLocation.setText(R.string.no_google_play_services_warning);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("STATE", "onPause");

        //Stop location updates
        if (apiClient != null && apiClient.isConnected()) {
            fusedClient.removeLocationUpdates(locationCallback);
            apiClient.disconnect();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantedResults) {

        Log.d("STATE", "onRequestPermissionResult");

        final ArrayList<String> permissionsRejected = new ArrayList<>();

        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                for (String permission : permissions) {
                    if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                        permissionsRejected.add(permission);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                        new AlertDialog.Builder(LocationActivity.this)
                                .setMessage("Allow these permissions. Location will not work without them.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestPermissions(permissionsRejected.toArray(new String[0]), ALL_PERMISSIONS_RESULT);
                                    }
                                }).setNegativeButton("Cancel", null).create().show();
                    }
                } else {
                    if (apiClient != null) {
                        apiClient.connect();
                    }
                }

                break;
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Log.d("STATE", "onConnected");

        if (!havePermissions()) { //OOPSIES Because of missing the !, wasted 15 mins trying to figure out what is wrong...
            return;
        }

        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        Log.d("STATE", "onLocationChanged");

        if (location != null) {
            lastLocation = location;
            currentLocation.setText(location.toString());
            locations.add(location);
            Log.d("LOCATIONS", locations.toString());
        }
    }
}
