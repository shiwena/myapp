package com.example.a123456.myapplication_050902;

import java.util.UUID;


/*
* 一些常量值
* */
public class Consts {

    // 0000 2902 0000 1000800000805F9B34FB
    public static final String BASE_UUID = "0000%s-0000-1000-8000-00805f9b34fb";

    //  service
    public static final UUID SERVICE_ALERT = UUID.fromString(String.format(BASE_UUID, "1802"));
    public static final UUID SERVICE_NOTIFICATION = UUID.fromString(String.format(BASE_UUID, "1811"));
    public static final UUID SERVICE_MIBAND1_SERVICE = UUID.fromString(String.format(BASE_UUID, "FEE0"));
    public static final UUID SERVICE_MIBAND2_SERVICE = UUID.fromString(String.format(BASE_UUID, "FEE1"));
    public static final UUID SERVICE_HEARTBEAT = UUID.fromString(String.format(BASE_UUID, "180D"));


    // characteristic
    public static final UUID CHARACTERISTIC_ALERT = UUID.fromString(String.format(BASE_UUID, "2A06"));
    public static final UUID CHARACTERISTIC_AUTH = UUID.fromString("00000009-0000-3512-2118-0009af100700");
    public static final UUID CHARACTERISTIC_HEART_NOTIFICATION = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");
    public static final UUID CHARACTERISTIC_SENSOR = UUID.fromString("00000001-0000-3512-2118-0009af100700");
    public static final UUID CHARACTERISTIC_STEPS = UUID.fromString("00000007-0000-3512-2118-0009af100700");
    public static final UUID CHARACTERISTIC_BUTTON_TOUCH = UUID.fromString("00000010-0000-3512-2118-0009af100700");
    public static final UUID CHARACTERISTIC_HZ = UUID.fromString("00000002-0000-3512-2118-0009af100700");

    // descriptor
    public static final UUID NOTIFICATION_DESCRIPTOR = UUID.fromString(String.format(BASE_UUID, "2902"));
    public static final UUID HEARTRATE_CONTROL_POINT = UUID.fromString("00002a39-0000-1000-8000-00805f9b34fb");

    // alert type
    public static final byte[] NONE = new byte[]{0x00, 0x00};
    public static final byte[] MESSAGE = new byte[]{0x01, 0x00};
    public static final byte[] PHONE = new byte[]{0x02, 0x00};
    public static final byte[] BYTE_LAST_HEART_RATE_SCAN = {15, 1, 1};
    public static final byte[] BYTE_NEW_HEART_RATE_SCAN = {15, 2, 1};
}