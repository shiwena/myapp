package com.example.a123456.myapplication_050902;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class DeviceItemAdapter extends BaseAdapter {

    public List<BluetoothDevice> mList;
    private LayoutInflater mInflater;

    public DeviceItemAdapter() {}

    public DeviceItemAdapter(Context context, List<BluetoothDevice> list) {
        mList = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();

        if  (convertView == null){
            convertView = mInflater.inflate(R.layout.devices_item, null);
            viewHolder.name = (TextView)convertView.findViewById(R.id.bluetoothname);
            viewHolder.uuid = (TextView)convertView.findViewById(R.id.uuid);
            viewHolder.status = (TextView)convertView.findViewById(R.id.status);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        BluetoothDevice bd = mList.get(position);
        viewHolder.name.setText(bd.getName());
        viewHolder.uuid.setText(bd.getAddress());

        return convertView;
    }

    class ViewHolder {
        private TextView name;
        private TextView uuid;
        private TextView status;
    }
}

