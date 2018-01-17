package com.example.dns.cityparking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
//import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by dns on 10.01.2018.
 */

public class DatabaseController {
    private final String TABLE = "parkingarea"; // название таблицы в бд
    // названия столбцов
   // private final String COLUMN_ID = "_id";
    private final String COLUMN_AVAILAB = "availability";
    private final String COLUMN_COORDINATES = "coordinates";
    private final String COLUMN_PRICE = "price";
    private final String COLUMN_COMMENT = "comment";
    ParkingDatabase db;
    Context context;
    Cursor cursor;
    SQLiteDatabase parkDB;


    public DatabaseController(Context context) {
        this.context = context;
        db = new  ParkingDatabase(context);

    }

    public void Insert(ParkingArea item) {
       /*  try {
            db.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        Log.d("my log", "insert db start");
        parkDB = db.openDataBase();
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_AVAILAB, item.getAvailable());
        cv.put(COLUMN_COORDINATES, item.getPointsString());
        cv.put(COLUMN_PRICE, item.getPrice());
        cv.put(COLUMN_COMMENT, item.getDescription());

        parkDB.insert(TABLE, null, cv);
        parkDB.close();
    }


    //метод для получения всех данных из БД
    public ArrayList<ParkingArea> getAll() {
        ArrayList<ParkingArea> parks = new ArrayList<ParkingArea>();
        try {
            db.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        parkDB = db.openDataBase();
        cursor = parkDB.query(this.TABLE, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
           // int id = cursor.getColumnIndex(this.COLUMN_ID);
            int av = cursor.getColumnIndex(this.COLUMN_AVAILAB);
            int coord = cursor.getColumnIndex(this.COLUMN_COORDINATES);
            int price = cursor.getColumnIndex(this.COLUMN_PRICE);
            int comment = cursor.getColumnIndex(this.COLUMN_COMMENT);

            do {
                ParkingArea park = new ParkingArea(cursor.getInt(av), toCoordinatesList(cursor.getString(coord)),
                                    cursor.getInt(price), cursor.getString(comment)); //cursor.getInt(id),перевести String в ArrayList<LatLng>
                parks.add(park);
            } while (cursor.moveToNext());

        } else {
           // Log.d("MyLog", "Data Base is empty");
        }

        cursor.close();
        parkDB.close();
        return parks;
    }



    private  ArrayList<LatLng> toCoordinatesList(String pointString){
        ArrayList<LatLng> crdList = new  ArrayList<LatLng>();
        double lat;
        double lng;
        String[] subStr;
        String[] subSubStr;
        subStr = pointString.split(";");
        for(int i = 0; i < subStr.length; i++) {
            subSubStr = subStr[i].split(",");
            lat = Double.parseDouble(subSubStr[0]);
            lng = Double.parseDouble(subSubStr[1]);
            crdList.add(new LatLng(lat, lng));
          //  Log.d("MyLog",subSubStr[0]);
        }
        return crdList;
    }



    //метод для поиска по параметрам (кроме радиуса)double radius,
    public ArrayList<ParkingArea> dbFilter(int a,  int p) {
        ArrayList<ParkingArea> parks = new ArrayList<ParkingArea>();
      /*  try {
            db.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
       // String[] projection = {this.COLUMN_AVAILAB, this.COLUMN_COORDINATES,
       //                        this.COLUMN_PRICE,this.COLUMN_COMMENT};
        Log.d("my log", "filt db start");
        //переделать БД и все связанное для available Text (String)????
        String selection =  this.COLUMN_AVAILAB + "=? AND " + this.COLUMN_PRICE + "<=?"; //this.COLUMN_PRICE
        String[] selectionArgs = {Integer.toString(a), Integer.toString(p)};

        parkDB = db.openDataBase();
        cursor = parkDB.query(this.TABLE, null, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
          //  int idColInd = cursor.getColumnIndex(this.COLUMN_ID);
            int av = cursor.getColumnIndex(this.COLUMN_AVAILAB);
            int coord = cursor.getColumnIndex(this.COLUMN_COORDINATES);
            int price = cursor.getColumnIndex(this.COLUMN_PRICE);
            int comment = cursor.getColumnIndex(this.COLUMN_COMMENT);

            do {
                ParkingArea park = new ParkingArea(cursor.getInt(av), toCoordinatesList(cursor.getString(coord)),
                                    cursor.getInt(price), cursor.getString(comment) );
                parks.add(park);
            } while (cursor.moveToNext());

        } else {
          //  Log.d(LOG_TAG, "Bd is empty");
        }
        cursor.close();
        parkDB.close();
        return parks;
    }
/*
    //функция перевода радиуса в значение широты и долготы
    private LatLng radiusToLatLng(double r){
        LatLng region;

        return region;
    }
*/

}
