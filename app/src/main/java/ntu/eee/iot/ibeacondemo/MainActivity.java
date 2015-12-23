package ntu.eee.iot.ibeacondemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.IBinder;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import ntu.eee.iot.ibeacondemo.api.BeaconActivity;
import ntu.eee.iot.ibeacondemo.service.ScanBluetoothService;

public class MainActivity extends BeaconActivity
{

    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initMap();
    }

    private void LogE(String str) {
        Log.e(TAG, str);
    }

    @Override
    public void changeFloor(int floor) {
        TextView tx_floor = (TextView)findViewById(R.id.TX_FLOOR);
        tx_floor.setText(floor + " level");
    }

    @Override
    public void showPosition(double lat, double lng) {
        LogE(lat + "    " + lng);
        if (marker!=null)
        {
            marker.remove();
        }

        marker = map.addCircle(new CircleOptions().center(new LatLng(lat,lng))
                .fillColor(Color.argb(100, 147, 112, 219)).radius(1).strokeWidth(0));
    }

    @Override
    public void showLog(String msg) {
        TextView tx_log = (TextView)findViewById(R.id.TX_LOG);
        tx_log.setText(msg);
    }
    private GoogleMap map;
    private GroundOverlay buildingMapImage =null;
    private Circle marker=null;
    /**
     * 初始化地图
     */
    private void  initMap() {

        map=((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setIndoorEnabled(true);
        //map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        map.getUiSettings().setIndoorLevelPickerEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);

        buildingMapImage = map.addGroundOverlay(new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.k44)).anchor(0,0).bearing(-45f)
                .position(new LatLng(1.342518999,103.679474999),188,23f));

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(1.342518999,103.679474999), 23);
        map.moveCamera(update);

    }
}
