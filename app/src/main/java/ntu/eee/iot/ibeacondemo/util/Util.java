package ntu.eee.iot.ibeacondemo.util;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ntu.eee.iot.ibeacondemo.pojo.Beacon;

/**
 * Created by zhujianjie on 2015/11/11.
 */
public class Util
{
    public static final String BEACON_SCAN_ACTION = "ntu.eee.iot.ibeacondemo.BeaconScanAction";
    public static final String BEACON_CHANGE_FLOOR_ACTION = "ntu.eee.iot.ibeacondemo.ChangeFloorAction";
    public static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static Beacon paraScan(String mac, int rssi, byte[] scanRecord)
    {
        int startByte = 2;
        while (startByte <= 5)
        {
            if (((int) scanRecord[startByte + 2] & 0xff) == 0x02
                    && ((int) scanRecord[startByte + 3] & 0xff) == 0x15)
            {
                break;
            }
            startByte++;
        }

        byte[] uuidBytes = new byte[16];
        System.arraycopy(scanRecord, startByte + 4, uuidBytes, 0, 16);
        String hexString = bytesToHex(uuidBytes);

        String uuid = hexString.substring( 0,  8) + "-"
                + hexString.substring( 8, 12) + "-"
                + hexString.substring(12, 16) + "-"
                + hexString.substring(16, 20) + "-"
                + hexString.substring(20, 32);

        int major = (scanRecord[startByte + 20] & 0xff)
                * 0x100 + (scanRecord[startByte + 21] & 0xff);

        int minor = (scanRecord[startByte + 22] & 0xff)
                * 0x100 + (scanRecord[startByte + 23] & 0xff);

        int txPower = (scanRecord[startByte + 24]);

        Beacon beacon = new Beacon(major+""+minor, uuid,mac,major+"",minor+"",rssi,txPower);

        return  beacon;

    }

    private  static String bytesToHex(byte[] bytes)
    {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++)
        {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }


    public static List<Beacon> readConf(Context context) {

        String db_name = "BLEdevice.db";
        DatabaseContext dbContext = new DatabaseContext(context);
        SQLiteHelper helper = new SQLiteHelper(dbContext,db_name);

        List<Beacon> beacons = helper.selectAllBeacon();
        for (Beacon beacon : beacons) {
            LogE(beacon.toString());
        }
        return beacons;
    }

    private static String TAG = "MainActivity  Util";

    private static void LogE(String str)
    {
        Log.e(TAG,str);
    }


    public static  String path = "/sdcard/sensorInfo.txt";

    public static List<Beacon> ReadConfigFile2() {

        List<Beacon> beacons = new ArrayList();
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader br = new BufferedReader(fileReader);

            String data = br.readLine();
            while( data != null ) {
                Beacon sensor = new Beacon();
                String[] info = data.split(",");

                sensor.id = info[0];
                sensor.major= info[1];
                sensor.minor= info[2];
                sensor.latitude = Double.parseDouble(info[3]);
                sensor.longitude = Double.parseDouble(info[4]);
                sensor.floor =Integer.parseInt(info[5]);
                sensor.max_rssi = Integer.parseInt(info[6]);
                beacons.add(sensor);
                Log.e("ReadConfigFile ", sensor.toString());
                data = br.readLine();
            }

            br.close();
        }
        catch (IOException e) {e.printStackTrace();}

        return  beacons;
    }
}
