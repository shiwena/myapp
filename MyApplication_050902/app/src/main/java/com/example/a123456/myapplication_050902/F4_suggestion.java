package com.example.a123456.myapplication_050902;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class F4_suggestion extends AppCompatActivity {


    TextView tv_cancel;
    TextView tv_send;
    TextView tv_username;
    EditText et_content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f4_suggestion);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        tv_username = (TextView)findViewById(R.id.tv_username);
        tv_username.setText(username+"");

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
                Log.i("aaa","123");
                Toast.makeText(F4_suggestion.this,"未完善",Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
