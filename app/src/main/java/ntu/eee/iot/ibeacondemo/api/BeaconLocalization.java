package ntu.eee.iot.ibeacondemo.api;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import ntu.eee.iot.ibeacondemo.data.BeacondataCotrol;
import ntu.eee.iot.ibeacondemo.util.Util;
import ntu.eee.iot.ibeacondemo.pojo.Beacon;

/**
 * Created by zhujianjie on 2015/11/11.
 */
public class BeaconLocalization
{
    private Context context;
    private BeacondataCotrol beacondataCotrol = new BeacondataCotrol();

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

    public BeaconLocalization(Context context)
    {
        this.context = context;
        initReceiver();
    }

    private void initReceiver()
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Util.BEACON_SCAN_ACTION);
        this.context.registerReceiver(receiver, filter);
    }

}
