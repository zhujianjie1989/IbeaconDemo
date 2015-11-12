package ntu.eee.iot.ibeacondemo.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import java.util.Timer;
import java.util.TimerTask;

public class ScanBluetoothService extends Service
{
    private Timer timer;
    private BLEScanCallBack leScanCallback;
    private BluetoothAdapter bluetoothAdapter;

    @Override
    public IBinder onBind(Intent intent)
    {
        initBlueTooth();
        initTask();
        return  new Binder();
    }

    private void  initBlueTooth()
    {
        try
        {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled())
            {
                throw new Exception("Bluetooth is not available");
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }


    public void initTask()
    {

        if (leScanCallback==null)
        {
            leScanCallback = new BLEScanCallBack(this);
        }

        timer = new Timer();
        timer.schedule(task,0, 100);
    }


    private TimerTask task = new TimerTask()
    {
        @Override
        public void run()
        {
            bluetoothAdapter.startLeScan(leScanCallback);

            try
            {
                Thread.sleep(200);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            bluetoothAdapter.stopLeScan(leScanCallback);

        }
    };

}
