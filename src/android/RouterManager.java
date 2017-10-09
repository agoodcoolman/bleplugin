package com.bleplugin;

/**
 * Created by jin on 2017/9/7.
 */

public class RouterManager {

  /**
   * 根据不同的类型去获取不同的打印的实例类
   * @param type
   * @return
   *  目前  0 号  芝柯打印机
   *  目前  1 号  南京富士通打印机
   */
  public static AbstrPrint getPrint(int type) {
    AbstrPrint instance;
    if (type == 0) {
       instance = BLEManager.getInstance();
    } else {
       instance = FiJistuPrintManger.getInstance();
    }
    return instance;
  }
}
