package ntu.eee.iot.ibeacondemo.pojo;

import java.util.Date;

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
	public double   latitude;
	public double 	longitude;

	private int pos = 0;
	private int length = 2 ;
	private int[] rssis = new int[length];


	public Beacon(){

		for (int i = 0 ;i < length;i++)
		{
			rssis[i]=-120;
		}
	}

	public Beacon(String ID, String UUID, String Mac,
				  String Major, String Minor, int rssi, int TxPower)
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
	public  String  toString()
	{
		return id+"\t"+mac+"\t"+major+"\t"+minor+"\t"+latitude+"\t"+longitude+"\t"+floor;
	}

	public  String  toString1()
	{
		return "major = "+major+" minor = "+minor+"  rssi = "+(rssi-max_rssi);
	}

	public void updateBeaconStatu(Beacon beacon)
	{
		this.setRssi(beacon.rssi);
		this.updateTime = new Date().getTime();
	}
}
