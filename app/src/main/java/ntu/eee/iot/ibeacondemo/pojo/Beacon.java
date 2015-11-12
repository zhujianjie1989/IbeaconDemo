package ntu.eee.iot.ibeacondemo.pojo;

import java.util.HashMap;

public class Beacon {
	public String 	id;
	public String 	mac="";
	public String 	major;
	public String 	minor;
	public String 	uuid;
	public int 		rssi;
	public int 		txPower;
	public int 		max_rssi;
	public int 		floor;
	public int 		type;
	public int 		pipeNum;
	public long 	updateTime;

	private int pos = 0;
	private int length = 3 ;
	private int[] rssis = new int[length];


	public Beacon(){

		for (int i = 0 ;i < length;i++)
		{
			rssis[i]=-120;
		}
	}

	public Beacon(String ID, String UUID,
				  String Mac, String Major,
				  String Minor, int rssi, int TxPower)
	{

		this.id= ID;
		this.uuid= UUID;
		this.mac=Mac;
		this.major  =Major;
		this.minor = Minor;
		this.rssi= rssi;
		this.txPower = TxPower;
	}
	public void setRssi(int rssi)
	{
		rssis[pos] = rssi;
		pos= (pos+1)%length;
		int sum=0;
		for (int i = 0 ;i < length;i++)
		{
			sum+=rssis[i];
		}
		this.rssi = sum/length;
	}
}
