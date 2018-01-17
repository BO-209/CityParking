package com.example.dns.cityparking;
import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import static com.example.dns.cityparking.R.id.map;

/**
 * Created by dns on 02.01.2018.
 */

public class ParkingArea {
    private int pointsNumber;
    private int available;
    private ArrayList<LatLng> points;
    private int price;
    private String description;

    ParkingArea(int available, ArrayList<LatLng> points, int price, String description){
        this.pointsNumber = points.size();
        this.available = available;
        this.points = points;
        this.price = price;
        this.description = description;

    }
/*
    public void setDescription(ArrayList<LatLng> pnts, int x, int av){
        pointsNumber = pnts.size();
        points = new ArrayList<LatLng>(pnts);
        price = x;
        available = av;
    }*/

    public int getAvailable(){
        return available;
    }

    public int getPointsNumber(){
        return pointsNumber;
    }

    public ArrayList<LatLng> getPoints(){return points;}

    public String getPointsString(){
        String pointsString = "";
        for (LatLng point: points){
            pointsString = pointsString.concat(Double.toString(point.latitude) + ", " + Double.toString(point.longitude) + "; ");
            Log.d("my log", pointsString);
        }
        pointsString = pointsString.substring(0, pointsString.length()-2);
        Log.d("my log", pointsString);
        return pointsString;
    }

    public int getPrice(){return price;}

    public String getDescription(){return description;}

    private void showDetails(){;}


}
