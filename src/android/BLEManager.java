package com.bleplugin;

import android.bluetooth.BluetoothDevice;

import zpSDK.zpSDK.zpSDK;

/**
 * Created by truly on 2016/4/26.
 * 芝科打印机。
 */
public class BLEManager extends AbstrPrint{
    private BLEManager(){}
    private static BLEManager instance;
    private static final Object sync = new Object();

    public static BLEManager getInstance(){
        if(instance == null) {
            synchronized (sync) {
                if (instance == null)
                    instance = new BLEManager();
            }
        }
        return instance;
    }

  @Override
  public boolean connect(final BluetoothDevice device) {
    return zpSDK.zp_open(bluetoothAdapter, (BluetoothDevice) device);

  }

  @Override
  public void createPager(int pageWidth, int pageHeight) {
    zpSDK.Create(pageWidth, pageHeight);
  }

  @Override
  public void drawText(int x, int y, String text, String font, int textSize, boolean bold, boolean rotate) {
    zpSDK.DrawText(x, y, text, font, textSize, bold, rotate);
  }

  @Override
  public void doPrint() {
    boolean isSendSucess = zpSDK.zp_page_print(false);
    zpSDK.Clear();

  }

  @Override
  public void drawBarcode1D(int x, int y, String text, String type, int width, int height, boolean rotate) {
    zpSDK.DrawBarcode1D(x, y, text, type, width, height, rotate);
  }

  @Override
  public void drawBarcodeQRcode(int x, int y, String text, int size) {
    zpSDK.DrawBarcodeQRcode(x, y, text, size);
  }

  @Override
  public void gotoMarklabel(int MaxFeedMM) {
    zpSDK.zp_goto_mark_label(MaxFeedMM);
  }

  @Override
  public void multilineText(int x, int y, int width, int height, String text, int fontSize, int rotate, int bold, boolean underline, boolean reverse) {
    zpSDK.drawTextbox(x, y, width, height, text, fontSize, rotate, bold, underline, reverse);
  }

  @Override
  public void drawRect(int left, int top, int right, int bottom, int linewidth) {
    zpSDK.DrawLine(left, top, right, top, linewidth);
    zpSDK.DrawLine(left, top, left, bottom, linewidth);
    zpSDK.DrawLine(right, top, right, bottom, linewidth);
    zpSDK.DrawLine(left, bottom, right, bottom, linewidth);
  }

  @Override
  public void drawLine(int x0, int y0, int x1, int y1, int lineWidth) {
    zpSDK.DrawLine(x0, y0, x1, y1, lineWidth);
  }

  @Override
  public void closePrint() {
    setDeviceName("");
    zpSDK.zp_close();
  }

  @Override
  public void finishConnect() {

  }
}
