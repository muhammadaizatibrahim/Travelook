package com.example.aizat.travelook_v1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aizat.travelook_v1.PlacesModel.MyPlaces;
import com.example.aizat.travelook_v1.PlacesModel.PlaceDetail;
import com.example.aizat.travelook_v1.PlacesModel.Results;
import com.example.aizat.travelook_v1.Remote.IGoogleAPIService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.security.Permission;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewPlaceList extends AppCompatActivity {

    private static final int MY_PERMISSION_CODE = 1000 ;
    private GoogleMap mMap;

    private double latitude,longitude;
    private Location mLastLocation;
    private TextView placeName,placeAddress;
    private String url;
    private RatingBar ratingBar;
    private Button buttondetail;

    IGoogleAPIService mService;

    PlaceDetail mPlace;

    MyPlaces currentPlaces;
    private String placeTypea;

    //new location
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    private LocationRequest mLocationRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_place_list);

        mService = Common.getGoogleAPIService();

        placeName = (TextView) findViewById(R.id.placeName);
        placeAddress = (TextView) findViewById(R.id.placeAddress);
        buttondetail = (Button) findViewById(R.id.buttondetail);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);


        Intent intent = getIntent();
        placeTypea = intent.getStringExtra("placeType");


        placeName.setText("");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        buildLocationCallBack();
        buildLocationRequest();


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest,locationCallback, Looper.myLooper());


        //String url = getUrl (latitude,longitude);




}

public void listPlace (final double latitude,double longitude,String placeType){

    String url = getUrl (latitude,longitude,placeType);

    mService.getNearByPlaces(url)
            .enqueue(new Callback<MyPlaces>() {
                @Override
                public void onResponse(Call<MyPlaces> call, Response<MyPlaces> response){
                    currentPlaces = response.body();
                    if (response.isSuccessful())
                    {
                        for (int i=0;i<response.body().getResults().length;i++){
                            Results googlePlace = response.body().getResults()[i];
                            double lat = Double.parseDouble(googlePlace.getGeometry().getLocation().getLat());
                            double lng = Double.parseDouble(googlePlace.getGeometry().getLocation().getLng());
                            LatLng latLng = new LatLng(lat,lng);

                            placeName.setText(googlePlace.getName());

                        }

                    }
                }

                @Override
                public void onFailure(Call<MyPlaces> call, Throwable t) {

                }


            });
}

    @Override
    protected void onStop(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        super.onStop();
    }

    private void buildLocationRequest() {
        {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(1000);
            mLocationRequest.setFastestInterval(1000);
            mLocationRequest.setSmallestDisplacement(10f);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        }
    }

   private void buildLocationCallBack() {
       locationCallback = new LocationCallback(){
           @Override
           public void onLocationResult(LocationResult locationResult) {
               super.onLocationResult(locationResult);
               mLastLocation = locationResult.getLastLocation();

               latitude = mLastLocation.getLatitude();
               longitude = mLastLocation.getLongitude();


               listPlace(latitude,longitude,placeTypea);


           }
       };

    }

    private String getPlaceDetailUrl(String place_id) {
        StringBuilder url = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json");
        url.append("?placeid="+ place_id);
        url.append("&key="+getResources().getString(R.string.browser_key));
        return url.toString();
    }

    private String getUrl(double latitude, double longitude, String placeType) {
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+10000);
        googlePlaceUrl.append("&type="+placeType);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+getResources().getString(R.string.browser_key));
        Log.d("getUrl",googlePlaceUrl.toString());
        return googlePlaceUrl.toString();
    }


    private boolean checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION))
                ActivityCompat.requestPermissions(this,new String[]{

                        android.Manifest.permission.ACCESS_FINE_LOCATION
                },MY_PERMISSION_CODE);
            else
                ActivityCompat.requestPermissions(this,new String[]{

                        Manifest.permission.ACCESS_FINE_LOCATION
                },MY_PERMISSION_CODE);
            return false;
        }
        else
            return true;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case MY_PERMISSION_CODE:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        buildLocationCallBack();
                        buildLocationRequest();

                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest,locationCallback, Looper.myLooper());
                    }
                }
            }
            break;
        }
    }
}
