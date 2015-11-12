package ntu.eee.iot.ibeacondemo.service;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import ntu.eee.iot.ibeacondemo.util.Util;

/**
 * Created by zhujianjie on 3/6/2015.
 */
public class BLEScanCallBack implements BluetoothAdapter.LeScanCallback
{
    public  Context content;

    public BLEScanCallBack(Context context)
    {
        this.content = context;
    }

    @Override
    public void onLeScan( BluetoothDevice device, int rssi,  byte[] scanRecord)
    {

        if (rssi > -20)
            return;

        Intent intent = new Intent();
        intent.setAction(Util.BEACON_SCAN_ACTION);

        Bundle bundle = new Bundle();
        bundle.putString("mac",device.getAddress());
        bundle.putInt("rssi", rssi);
        bundle.putByteArray("scanRecord", scanRecord);
        intent.putExtras(bundle);

        content.sendBroadcast(intent);
    }


}
