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

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button button18;
    Button button19;

    EditText editText;
    EditText editText2;

    private String Userid;
    private String Userpw;
    private String pw1;
    private Boolean flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        button18 = (Button) findViewById(R.id.button18);//登录
        button19 = (Button) findViewById(R.id.button19);//注册

        editText = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);
        button18.setOnClickListener(this);
        button19.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button18: {
                Userid = editText.getText().toString();
                pw1 = editText2.getText().toString();
//                Toast.makeText(this,"点击1次",Toast.LENGTH_SHORT).show();
//                Toast.makeText(this,Userid,Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("Loginactivity", "666");
                        Looper.prepare();
                        try {
                            Class.forName("com.mysql.jdbc.Driver");
                            java.sql.Connection cn = DriverManager.getConnection("jdbc:mysql://47.98.170.72:3306/myapp", "root", "SWsw1997");
//                            String a="1";
                            String sql = "SELECT Userpw FROM user where Userid='" + Userid + "'";
                            Statement st = (Statement) cn.createStatement();
                            ResultSet rs = st.executeQuery(sql);
                            if (rs.next()) {
                                Log.i("Mainactivity", "有数据");
                                flag = true;
                            } else {
                                flag = false;
                                Log.i("Mainactivity", "没有数据");
                            }

                            rs = st.executeQuery(sql);
                            while (rs.next()) {
                                Userpw = rs.getString("Userpw");
                                Log.i("Loginactivity", Userpw);
                            }
                            cn.close();
                            st.close();
                            rs.close();

//                     if(flag){
//                         if(pw1.equals(Userpw)){
//                         Toast.makeText(this,"登陆成功",Toast.LENGTH_SHORT).show();
//                    }
//                }
                            if (flag == true) {
//                            Toast.makeText(MainActivity.this,"有东西",Toast.LENGTH_SHORT).show();
                                if (pw1.equals(Userpw)) {
                                    User user = new User(Userid, Userpw);

                                    Flag.user = user;
                                    String test1 = user.Userid;
                                    Log.i("Loginactivity", "————当前用户为————————：" + test1);
                                    Toast.makeText(LoginActivity.this, "登陆成功！", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent();
                                    intent.setClass(LoginActivity.this, MainActivity.class);
                                    //组员LGY需要Userid的传值。
                                    intent.putExtra("User_id", Userid);
                                    startActivity(intent);


                                } else {
                                    Toast.makeText(LoginActivity.this, "登陆失败！账号或密码错误", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "账号未注册！！", Toast.LENGTH_SHORT).show();
                            }


                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        Looper.loop();

                    }
                }).start();


                break;
            }
//            下一个
            case R.id.button19: {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
//                finish();
                break;

            }


        }
    }


    public boolean login(final String username, String pas) {

        boolean f = false;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            java.sql.Connection cn = DriverManager.getConnection("jdbc:mysql://47.98.170.72:3306/myapp",
                    "root", "SWsw1997");
            String sql = "SELECT Userpw FROM user where Userid='" + username + "'";
            Statement st = (Statement) cn.createStatement();
            ResultSet rs;

            rs = st.executeQuery(sql);
            if(rs.next()) {
                Userpw = rs.getString("Userpw");
                if (Userpw.equals(pas)){
                    return true;
                }
                if(!Userpw.equals(pas)){
                    return false;
                }
            }else return false;
            cn.close();
            st.close();
            rs.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return f;
    }


}
