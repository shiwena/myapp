package com.example.a123456.myapplication_050902;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    Button zhucewancheng;
    Button fanhuidenglu;
    EditText etzhanghao;
    EditText etxingming;
    EditText etmima;
    EditText etnianling;
    EditText etxingbie;
    EditText etdianhua;

    private String zhanghao;
    private String xingming;
    private String mima;
    private String nianling;
    private String xingbie;
    private String dianhua;
    private Boolean flagr=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        zhucewancheng=(Button)findViewById(R.id.zhucewancheng);
        fanhuidenglu=(Button)findViewById(R.id.fanhuidenglu);
        etzhanghao=(EditText)findViewById(R.id.etzhanghao);
        etxingming=(EditText)findViewById(R.id.etxingming);
        etnianling=(EditText)findViewById(R.id.etnianling);
        etmima=(EditText)findViewById(R.id.etmima);
        etxingbie=(EditText)findViewById(R.id.etxingbie);
        etdianhua=(EditText)findViewById(R.id.etdianhua);
        zhucewancheng.setOnClickListener(this);
        fanhuidenglu.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.zhucewancheng: {
                zhanghao=etzhanghao.getText().toString();
                xingming=etxingming.getText().toString();
                mima=etmima.getText().toString();
                nianling=etnianling.getText().toString();
                xingbie=etxingbie.getText().toString();
                dianhua=etdianhua.getText().toString();
                if(zhanghao.equals("")){
                    Toast.makeText(this,"账号不能为空！",Toast.LENGTH_LONG).show();
                }
                if(mima.length()<6){
                    Toast.makeText(this,"密码不能少于6位",Toast.LENGTH_LONG).show();
                }
                if(zhanghao.length()>0&&mima.length()>=6) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("Loginactivity", "666");
                            Looper.prepare();
                            try {
                                Class.forName("com.mysql.jdbc.Driver");
                                java.sql.Connection cn = DriverManager.getConnection("jdbc:mysql://47.98.170.72:3306/myapp?characterEncoding=utf8", "root", "SWsw1997");
//                            String a="1";
                                Date date = new Date(System.currentTimeMillis());
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String time = sdf.format(date);
                                String sql = "SELECT Userid FROM user where Userid='" + zhanghao + "';";
                                String sql1 = "INSERT INTO user VALUES('" + zhanghao + "','" + xingming + "','" + mima + "','" + nianling + "','" + xingbie + "','" + dianhua + "','" + time + "');";
                                Statement st = (Statement) cn.createStatement();
                                ResultSet rs = st.executeQuery(sql);
                                if (rs.next()) {
                                    Log.i("Mainactivity", "有数据");
                                    flagr = true;
                                } else {
                                    flagr = false;
                                    Log.i("Mainactivity", "没有数据");
                                }
//                                if(rs.next()){
//                                    Toast.makeText(RegisterActivity.this,"该账号已被注册",Toast.LENGTH_SHORT).show();
//                                    Log.i("Loginactivity","777");
//                                }else{
//                                    st.executeUpdate(sql1);
//                                    Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
//                                }
                                if (flagr) {
                                    Toast.makeText(RegisterActivity.this, "该账号已被注册", Toast.LENGTH_LONG).show();
                                    Log.i("Loginactivity", "777");
                                } else {
                                    st.executeUpdate(sql1);
                                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                                }
                                cn.close();
                                st.close();
                                rs.close();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            Looper.loop();
                        }
                    }).start();
                }
//                    Toast.makeText(this,"yes",Toast.LENGTH_SHORT).show();
                break;
            }

            case R.id.fanhuidenglu: {
//                Intent intent = new Intent();
//                intent.setClass(RegisterActivity.this, LoginActivity.class);
//                startActivity(intent);
                finish();
                break;
            }

        }

    }
}
