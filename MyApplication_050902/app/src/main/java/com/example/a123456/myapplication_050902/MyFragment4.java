package com.example.a123456.myapplication_050902;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Jay on 2015/8/28 0028.
 */
public class MyFragment4 extends Fragment {

    public MyFragment4() {
    }

    TextView tv_clearcache;
    TextView tv_about;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment4,container,false);



        /**
         * 清缓存
         */
        tv_clearcache = (TextView)view.findViewById(R.id.f4_clearcache);
        tv_clearcache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                //    设置Title的图标
                builder.setIcon(R.mipmap.ic_launcher);

                //    设置Content来显示一个信息
                builder.setMessage("确定清除缓存吗？");
                //    设置一个PositiveButton
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Toast.makeText(getContext(), "positive", Toast.LENGTH_SHORT).show();
//                        cleanMessageUtil.clearAllCache(getContext());
                        CleanMessageUtil.clearAllCache(getContext());
                    }
                });
                //    设置一个NegativeButton
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Toast.makeText(getContext(), "negative", Toast.LENGTH_SHORT).show();
                    }
                });

                //    显示出该对话框
                builder.show();


            }
        });

        tv_about = (TextView) view.findViewById(R.id.f4_about);
        tv_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),About.class);
                startActivity(intent);
            }
        });






        return view;
    }












}
