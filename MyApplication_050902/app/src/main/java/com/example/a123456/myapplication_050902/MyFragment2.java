package com.example.a123456.myapplication_050902;

import android.app.AlertDialog;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Jay on 2015/8/28 0028.
 */
public class MyFragment2 extends Fragment {

    private final String TAG = "ccc";

    Button heart11;

    private Handler handler = new Handler(Looper.getMainLooper());
    public boolean isMeasuring = false;

    private fg_2_Trigger fg_callback = null;

    public MyFragment2() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2,container,false);

        initView(view);

        return view;
    }

    @Override
    public void onAttach(Context context) {

        try {
            fg_callback = (fg_2_Trigger)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement");
        }

        super.onAttach(context);
    }

    private void initView(View view) {
        TextView txt_content = (TextView) view.findViewById(R.id.txt_content);
        heart11=(Button) view.findViewById(R.id.heart1);

        txt_content.setText("实时监控");
        heart11.setText("开始");

        this.addListenner(fg_callback);

        initData();

    }

    public void initData() {
        if (isMeasuring) {
            heart11.setText("准备中...");
        }
    }

    /*Activity创建时设置点击事件*/
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

        heart11.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (isMeasuring) return ;
                Log.d(TAG, "hfdslfjsla");
                raiseOnTriggerDo();
            }
        });

        heart11.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (!fg_callback.isMeasuring()) return false;
                showNormalDialog();
                return true;
            }
        });

    }

    /*工具回话框*/
    private void showNormalDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(getContext());
        normalDialog.setIcon(R.mipmap.ic_launcher_round);
        normalDialog.setTitle("");
        normalDialog.setMessage("确定要停下吗?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        isMeasuring = false;
                        raiseOnTriggerUndo();

                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...
                    }
                });
        // 显示
        normalDialog.show();
    }

    /*心跳值正常值测试*/
    public boolean isSafe(int heartBeat) {

        if (heartBeat > 180 || heartBeat < 40) {
            return false;
        }
        else {
            return true;
        }
    }

    /*打电话求助*/
    public void callHelper() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        //赋值标题
        if (builder == null) return ;
        builder.setTitle("ALERT")
                //logo赋值
                .setIcon(R.mipmap.ic_launcher)
                //内容赋值
                .setMessage("警报！")
                //确定按钮
                .setPositiveButton("拨打求助电话", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        //设置Intent对象动作
                        intent.setAction(Intent.ACTION_CALL);
                        //设置拨打电话号码
                        intent.setData(Uri.parse("tel:17807710593"));
                        startActivity(intent);
                    }
                })
                //取消按钮
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        //显示弹框
        builder.show();
    }

    public void onNotification(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        UUID alertUUID = characteristic.getUuid();
        //isMeasuring = true;
        if (alertUUID.equals(Consts.CHARACTERISTIC_HEART_NOTIFICATION)) {
            final byte hearbeat =
                    characteristic.getValue()[1];
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (!isSafe(hearbeat)) {
                        callHelper();

                    }
                    heart11.setText(Byte.toString(hearbeat));
                    /*
                    Toast.makeText(getContext(),
                            "Heartbeat: " + Byte.toString(hearbeat)
                            , Toast.LENGTH_SHORT).show();
                    */
                }
            });
        }
        else if (alertUUID.equals(Consts.CHARACTERISTIC_BUTTON_TOUCH)) {
            handler.post(new Runnable() {

                @Override
                public void run() {
                    //getNewHeartBeat();
                    Toast.makeText(getContext(),
                            "Button Press! "
                            , Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void onWrite(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, final int status) {

        // 处理
        if (!characteristic.getUuid().toString().equals(Consts.CHARACTERISTIC_HEART_NOTIFICATION.toString()))
            heart11.setText("准备中...");
        if (characteristic.getUuid().toString().equals(Consts.NOTIFICATION_DESCRIPTOR.toString()))
            heart11.setText("开始");
    }

    public void onRead(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, final int status) {

        // 处理
        if (!characteristic.getUuid().toString().equals(Consts.CHARACTERISTIC_HEART_NOTIFICATION.toString()))
            heart11.setText("准备中...");
    }

    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        if (descriptor.getUuid().toString().equals(Consts.NOTIFICATION_DESCRIPTOR.toString()))
            heart11.setText("开始");
    }

    /*实时检测和主Activity交互数据的接口*/
    public interface fg_2_Trigger{
        void onTriggerTheMeasure();
        void onTriggerUndoTheMeasure();
        boolean isMeasuring();
    }

    private ArrayList<fg_2_Trigger> listenners = new ArrayList<>();

    public void addListenner(fg_2_Trigger toAdd){
        listenners.add(toAdd);
    }

    public void raiseOnTriggerDo() {
        for (fg_2_Trigger listenner : listenners) {
            listenner.onTriggerTheMeasure();
        }
    }

    public void raiseOnTriggerUndo() {
        for (fg_2_Trigger listenner : listenners)
            listenner.onTriggerUndoTheMeasure();
    }

}
