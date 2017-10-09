package com.bleplugin;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;

/**
 * Created by jin on 2017/9/7.
 */

public abstract class AbstrPrint {
  public static Context context;
  protected boolean isConnect = false;
  protected BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
  protected String deviceName = "";

  /**
   * 扫描蓝牙
   */
  public void startLeScan() {
    bluetoothAdapter.startDiscovery();
  }

  /**
   * 停止扫描
   */
  public void stopLeScan(){
    bluetoothAdapter.cancelDiscovery();
  }

  public void setDeviceName(String deviceName) {
    this.deviceName = deviceName;
  }

  public String getDeviceName() {
    return deviceName;
  }
  /**
   * 链接
   * @param device
   * @return
   */
  public abstract boolean connect(BluetoothDevice device);

  public boolean isEnable(){
    return bluetoothAdapter.isEnabled();
  }

  /**
   * 创建页面 设置宽高
   * @param pageWidth
   * @param pageHeight
   */
  public abstract void createPager(int pageWidth, int pageHeight);

  public abstract void drawText(int x, int y, String text,String font,int textSize, boolean bold, boolean rotate);

  public abstract void doPrint();

  public abstract void drawBarcode1D(int x,int y,String text,String type,int width,int height,boolean rotate);

  public abstract void drawBarcodeQRcode(int x,int y,String text,int size);

  public abstract void gotoMarklabel(int MaxFeedMM);

  public abstract void multilineText(int x, int y, int width, int height, String text, int fontSize, int rotate, int bold, boolean underline, boolean reverse);

  public abstract void drawRect(int left, int top, int right, int bottom, int linewidth);

  public abstract void drawLine(int x0,int y0,int x1,int y1,int lineWidth);

  public abstract void closePrint();

  /**
   * 蓝牙链接结束
   */
  public abstract void finishConnect();


}
