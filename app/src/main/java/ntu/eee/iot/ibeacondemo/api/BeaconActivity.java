package ntu.eee.iot.ibeacondemo.api;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


import ntu.eee.iot.ibeacondemo.service.ScanBluetoothService;

/**
 * Created by zhujianjie on 2015/11/17.
 */
public abstract class BeaconActivity extends Activity {
     public abstract void changeFloor(int floor);
     public abstract void showPosition(double lat,double lng);
     public abstract void showLog(String msg);
     private BeaconLocalizationAPI api ;
     private Handler handler = new Handler()
     {
          @Override
          public void handleMessage(Message msg) {
               super.handleMessage(msg);
               if (msg.arg1==1)
               {
                    Bundle bundle = msg.getData();
                    double lat = bundle.getDouble("lat");
                    double lng = bundle.getDouble("lng");
                    showPosition(lat,lng);
               }else if (msg.arg1==2)
               {
                    Bundle bundle = msg.getData();
                    int floor = bundle.getInt("floor");
                    changeFloor(floor);
               }
               else if (msg.arg1==3)
               {

                   showLog((String )msg.obj);
               }

          }
     };
     @Override
     protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          api = new BeaconLocalizationAPI(this);
     }
     @Override
     protected void onResume() {
          super.onResume();
          api.bindService();
          api.startUpTimerToCalculatePostion();
     }

     @Override
     protected void onDestroy() {
          super.onDestroy();
          api.unbindserver();
     }

     public void notifyShowPostion(double lat,double lng){
          Message msg = new Message();
          msg.arg1=1;
          Bundle bundle = new Bundle();
          bundle.putDouble("lat",lat);
          bundle.putDouble("lng",lng);
          msg.setData(bundle);
          handler.sendMessage(msg);
     }

     public void notifyFloorChange(int floor)
     {
          Message msg = new Message();
          msg.arg1=2;
          Bundle bundle = new Bundle();
          bundle.putInt("floor", floor);
          msg.setData(bundle);
          handler.sendMessage(msg);
     }

     public void notifyshowLog(String log)
     {
          Message msg = new Message();
          msg.arg1=3;
          msg.obj = log;
          handler.sendMessage(msg);
     }
}
