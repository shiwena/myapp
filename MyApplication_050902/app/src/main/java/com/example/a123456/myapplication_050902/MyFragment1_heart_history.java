package com.example.a123456.myapplication_050902;

import android.content.Intent;
import android.graphics.Color;
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

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class MyFragment1_heart_history extends AppCompatActivity {
    private Queue<String> Userheart = new LinkedList<String>();//用于存储数据库的数据，然后作为历史记录的数据
    private Queue<String> Usertime = new LinkedList<String>();
    private Queue<String> Userheart2 = new LinkedList<String>();//作为折线图的数据。
    private Queue<String> Usertime2 = new LinkedList<String>();
    //为什么设两对相同的呢？因为组员LGY想把历史数据和折线图的处理分开写。
    private String Uheart="";//这一对用作中间数据。
    private String Utime="";

    private String jieguo;//当然是存分析结果的啦
    private String User_id;
    private int xl=0;//心率的int型
    private SimpleDateFormat t = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//时间格式
    private SimpleDateFormat t2 = new SimpleDateFormat("yyyy-MM-dd");
    private Date date2;
    private Date date3;

    //折线图测试
    private LineChartView lineChart;

    String[] date1 = {"5-23", "5-22", "6-22", "5-23", "5-22", "2-22", "5-22", "4-22", "9-22", "10-22", "11-22", "12-22", "1-22", "6-22", "5-23", "5-22", "2-22", "5-22", "4-22", "9-22", "10-22", "11-22", "12-22", "4-22", "9-22", "10-22", "11-22", "zxc"};//X轴的标注
    int[] score = {74, 22, 18, 79, 20, 74, 20, 74, 42, 90, 74, 42, 90, 50, 42, 90, 33, 10, 74, 22, 18, 79, 20, 74, 22, 18, 79, 120};//图表的数据
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();


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
                        Uheart=rs.getString("Userheart");
                        Userheart.offer(Uheart);
                        Userheart2.offer(Uheart);
                        Utime=rs.getString("Testtime");
                        try {
                            date2=t.parse(Utime);
                            date3=t.parse(Utime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Usertime.offer(t.format(date2));//将String变为date,又将date变回String并不是无意义的，因为需要规范时间格式。
                        Usertime2.offer(t2.format(date3));
                        //Log.i("heart1111111",Userheart.poll()+Usertime.poll());
                        //Log.i("heart2",Userheart2.poll()+"\t"+Usertime2.poll());

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
                //折线图的调用
                lineChart = (LineChartView) findViewById(R.id.line_chart);
                getAxisXLables();//获取x轴的标注
                getAxisPoints();//获取坐标点
                initLineChart();//初始化




                List<Map<String, Object>> listitem = new ArrayList<Map<String, Object>>();
                List<Map<String, Object>> listitem2 = new ArrayList<Map<String, Object>>();
                List<Map<String, Object>> listitem3 = new ArrayList<Map<String, Object>>();
                //Log.i("heart2222222",Userheart.poll()+Usertime.poll());
                while (Usertime.peek() != null) {
                    Map<String, Object> showitem = new HashMap<String, Object>();
                    Map<String, Object> showitem2 = new HashMap<String, Object>();
                    Map<String, Object> showitem3 = new HashMap<String, Object>();
                    xl=Integer.parseInt(Userheart.peek());


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
                    showitem.put("shijian", Usertime.poll());
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
    /**
     * 初始化LineChart的一些设置
     */
    private void initLineChart() {
        Line line = new Line(mPointValues).setColor(Color.parseColor("#57C1FD"));  //折线的颜色
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
        line.setCubic(false);//曲线是否平滑
//	    line.setStrokeWidth(3);//线条的粗细，默认是3
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
//		line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(false);  //X轴下面坐标轴字体是斜的显示还是直的，true是斜的显示
//	    axisX.setTextColor(Color.WHITE);  //设置字体颜色
        axisX.setTextColor(Color.parseColor("#666666"));//黑色

        axisX.setName("日期");  //x轴备注
        axisX.setTextSize(11);//设置字体大小
        axisX.setMaxLabelChars(7); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
//	    data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线


        Axis axisY = new Axis();  //Y轴
        axisY.setName("心率: 分/下");//y轴标注
        axisY.setTextColor(Color.parseColor("#666666"));
        axisY.setTextSize(11);//设置字体大小
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边
        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);  //缩放类型，水平
        lineChart.setMaxZoom((float) 3);//缩放比例
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);
        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 0;
        v.right = 7;
        lineChart.setCurrentViewport(v);
    }

    /**
     * X 轴的显示
     */
    private void getAxisXLables() {
        int i = 0;
        while (Usertime2.peek() != null){
            //Log.i("zhexiantupoint1",Usertime2.poll());
            mAxisXValues.add(new AxisValue(i).setLabel(Usertime2.poll()));
            i++;
        }
//        for (int i = 0; i < date1.length; i++) {
//            mAxisXValues.add(new AxisValue(i).setLabel(Usertime2.poll()));
//            //mAxisXValues.add(new AxisValue(i).setLabel(date1[i]));
//        }

    }

    /**
     * 图表的每个点的显示
     */
    private void getAxisPoints() {
        int i = 0;
        while (Userheart2.peek() != null){
            //Log.i("zhexiantupoint2",Userheart2.poll());
            mPointValues.add(new PointValue(i, Float.parseFloat(Userheart2.poll())));
            i++;
        }
//        for (int i = 0; i < score.length; i++) {
//            mPointValues.add(new PointValue(i, score[i]));
//        }
    }
}
