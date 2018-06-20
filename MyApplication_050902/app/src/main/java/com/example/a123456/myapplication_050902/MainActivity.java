package com.example.a123456.myapplication_050902;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener,
        BLEMiBand2Helper.BLEAction, MyFragment2.fg_2_Trigger{
    private TextView txt_topbar;
    private RadioGroup  rg_tab_bar;
    private RadioButton rb_channel;
    private RadioButton rb_message;
    private RadioButton rb_better;
    private RadioButton rb_setting;

    private ViewPager vpager;
    //组员LGY添加的接收Userid
    private String User_id=null;

    private final String TAG = "ccc";
    private Handler handler = new Handler(Looper.getMainLooper());

    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR = 3;

    private MyFragmentPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //接收用户的id
        Intent i = getIntent();
        User_id=i.getStringExtra("User_id");
        Log.i("MainactivityLGY","————当前用户为————————："+User_id);


        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),User_id);
        bindViews();
        rb_channel.setChecked(true);


    }

    private void bindViews() {
        txt_topbar = (TextView) findViewById(R.id.txt_topbar);
        rg_tab_bar = (RadioGroup) findViewById(R.id.rg_tab_bar);
        rb_channel = (RadioButton) findViewById(R.id.rb_channel);
        rb_message = (RadioButton) findViewById(R.id.rb_message);
        rb_better = (RadioButton) findViewById(R.id.rb_better);
        rb_setting = (RadioButton) findViewById(R.id.rb_setting);
        rg_tab_bar.setOnCheckedChangeListener(this);

        vpager = (ViewPager) findViewById(R.id.vpager);
        vpager.setAdapter(mAdapter);
        vpager.setCurrentItem(0);
        vpager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
        if (state == 2) {
            switch (vpager.getCurrentItem()) {
                case PAGE_ONE:
                    rb_channel.setChecked(true);
                    break;
                case PAGE_TWO:
                    rb_message.setChecked(true);
                    break;
                case PAGE_THREE:
                    rb_better.setChecked(true);
                    break;
                case PAGE_FOUR:
                    rb_setting.setChecked(true);
                    break;
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_channel:
                vpager.setCurrentItem(PAGE_ONE);
                break;
            case R.id.rb_message:
                vpager.setCurrentItem(PAGE_TWO);
                break;
            case R.id.rb_better:
                vpager.setCurrentItem(PAGE_THREE);
                break;
            case R.id.rb_setting:
                vpager.setCurrentItem(PAGE_FOUR);
                break;
        }
    }

    @Override
    public void onDisconnecting() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                int fg_3 = vpager.getCurrentItem();
                if (fg_3 == PAGE_THREE) {
                    MyFragment3 fg3 = (MyFragment3) mAdapter.currentFragment;
                    if (fg3 != null) {
                        fg3.onDisconnecting();
                    }
                }
            }
        });
        Log.d(TAG, "Disconnecting...");
    }

    @Override
    public void onConnecting() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                int fg_3 = vpager.getCurrentItem();
                if (fg_3 == PAGE_THREE) {
                    MyFragment3 fg3 = (MyFragment3) mAdapter.currentFragment;
                    if (fg3 != null) {
                        fg3.onConnecting();
                    }
                }
            }
        });
        Log.d(TAG, "Connecting...");
    }

    @Override
    public void onScanning() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                int fg_3 = vpager.getCurrentItem();

                if (fg_3 == PAGE_THREE) {
                    MyFragment3 fg3 = (MyFragment3) mAdapter.currentFragment;
                    if (fg3 != null) {
                        fg3.onScanning();
                    }
                }
            }
        });
    }

    @Override
    public void onDisconnected() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                int fg_3 = vpager.getCurrentItem();
                if (fg_3 == PAGE_THREE) {
                    MyFragment3 fg3 = (MyFragment3) mAdapter.currentFragment;
                    if (fg3 != null) {
                        fg3.onDisconnected();
                    }
                }
            }
        });
        Log.d(TAG, "Disconnected.");
    }

    @Override
    public void onConnected() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                int fg_3 = vpager.getCurrentItem();
                if (fg_3 == PAGE_THREE) {
                    MyFragment3 fg3 = (MyFragment3) mAdapter.currentFragment;
                    if (fg3 != null) {
                        fg3.onConnected();
                    }
                }
            }
        });
        Log.d(TAG, "Connected...");
    }

    @Override
    public void onRead(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, final int status) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                int fg_2 = vpager.getCurrentItem();
                if (fg_2 == PAGE_TWO) {
                    MyFragment2 fg2 = (MyFragment2) mAdapter.currentFragment;
                    if (fg2 != null) {
                        fg2.onRead(gatt, characteristic, status);
                    }
                }
            }
        });
        Log.d(TAG, "Reading...");
    }

    @Override
    public void onWrite(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, final int status) {

        // 在这里应该做区别处理，为方便，统一执行相同的，，，
        handler.post(new Runnable() {
            @Override
            public void run() {
                int fg_2 = vpager.getCurrentItem();
                if (fg_2 == PAGE_TWO) {
                    MyFragment2 fg2 = (MyFragment2) mAdapter.currentFragment;
                    if (fg2 != null) {
                        fg2.onWrite(gatt, characteristic, status);
                    }
                }
            }
        });
        Log.d(TAG, "Writing...");
    }

    @Override
    public void onNotification(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {

        // 在这里应该做区别处理，为方便，统一执行相同的，，，
        handler.post(new Runnable() {
            @Override
            public void run() {
                int fg_2 = vpager.getCurrentItem();
                if (fg_2 == PAGE_TWO) {
                    MyFragment2 fg2 = (MyFragment2) mAdapter.currentFragment;
                    if (fg2 != null) {
                        fg2.onNotification(gatt, characteristic);
                    }
                }
            }
        });
        Log.d(TAG, "Dispatched.");
    }

    @Override
    public void onTriggerTheMeasure() {

        // 从fg2来的信号告诉fg3开始测量啦！！！
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "来自fg2de信号");
                int fg_2 = vpager.getCurrentItem();
                if (fg_2 == PAGE_TWO) {
                    MyFragment3 fg3 = (MyFragment3)mAdapter.getItem(PAGE_THREE);
                    if (fg3 != null) {
                        if (fg3.helper != null && fg3.helper.isConnectedGatt) {
                            fg3.startMeasure();
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "请先连接设备d(^o^)b", Toast.LENGTH_SHORT).show();
                                    vpager.setCurrentItem(2);
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onTriggerUndoTheMeasure() {

        // 从fg2来的信号告诉fg3停止测量啦！！！
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "来自fg2de信号");
                int fg_2 = vpager.getCurrentItem();
                if (fg_2 == PAGE_TWO) {
                    MyFragment3 fg3 = (MyFragment3)mAdapter.getItem(PAGE_THREE);
                    if (fg3 != null) {
                        if (fg3.helper != null && fg3.helper.isConnectedGatt && fg3.helper.isMeasuring) {
                            fg3.stopMeasure();
                        } else {
                            // ignore the event.do nothing.
                        }
                    } else {
                        // ignore the event.do nothing.
                    }
                }
            }
        });
    }
}
