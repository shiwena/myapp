package com.example.a123456.myapplication_050902;

import android.Manifest;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * Created by Jay on 2015/8/28 0028.
 */
public class MyFragment3 extends Fragment implements View.OnClickListener{

    private final String TAG = "FGO";

    Timer timer = null;

    Handler handler = new Handler(Looper.getMainLooper());

    private Button btnSearch;
    private TextView txtStatus;
    private TextView txtCurrentDevice;
    private ListView listView;

    BLEMiBand2Helper helper = null;
    BLEMiBand2Helper.BLEAction mCallback = null;

    public MyFragment3() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment3,container,false);

        helper.addListener(mCallback);
        helper.init();
        initView(view);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            helper = new BLEMiBand2Helper(context, handler);
            mCallback = (BLEMiBand2Helper.BLEAction)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement");
        }
    }

    private void initView(View view) {
        btnSearch = view.findViewById(R.id.btnSearch);
        listView = view.findViewById(R.id.listView);
        txtStatus = view.findViewById(R.id.txtStatus);
        btnSearch.setOnClickListener(this);
        txtCurrentDevice = view.findViewById(R.id.txtCurrentDevice);
        TextView txt_content = (TextView) view.findViewById(R.id.fg_4_txtHead);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (helper.isSearching) {
                    helper.stopScan();
                }
                helper.bluetoothDevice = helper.bluetoothDevicesList.get(position);
                if (helper.bluetoothDevice == null) {
                    Log.d(TAG, "List onItem is null:");
                }
                if (helper.bluetoothDevice == null)
                    return ;
                helper.connectGatt();
                if (helper.bluetoothGatt == null)
                    return ;
                txtStatus.setText("连接"+helper.bluetoothDevice.getName().toString()+" 中...");
            }
        });

        txt_content.setText("名字是我");

        setStatus();
    }

    public void setStatus() {
        if (helper != null && helper.isConnectedGatt) {
            txtStatus.setText("连接成功");
            txtCurrentDevice.setText(helper.bluetoothDevice.getName());
            listView.setAdapter(new DeviceItemAdapter(getContext() , helper.bluetoothDevicesList));
        }
    }

    // 开始定时测量
    public void startMeasure() {
        /*
        if (!helper.isConnected()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), "请先连接蓝牙设备", Toast.LENGTH_SHORT).show();
                }
            });
            return ;
        }*/
        if(!helper.isMeasuring) {
            helper.isMeasuring = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //helper.startTimer();
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            helper.pingCheck();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        }, 10000, 14000);

                        helper.writeDataDescriptor(
                                Consts.SERVICE_HEARTBEAT,
                                Consts.CHARACTERISTIC_HEART_NOTIFICATION,
                                Consts.NOTIFICATION_DESCRIPTOR,
                                new byte[]{0x01, 0x00}
                        );
                        Thread.sleep(500);
                        helper.DelayWriteData(
                                Consts.SERVICE_HEARTBEAT,
                                Consts.HEARTRATE_CONTROL_POINT,
                                new byte[]{0x15, 0x02, 0x00},
                                1500
                        );
                        helper.DelayWriteData(
                                Consts.SERVICE_HEARTBEAT,
                                Consts.HEARTRATE_CONTROL_POINT,
                                new byte[]{0x15, 0x01, 0x00},
                                1500
                        );
                        helper.DelayWriteData(
                                Consts.SERVICE_MIBAND1_SERVICE,
                                Consts.CHARACTERISTIC_SENSOR,
                                new byte[]{0x01, 0x03, 0x19},
                                2500
                        );
                        helper.writeDataDescriptor(
                                Consts.SERVICE_HEARTBEAT,
                                Consts.CHARACTERISTIC_HEART_NOTIFICATION,
                                Consts.NOTIFICATION_DESCRIPTOR,
                                new byte[]{0x01, 0x00}
                        );
                        Thread.sleep(500);
                        helper.DelayWriteData(
                                Consts.SERVICE_HEARTBEAT,
                                Consts.HEARTRATE_CONTROL_POINT,
                                new byte[]{0x15, 0x01, 0x01},
                                5000
                        );
                        helper.DelayWriteData(
                                Consts.SERVICE_MIBAND1_SERVICE,
                                Consts.CHARACTERISTIC_SENSOR,
                                new byte[]{0x02},
                                5000
                        );
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    // 停止定时测量
    public void stopMeasure() {
        /*
        if (helper != null && helper.isConnectedGatt && helper.isMeasuring) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    realtimeMeasureStop.start();
                }
            });
        }
        */
        // 线程不可以重用，所以每次都要重新开

        timer.cancel();
        timer = null;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    helper.DelayWriteData(
                            Consts.SERVICE_HEARTBEAT,
                            Consts.HEARTRATE_CONTROL_POINT,
                            new byte[]{0x15, 0x01, 0x00},
                            500
                    );;
                    helper.DelayWriteData(
                            Consts.SERVICE_HEARTBEAT,
                            Consts.HEARTRATE_CONTROL_POINT,
                            new byte[]{0x15, 0x01, 0x00},
                            500
                    );
                    helper.writeDataDescriptor(
                            Consts.SERVICE_HEARTBEAT,
                            Consts.CHARACTERISTIC_HEART_NOTIFICATION,
                            Consts.NOTIFICATION_DESCRIPTOR,
                            new byte[]{0x00, 0x00}
                    );
                    helper.DelayWriteData(
                            Consts.SERVICE_MIBAND1_SERVICE,
                            Consts.CHARACTERISTIC_SENSOR,
                            new byte[]{0x03},
                            1500
                    );
                    helper.writeDataDescriptor(
                            Consts.SERVICE_MIBAND1_SERVICE,
                            Consts.CHARACTERISTIC_HZ,
                            Consts.NOTIFICATION_DESCRIPTOR,
                            new byte[]{0x00, 0x00}
                    );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        helper.disConnect();
        super.onDestroy();
    }

    /*
     *因为是直接使用点击搜索，所以相关变量的初始化工作要做好
    */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSearch:
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    //请求权限
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        //判断是否需要解释
                        Toast.makeText(getContext(), "需要蓝牙权限", Toast.LENGTH_SHORT).show();
                    }
                }
                if (!helper.bluetoothAdapter.isEnabled()) {
                    Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(turnOn, 0);
                }

                Thread scanThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // 在search中， 只是运行15秒就让scan部分停止
                            helper.search();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                scanThread.start();
                break;
        }
    }


    // 更新UI一定要在主线程中执行
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "Request code: "+requestCode);
        Log.d(TAG, "Result code: "+resultCode);

        if (requestCode == 1) {
            if (isLocationOpen(getContext())) {
                Log.i(TAG, " request location permission success"); //Android6.0需要动态申请权限
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    //请求权限
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        //判断是否需要解释
                        Toast.makeText(getContext(), "需要蓝牙权限", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                //若未开启位置信息功能,则退出该应用
                Log.d(TAG, "NO Permission");
            }
        }

        if (requestCode == 0) {
            if (resultCode == -1) {
                Thread scanThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            helper.search();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                scanThread.start();
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /** *判断位置信息是否开启 * @param context * @return */
    //开启位置服务,支持获取ble蓝牙扫描结果
    public static boolean isLocationOpen(final Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);//gps定位
        boolean isGpsProvider = manager.isProviderEnabled(LocationManager.GPS_PROVIDER); //网络定位
        boolean isNetWorkProvider = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return isGpsProvider|| isNetWorkProvider;
    }

    public void onDisconnecting() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                txtStatus.setText("断开连接中...");
            }
        });
        Log.d(TAG, "Disconnecting...");
    }

    public void onConnecting() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                txtStatus.setText("连接中...");
            }
        });
        Log.d(TAG, "Connecting...");
    }

    public void onScanning() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listView.setAdapter(new DeviceItemAdapter(getContext() , helper.bluetoothDevicesList));
            }
        });
    }

    public void onDisconnected() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                txtStatus.setText("已断开连接");
                txtCurrentDevice.setText("无");
            }
        });
        Log.d(TAG, "Disconnected.");
    }

    public void onConnected() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                txtStatus.setText("连接成功");
                txtCurrentDevice.setText(helper.bluetoothDevice.getName());
            }
        });
        Log.d(TAG, "Connecting...");
    }

    public void onRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        Log.d(TAG, "Reading...");
    }

    public void onWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        Log.d(TAG, "Writing...");
    }

    public void onNotification(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        UUID alertUUID = characteristic.getUuid();
        if (alertUUID.equals(Consts.CHARACTERISTIC_HEART_NOTIFICATION)) {
            final byte hearbeat =
                    characteristic.getValue()[1];
            handler.post(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(getContext(),
                            "Heartbeat: " + Byte.toString(hearbeat)
                            , Toast.LENGTH_SHORT).show();
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

}
