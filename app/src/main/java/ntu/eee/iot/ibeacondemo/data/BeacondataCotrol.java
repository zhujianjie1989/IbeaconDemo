package ntu.eee.iot.ibeacondemo.data;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ntu.eee.iot.ibeacondemo.api.BeaconActivity;
import ntu.eee.iot.ibeacondemo.api.BeaconLocalizationAPI;
import ntu.eee.iot.ibeacondemo.pojo.Beacon;
import ntu.eee.iot.ibeacondemo.util.Util;

/**
 * Created by zhujianjie on 2015/11/11.
 */
public class BeacondataCotrol
{
    private Map<String,Beacon> beaconMap;
    public Map<String,Beacon> beaconScanMap;
    private BeaconLocalizationAPI api ;
    private Date Scan_UpdateTime;
    public static enum BeaconType {OUTDOOR,INDOOR,STAIRS,ELEVATOR};

    public BeacondataCotrol(BeaconLocalizationAPI Bapi)
    {
        this.api= Bapi;
        beaconMap =  new HashMap();
        beaconScanMap =  new HashMap();
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

    private int curr_floor = -1;
    public void dealBeacon(Beacon beacon)
    {
        if (beaconMap.containsKey(beacon.id))
        {
            Beacon sensor =  beaconMap.get(beacon.id);
            sensor.updateBeaconStatu(beacon);
            Log.e("CHANGE_FLOOR_ACTION", "floor = " + curr_floor);


            if ((sensor.rssi - sensor.max_rssi) > -27)
            {
                //添加到当前可扫描到的beacon列表
                beaconScanMap.put(sensor.id,sensor);
                //更新扫描到beacon的时间
                Scan_UpdateTime = new Date();
            }

            //判断是否楼成切换
            if (curr_floor != sensor.floor && (sensor.rssi - sensor.max_rssi) > -27 )
            {
                curr_floor = sensor.floor;
                api.nofityChangeFloor(curr_floor);
            }
        }

    }

    public void cleanValidBeacon()
    {
        Date now = new Date();

        //用一个时间段内扫描到的beacon计算
        Iterator<String> iter = beaconScanMap.keySet().iterator();
        List<Beacon> removeList = new ArrayList<>();
        while (iter.hasNext())
        {
            String key =  iter.next();
            Beacon sensor = beaconScanMap.get(key);
            if (now.getTime() - sensor.updateTime > 2000)
            {
                removeList.add(sensor);
            }
        }

        for (int index = 0 ; index < removeList.size();index++)
        {
            Beacon beacon = removeList.get(index);
            beaconScanMap.remove(beacon.id);
        }

        removeList =null;
    }

    public Beacon getMaxBeacon(){
        int max = -10000;
        Beacon maxBeacon = null;
        Iterator<String> iter = beaconScanMap.keySet().iterator();
        List<Beacon> removeList = new ArrayList<>();
        while (iter.hasNext())
        {
            String key =  iter.next();
            Beacon sensor = beaconScanMap.get(key);
            if (sensor.rssi >max)
            {
                maxBeacon = sensor;
                max = sensor.rssi;
            }
        }

        return  maxBeacon;
    }

    public String getALL(){
        String str = "";
        Iterator<String> iter = beaconScanMap.keySet().iterator();
        List<Beacon> removeList = new ArrayList<>();
        while (iter.hasNext())
        {
            String key =  iter.next();
            Beacon sensor = beaconScanMap.get(key);
            str += sensor.toString1()+"\n";
        }
        return  str;
    }

    public int getSize()
    {
        return  beaconScanMap.size();
    }


}
