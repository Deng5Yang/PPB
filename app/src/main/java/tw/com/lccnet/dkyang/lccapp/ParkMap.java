package tw.com.lccnet.dkyang.lccapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ParkMap extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    double lat, lng;
    String name;
    int rest;
    String[] arrLat, arrLng, arrRest, arrName;
    LocationManager locationManager;
    Location location;
    double latitude, longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle bundle = getIntent().getExtras();
        lat = Double.parseDouble(bundle.getString("lat"));
        lng = Double.parseDouble(bundle.getString("lng"));
        name = bundle.getString("name");
        rest = Integer.parseInt(bundle.getString("rest"));
        arrLat = bundle.getStringArray("arrLat");
        arrLng = bundle.getStringArray("arrLng");
        arrRest = bundle.getStringArray("arrRest");
        arrName = bundle.getStringArray("arrName");


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationInit();


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
        LatLng sydney = null;

        for (int i = 0; i < arrLat.length; i++) {
            //字串轉DOUBLE,因為LatLng裡面要放的值是DOUBLE
            lat = Double.parseDouble(arrLat[i]);
            lng = Double.parseDouble(arrLng[i]);
            name = String.valueOf(arrName[i]);


            sydney = new LatLng(lat, lng);

            if (arrRest[i] != null)
                rest = Integer.parseInt(arrRest[i]);
            else
                rest = 0;


            if (rest == 0) {
                mMap.addMarker(new MarkerOptions().position(sydney).title(name).snippet("目前空位 :" + 0));
            } else {
                //把標變綠色
                mMap.addMarker(new MarkerOptions().position(sydney).title(name).snippet("目前空位 :" + rest)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));


        //導航功能
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker args) {
                return null;
            }


            //進來後問要不要導航
            @Override
            public View getInfoContents(Marker args) {
                mMap.setOnInfoWindowClickListener(
                        new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {

                                AlertDialog.Builder dialog =
                                        new AlertDialog.Builder(ParkMap.this);
                                //setCancelable--->對話框是否可以取消
                                dialog.setTitle("導航").setMessage("是否導航").setCancelable(true);
                                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialoginterface, int i) {

                                        //自己寫的導航方法
                                        goDirection();

                                    }
                                });

                                dialog.setNegativeButton("Cancel", null);
                                dialog.show();

                            }
                        });
                return null;
            }
        });


    }


    public void goDirection() {


        //這裡直接給經緯度,寫死了
        Uri uri = Uri.parse("google.navigation:q=24.986525,121.515312\n");
        Intent mapit = new Intent(Intent.ACTION_VIEW, uri);

        //要使用下面這個app
        mapit.setPackage("com.google.android.apps.maps");

        //判斷有沒有裝這個app
        if (mapit.resolveActivity(getPackageManager()) != null) {
            startActivity(mapit);
        } else {

            //saddr--->出發點,daddr--->目的地
            Intent it = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com.tw/maps=?saddr="+ latitude + "," + longitude +
                            "& daddr=24.9865,121.5153"));
            startActivity(it);

        }


    }


    @Override
    public void onLocationChanged(Location location) {
        getLocation(location);
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

    protected void onResume() {
        super.onResume();

        //1000--->1000ms,5--->5meter
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) &
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                new AlertDialog.Builder(ParkMap.this)
                        .setMessage("需要定位權限")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                ActivityCompat.requestPermissions(ParkMap.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION},
                                        101);//101是自定義的KEY
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .show();
            } else {
                ActivityCompat.requestPermissions(ParkMap.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        101);
            }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, this);
    }

    private void locationInit() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION) &
                    ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_COARSE_LOCATION)){
                new AlertDialog.Builder(ParkMap.this)
                        .setMessage("需要定位權限")
                        .setPositiveButton("Yes" , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                ActivityCompat.requestPermissions(ParkMap.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION},
                                        101);//101是自定義的KEY
                            }
                        })
                        .setNegativeButton("No" , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .show();
            }else{
                ActivityCompat.requestPermissions(ParkMap.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        101);
            }
        }
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(location != null){
            getLocation(location);
        }else{
            //GPS抓不到的話改用網路抓
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            getLocation(location);
        }

    }


    private void getLocation(Location location){
        if(location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
    }





}
