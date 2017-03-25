package address.app.com.shareaddress;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

class LocationHelper implements GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback, LocationListener, ResultCallback {

    private LocationRequest mLocationRequest;

    private GoogleApiClient googleApiClient;
    private GoogleMap map;

    private String cityName, streetName, streetNumber, postcode;
    private AddressFormActivity activity;

    LocationHelper(AddressFormActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, this);
        }

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        googleApiClient,
                        builder.build()
                );
        result.setResultCallback(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    void inflateMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) activity.getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onResult(@NonNull Result result) {
        final Status status = result.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                // NO need to show the dialog;
                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  GPS turned off, Show the user a dialog
                try {
                    status.startResolutionForResult(
                            activity, 100);
                } catch (IntentSender.SendIntentException e) {
                    Log.e("", "Exception:SendIntentException" + e);
                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        setAddressFields(location);
        //Place current location marker
        LatLng latLng = new LatLng(getLatitudeValue(location), getLongitudeValue(location));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        map.addMarker(markerOptions);
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.zoomTo(11));
    }

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(activity,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Explanation needed.
                ActivityCompat.requestPermissions(activity,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    void onRequestPermissionsResult(int requestCode,
                                    @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(activity,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        buildGoogleApiClient();
                    }

                } else {
                    Toast.makeText(activity, "permission denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    private void setAddressFields(Location location) {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        List<android.location.Address> address;
        try {
            address = geocoder.getFromLocation(getLatitudeValue(location), getLongitudeValue(location), 1);
            cityName = getCityName(address);
            streetName = getStreetName(address);
            streetNumber = getStreetNumber(address);
            postcode = getPostalCode(address);
            activity.updateAddress(new Address(cityName, streetName, streetNumber, postcode));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double getLatitudeValue(Location location) {
        return location.getLatitude();
    }

    private double getLongitudeValue(Location location) {
        return location.getLongitude();
    }

    private String getStreetName(List<android.location.Address> address) {
        return address.get(0).getThoroughfare();
    }

    private String getStreetNumber(List<android.location.Address> address) {
        return address.get(0).getFeatureName();
    }

    private String getPostalCode(List<android.location.Address> address) {
        return address.get(0).getPostalCode();
    }

    private String getCityName(List<android.location.Address> address) {
        return address.get(0).getLocality();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }


    void onActivityResult(int requestCode, int resultCode, Intent data) {
        int REQUEST_CHECK_SETTINGS = 100;
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    return;
                }
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, this);
            } else {
                Toast.makeText(activity, "GPS is not enabled", Toast.LENGTH_LONG).show();
            }
        }
    }

    GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    String getCityName() {
        return cityName;
    }

    void setCityName(String cityName) {
        this.cityName = cityName;
    }

    String getStreetNumber() {
        return streetNumber;
    }

    void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    String getStreetName() {
        return streetName;
    }

    void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    String getPostcode() {
        return postcode;
    }

    void setPostcode(String postcode) {
        this.postcode = postcode;
    }

}
