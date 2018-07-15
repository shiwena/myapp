package com.example.a123456.myapplication_050902;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class F4_suggestion extends AppCompatActivity {


    TextView tv_cancel;
    TextView tv_send;
    TextView tv_username;
    EditText et_content;
    private String username;
    private String detail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f4_suggestion);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_username.setText(username + "");

        et_content = (EditText) findViewById(R.id.et_content);




        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        tv_send = (TextView) findViewById(R.id.tv_send);
        tv_send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                detail = et_content.getText().toString();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();

                        try {
                            Class.forName("com.mysql.jdbc.Driver");
                            java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://119.23.70.24:3306/car", "root", "qwaszx");

                            String sql = "insert into user_suggestion(username,detail) values ('" + username + "','" + detail + "')";
                            Statement st = (Statement) con.createStatement();
                            st.execute(sql);

                            con.close();
                            st.close();

                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        Looper.loop();

                    }
                }).start();


                et_content.setText("");
                Toast.makeText(F4_suggestion.this, "发送成功", Toast.LENGTH_SHORT).show();

            }

        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
