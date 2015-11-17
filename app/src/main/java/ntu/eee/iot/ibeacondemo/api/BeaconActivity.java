package ntu.eee.iot.ibeacondemo.api;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import ntu.eee.iot.ibeacondemo.service.ScanBluetoothService;

/**
 * Created by zhujianjie on 2015/11/17.
 */
public abstract class BeaconActivity extends Activity {
     public abstract void changeFloor(int floor);
     public abstract void showPosition(double lat,double lng);
     public abstract void showLog(String msg);
     private BeaconLocalizationAPI api ;
     @Override
     protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          api = new BeaconLocalizationAPI(this);
     }
     @Override
     protected void onResume() {
          super.onResume();
         api.bindService();
     }

     @Override
     protected void onDestroy() {
          super.onDestroy();
          api.unbindserver();
     }
}
