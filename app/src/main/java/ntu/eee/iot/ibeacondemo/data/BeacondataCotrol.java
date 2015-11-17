package ntu.eee.iot.ibeacondemo.data;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import ntu.eee.iot.ibeacondemo.pojo.Beacon;
import ntu.eee.iot.ibeacondemo.util.Util;

/**
 * Created by zhujianjie on 2015/11/11.
 */
public class BeacondataCotrol
{
    private Map<String,Beacon> beaconMap;
    private Context context ;

    public BeacondataCotrol(Context context)
    {
        this.context= context;
        beaconMap =  new HashMap();
    }

    public void putBeacon(Beacon beacon)
    {
        beaconMap.put(beacon.id,beacon);
    }

    public void putBeacons(List<Beacon> list)
    {
        for (Beacon beacon :list)
        {
            beaconMap.put(beacon.id,beacon);
        }
    }

    public void updataBeaconRssi(Beacon beacon)
    {
        beaconMap.get(beacon.id).rssi = beacon.rssi;
    }

    private int curr_floor = -1;
    public void dealBeacon(Beacon beacon)
    {
        Intent intent1 = new Intent();
        intent1.setAction(Util.BEACON_CHANGE_FLOOR_ACTION);
        this.context.sendBroadcast(intent1);

        Log.e("CHANGE_FLOOR_ACTION", "floor = " + curr_floor);
        if (beaconMap.containsKey(beacon.id))
        {
           // Log.e("CHANGE_FLOOR_ACTION", "beaconMap.containsKey(beacon.id)"+beaconMap.containsKey(beacon.id));
            Beacon sensor =  beaconMap.get(beacon.id);
            sensor.updateBeaconStatu(beacon);

            //判断是否楼成切换
            if (curr_floor != sensor.floor /*&& (sensor.rssi - sensor.max_rssi) > -27*/ )
            {
                curr_floor = sensor.floor;
              //  Log.e("CHANGE_FLOOR_ACTION", "floor = " + curr_floor);
                Intent intent = new Intent();
                intent.setAction(Util.BEACON_CHANGE_FLOOR_ACTION);

                Bundle bundle = new Bundle();
                bundle.putInt("floor", curr_floor);
                intent.putExtras(bundle);

                context.sendBroadcast(intent);
              //  Log.e("CHANGE_FLOOR_ACTION", "floor = " + curr_floor);
            }
        }

    }


}
