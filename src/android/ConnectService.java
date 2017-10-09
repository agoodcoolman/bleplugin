package com.bleplugin;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by jin on 2017/8/31.
 */

public class ConnectService extends Service {

  private BluetoothBinder bluetoothBinder = new BluetoothBinder();
  private AbstrPrint print;
  private int preDeviceID = 0;
  @Nullable
   @Override
   public IBinder onBind(Intent intent) {
       // 刚开始获取名字 就绑定了。
       int type = intent.getIntExtra("Type", 0);
       preDeviceID = type;
       print = RouterManager.getPrint(type);
       print.context = getApplicationContext();
       return bluetoothBinder;
   }



  @Override
  public void onRebind(Intent intent) {
    super.onRebind(intent);
    //  前面绑定了，现在重新绑定。

  }

  @Override
  public boolean onUnbind(Intent intent) {
    return super.onUnbind(intent);
  }

  public class BluetoothBinder extends Binder {
     public BluetoothBinder() {
     }

     public boolean connect(BluetoothDevice bluetoothDevice, int type) {
       // 链接之前判断一下，启动的是哪个打印机的软件
       // 最开始绑定链接的时候查询了链接打印的名字了的，当时最开始绑定的是 0，能查到名字就行的
       if (type != preDeviceID) {
         print = RouterManager.getPrint(type);
         preDeviceID = type;
       }
       boolean connect = print.connect(bluetoothDevice);
       if (connect) {
         print.setDeviceName(bluetoothDevice.getName());
         print.isConnect = true;
       } else {
         print.isConnect = false;
       }
       return connect;
     }

     public String getDeviceName() {
       if (print != null)
        return print.getDeviceName();
       else
         return "";
     }

     public void create(int pageWidth, int pageHeight) {
       print.createPager(pageWidth, pageHeight);
     }

     public void drawText(int x, int y, String text,String font,int textSize, boolean bold, boolean rotate) {
       print.drawText(x,y ,text,font,textSize,bold,rotate);
     }

     public void doPrint() {
       print.doPrint();
     }
     public void drawBarcode1D(int x,int y,String text,String type,int width,int height,boolean rotate) {
       print.drawBarcode1D(x,y,text,type,width,height,rotate);
     }

     public void drawBarcodeQRcode(int x,int y,String text,int size){
       print.drawBarcodeQRcode(x,y,text,size);
     }

     public void gotoMarklabel(int MaxFeedMM){
       print.gotoMarklabel(250);
     }
      public void multilineText(int x, int y, int width, int height, String text, int fontSize, int rotate, int bold, boolean underline, boolean reverse){
        print.multilineText(x,y,width,height,text,fontSize,rotate,bold,underline,reverse);
      }
      public void drawRect(int left, int top, int right, int bottom, int linewidth) {
        print.drawRect(left,top, right, bottom, linewidth);

      }

      public void drawLine(int x0,int y0,int x1,int y1,int lineWidth){
        print.drawLine(x0, y0, x1, y1, lineWidth);
      }

      public void finishConnect() {
        print.finishConnect();
      }

      public void closeConnectDevice() {
        print.closePrint();
        print.isConnect = false;
      }

      /**
       * 扫描蓝牙
       */
      public void startLeScan() {
        print.startLeScan();
      }

      /**
       * 停止扫描
       */
      public void stopLeScan(){
        print.stopLeScan();
      }

  }
}
