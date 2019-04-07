package com.example.nikmul19.medicine;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.PluralsRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import  com.google.android.gms.location.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.android.libraries.places.api.Places;

import com.google.android.gms.places_placereport.*;
import com.google.android.libraries.places.api.net.PlacesClient;

//import com.google.android


import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import static com.example.nikmul19.medicine.BuildConfig.GOOGLE_MAPS_API_KEY;
import com.google.android.libraries.places.api.Places;

import org.json.JSONArray;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean mLocationPermissionGranted=false;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 542;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean mGpsEnabled=false;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getLocationPermission();
        mFusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this);

//        Places.initialize(getApplicationContext(),GOOGLE_MAPS_API_KEY);
        PlacesClient placesClient = Places.createClient(this);
        //urlRequest();


    }

    public void urlRequest(LatLng curr){

        final RequestQueue req= Volley.newRequestQueue(this);
       // JsonObjectRequest jsonObjectRequest = new JsonObjectRequest()
        String nearByUrl="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+curr.latitude+","+curr.longitude+"&radius=1000&type=pharmacy&key=AIzaSyBaSEx12dj942y2Jewb81mmRuzyMkbUu2c";
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, nearByUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
               // Toast.makeText(getApplicationContext(),response+"",Toast.LENGTH_LONG).show();
                Log.i("PLACES", response + "");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("PLACES", error + "");

            }
        });
        req.add(jsonArrayRequest);

    }


    private  void getLocationPermission(){

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){

            mLocationPermissionGranted=true;

        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted=false;
        switch (requestCode){
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    mLocationPermissionGranted=true;
                }

        }
    }

    private void getCurrentPlace(){
        //List<Place.>

//        Places.initialize(getApplicationContext(), GOOGLE_MAPS_API_KEY);
        PlacesClient placesClient= Places.createClient(this);

    }


    private void getCurrentLocation(){


        try{
            Log.i("Location","inside try");
            LocationManager lm= (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            mGpsEnabled=lm.isProviderEnabled(LocationManager.GPS_PROVIDER);


            if(mLocationPermissionGranted && mGpsEnabled){
            }
            else{
                AlertDialog.Builder alertDialogBuilder= new AlertDialog.Builder(this);
                alertDialogBuilder.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"Hey",Toast.LENGTH_SHORT).show();

                        getApplicationContext().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        getCurrentLocation();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                alertDialogBuilder.setMessage("Location Not enabled");
                alertDialogBuilder.show();
            }
        }
        catch (Exception e){}
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);

        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        getCurrentLocation();
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
