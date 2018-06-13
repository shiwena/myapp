package com.example.a123456.myapplication_050902;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class F4_suggestion extends AppCompatActivity {


    TextView tv_cancel;
    TextView tv_send;
    EditText et_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f4_suggestion);

        et_content = (EditText)findViewById(R.id.et_content);


        tv_cancel = (TextView)findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        tv_send = (TextView)findViewById(R.id.tv_send);
        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });




    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
