package com.example.a123456.myapplication_050902;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class UserHeightDialog extends PopupWindow {

    private Context mContext;

    private View view;

    private Button btn_save;

    public EditText text_height;


    public UserHeightDialog(Activity mContext, View.OnClickListener itemsOnClick) {

        this.mContext = mContext;

        this.view = LayoutInflater.from(mContext).inflate(R.layout.userheight, null);

        text_height = (EditText) view.findViewById(R.id.text_height);

        btn_save =  (Button) view.findViewById(R.id.btn_saveheight);

        // 设置按钮监听
        btn_save.setOnClickListener(itemsOnClick);

        // 设置外部可点击
        this.setOutsideTouchable(true);


        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);

        // 设置弹出窗体的宽和高
        /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = mContext.getWindow();

        WindowManager m = mContext.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值

        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth((int) (d.getWidth() * 0.8));

        // 设置弹出窗体可点击
        this.setFocusable(true);

    }
}
