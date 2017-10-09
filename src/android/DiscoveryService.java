package com.bleplugin;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by truly on 2016/4/22.
 */
public class DiscoveryService extends Service {

    private Handler handler;
    protected BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    DiscoveryBinder discoveryBinder = new DiscoveryBinder();
    public List<BluetoothDevice> devices = Collections.synchronizedList(new ArrayList<BluetoothDevice>());

    @Override
    public IBinder onBind(Intent intent) {
        IntentFilter discoveryFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(discoveryReceiver, discoveryFilter);
        IntentFilter foundFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(foundReceiver, foundFilter);
        BluetoothAdapter.getDefaultAdapter().startDiscovery();
        return discoveryBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        unregisterReceiver(discoveryReceiver);
        unregisterReceiver(foundReceiver);
        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class DiscoveryBinder extends Binder{
        public DiscoveryService getService(){
            return DiscoveryService.this;
        }

        public void setHandler(Handler h) {
          handler = h;
        }

        public BluetoothDevice getBluetoothDevice(String DeviceName, String DeviceAddress) {
          for(int i = 0; i < devices.size(); i ++){
            BluetoothDevice single = devices.get(i);
            try {
              if(single.getAddress().equals(DeviceAddress) && single.getName().equals(DeviceName)){
                return single;
              }
            } catch (Exception e) {
              e.printStackTrace();
              return null;
            }
          }
          return null;
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    private BroadcastReceiver foundReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            boolean isExists = false;
            for(int i = 0; i < devices.size(); i ++){
                if(devices.get(i).getAddress().equals(device.getAddress())){
                    isExists = true;
                    break;
                }
            }
            // 不在列表中才去添加
            if(!isExists){
                devices.add(device);
                DeviceInfo deviceInfo = new DeviceInfo();
                deviceInfo.DeviceName = device.getName();
                deviceInfo.DeviceAddress = device.getAddress();

                Message msg = Message.obtain();
                msg.what = PopWindowForBLEList.RECEIVE_DEVICE;
                Bundle bundle = new Bundle();
                bundle.putSerializable("device",deviceInfo);
                msg.setData(bundle);
                handler.sendMessage(msg);
            }


        }
    };

    private BroadcastReceiver discoveryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
             /*unregisterReceiver(foundReceiver);
             unregisterReceiver(this);*/

        }
    };
  public boolean isEnable(){
    return bluetoothAdapter.isEnabled();
  }

}
