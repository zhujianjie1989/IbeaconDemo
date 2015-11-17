package ntu.eee.iot.ibeacondemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import ntu.eee.iot.ibeacondemo.api.BeaconActivity;
import ntu.eee.iot.ibeacondemo.service.ScanBluetoothService;

public class MainActivity extends BeaconActivity
{

    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void LogE(String str) {
        Log.e(TAG,str);
    }

    @Override
    public void changeFloor(int floor) {
        TextView tx_floor = (TextView)findViewById(R.id.TX_FLOOR);
        tx_floor.setText(floor+" level");
    }

    @Override
    public void showPosition(double lat, double lng) {

    }

    @Override
    public void showLog(String msg) {
        TextView tx_log = (TextView)findViewById(R.id.TX_LOG);
        tx_log.setText(msg);
    }
}
