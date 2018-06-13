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
import android.widget.Button;
import android.widget.LinearLayout;
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
    TextView bt_logout;
    TextView tv_suggestion;
    TextView tv_version;


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

                //    设置信息
                builder.setMessage("确定清除缓存吗？");
                //    设置一个PositiveButton
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //Toast.makeText(getContext(), "positive", Toast.LENGTH_SHORT).show();


                        //输出缓存大小
                        try {
                            Log.i("111","缓存："+DataCleanManager.getTotalCacheSize(getContext()));
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                        //     清缓存
                        //     DataCleanManager.clearAllCache(getContext());


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

        /**
         * 关于
         */

        tv_about = (TextView) view.findViewById(R.id.f4_about);
        tv_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),F4_about.class);
                startActivity(intent);
            }
        });


        /**
         * 意见反馈
         */
        tv_suggestion = (TextView) view.findViewById(R.id.bt_suggestion);
        tv_suggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),F4_suggestion.class);
                startActivity(intent);
            }
        });


        /**
         * 版本检查
         */

        tv_version = (TextView)view.findViewById(R.id.tv_version);
        tv_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder((getContext()));

                builder.setTitle("");
                builder.setMessage("已是最新版本");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //do nothing
                    }
                });
                builder.show();

            }
        });


        /**
         * 退出登录
         */
        bt_logout = (TextView) view.findViewById(R.id.bt_logout);
        bt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog mLogoutDialog = new Dialog(getContext(), R.style.my_dialog);
                LinearLayout root = (LinearLayout) LayoutInflater.from(getContext()).inflate(
                        R.layout.dialog_logout, null);

                mLogoutDialog.setContentView(root);
                Window dialogWindow = mLogoutDialog.getWindow();
                dialogWindow.setGravity(Gravity.BOTTOM);
                //dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
                WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
                lp.x = 0; // 新位置X坐标
                lp.y = -20; // 新位置Y坐标
                lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度

                root.measure(0, 0);
                lp.height = root.getMeasuredHeight();
                lp.alpha = 9f; // 透明度
                dialogWindow.setAttributes(lp);
                mLogoutDialog.show();

                root.findViewById(R.id.btn_comfirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(),LoginActivity.class);
                        startActivity(intent);
                    }
                });

                root.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mLogoutDialog.dismiss();
                    }
                });




            }
        });






        return view;
    }







}
