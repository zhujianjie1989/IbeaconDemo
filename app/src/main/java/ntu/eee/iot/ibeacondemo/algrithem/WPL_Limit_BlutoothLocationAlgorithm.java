package ntu.eee.iot.ibeacondemo.algrithem;

import android.os.Handler;
import java.util.ArrayList;
import java.util.List;
import ntu.eee.iot.ibeacondemo.pojo.Beacon;

public class WPL_Limit_BlutoothLocationAlgorithm extends BluetoothLocalizationAlgorithm {

	private int length=2;   // select a few number of signal

	@Override
	public void DoLocalization( )
	{
/*
		Double rssi_max=-40.0;
		Double rssi_min=-150.0;
		Double sumDef=0.0;
		double x= 0;
		double y =0;
		List<Beacon> sensorList = SortWifiSignal();

		if (sensorList.size()>0
				&& (sensorList.get(0).type == GlobalData.BeaconType.INDOOR.ordinal()
		|| sensorList.get(0).type == GlobalData.BeaconType.STAIRS.ordinal())){
			GlobalData.currentPosition = sensorList.get(0).position;
			GlobalData.calculateBeacons.clear();
			GlobalData.calculateBeacons.add(sensorList.get(0));
			return ;
		}


		cleanScanbeaconlist(sensorList);


		GlobalData.calculateBeacons.clear();
		if (sensorList.size()>=2){
			GlobalData.calculateBeacons.add(sensorList.get(0));
			GlobalData.calculateBeacons.add(sensorList.get(1));
		}else if (sensorList.size() > 0 ){
			GlobalData.calculateBeacons.add(sensorList.get(0));
		}



		int len=0;
		if (length>sensorList.size())
		{
			len=sensorList.size();
		}
		else {
			len=length;
		}

		for (int i = 0; i < len; i++) {
			Beacon sensor = sensorList.get(i);

			if (sensor.rssi <= -100)
				continue;

			rssi_max = sensor.max_rssi*1.0;

			Double tmprssi = rssi_max-sensor.rssi;
			if (tmprssi>rssi_max-rssi_min)
				tmprssi=rssi_max-rssi_min;
			Double def= 1.0 / Math.pow(10, (0.8 * tmprssi / 10));
			sumDef=sumDef+def;
			x= x + def * sensor.position.latitude;
			y= y + def * sensor.position.longitude;
		}

		if (sumDef == 0 )
			return;

		x = x / sumDef;
		y = y / sumDef;

		GlobalData.currentPosition = new LatLng(x,y);
*/

	}

	/*private  void cleanScanbeaconlist(List<Beacon> sensorList){
		List<Beacon> removeList = new ArrayList<>();
		for (int index = 0; index < sensorList.size() ; index++) {
			Beacon beacon = sensorList.get(index);
			switch (GlobalData.BeaconType.values()[beacon.type]){
				case  STAIRS:
				case  INDOOR:
					removeList.add(beacon);
					break;
				default:
					break;
			}
		}

		for (Beacon beacon : removeList) {
			sensorList.remove(beacon);
		}

	}

	private List<Beacon> SortWifiSignal()
	{
		List<Beacon> signalList = new ArrayList<Beacon>();

		Iterator<Entry<String, Beacon>> iter =GlobalData.scanbeaconlist.entrySet().iterator();  //用一个时间段内扫描到的beacon计算

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
	}*/

}
