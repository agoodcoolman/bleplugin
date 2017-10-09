package com.bleplugin;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;

import com.application.sdk.DataOperater;
import com.application.sdk.Printer;
import com.application.sdk.PrinterObject;
import com.ivt.bluetooth.ibridge.Ancs.AncsUtils;
import com.ivt.bluetooth.ibridge.BluetoothIBridgeAdapter;
import com.ivt.bluetooth.ibridge.BluetoothIBridgeDevice;

import java.io.UnsupportedEncodingException;

/**
 * Created by jin on 2017/9/7.
 * LPK130X 热敏标签打印机
 */

public class FiJistuPrintManger extends AbstrPrint implements BluetoothIBridgeAdapter.EventReceiver {

  private PowerManager.WakeLock mWakeLock;
  private PrinterObject printerObject;
  private Printer printer;
  private BluetoothIBridgeDevice mSelectedDevice;
  private BluetoothIBridgeAdapter mAdapter;
  private DataOperater dataOperater;
  private int factor = 10;
  private FiJistuPrintManger(){}
  private static FiJistuPrintManger instance;

  public static FiJistuPrintManger getInstance(){
    if(instance == null) {
      synchronized (FiJistuPrintManger.class) {
        if (instance == null)
          instance = new FiJistuPrintManger();
      }
    }
    return instance;
  }


  @Override
  public boolean connect(BluetoothDevice device) {
      // 这个不加的话，内部会报错，加了这里内部SDK就不报错，不可以删除
      Looper.prepare();
      mAdapter = BluetoothIBridgeAdapter.sharedInstance(context);
      if(!mAdapter.isEnabled()){
        mAdapter.setEnabled(true);
      }
      if(Build.VERSION.SDK_INT >= 10){
        mAdapter.setLinkKeyNeedAuthenticated(false);
      }else{
        mAdapter.setLinkKeyNeedAuthenticated(true);
      }
      acquireWakeLock();
      android.util.Log.i("TestService", "onCreate");
      mAdapter.registerEventReceiver(this);

      mAdapter.ancsAddAppToWhiteList(AncsUtils.APP_PACKAGE_NAME_INCOMING_CALL, "Incoming Call", "refuse", "accept");
      mAdapter.ancsAddAppToWhiteList(AncsUtils.APP_PACKAGE_NAME_MISS_CALL, "Miss Call", "clear", "dial");
      mAdapter.ancsAddAppToWhiteList(AncsUtils.APP_PACKAGE_NAME_SMS, "SMS", "Clear", null);
      mAdapter.ancsRegisterReceiver(new BluetoothIBridgeAdapter.AncsReceiver() {
        @Override
        public void onPerformNotificationAction(String appIdentifier, byte actionID) {
          if (appIdentifier.equals(AncsUtils.APP_PACKAGE_NAME_INCOMING_CALL)) {
            if (actionID == AncsUtils.ACTION_ID_POSITIVE) {

              android.util.Log.i("TestService", "accept incoming call here");
            } else if (actionID == AncsUtils.ACTION_ID_NEGATICE) {

              android.util.Log.i("TestService", "refuse incoming call here");
            }
          } if (appIdentifier.equals(AncsUtils.APP_PACKAGE_NAME_MISS_CALL)) {
            if (actionID == AncsUtils.ACTION_ID_POSITIVE) {

              android.util.Log.i("TestService", "dial");
            } else if (actionID == AncsUtils.ACTION_ID_NEGATICE) {

              android.util.Log.i("TestService", "clear");
            }
          } else if (appIdentifier.equals(AncsUtils.APP_PACKAGE_NAME_SMS)) {
            if (actionID == AncsUtils.ACTION_ID_NEGATICE) {

              android.util.Log.i("TestService", "clear");
            }
          } else {
            if (actionID == AncsUtils.ACTION_ID_NEGATICE) {

              android.util.Log.i("TestService", "clear");
            } else if (actionID == AncsUtils.ACTION_ID_POSITIVE) {
              PackageManager packageManager = context.getPackageManager();
              Intent intent = new Intent();
              intent = packageManager.getLaunchIntentForPackage(appIdentifier);
              if (intent != null) {

              } else {
                android.util.Log.i("TestService", "APP not found!");
              }
            }
          }
        }
      });
    mAdapter.setAutoBondBeforConnect(true);
    mAdapter.setAutoWritePincode(true);
    mAdapter.setPincode("1234");
    mSelectedDevice = BluetoothIBridgeDevice.createBluetoothIBridgeDevice(device.getAddress(), BluetoothIBridgeDevice.DEVICE_TYPE_CLASSIC);
    boolean b = mAdapter.connectDevice(mSelectedDevice);
    return b;
  }

