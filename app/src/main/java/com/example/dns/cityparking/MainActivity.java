package com.example.dns.cityparking;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;



import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;

import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, OnMapClickListener,
        View.OnClickListener, GoogleMap.OnPolylineClickListener, GoogleMap.OnPolygonClickListener, GoogleMap.OnMarkerDragListener {
    private GoogleMap map;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location currentLocation;
    private LatLng carLocation;
    private boolean mLocationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private ArrayList<ParkingArea> parkingAreas;
    private DatabaseController DBContr;
    //private ParkingArea currentParkingArea;
    //private ArrayList<LatLng> points = new ArrayList<LatLng>();
    ImageButton btnFilt;
    ImageButton btnMark;
    ImageButton btnAdd;
    private boolean flagAdd;
    ArrayList<LatLng> newPoints;
    final int REQUEST_CODE_FILT = 1;
    final int REQUEST_CODE_ADD = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //map.setBuiltInZoomControls(true);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        btnFilt = (ImageButton) findViewById(R.id.buttonFilt);
        btnAdd = (ImageButton) findViewById(R.id.buttonAdd);
        btnMark = (ImageButton) findViewById(R.id.buttonMark);
        DBContr = new DatabaseController(this.getApplicationContext());
        parkingAreas = new  ArrayList<ParkingArea>();
        btnFilt.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnMark.setOnClickListener(this);
        flagAdd = false;
        Log.d("my log", "App start");

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        getLocationPermission();
        updateLocationUI();
        getLocation();
        Log.d("my log", "Map start");
        parkingAreas = DBContr.getAll();
        showParkingAreas();
        map.setOnMapClickListener(this);
        map.setOnPolylineClickListener(this);
        map.setOnPolygonClickListener(this);

    }


   private void getLocation(){
       try {
           if (mLocationPermissionGranted) {
               Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
               locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            currentLocation = task.getResult();
                            Log.d("my log","ccc");
                          //  LatLng currentMarker = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                          //  map.addMarker(new MarkerOptions().position(currentMarker)); //icon(BitmapDescriptorFactory.fromResource(R.drawable.car))
                          //  map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentMarker, 15));
                        } else {
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(56.19, 44.01), 10)); //NN centre
                            map.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
           Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                currentLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            }
    }


    private void showParkingAreas(){
        int colorParking;
       // getLocation();
        for(ParkingArea park: parkingAreas){
            if (park.getAvailable() == 0){
                colorParking = Color.RED ;
            }else colorParking = Color.GREEN ;
            if (park.getPointsNumber() == 2){
                Polyline parkingPolyline = map.addPolyline(new PolylineOptions().addAll(park.getPoints())
                        .width(5)
                        .color(colorParking));
                parkingPolyline.setTag("Description: " + park.getDescription()+ ", price:" + Integer.toString(park.getPrice()));
                parkingPolyline.setClickable(true);

            }else if (park.getPointsNumber() > 2){
                Polygon parkingPolylygon = map.addPolygon(new PolygonOptions().addAll(park.getPoints())
                        .strokeColor(colorParking)
                        .fillColor(colorParking));
                parkingPolylygon.setTag("Description: " + park.getDescription()+ ", price:" + Integer.toString(park.getPrice()));
                parkingPolylygon.setClickable(true);
                }
        }
    }

    @Override
    public void onPolylineClick(Polyline polyline) {
        Toast.makeText(this, polyline.getTag().toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPolygonClick(Polygon polygon) {
        Toast.makeText(this, polygon.getTag().toString(), Toast.LENGTH_LONG).show();
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()) {
            case R.id.buttonFilt:
                intent = new Intent(this, FiltActivity.class);
                startActivityForResult(intent, REQUEST_CODE_FILT);
                break;
            case R.id.buttonMark:
                getLocation();
                carLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                Log.d("my log",Double.toString(carLocation.latitude) + ", " + Double.toString(carLocation.longitude));
                map.addMarker(new MarkerOptions().position(carLocation).draggable(true).
                        icon(BitmapDescriptorFactory.fromResource(R.drawable.car_marker)));
                map.setOnMarkerDragListener(this);
                break;
            case R.id.buttonAdd:
                if (!flagAdd){
                    flagAdd = true;
                    btnAdd.setImageResource(R.drawable.ic_adding);
                    newPoints = new ArrayList<LatLng>();

                }else if(flagAdd){
                    btnAdd.setImageResource(R.drawable.ic_add);
                    flagAdd=false;
                    Log.d("my log", "adding");
                    intent = new Intent(this, AddActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_ADD);
                }
                break;
        }
    }
    @Override
    public void onMarkerDragEnd(Marker marker){
        carLocation = marker.getPosition();
    }
    @Override
    public void onMarkerDragStart(Marker marker){}

    @Override
    public void onMarkerDrag(Marker marker){}



    @Override
    public void onMapClick(LatLng point) {
        if(flagAdd){
            newPoints.add(point);
            Log.d("my log", Double.toString(point.latitude) + ", " + Double.toString(point.longitude));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int price;
        int available;
        String description;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case REQUEST_CODE_FILT:
                    //  if (data != null) {
                    available = data.getIntExtra("available", 1);
                    price = Integer.parseInt(data.getStringExtra("price"));
                    parkingAreas = DBContr.dbFilter(available, price);
                    Log.d("my log", "get filt db");
                    map.clear();
                    showParkingAreas();//}
                    break;
                case REQUEST_CODE_ADD:
                    Log.d("my log", "get insert db");
                    available = data.getIntExtra("available", 1);
                    price = Integer.parseInt(data.getStringExtra("price"));
                    description = data.getStringExtra("description");
                    ParkingArea newParking = new ParkingArea(available, newPoints, price, description);
                    Log.d("my log", newParking.getDescription());
                    DBContr.Insert(newParking);
                    parkingAreas = DBContr.getAll();
                    map.clear();
                    showParkingAreas();
            }
        }
    }
}


