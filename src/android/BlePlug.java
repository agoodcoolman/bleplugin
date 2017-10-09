package com.bleplugin;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.text.TextUtils;
import android.widget.Toast;

import com.ionicframework.hytapp30644240.R;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import static com.bleplugin.OperationCmd.close;
import static com.ionicframework.hytapp30644240.R.id.device;

public class BlePlug extends CordovaPlugin {

    private Context context;
    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mBluetoothDevice;
    private CallbackContext callbackContext;
    private ConnectService.BluetoothBinder bluetoothBinder;

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

  @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        this.context = cordova.getActivity();
        // 检查当前手机是否支持ble 蓝牙,如果不支持退出程序
        if (!context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE)) {

            Toast.makeText(context, R.string.ble_not_supported, Toast.LENGTH_SHORT)
                    .show();
        }else{

            final BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = bluetoothManager.getAdapter();

            // 检查设备上是否支持蓝牙
            if (mBluetoothAdapter == null) {
                Toast.makeText(context, R.string.ble_not_supported,
                        Toast.LENGTH_SHORT).show();
            }

          serviceIntent2 = new Intent(this.context,ConnectService.class);
          this.context.bindService(serviceIntent2, bluetoothConn, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
      this.callbackContext = callbackContext;
        if (action.equals(OperationCmd.scan)) {
            if (mBluetoothAdapter != null) {
                // 为了确保设备上蓝牙能使用, 如果当前蓝牙设备没启用,弹出对话框向用户要求授予权限来启用
                if (!mBluetoothAdapter.isEnabled()) {
                    //
                    // 打开蓝牙设备管理器
                    Intent enableBtIntent = new Intent(
                            BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    ((Activity) context).startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
                int deviceID = args.getJSONObject(0).getInt("DeviceID");
                Intent intent = new Intent(context,PopWindowForBLEList.class);
                intent.putExtra("DeviceID", deviceID);
                this.cordova.startActivityForResult(this, intent, 0x888);
                return true;
            }
        }else if(action.equals(OperationCmd.startPageZhike)){ //设置页面大小
             int pageWidth = 600;
             int pageHeight =430;

             if(!args.getJSONObject(0).isNull("pageWidth")){
                 pageWidth = args.getJSONObject(0).getInt("pageWidth");
             }
             if(!args.getJSONObject(0).isNull("pageHeight")){
                 pageHeight = args.getJSONObject(0).getInt("pageHeight");
             }

            bluetoothBinder.create(pageWidth, pageHeight);
            callbackContext.success("ok");
            return true;
        }else if (action.equals(OperationCmd.printTextZhike)) {
            int x = args.getJSONObject(1).getInt("x");
            int y = args.getJSONObject(1).getInt("y");
            String text = args.get(0).toString();
            String fontName = args.getJSONObject(1).getString("fontName");
            int textSize = args.getJSONObject(1).getInt("textSize");
            boolean bold = args.getJSONObject(1).getBoolean("bold");
            boolean rotate = args.getJSONObject(1).getBoolean("rotate");
            bluetoothBinder.drawText(x,y,text,fontName,textSize,bold,rotate);
            callbackContext.success("ok");
            return true;
        } else if (action.equals(OperationCmd.doPrint)) {
            bluetoothBinder.doPrint();
            callbackContext.success("ok");
            return true;
        }  else if(action.equals(OperationCmd.printBarcodeZhike)){
            int x = args.getJSONObject(1).getInt("x");
            int y = args.getJSONObject(1).getInt("y");
            String text = args.get(0).toString();
            String type = args.getJSONObject(1).getString("type");
            int width = args.getJSONObject(1).getInt("width");
            int height = args.getJSONObject(1).getInt("height");
            boolean rotate = args.getJSONObject(1).getBoolean("rotate");

            bluetoothBinder.drawBarcode1D(x,y,text,type,width,height,rotate);
            callbackContext.success("ok");
            return true;
        }else if(action.equals(OperationCmd.printQRcodeZhike)){
            int x = args.getJSONObject(1).getInt("x");
            int y = args.getJSONObject(1).getInt("y");
            String text = args.get(0).toString();
            int size = args.getJSONObject(1).getInt("size");
            bluetoothBinder.drawBarcodeQRcode(x,y,text,size);
            callbackContext.success("ok");
            return true;
        }else if(action.equals(OperationCmd.bluetoothName)){
          if (bluetoothBinder != null) {
            String deviceName = bluetoothBinder.getDeviceName();
            if(!TextUtils.isEmpty(deviceName)){
              callbackContext.success(deviceName);

            }else{
              callbackContext.error("未连接蓝牙设备");
            }
          } else {
            callbackContext.error("未连接蓝牙设备");
          }

          cordova.getActivity().stopService(serviceIntent2);
          return true;
        }else if(action.equals(OperationCmd.gotoMarklabel)){
            bluetoothBinder.gotoMarklabel(250);
            callbackContext.success("ok");
            return true;
        }else if(action.equals(OperationCmd.multilineText)){
            int x = args.getJSONObject(1).getInt("x");
            int y = args.getJSONObject(1).getInt("y");
            String text = args.get(0).toString();
            int width = args.getJSONObject(1).getInt("width");
            int height = args.getJSONObject(1).getInt("height");
            int fontSize = args.getJSONObject(1).getInt("fontSize");
            int bold = args.getJSONObject(1).getInt("bold");
            int rotate = args.getJSONObject(1).getInt("rotate");
            boolean underline = args.getJSONObject(1).getBoolean("underline");
            boolean reverse = args.getJSONObject(1).getBoolean("reverse");
            bluetoothBinder.multilineText(x,y,width,height,text,fontSize,rotate,bold,underline,reverse);
            callbackContext.success("ok");
        } else if (action.equals(OperationCmd.rect)) {
          int left = args.getJSONObject(0).getInt("left");
          int top = args.getJSONObject(0).getInt("top");
          int right = args.getJSONObject(0).getInt("right");
          int bottom = args.getJSONObject(0).getInt("bottom");
          int linewidth = args.getJSONObject(0).getInt("linewidth");

          bluetoothBinder.drawRect(left, top, right, bottom, linewidth);

          callbackContext.success("ok");
          return true;
        }else if (action.equals(OperationCmd.line)) {
          int x0 = args.getJSONObject(0).getInt("x0");
          int y0 = args.getJSONObject(0).getInt("y0");
          int x1 = args.getJSONObject(0).getInt("x1");
          int y1 = args.getJSONObject(0).getInt("y1");
          int linewidth = args.getJSONObject(0).getInt("linewidth");
          bluetoothBinder.drawLine(x0, y0, x1, y1, linewidth);
          callbackContext.success("ok");

          return true;
        }else if(action.equals(OperationCmd.close)) {
          bluetoothBinder.closeConnectDevice();
          this.cordova.getActivity().unbindService(bluetoothConn);
          return true;
        }
        return false;
    }

    @Override
  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    super.onActivityResult(requestCode, resultCode, intent);

    if (requestCode == 0x888 && intent != null) {
      String device_name = intent.getStringExtra("DEVICE_NAME");
      if (device_name != null)
        this.callbackContext.success(device_name);
      else
        this.callbackContext.error("error");
    } else {
      this.callbackContext.error("error");
    }
  }
}