  @Override
  public void createPager(int pageWidth, int pageHeight) {
    printerObject = new PrinterObject();
    printer = new Printer();
    if (mSelectedDevice.getDeviceType() == BluetoothIBridgeDevice.DEVICE_TYPE_BLE) {
                      /*  mAdapter.bleSetTargetUUIDs(mSelectedDevice, "49535343-fe7d-4ae5-8fa9-9fafd205e455"
                                , "49535343-1e4d-4bd9-ba61-23c647249616", "49535343-8841-43f4-a8d4-ecbe34729bb3"); */
      mAdapter.bleSetTargetUUIDs(mSelectedDevice, "1b7e8251-2877-41c3-b46e-cf057c562023"
        , "8ac32d3f-5cb9-4d44-bec2-ee689169f626", "5e9bf2a8-f93f-4481-a67e-3b2f4a07891a");
    }

    dataOperater = new DataOperater(mSelectedDevice, mAdapter);
    printerObject.add(printer.createPage(pageWidth, pageHeight));


  }

  @Override
  public void drawText(int x, int y, String text, String font, int textSize, boolean bold, boolean rotate) {
    // 这里要适配机器的文字大小。通常是10 20 30 这样设置的
    int t = textSize/10;
    t = (t == 0?1:(t > 7? 7:t));
    try {
      boolean equals = "黑体".equals(font);
      printerObject.add(printer.Page_setText(x, y -factor,  text, t == 2? (t + 2):(t +1), 0, equals?4:0, false, false));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void doPrint() {
    printerObject.add(printer.printPage(0, 1));
    dataOperater.sendContentData(printerObject);
  }

  @Override
  public void drawBarcode1D(int x, int y, String text, String type, int width, int height, boolean rotate) {
    /**
     * x - 打印的起始横坐标
     * y - 打印的起始纵坐标
     * str - 字符串
     * barcodetype - 条码类型 0：CODE39；1：CODE128；2：CODE93；3：CODEBAR；4：EAN8；5：EAN13；6：UPCA ;7:UPC-E;8:ITF
     * rotate - 旋转角度 0：不旋转；1：90度；2：180°；3:270°
     * barWidth - 条码宽度
     * barHeight - 条码高度
     */
    try {
      printerObject.add(printer.Page_drawBar(x, y -factor, text, 1, 0, width, height));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void drawBarcodeQRcode(int x, int y, String text, int size) {
    /**
     *
     * x - 打印的起始横坐标
     * y - 打印的起始纵坐标
     * rotate - 旋转角度 0：不旋转；1：90度；2：180°；3:270°
     * Ver - DQCODE版本号  1 ~ 40 每增加1个等级，每边加4
     * lel - 纠错等级 0：纠错等级为L 1：纠错等级为M 2：纠错等级为Q 3：纠错等级为H
     * Text - 要打印的字符串
     */
    printerObject.add(printer.Page_printQrCode(x, y -factor, 0, 2, 1, text));
  }

  @Override
  public void gotoMarklabel(int MaxFeedMM) {
    printerObject.add(printer.feedToBlack());
  }

  @Override
  public void multilineText(int x, int y, int width, int height, String text, int fontSize, int rotate, int bold, boolean underline, boolean reverse) {

  }

  @Override
  public void drawRect(int left, int top, int right, int bottom, int linewidth) {
    drawLine(left, top, right, top, linewidth);
    drawLine(left, top, left, bottom, linewidth);
    drawLine(right, top, right, bottom, linewidth);
    drawLine(left, bottom, right, bottom, linewidth);
  }

  @Override
  public void drawLine(int x0, int y0, int x1, int y1, int lineWidth) {

    printerObject.add(printer.Page_drawLine(x0, y0 -factor <0?0:y0 -factor, x1, y1 -factor<0?0:y1 -factor));
  }

  @Override
  public void closePrint() {
    if (mSelectedDevice.isConnected() && mAdapter != null)
      mAdapter.disconnectDevice(mSelectedDevice);

    setDeviceName("");
  }

  @Override
  public void finishConnect() {
    releaseWakeLock();
  }


  private void acquireWakeLock() {
    PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
    mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, this.getClass().getCanonicalName());
    mWakeLock.acquire();
  }

  private void releaseWakeLock() {
    if (mWakeLock != null && mWakeLock.isHeld()) {
      mWakeLock.release();
      mWakeLock = null;
    }
  }

}
