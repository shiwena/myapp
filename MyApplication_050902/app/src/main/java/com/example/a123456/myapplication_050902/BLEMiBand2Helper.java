package com.example.a123456.myapplication_050902;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import static android.content.Context.BLUETOOTH_SERVICE;

public class BLEMiBand2Helper {

    private final String TAG = "FGO";

    public boolean isConnectedGatt = false;
    public boolean isSearching = false;
    public boolean isMeasuring = false;

    private Context helperContext = null;

    BluetoothAdapter bluetoothAdapter = null;
    BluetoothLeScanner bluetoothLeScanner = null;
    List<BluetoothDevice> bluetoothDevicesList = new ArrayList<>();
    BluetoothDevice bluetoothDevice = null;
    BluetoothGatt bluetoothGatt = null;

    BluetoothGattService bluetoothGattServices;
    BluetoothGattCharacteristic characteristic_zd;
    BluetoothGattCharacteristic characteristic_jb;

    List<String> servicesList = new ArrayList<>();

    Timer timer = null;

    private myCheckoutTask checkout = null;

    private Handler helperHandler = null;

    public void stopScan() {
        if (bluetoothAdapter == null || bluetoothLeScanner == null)
            return ;
        bluetoothLeScanner.stopScan(scanCallback);
        isSearching = false;
    }

