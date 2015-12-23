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
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import ntu.eee.iot.ibeacondemo.algrithem.WPL_Limit_BlutoothLocationAlgorithm;
import ntu.eee.iot.ibeacondemo.data.BeacondataCotrol;
import ntu.eee.iot.ibeacondemo.service.ScanBluetoothService;
import ntu.eee.iot.ibeacondemo.util.Util;
import ntu.eee.iot.ibeacondemo.pojo.Beacon;

/**
 * Created by zhujianjie on 2015/11/11.
 */
public class BeaconLocalizationAPI
{
    private BeaconActivity context;
    private BeacondataCotrol beacondataCotrol;
    private WPL_Limit_BlutoothLocationAlgorithm algorithm = new WPL_Limit_BlutoothLocationAlgorithm();
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
            if (intent.getAction().equals(Util.BEACON_SCAN_ACTION))
            {
                Bundle bundle = intent.getExtras();
                String mac = bundle.getString("mac");
                int rssi = bundle.getInt("rssi");
                byte[] scanRecord = bundle.getByteArray("scanRecord");
                Beacon beacon = Util.paraScan(mac, rssi, scanRecord);
                beacondataCotrol.dealBeacon(beacon);
            }
        }
    };

    public BeaconLocalizationAPI(BeaconActivity context)
    {
        this.context = context;
        this.initBroadcastReceiverIntentFilter();
        this.initBeaconDataFromSQLiteDataBase();
    }

    public void bindService()
    {
        Intent intent = new Intent(context,ScanBluetoothService.class);
        context.bindService(intent, conn, context.BIND_AUTO_CREATE);
    }

    public void unbindserver()
    {
        context.unbindService(conn);
        this.context.unregisterReceiver(receiver);
    }

    private void initBroadcastReceiverIntentFilter()
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Util.BEACON_SCAN_ACTION);
        this.context.registerReceiver(receiver, filter);
    }

    private void initBeaconDataFromSQLiteDataBase()
    {
        beacondataCotrol = new BeacondataCotrol(this);
        List<Beacon> beacons = Util.readConf(context);
        beacondataCotrol.putBeacons(beacons);
    }

    public void nofityChangeFloor(int floor)
    {
        context.notifyFloorChange(floor);
    }

    public void notifyPosition(double lat,double lng)
    {

        context.notifyShowPostion(lat,lng);
    }

    private int count = 0;
    private Timer doLocallizationTimer ;

    public  void startUpTimerToCalculatePostion()
    {
        if (doLocallizationTimer == null)
        {
            doLocallizationTimer = new Timer();
        }

        doLocallizationTimer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {

                if (count%3==0)
                {
                    beacondataCotrol.cleanValidBeacon();
                    Log.e("cleanValidBeacon","cleanValidBeacon");
                }

                algorithm.DoLocalization(beacondataCotrol);
                double lat = algorithm.currentPosition.lat;
                double lng = algorithm.currentPosition.lng;
                notifyPosition(lat,lng);
                count++;
            }
        },0,1000);
    }

}
