package com.bleplugin;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ionicframework.hytapp30644240.R;

/**
 * Created by jin on 2017/9/15.
 */

public class LoadingDialog extends Dialog {
  private TextView tv;

  public LoadingDialog(Context context) {
    super(context, R.style.loadingDialogStyle);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dialog_loading);
    tv = (TextView) findViewById(R.id.tv);
    tv.setText("正在连接蓝牙.....");
    LinearLayout linearLayout = (LinearLayout) this.findViewById(R.id.LinearLayout);
    linearLayout.getBackground().setAlpha(210);
  }
}
