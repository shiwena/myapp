package com.example.a123456.myapplication_050902;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyFragment1 extends Fragment {

    EditText heart;
    EditText weight;
    EditText bpress;
    EditText temp;
    EditText fat;
    Button fenxi;
    Button tts;
    ImageView heartbutton;
    TextView tv_result;
    TextView Text_xinlv;
    TextView Text_tizhong;
    TextView Text_xueya;
    TextView Text_tiwen;
    TextView Text_xuezhi;
    Bundle bundle;

    private Date date;
    private String Testtime;
    private String Userid;
    private int heart1 = 0;     //心率标志。
    private int BMI1 = 0;       //BMI标志。
    private int bpress1 = 0;    //血压标志
    private int temp1 = 0;      //体温标志
    private int fat1 = 0;       //体脂标志
    private SimpleDateFormat t = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//时间格式

    private Double height=0.0;
    //以下为各指标的数据类型，因此放在一起。
    private int xl=0;
    private Double xz=0.0;
    private double tw=0.0;
    private String xxyy[]=new String[2];
    private int xy[]=new int[2];
    private Double BMI=0.0;
    private Double tz=0.0;
    //
    private String xinlv;
    private String tizhong;
    private String xueya;
    private String tiwen;
    private String xuezhi;
    private TextToSpeech textToSpeech;
    //以下为对健康的分析建议。
    String xinlvjianyi[]={"","您的心率过快。","您的心率远高于正常值，可能有心脏疾病。","您的心率过低。","您的心率远低于正常值，可能有生命危险！"};
    String BMIjianyi[]={"","您的体重过重","您的体重过重，属于肥胖。","您的体重过重，属于非常肥胖。","您的体重过低。","您还未录入身高信息，无法进行BMI计算！"};
    String xueyajianyi[]={"","您的血压过高。","您的血压过低。"};
    String tiwenjianyi[]={"","您体温过高，请注意休息。","您正在发高烧，请到医院就医。","您体温过低，请注意休息。"};
    String xuezhijianyi[]={"","您的血脂过低。","您的血脂过高。"};


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
        heartbutton=(ImageView) view.findViewById(R.id.heart_button);
        txt_content.setText("我的状态");
        tv_result=(TextView)view.findViewById(R.id.tv_result);
        Text_xinlv=(TextView)view.findViewById(R.id.xinlv);
        Text_tizhong=(TextView)view.findViewById(R.id.tizhong);
        Text_xueya=(TextView)view.findViewById(R.id.xueya);
        Text_tiwen=(TextView)view.findViewById(R.id.tiwen);
        Text_xuezhi=(TextView)view.findViewById(R.id.xuezhi);

        //这两行是为了获取从MyFragmentPagerAdapter.java中传来的用户id
        bundle = getArguments();
        Userid=bundle.getString("id");

        Log.i("MyFragment1LGY","————当前用户为————————："+Userid);
        //数据库查询身高数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("Loginactivity", "666");
                Looper.prepare();
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    java.sql.Connection cn = DriverManager.getConnection("jdbc:mysql://47.98.170.72:3306/myapp?characterEncoding=utf8", "root", "SWsw1997");
                    java.sql.Date date = new java.sql.Date(System.currentTimeMillis());


                    String sql = "SELECT userheight FROM user where Userid='" + Userid + "';";

                    Statement st = (Statement) cn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    if (rs.next()) {
                        height=Double.parseDouble(rs.getString("userheight"));
                        Log.i("Mainactivity", "有数据+"+height);

                    } else {
                        Log.i("Mainactivity", "没有数据");
                    }
//

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
                xinlv = "";
                tizhong = "";
                xueya = "";
                tiwen = "";
                xuezhi = "";
                heart1 = 0;
                BMI1 = 0;
                bpress1 = 0;
                temp1 = 0;
                fat1 = 0;       //将这些标志初始为空，要不然玩多次之后就会出错
                String res="您的";            //要显示的文字
                boolean health=true;        //如果接下来的健康数据没问题，那么health将一直为true
                date = new Date();//按下“分析建议”按钮的时间
                Testtime = t.format(date);//date的String形式
                xinlv = heart.getText().toString();    //心率
                if (!xinlv.equals("")) {
                    Text_xinlv.setText(Testtime);
                    xl = Integer.parseInt(xinlv);
                }     //心率的int型

                tizhong = weight.getText().toString(); //体重
                if (!tizhong.equals(""))
                {
                    Text_tizhong.setText(Testtime);
                    tz=Double.parseDouble(tizhong);
                }   //体重的Double型
                xueya=bpress.getText().toString();   //血压
                if(!xueya.equals("")) {
                    Text_xueya.setText(Testtime);
                    xxyy = xueya.split("/");     //存储血压中间值
                    for (int i = 0; i <= 1; i++) {
                        xy[i] = Integer.parseInt(xxyy[i]);
                    }//将血压的收缩压和舒张压分别存储为int型
                }
                tiwen=temp.getText().toString();     //体温
                if(!tiwen.equals("")) {
                    Text_tiwen.setText(Testtime);
                    tw = Double.parseDouble(tiwen);     //体温的Double型
                }
                xuezhi=fat.getText().toString();     //血脂
                if(!xuezhi.equals("")) {
                    Text_xuezhi.setText(Testtime);
                    xz = Double.parseDouble(xuezhi);
                }







                //接下来将判断各项数据是否为空
                if(!xinlv.equals(""))
                {
                    res+="心率为"+xinlv+",";
                    if(xl>100)//心率过快
                    {
                        heart1=1;
                        health=false;
                    }
                    else if (xl>160)//心脏疾病
                    {
                        heart1=2;
                        health=false;
                    }
                    else if(xl>=40&&xl<60)//心率过缓
                    {
                        heart1=3;
                        health=false;
                    }
                    else if(xl<40)//生命危险
                    {
                        heart1=4;
                        health=false;
                    }
                }
                if(!tizhong.equals(""))
                {

                    res+="体重为"+tizhong+",";
                    if(height>0){                  //只有在录入身高时才能进行BMI计算
                        BMI=tz/(height*height);
                        if(BMI>23.9&&BMI<=27)//过重
                        {
                            BMI1 = 1;
                            health=false;
                        }
                        else if(BMI>27&&BMI<=32)//肥胖
                        {
                            BMI1 = 2;
                            health=false;
                        }
                        else if(BMI>32)//非常肥胖
                        {
                            BMI1 = 3;
                            health=false;
                        }
                        else if(BMI<18.5)//过轻
                        {
                            BMI1 = 4;
                            health=false;
                        }
                    }
                    else{
                        BMI1=5;
                    }

                }
                if(!xueya.equals(""))
                {
                    if(xy[0]>=140&&xy[1]>=90)//高血压
                    {
                        bpress1 = 1;
                        health=false;
                    }
                    else if(xy[0]<90&&xy[1]<60){//低血压
                        bpress1 = 2;
                        health=false;
                    }
                    res+="血压为"+xueya+",";
                }
                if(!tiwen.equals(""))
                {
                    if(tw>=37.3&&tw<=38.0){//发烧
                        temp1 = 1;
                        health=false;
                    }
                    else if(tw>=38.1)//高烧
                    {
                        temp1 = 2;
                        health=false;
                    }
                    else if(tw<36)//低烧
                    {
                        temp1 = 3;
                        health=false;
                    }
                    res+="体温为"+tiwen+",";
                }
                if(!xuezhi.equals(""))
                {

                    if(xz<2.9){//血脂过低
                        health=false;
                        fat1=1;
                    }
                    else if (xz>5.17)//血脂过高
                    {
                        health=false;
                        fat1=2;
                    }
                    res+="血脂为"+xuezhi+"。";
                }
                if(health==true)
                {
                    res+="情况基本正常！！";
                }
                res+=xinlvjianyi[heart1];
                res+=BMIjianyi[BMI1];
                res+=xueyajianyi[bpress1];
                res+=xuezhijianyi[fat1];
                res+=tiwenjianyi[temp1];
//                Log.i("i",heart.getText().toString());
                tv_result.setText(res);



                //向数据库中插入未空的信息。
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        try
                        {
                            Class.forName("com.mysql.jdbc.Driver");
                            java.sql.Connection cn = DriverManager.getConnection("jdbc:mysql://47.98.170.72:3306/myapp?characterEncoding=utf8", "root", "SWsw1997");
//
                            Log.i("MyFragment1LGY","————正在插入数据库————————："+Testtime);
                            String sqlbfat = "INSERT INTO userbloodfat VALUES('" + Userid + "','" + Testtime+ "','" + xz + "');";//插入血脂
                            String sqlbpress="INSERT INTO userbloodpress VALUES('" + Userid + "','" + Testtime+ "','" + xy[0]+ "','"+xy[1]+"');";//插入血压
                            String sqlheart = "INSERT INTO userheart VALUES('" + Userid + "','" + Testtime+ "','" + xl + "');";//插入心率
                            String sqltemp = "INSERT INTO usertemperature VALUES('" + Userid + "','" + Testtime+ "','" + tw + "');";//插入体温
                            String sqlweight="INSERT INTO userweight VALUES('" + Userid + "','" + Testtime+ "','" + tz+ "','"+BMI+"');";//插入体重和BMI
                            Statement st = (Statement) cn.createStatement();

                            if(!xinlv.equals("")) {st.executeUpdate(sqlheart);}
                            if(!tizhong.equals("")){st.executeUpdate(sqlweight);}
                            if(!xueya.equals("")){st.executeUpdate(sqlbpress);
                            }
                            if(!tiwen.equals("")){st.executeUpdate(sqltemp);}
                            if(!xuezhi.equals("")){st.executeUpdate(sqlbfat);}

                            cn.close();
                            st.close();


                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        Looper.loop();
                    }
                }).start();
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
        heartbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(getActivity().getApplicationContext(),MyFragment1_heart_history.class);
                intent1.putExtra("User_id",Userid);
                startActivity(intent1);

            }
        });



    }


}
