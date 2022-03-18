package com.example.geolocationbasis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Button showMapButton;
    TextView latText, lonText, timeText;

    LocationManager locationManager;
    Location location;

    private boolean granted = false;
    private final int LOCATION_PERMISSION = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showMapButton   =   findViewById(R.id.toMapButton);
        lonText         =   findViewById(R.id.lon);
        latText         =   findViewById(R.id.lat);
        timeText        =   findViewById(R.id.timeText);

        //TODO подключить менеджер местоположения
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (granted||checkPermission())
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000*10,20,listener);
    }

    //TODO описать LocationListener
    LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location == null){
                return;
            }
            else {
                showLocation(location);
            }

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            //TODO предусмотреть отключени разрешения
        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private boolean checkPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION )!= PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION);//В массив предаем сразу несколько разрешений, можно сразу запросить пару которая считаеться личнйо и опасной.
            //Идет заброс разрешения у пользователя;
            return false;
        }
        else {
            return true;
        }

    };
    private void showLocation(Location location){
         String koord = String.valueOf(location.getLatitude());
         latText.setText(koord);
         koord = String.valueOf(location.getLatitude());
         lonText.setText(koord);
         koord = new Date(location.getTime()).toString();
         timeText.setText(koord);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //TODO реализовать получение координат с запросом разрешения
        if (granted = checkPermission()){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000*10,20,listener);
            if (locationManager!=null){
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location!=null){
                    showLocation(location);
                }
            }
        }

    }

    //TODO переопределить функцию обратного вызова для обработки ответа пользователя


    @Override
    //Если reques code совпадет с нашей констатной то все окей то пользователь разрешил. грант резал то что разрешено.
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode ==LOCATION_PERMISSION){
            granted = true;
            if (grantResults.length>0){
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i]!= PackageManager.PERMISSION_GRANTED){
                        granted = false;//Вообще не работает, если нет разрешения на одно
                    }

                }

            }else {
                granted=false;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(listener);
    }
}
