package com.bleplugin;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ionicframework.hytapp30644240.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PopWindowForBLEList extends Activity {
  private ListView ble_list;
  private List<DeviceInfo> devices = new ArrayList<DeviceInfo>();
  public static final int RECEIVE_DEVICE = 0;
  public static final int CLOSE = 1;
  public static final int NO_SUPPORT = 2;
  public static final int SHOW_DIALOG = 3;
  public static final int DIMISS_DIALOG = 4;
  private BLEListAdapter adapter;
  private DiscoveryService.DiscoveryBinder discoveryService;
  private Button btnCancle;
  private ConnectService.BluetoothBinder bluetoothBinder;
  private Intent serviceIntent;
  private Handler handler = new Handler() {

    private LoadingDialog dialog;

    @Override
    public void dispatchMessage(Message msg) {
      switch (msg.what) {
        case RECEIVE_DEVICE:
          // 已经判断过是否是相同的了。
          Bundle bundle = msg.getData();
          DeviceInfo deviceInfo = (DeviceInfo) bundle.getSerializable("device");
          boolean currentTypeDevice = isCurrentTypeDevice(deviceInfo.DeviceName);
          if (currentTypeDevice) {
            devices.add(deviceInfo);
            adapter.notifyDataSetChanged();
          }

          break;
        case CLOSE:

          String obj = (String) msg.obj;
          setResult(0x789, getIntent().putExtra("DEVICE_NAME", obj));

          PopWindowForBLEList.this.finish();
          dialog.dismiss();
          bluetoothBinder.finishConnect();
          break;
        case NO_SUPPORT:
          Toast.makeText(PopWindowForBLEList.this, "不支持的设备", Toast.LENGTH_SHORT).show();
          break;
        case SHOW_DIALOG:
          dialog = new LoadingDialog(PopWindowForBLEList.this);
          dialog.setCanceledOnTouchOutside(false);
          dialog.show();
          break;

        case DIMISS_DIALOG:
          dialog.dismiss();
          break;

      }

    }
  };


  private ServiceConnection conn = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      discoveryService = ((DiscoveryService.DiscoveryBinder) service);
      discoveryService.setHandler(handler);

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
  };

  private ServiceConnection bluetoothConn = new ServiceConnection() {

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
      bluetoothBinder = (ConnectService.BluetoothBinder) iBinder;
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }
  };
  private Intent serviceIntent2;
  private int deviceID;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.popwindow_for_ble_list);
    deviceID = getIntent().getIntExtra("DeviceID", 0);
    ble_list = (ListView) findViewById(R.id.ble_list);
    adapter = new BLEListAdapter(this, devices);
    ble_list.setAdapter(adapter);
    btnCancle = (Button) findViewById(R.id.btn_cancel);
    btnCancle.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String deviceName = bluetoothBinder.getDeviceName();

        if (deviceName != null && !TextUtils.isEmpty(deviceName))
          setResult(0x789, getIntent().putExtra("DEVICE_NAME", deviceName));

        PopWindowForBLEList.this.finish();
      }
    });
    ble_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        final TextView tv_address = (TextView) view.findViewById(R.id.device_address);
        final TextView tv_name = (TextView) view.findViewById(R.id.device_name);

        new Thread() {
          @Override
          public void run() {
            // 显示进度
            Message mes1 = Message.obtain();
            mes1.what = SHOW_DIALOG;
            handler.sendMessage(mes1);

            BluetoothDevice bluetoothDevice = discoveryService.getBluetoothDevice(tv_name.getText().toString(), tv_address.getText().toString());
            if (bluetoothDevice != null) {
              Log.i("TAG1", Thread.currentThread().toString());
              boolean name = bluetoothBinder.connect(bluetoothDevice, deviceID);
              if (name) {
                Message mes = Message.obtain();
                mes.what = CLOSE;
                mes.obj = bluetoothDevice.getName();
                handler.sendMessage(mes);
                stopService(serviceIntent);
              } else {
                // 链接失败
              }

            } else {
              // 不支持的设备 提示
              Message mes = Message.obtain();
              mes.what = NO_SUPPORT;
              handler.sendMessage(mes);
              Log.i("TAG2", Thread.currentThread().toString());
            }
          }
        }.start();


      }
    });
    //开启服务
    serviceIntent = new Intent(this, DiscoveryService.class);
    this.bindService(serviceIntent, conn, Context.BIND_AUTO_CREATE);

    serviceIntent2 = new Intent(this, ConnectService.class);
    this.bindService(serviceIntent2, bluetoothConn, Context.BIND_AUTO_CREATE);
  }

 /* @Override
  protected void onDestroy() {
    super.onDestroy();
    stopService(serviceIntent);
  }*/


 // 通过当前要连接的设备的型号
  // 没有名字的蓝牙设备都不连接
  public boolean isCurrentTypeDevice(String name) {
    if (TextUtils.isEmpty(name)) {
      return false;
    }
    if (deviceID == 0) {
      // 目前 老机器的适配
      // 这里做法：剔除成都的机器，其它的机型都显示
      Matcher matcher = Pattern.compile(".*LPK.*").matcher(name);
      // LPK是目前成都的机器
      return !matcher.find();
    } else if (deviceID == 1){
      // 只显示成都的机器
      return Pattern.compile(".*LPK.*").matcher(name).find();
    }
    return false;
  }

  public Handler getHandler() {
    return this.handler;
  }

  public class BLEListAdapter extends BaseAdapter {

    private Context context;
    private List<DeviceInfo> data;

    public BLEListAdapter(Context context, List<DeviceInfo> data) {
      this.context = context;
      this.data = data;
    }

    @Override
    public int getCount() {
      return data.size();
    }

    @Override
    public Object getItem(int position) {
      return data.get(position);
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      ViewHolder viewHolder;
      if (convertView == null) {
        viewHolder = new ViewHolder();
        convertView = LayoutInflater.from(context).inflate(R.layout.popwindow_for_ble_list_item, null);
        viewHolder.DeviceName = (TextView) convertView.findViewById(R.id.device_name);
        viewHolder.DeviceAddress = (TextView) convertView.findViewById(R.id.device_address);
        convertView.setTag(viewHolder);
      } else {
        viewHolder = (ViewHolder) convertView.getTag();
      }
      DeviceInfo deviceInfo = data.get(position);
      viewHolder.DeviceName.setText(deviceInfo.DeviceName);
      viewHolder.DeviceAddress.setText(deviceInfo.DeviceAddress);
      return convertView;
    }
  }


  public class ViewHolder {
    public TextView DeviceName;
    public TextView DeviceAddress;
  }
}
