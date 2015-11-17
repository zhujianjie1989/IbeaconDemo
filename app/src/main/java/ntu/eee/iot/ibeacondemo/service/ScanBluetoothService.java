package ntu.eee.iot.ibeacondemo.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import ntu.eee.iot.ibeacondemo.util.Util;

public class ScanBluetoothService extends Service {

    private Timer timer;
    private BLEScanCallBack leScanCallback;
    private BluetoothAdapter BLEAdapter;

    public class LocalBinder extends Binder {
        public ScanBluetoothService getService() {
            // Return this instance of LocalService so clients can call public methods
            return ScanBluetoothService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {

        initBluetoothAdapter();
        initTimerAndTimerTask();
        return  new  LocalBinder();
    }

    private void  initBluetoothAdapter() {

        try {
            BLEAdapter = BluetoothAdapter.getDefaultAdapter();
            if (BLEAdapter == null || !BLEAdapter.isEnabled()) {
                throw new Exception("Bluetooth is not available");
            }
        }
        catch (Exception ex) {ex.printStackTrace();}
    }


    public void initTimerAndTimerTask() {

        if (leScanCallback==null) {
            leScanCallback = new BLEScanCallBack(this);
        }

        timer = new Timer();
        timer.schedule(task,0, 100);
    }


    private TimerTask task = new TimerTask()
    {
        @Override
        public void run() {

            BLEAdapter.startLeScan(leScanCallback);
            try {Thread.sleep(200);}
            catch (Exception e) {e.printStackTrace();}
            BLEAdapter.stopLeScan(leScanCallback);
        }
    };

    class BLEScanCallBack implements BluetoothAdapter.LeScanCallback {
        public Context content;

        public BLEScanCallBack(Context context) {
            this.content = context;
        }

        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {

            if (rssi > -20)
                return;

            Intent intent = new Intent();
            intent.setAction(Util.BEACON_SCAN_ACTION);

            Bundle bundle = new Bundle();
            bundle.putString("mac", device.getAddress());
            bundle.putInt("rssi", rssi);
            bundle.putByteArray("scanRecord", scanRecord);
            intent.putExtras(bundle);

            content.sendBroadcast(intent);
        }
    }

}
