package tw.com.lccnet.dkyang.lccapp;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Array;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String[] arrLat,arrLng,busLng,busLat,busStop;
    double lat,lng;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Bundle bundle = getIntent().getExtras();

        //站牌
        arrLat = bundle.getStringArray("lat");
        arrLng = bundle.getStringArray("lng");
        busStop = bundle.getStringArray("busstop");

        //車
        busLat = bundle.getStringArray("buslat");
        busLng = bundle.getStringArray("buslng");







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
    //要扎針的話一定要寫在onMapReady這個方法裡面
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //扎針


        LatLng sydney=null;

        for(int i=0;i<arrLat.length;i++) {
            //字串轉DOUBLE,因為LatLng裡面要放的值是DOUBLE
            lat = Double.parseDouble(arrLat[i]);
            lng = Double.parseDouble(arrLng[i]);

            sydney = new LatLng(lat,lng);

            //snippet可以在標題下面有文字(跟說明差不多)
            mMap.addMarker(new MarkerOptions().position(sydney).title(busStop[i])
                    .snippet("說明")
                    .icon(BitmapDescriptorFactory//設定圖
                            .fromResource(R.mipmap.station)));

        }

        for(int i=0;i<busLat.length;i++) {
            //字串轉DOUBLE,因為LatLng裡面要放的值是DOUBLE
            lat = Double.parseDouble(busLat[i]);
            lng = Double.parseDouble(busLng[i]);

            sydney = new LatLng(lat,lng);


            mMap.addMarker(new MarkerOptions().position(sydney)
                    .icon(BitmapDescriptorFactory//設定圖
                            .fromResource(R.mipmap.bus_icon)));

        }


        //newLatLngZoom(中心點,縮放值->>數字越小越遠)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,12));
    }
}
