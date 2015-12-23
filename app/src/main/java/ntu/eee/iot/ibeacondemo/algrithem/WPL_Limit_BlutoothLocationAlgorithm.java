package ntu.eee.iot.ibeacondemo.algrithem;

import android.os.Handler;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ntu.eee.iot.ibeacondemo.data.BeacondataCotrol;
import ntu.eee.iot.ibeacondemo.pojo.Beacon;

public class WPL_Limit_BlutoothLocationAlgorithm extends BluetoothLocalizationAlgorithm {
	public class Position{
		public double lat;
		public double lng;

		public void setLatLng(double lat,double lng)
		{
			this.lat = lat;
			this.lng = lng;
		}
	}

	private int length=2;   // select a few number of signal
	public  Position currentPosition = new Position();

	@Override
	public void DoLocalization( BeacondataCotrol beacondataCotrol)
	{
		Double rssi_max=-40.0;
		Double rssi_min=-150.0;
		Double sumDef=0.0;
		double x= 0;
		double y =0;
		List<Beacon> sensorList = SortWifiSignal(beacondataCotrol.beaconScanMap);

		if (sensorList.size()>0
				&& (sensorList.get(0).type == BeacondataCotrol.BeaconType.INDOOR.ordinal()
					|| sensorList.get(0).type == BeacondataCotrol.BeaconType.STAIRS.ordinal()))
		{
			currentPosition.setLatLng(sensorList.get(0).latitude, sensorList.get(0).longitude);
			beacondataCotrol.beaconScanMap.clear();
			beacondataCotrol.beaconScanMap.put(sensorList.get(0).id, sensorList.get(0));
			return ;
		}

		cleanScanbeaconlist(sensorList);
		beacondataCotrol.beaconScanMap.clear();
		if (sensorList.size()>=2)
		{
			beacondataCotrol.beaconScanMap.put(sensorList.get(0).id, sensorList.get(0));
			beacondataCotrol.beaconScanMap.put(sensorList.get(1).id, sensorList.get(1));
		}
		else if (sensorList.size() > 0 )
		{
			beacondataCotrol.beaconScanMap.put(sensorList.get(0).id, sensorList.get(0));
		}


		int len=0;
		if (length>sensorList.size())
		{
			len=sensorList.size();
		}
		else
		{
			len=length;
		}

		for (int i = 0; i < len; i++)
		{
			Beacon sensor = sensorList.get(i);

			if (sensor.rssi <= -100)
			{
				continue;
			}

			rssi_max = sensor.max_rssi*1.0;

			Double tmprssi = rssi_max-sensor.rssi;
			if (tmprssi>rssi_max-rssi_min)
			{
				tmprssi=rssi_max-rssi_min;
			}

			Double def= 1.0 / Math.pow(10, (0.8 * tmprssi / 10));
			sumDef=sumDef+def;
			x= x + def * sensor.latitude;
			y= y + def * sensor.longitude;
		}

		if (sumDef == 0 )
		{
			return;
		}

		x = x / sumDef;
		y = y / sumDef;

		currentPosition.setLatLng(x,y);

	}

	private  void cleanScanbeaconlist(List<Beacon> sensorList)
	{
		List<Beacon> removeList = new ArrayList<>();
		for (int index = 0; index < sensorList.size() ; index++)
		{
			Beacon beacon = sensorList.get(index);
			BeacondataCotrol.BeaconType type = BeacondataCotrol.BeaconType.values()[beacon.type];
			switch (type)
			{
				case  STAIRS:
				case  INDOOR:
					removeList.add(beacon);
					break;
				default:
					break;
			}
		}

		for (Beacon beacon : removeList)
		{
			sensorList.remove(beacon);
		}

	}

	private List<Beacon> SortWifiSignal(Map<String,Beacon> beaconMap)
	{
		List<Beacon> signalList = new ArrayList<Beacon>();

		//用一个时间段内扫描到的beacon计算
		Iterator<Entry<String, Beacon>> iter = beaconMap.entrySet().iterator();

		while (iter.hasNext())
		{
			Entry<String, Beacon> entry = (Entry<String, Beacon>) iter.next();
			Beacon sensor = entry.getValue();
			signalList.add(sensor);
		}


		for (int i = 0; i < signalList.size()-1; i++)
		{
			for (int j = i+1; j < signalList.size(); j++)
			{
				int rssi_i=signalList.get(i).rssi-signalList.get(i).max_rssi;
				int rssi_j=signalList.get(j).rssi-signalList.get(j).max_rssi;

				if (rssi_i<rssi_j)
				{
					Beacon s=signalList.remove(j);
					signalList.add(i, s);
				}
			}
		}

		return signalList;
	}

}
