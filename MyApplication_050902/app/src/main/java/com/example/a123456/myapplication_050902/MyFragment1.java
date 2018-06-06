package com.example.a123456.myapplication_050902;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MyFragment1 extends Fragment {

    EditText heart;
    EditText weight;
    EditText bpress;
    EditText temp;
    EditText fat;
    Button fenxi;
    Button tts;
    TextView tv_result;


    private int heart1 = 0;
    private int weight1 = 0;
    private int bpress1 = 0;
    private int temp1 = 0;
    private int fat1 = 0;
    private TextToSpeech textToSpeech;


    public MyFragment1() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, container, false);
        TextView txt_content = (TextView) view.findViewById(R.id.txt_content);


        heart = (EditText) view.findViewById(R.id.heart);
        weight = (EditText) view.findViewById(R.id.weight);
        bpress = (EditText) view.findViewById(R.id.bpress);
        temp = (EditText) view.findViewById(R.id.temp);
        fat = (EditText) view.findViewById(R.id.fat);
        fenxi = (Button) view.findViewById(R.id.fenxi);
        tts = (Button) view.findViewById(R.id.tts);
        txt_content.setText("我的状态");
        tv_result=(TextView)view.findViewById(R.id.tv_result);
//

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);


        fenxi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                Toast.makeText(getActivity(), "success2", Toast.LENGTH_SHORT).show();
                String res="您的心率为"+heart.getText().toString();
                res+=",体重为"+weight.getText().toString();
                res+=",血压为"+bpress.getText().toString();
                res+=",体温为"+temp.getText().toString();
                res+=",血脂为"+fat.getText().toString();
                res+="。您的情况基本正常！！";
//                Log.i("i",heart.getText().toString());
                tv_result.setText(res);

            }
        });

        textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener(){
            @Override
            public void onInit(int status){
                if (status == textToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.CHINA);
                    if (result != TextToSpeech.LANG_COUNTRY_AVAILABLE && result != TextToSpeech.LANG_AVAILABLE){
                        Toast.makeText(getActivity(),"暂不支持该语言",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        tts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textToSpeech.speak(tv_result.getText().toString(),TextToSpeech.QUEUE_ADD,null);

            }
        });



    }


}
