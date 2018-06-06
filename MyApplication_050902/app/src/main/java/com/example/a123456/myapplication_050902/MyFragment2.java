package com.example.a123456.myapplication_050902;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by Jay on 2015/8/28 0028.
 */
public class MyFragment2 extends Fragment {




    Button heart11;
    public MyFragment2() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2,container,false);
        TextView txt_content = (TextView) view.findViewById(R.id.txt_content);
//        heart1 = (TextView) view.findViewById(R.id.heart1);
        txt_content.setText("实时监控");
        heart11=(Button) view.findViewById(R.id.heart1);
        heart11.setText("0");
//        heart11=(Button) view.findViewById(R.id.heart1);
//        heart1=(TextView)view.findViewById(R.id.heart1);


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);


        heart11.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int max=200;
                int min=0;
                Random random=new Random();
                int num=random.nextInt(max)%(max-min+1)+min;


                heart11.setText(""+num);

                if(num<60||num>150){


                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    //赋值标题
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


////                Toast.makeText(getActivity(), "success2", Toast.LENGTH_SHORT).show();
//                String res="欢迎使用。您的心率为"+heart.getText().toString();
//                res+=",体重为"+weight.getText().toString();
//                res+=",血压为"+bpress.getText().toString();
//                res+=",体温为"+temp.getText().toString();
//                res+=",血脂为"+fat.getText().toString();
//                res+="。您的情况基本正常！！";
////                Log.i("i",heart.getText().toString());
//                tv_result.setText(res);


            }
        });

    }


}
