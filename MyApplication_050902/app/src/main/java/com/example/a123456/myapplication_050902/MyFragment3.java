package com.example.a123456.myapplication_050902;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Jay on 2015/8/28 0028.
 */
public class MyFragment3 extends Fragment {

    public MyFragment3() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment3,container,false);
        TextView txt_content = (TextView) view.findViewById(R.id.txt_content);


        return view;
    }
}
