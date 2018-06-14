package com.example.a123456.myapplication_050902;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class MyFragment1_heart_history extends AppCompatActivity {
    private Queue<String> Userheart = new LinkedList<String>();
    private Queue<String> Usertime = new LinkedList<String>();
    private String jieguo;
    private String User_id;
    private int xl=0;
    private SimpleDateFormat t = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//时间格式
    Date date;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment1_heart_history);

        Intent i = getIntent();
        User_id = i.getStringExtra("User_id");
        Log.i("heart_history", "————当前用户为————————：" + User_id);


        //从数据库中获取历史数据。
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("hearthistory", "666");
                Looper.prepare();
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    java.sql.Connection cn = DriverManager.getConnection("jdbc:mysql://47.98.170.72:3306/myapp?characterEncoding=utf8", "root", "SWsw1997");
                    java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time = sdf.format(date);
                    String sql = "select * from userheart where Userid ='" + User_id + "'order by Testtime desc;";

                    Statement st = (Statement) cn.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while (rs.next()) {

                        Userheart.offer(rs.getString("Userheart"));
                        Usertime.offer(rs.getString("Testtime"));
                        //Log.i("heart1111111",Userheart.poll()+Usertime.poll());

                    }
//
                    //Log.i("heart4444444",Userheart.poll()+Usertime.poll());
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
        //Log.i("heart33333333",Userheart.poll()+Usertime.poll());
        long delay = 1000;
        new Handler().postDelayed(new Runnable() {//这里也要写线程，并且延迟时间要长，不然会抢在数据库操作之前执行。
            public void run() {



                List<Map<String, Object>> listitem = new ArrayList<Map<String, Object>>();
                List<Map<String, Object>> listitem2 = new ArrayList<Map<String, Object>>();
                List<Map<String, Object>> listitem3 = new ArrayList<Map<String, Object>>();
                //Log.i("heart2222222",Userheart.poll()+Usertime.poll());
                while (Usertime.peek() != null) {
                    Map<String, Object> showitem = new HashMap<String, Object>();
                    Map<String, Object> showitem2 = new HashMap<String, Object>();
                    Map<String, Object> showitem3 = new HashMap<String, Object>();
                    xl=Integer.parseInt(Userheart.peek());
                    try {
                        date=t.parse(Usertime.poll());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    jieguo="心率正常";
                    if (xl > 100&&xl<160)//心率过快
                    {
                        jieguo="心率较快";
                    }else if(xl>=160){
                        jieguo="心率过快";
                    }
                    else if (xl < 60)//心率过缓
                    {
                        jieguo="心率过缓";
                    }
                    showitem.put("shijian", t.format(date));//将String变为date,又将date变回String并不是无意义的，因为需要规范时间格式。
                    showitem2.put("xinlv", Userheart.poll());
                    showitem3.put("jieguo", jieguo);
                    listitem.add(showitem);
                    listitem2.add(showitem2);
                    listitem3.add(showitem3);
                }
                //创建一个simpleAdapter
                SimpleAdapter myAdapter = new SimpleAdapter(getApplicationContext(), listitem, R.layout.fragment1_heart_adapter, new String[]{"shijian"}, new int[]{R.id.heart_time});
                SimpleAdapter myAdapter2 = new SimpleAdapter(getApplicationContext(), listitem2, R.layout.fragment1_heart_adapter2, new String[]{"xinlv"}, new int[]{R.id.heart_heart});
                SimpleAdapter myAdapter3 = new SimpleAdapter(getApplicationContext(), listitem3, R.layout.fragment1_heart_adapter3, new String[]{"jieguo"}, new int[]{R.id.heart_result});
                ListView listView = (ListView) findViewById(R.id.list_view);
                ListView listView2 = (ListView) findViewById(R.id.list_view2);
                ListView listView3 = (ListView) findViewById(R.id.list_view3);
                listView.setAdapter(myAdapter);
                listView2.setAdapter(myAdapter2);
                listView3.setAdapter(myAdapter3);
                listView.setVerticalScrollBarEnabled(false);//禁止滚动条
                listView.setFastScrollEnabled(false);
                listView2.setVerticalScrollBarEnabled(false);
                listView2.setFastScrollEnabled(false);
                listView3.setVerticalScrollBarEnabled(false);
                listView3.setFastScrollEnabled(false);
                setListViewOnTouchAndScrollListener(listView,listView2,listView3);
            }
        }, delay);
    }
    public void setListViewOnTouchAndScrollListener(final ListView listView,final ListView listView2,final ListView listView3){


        //设置listview2列表的scroll监听，用于滑动过程中左右不同步时校正
        listView2.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //如果停止滑动
                if(scrollState == 0 || scrollState == 1){
                    //获得第一个子view
                    View subView = view.getChildAt(0);

                    if(subView !=null){
                        final int top = subView.getTop();
                        final int top1 = listView.getChildAt(0).getTop();
                        final int top3 = listView3.getChildAt(0).getTop();
                        final int position = view.getFirstVisiblePosition();

                        //如果两个首个显示的子view高度不等
                        if(top != top1&&top!=top3&&top1!=top3){
                            listView.setSelectionFromTop(position, top);
                            listView3.setSelectionFromTop(position, top);
                        }
                    }
                }

            }

            public void onScroll(AbsListView view, final int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                View subView = view.getChildAt(0);
                if(subView != null){
                    final int top = subView.getTop();

//      //如果三个首个显示的子view高度不等
                    int top1 = listView.getChildAt(0).getTop();

                    //if(!(top1 - 2 < top &&top < top1 + 2)){
                        listView.setSelectionFromTop(firstVisibleItem, top);
                        listView2.setSelectionFromTop(firstVisibleItem, top);
                        listView3.setSelectionFromTop(firstVisibleItem, top);
                    //}

                }
            }
        });

        //设置listview列表的scroll监听，用于滑动过程中左右不同步时校正
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == 0 || scrollState == 1){
                    //获得第一个子view
                    View subView = view.getChildAt(0);

                    if(subView !=null){
                        final int top = subView.getTop();
                        final int top2 = listView2.getChildAt(0).getTop();
                        final int top3=listView3.getChildAt(0).getTop();
                        final int position = view.getFirstVisiblePosition();

                        //如果两个首个显示的子view高度不等
                        if(top != top2&&top!=top3&&top2!=top3){
                            listView.setSelectionFromTop(position, top);
                            listView2.setSelectionFromTop(position, top);
                            listView3.setSelectionFromTop(position, top);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, final int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                View subView = view.getChildAt(0);
                if(subView != null){
                    final int top = subView.getTop();
                    listView.setSelectionFromTop(firstVisibleItem, top);
                    listView2.setSelectionFromTop(firstVisibleItem, top);
                    listView3.setSelectionFromTop(firstVisibleItem, top);
                }
            }
        });
        //设置listview3列表的scroll监听，用于滑动过程中左右不同步时校正
        listView3.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == 0 || scrollState == 1){
                    //获得第一个子view
                    View subView = view.getChildAt(0);

                    if(subView !=null){
                        final int top = subView.getTop();
                        final int top1 = listView.getChildAt(0).getTop();
                        final int top2 = listView2.getChildAt(0).getTop();

                        final int position = view.getFirstVisiblePosition();

                        //如果两个首个显示的子view高度不等
                        if(top != top1&&top!=top2&&top1!=top2){
                            listView.setSelectionFromTop(position, top);
                            listView2.setSelectionFromTop(position, top);
                            listView3.setSelectionFromTop(position, top);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, final int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                View subView = view.getChildAt(0);
                if(subView != null){
                    final int top = subView.getTop();
                    listView.setSelectionFromTop(firstVisibleItem, top);
                    listView2.setSelectionFromTop(firstVisibleItem, top);
                    listView3.setSelectionFromTop(firstVisibleItem, top);
                }
            }
        });
    }

}
