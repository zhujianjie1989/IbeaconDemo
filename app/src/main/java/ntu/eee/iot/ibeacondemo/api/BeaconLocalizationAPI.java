package ntu.eee.iot.ibeacondemo.api;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import ntu.eee.iot.ibeacondemo.data.BeacondataCotrol;
import ntu.eee.iot.ibeacondemo.service.ScanBluetoothService;
import ntu.eee.iot.ibeacondemo.util.Util;
import ntu.eee.iot.ibeacondemo.pojo.Beacon;

/**
 * Created by zhujianjie on 2015/11/11.
 */
public class BeaconLocalizationAPI
{
    private Context context;
    private BeacondataCotrol beacondataCotrol;
    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("onServiceConnected","onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {}

    };

    private BroadcastReceiver receiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Log.e("BroadcastReceiver", intent.getAction());
            if (intent.getAction().equals(Util.BEACON_SCAN_ACTION))
            {
                Bundle bundle = intent.getExtras();
                String mac = bundle.getString("mac");
                int rssi = bundle.getInt("rssi");
                byte[] scanRecord = bundle.getByteArray("scanRecord");
                Beacon beacon = Util.paraScan(mac, rssi, scanRecord);
                beacondataCotrol.dealBeacon(beacon);
               // Log.e("BroadcastReceiver",beacon.toString());

                Class activiry = context.getClass();
                try {
                    Method method = activiry.getDeclaredMethod("showLog", String.class);
                    method.invoke(context,beacon.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

             if (intent.getAction().equals(Util.BEACON_CHANGE_FLOOR_ACTION))
            {
                Bundle bundle = intent.getExtras();
                int floor = bundle.getInt("floor");
                Class activiry = context.getClass();

                try {
                    Method method = activiry.getDeclaredMethod("changeFloor", int.class);
                    Log.e("BroadcastReceiver", "floor BroadcastReceiver = " + floor);
                    method.invoke(context,floor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    };

    public BeaconLocalizationAPI(Context context) {
        this.context = context;
        this.initBroadcastReceiverIntentFilter();
        this.initBeaconDataFromSQLiteDataBase();
    }

    public void bindService(){
        Intent intent = new Intent(context,ScanBluetoothService.class);
        context.bindService(intent, conn, context.BIND_AUTO_CREATE);
    }

    public void unbindserver(){
        context.unbindService(conn);
    }

    private void initBroadcastReceiverIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Util.BEACON_SCAN_ACTION);
        this.context.registerReceiver(receiver, filter);
    }

    private void initBeaconDataFromSQLiteDataBase(){
        beacondataCotrol = new BeacondataCotrol(context);
        List<Beacon> beacons = Util.readConf(context);
        beacondataCotrol.putBeacons(beacons);
    }

}
