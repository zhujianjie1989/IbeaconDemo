package ntu.eee.iot.ibeacondemo.data;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import ntu.eee.iot.ibeacondemo.pojo.Beacon;

/**
 * Created by zhujianjie on 2015/11/11.
 */
public class BeacondataCotrol
{
    private Map<String,Beacon> beaconMap = new HashMap();

    public void putBeacon(Beacon beacon)
    {
        beaconMap.put(beacon.id,beacon);
    }

    public void updataBeaconRssi(Beacon beacon)
    {
        beaconMap.get(beacon.id).rssi = beacon.rssi;
    }
    public void dealBeacon(Beacon beacon)
    {

    }


}