    public void disConnect() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (bluetoothLeScanner != null)
            bluetoothLeScanner.stopScan(scanCallback);
        if (bluetoothGatt != null)
            bluetoothGatt.disconnect();
    }

    public void connectGatt() {
        if (bluetoothAdapter == null || bluetoothDevice == null)
            return ;
        bluetoothGatt = bluetoothDevice.connectGatt(helperContext, false, gattCallback);
    }

    public void startTimer() {
        if (timer != null){
            timer.cancel();
            timer = null;
        }
        timer = new Timer();
        checkout = new myCheckoutTask();
        helperHandler.post(new Runnable() {
            @Override
            public void run() {
                timer.schedule(checkout, 1000, 12000);
            }
        });
        /*
        helperHandler.post(new Runnable() {
            @Override
            public void run() {
                timer.scheduleAtFixedRate(checkout, 1, 10000);
            }
        });
        */
    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            checkout.cancel();
            checkout = null;
        }
    }

    private class myCheckoutTask extends TimerTask {

        @Override
        public void run() {
            Message message = new Message();
            message.what = 1;
            helperHandler.sendMessage(message);
        }
    }

    public BLEMiBand2Helper() {}

    public BLEMiBand2Helper(Context context, android.os.Handler handler) {
        helperContext = context;
        helperHandler = handler;

        if (helperContext == null) {
            Log.d(TAG, "KKKK");
        }
        if (helperHandler == null) {
            Log.d(TAG, "QQQQQ");
        }
    }

    public  boolean isConnected() {
        return  isConnectedGatt;
    }
    public boolean isDeviceNull() { return bluetoothDevice == null;}
    public boolean isGattNull() { return bluetoothGatt == null;}

    void init() {
        //timer = new Timer();
        if (helperContext == null) {
            Log.d(TAG, "NNNNNN");
        }
        BluetoothManager bluetoothManager = (BluetoothManager) helperContext.getSystemService(BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
    }

    public void pingCheck() throws InterruptedException {
        // char_ctrl.write(b'\x16', True)
        if (!isConnectedGatt || bluetoothGatt == null)
            return ;
        Log.d(TAG, "Checked.2");
        DelayWriteData(
                Consts.SERVICE_HEARTBEAT,
                Consts.HEARTRATE_CONTROL_POINT,
                new byte[] {0x16},
                0
        );
        Log.d(TAG, "Checked.");
    }

    // setCharacticNo...会在读取数据后调用conCharacteristicRead方法
    //要注意的问题：https://blog.csdn.net/Tim3366/article/details/43965313
    private BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, final int newState) {

            helperHandler.post(new Runnable() {
                @Override
                public void run() {
                    switch (newState) {
                        case BluetoothGatt.STATE_CONNECTED:
                            isConnectedGatt = true;
                            bluetoothLeScanner.stopScan(scanCallback);
                            bluetoothGatt.discoverServices();
                            raiseonConnected();
                            break;
                        case BluetoothGatt.STATE_DISCONNECTED:
                            stopTimer();
                            raiseonDisconnected();
                            isConnectedGatt = false;
                            isMeasuring = false;
                            break;
                        case BluetoothGatt.STATE_CONNECTING:
                            raiseonConnecting();
                            break;
                        case BluetoothGatt.STATE_DISCONNECTING:
                            raiseonDisconnecting();
                            isMeasuring = false;
                            break;
                    }
                }
            });
            super.onConnectionStateChange(gatt, status, newState);
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {

            if (status == bluetoothGatt.GATT_SUCCESS) {
                final List<BluetoothGattService> services = bluetoothGatt.getServices();
                helperHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //List<String> serlist = new ArrayList<>();
                        for (final BluetoothGattService bluetoothGattService : services) {
                            Log.i(TAG, "onServicesDiscovered: " +" 发现了服务："+" "+ bluetoothGattService.getUuid());
                            List<BluetoothGattCharacteristic> charc = bluetoothGattService.getCharacteristics();
                            for (BluetoothGattCharacteristic charac : charc) {
                                Log.i(TAG, "<"+bluetoothGattService.getUuid()+">"+" 服务有特征:->" +  charac.getUuid());
                                //找到透传特征值
                                // 00002a06-0000-1000-8000-00805f9b34fb 小米手环震动特征值 0x01震动 0x02强震
                                if (charac.getUuid().toString().equals("00002a06-0000-1000-8000-00805f9b34fb")) {
                                    //设备 震动特征值
                                    //  <00001802-0000-1000-8000-00805f9b34fb> 服务有特征:->00002a06-0000-1000-8000-00805f9b34fb
                                    characteristic_zd = charac;
                                    characteristic_zd.setValue(Consts.MESSAGE);
                                    bluetoothGatt.setCharacteristicNotification(characteristic_zd, true);
                                    bluetoothGatt.writeCharacteristic(characteristic_zd);

                                } else if (charac.getUuid().toString().equals("00000007-0000-3512-2118-0009af100700")) {
                                    //设备 步数
                                    characteristic_jb = charac;
                                    bluetoothGatt.setCharacteristicNotification(characteristic_jb, true);
                                    bluetoothGatt.readCharacteristic(characteristic_jb);
                                } else if (charac.getUuid().toString().equals("0000180a-0000-1000-8000-00805f9b34fb")) {
                                    characteristic_jb = charac;
                                    bluetoothGatt.readCharacteristic(charac);
                                    //设备 电量特征值
                                }else if (charac.getUuid().toString().equals("")) {
                                    //
                                }
                            }
                            servicesList.add(bluetoothGattService.getUuid().toString());
                        }
                    }
                });
            }
            super.onServicesDiscovered(gatt, status);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.d(TAG, "onCharacteristicRead: "+characteristic.getUuid().toString());
            Log.d(TAG, Arrays.toString(characteristic.getValue()));
            Log.d(TAG, "Status: "+String.valueOf(status));
            if (status == bluetoothGatt.GATT_SUCCESS) {
                if (characteristic.getUuid().toString().equals(Consts.CHARACTERISTIC_STEPS.toString())) {
                    final int num_1 = characteristic.getValue()[1]+characteristic.getValue()[2]*256+characteristic.getValue()[3]*256*256;
                    helperHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "步数："+String.valueOf(num_1));
                            Message msg = new Message();
                            msg.what = 2;

                            // txtStepNum.setText("走了" + num_1 + "步");
                        }
                    });
                } else if (characteristic.getUuid().toString().equals("")){

                } else {
                    Log.d(TAG, characteristic.getUuid().toString());
                    Log.d(TAG, "未识别数据类型,数据如下：");
                    Log.d(TAG, Arrays.toString(characteristic.getValue()));
                }
            }
            raiseonRead(gatt, characteristic, status);
            super.onCharacteristicRead(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            int leng = characteristic.getValue().length;

            Log.d(TAG, "onCharacteristicWrite: "+characteristic.getUuid().toString());
            Log.d(TAG, Arrays.toString(characteristic.getValue()));
            Log.d(TAG, "Status: "+String.valueOf(status));
            raiseonWrite(gatt, characteristic, status);
            super.onCharacteristicWrite(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            Log.d(TAG, "onCharacterticChanged: "+characteristic.getUuid().toString());
            Log.d(TAG, Arrays.toString(characteristic.getValue()));
            raiseonNotification(gatt, characteristic);
            super.onCharacteristicChanged(gatt, characteristic);
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            Log.d(TAG, "onDescriptorRead: "+descriptor.getUuid().toString());
            Log.d(TAG, Arrays.toString(descriptor.getValue()));
            Log.d(TAG, "Status: "+String.valueOf(status));
            super.onDescriptorRead(gatt, descriptor, status);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            Log.d(TAG, "onDescriptorWrite: "+descriptor.getUuid().toString());
            Log.d(TAG, Arrays.toString(descriptor.getValue()));
            Log.d(TAG, "Status: "+String.valueOf(status));
            super.onDescriptorWrite(gatt, descriptor, status);
        }
    };

    public void search() throws InterruptedException {

        //http://a1anwang.com/post-37.html

        // 设定扫描时长，判断是都正在扫描
        Log.d(TAG, "蓝牙正在扫描...");
        if (isSearching) return ;
        bluetoothDevicesList.clear();
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        if (bluetoothLeScanner != null) {
            bluetoothLeScanner.startScan(scanCallback);
            isSearching = true;
        }
    }

    public void DelayWriteData(UUID service, UUID characteristic, byte[] data, int delay) throws InterruptedException {
        if (!isConnectedGatt || bluetoothGatt == null) {
            return ;
        }
        Thread.sleep(delay);
        writeData(service, characteristic, data);
    }

    /*
    public BluetoothAdapter.LeScanCallback leCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
            Log.i(TAG, "onLeScan: " + bluetoothDevice.getName() + "/t" + bluetoothDevice.getAddress() + "/t" + bluetoothDevice.getBondState());

            //重复过滤方法，列表中包含不该设备才加入列表中，并刷新列表
            if (!bluetoothDevicesList.contains(bluetoothDevice)) {
                //将设备加入列表数据中
                bluetoothDevicesList.add(bluetoothDevice);
            }

        }
    };
    */

    public ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            BluetoothDevice bluetoothDevice = result.getDevice();
            String name = result.getDevice().getName();
            String address = result.getDevice().getAddress();
            int status = result.getDevice().getBondState();
            Log.d(TAG, "onScanRusult: " + name + " " + address + " "+status+' ');

            if (name != null && !bluetoothDevicesList.contains(bluetoothDevice))
                bluetoothDevicesList.add(bluetoothDevice);
            raiseonScanning();
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            isSearching = true;
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            // scanCallback.onScanFailed(errorCode);
            //https://blog.csdn.net/chy555chy/article/details/53788748
            isSearching = false;
            if (bluetoothAdapter != null) {
                // 一旦发生错误，除了重启蓝牙再没有其它解决办法
                bluetoothAdapter.disable();
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        while (true) {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            //要等待蓝牙彻底关闭，然后再打开，才能实现重启效果
                            if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {
                                bluetoothAdapter.enable();
                                break;
                            }
                        }
                    }
                }).start();
            }
        }

    };

    public void writeDataDescriptor(UUID service, UUID characterister, UUID descriptor, byte[] data) throws InterruptedException {
        if (!isConnectedGatt || bluetoothGatt == null) {
            //Toast.makeText(, "请先连接蓝牙设备", Toast.LENGTH_SHORT).show();
            return ;
        }
        Log.d(TAG, "writeDataDescriptor: ");
        boolean status;
        BluetoothGattService bt_service = bluetoothGatt.getService(service);
        if (bt_service != null) {
            BluetoothGattCharacteristic bt_char = bt_service.getCharacteristic(characterister);
            status = bluetoothGatt.setCharacteristicNotification(bt_char, true);
            Log.d(TAG, "Setting notification with status: "+status);
            if (bt_char != null) {
                BluetoothGattDescriptor bt_desc= bt_char.getDescriptor(descriptor);
                if (bt_desc != null) {
                    bt_desc.setValue(data);
                    status = bluetoothGatt.writeDescriptor(bt_desc);
                    Log.d(TAG, "onSetNotificationWithDescriptor: writing desc in status: " + String.valueOf(status));
                } else {
                    //Toast.makeText(MainActivity.this, "连接错误，请重新连接", Toast.LENGTH_SHORT).show();
                    return ;
                }
            } else {
                //Toast.makeText(MainActivity.this, "连接错误，请重新连接", Toast.LENGTH_SHORT).show();
                return ;
            }
        } else {
            //Toast.makeText(MainActivity.this, "连接错误，请重新连接", Toast.LENGTH_SHORT).show();
            return ;
        }
    }

    public void writeData(UUID service, UUID Characteristics,byte[] data) {
        if (!isConnectedGatt || bluetoothGatt == null) {
            Log.d(TAG, "Cant read from BLE, not initialized.");
            return;
        }

        Log.d(TAG, "* Getting gatt service, UUID:" + service.toString());
        BluetoothGattService myGatService =
                bluetoothGatt.getService(service /*Consts.UUID_SERVICE_HEARTBEAT*/);
        if (myGatService != null) {
            Log.d(TAG, "* Getting gatt Characteristic. UUID: " + Characteristics.toString());

            BluetoothGattCharacteristic myGatChar
                    = myGatService.getCharacteristic(Characteristics /*Consts.UUID_START_HEARTRATE_CONTROL_POINT*/);
            if (myGatChar != null) {
                Log.d(TAG, "* Writing trigger");
                bluetoothGatt.setCharacteristicNotification(myGatChar, true);
                myGatChar.setValue(data /*Consts.BYTE_NEW_HEART_RATE_SCAN*/);

                boolean status =  bluetoothGatt.writeCharacteristic(myGatChar);
                Log.d(TAG, "* Writting trigger status :" + status);
            }
        }
    }

    public interface BLEAction {
        void onScanning();
        void onDisconnected();
        void onDisconnecting();
        void onConnected();
        void onConnecting();
        void onRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status);
        void onWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status);
        void onNotification(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic);
    }

    /* =========  Handling Events  ============== */

    private ArrayList<BLEAction> listeners = new ArrayList<BLEAction>();

    public void addListener(BLEAction toAdd) {
        listeners.add(toAdd);
    }

    public void removeListener(BLEAction toDel) {
        listeners.remove(toDel);
    }

    public void raiseonNotification(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        // Notify everybody that may be interested.
        for (BLEAction listener : listeners)
            listener.onNotification( gatt,characteristic);
    }

    public void raiseonRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic,int status) {
        // Notify everybody that may be interested.
        for (BLEAction listener : listeners)
            listener.onRead( gatt,characteristic,status);
    }

    public void raiseonWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic,int status) {
        // Notify everybody that may be interested.
        for (BLEAction listener : listeners)
            listener.onWrite( gatt,characteristic,status);
    }

    public void raiseonDisconnected() {
        // Notify everybody that may be interested.
        for (BLEAction listener : listeners)
            listener.onDisconnected();
    }

    public void raiseonDisconnecting() {
        // Notify everybody that may be interested.
        for (BLEAction listener : listeners)
            listener.onDisconnecting();
    }

    public void raiseonConnected() {
        // Notify everybody that may be interested.
        for (BLEAction listener : listeners)
            listener.onConnected();
    }

    public void raiseonConnecting() {
        // Notify everybody that may be interested.
        for (BLEAction listener : listeners)
            listener.onConnecting();
    }

    public void raiseonScanning() {
        // Notify everybody that may be interested.
        for (BLEAction listener : listeners)
            listener.onScanning();
    }
}