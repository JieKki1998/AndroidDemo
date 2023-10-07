package com.example.androiddemo;

import static java.lang.Math.sqrt;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class AnalysisActivity extends AppCompatActivity {
    int a=0,b=0;
    double R方=0.2f;
    TextView tv_expfunction;
    List<Integer> list;
    //设置数据
    List<Entry> entries01 = new ArrayList<>();//回归的
    List<Entry> entries02 = new ArrayList<>();//实际的
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        Intent intent = getIntent();
        list= (List<Integer>) intent.getSerializableExtra("list");
        Toast.makeText(this,list.toString(),Toast.LENGTH_LONG).show();
        Log.i("jiejie",list+"");
        tv_expfunction = findViewById(R.id.tv_expfunction);
        getFittingLine(list);
        initData();
        initChart();
        tv_expfunction.setText("y="+b+"*x-"+a+"\nR^2="+R方);
    }

    private void getFittingLine(List<Integer>  list) {
        int SUM01=0,SUM02=0,AVG01=0,AVG02=0,s1=0,s2=0,R_SUM01=0,R_SUM02=0,R_SUM03=0;
        for (int i = 0; i < list.size(); i++) {
            SUM01 += i*list.get(i);
            SUM02 +=i*i;
            s1+=i;
            s2+=list.get(i);
        }
        AVG01 = s1/list.size();
        AVG02 = s2/list.size();
        for (int i = 0; i < list.size(); i++) {
           R_SUM01+=(i-AVG01)*(list.get(i)-AVG02);
           R_SUM02 +=(i-AVG01)*(i-AVG01);
           R_SUM03+=(list.get(i)-AVG02)*(list.get(i)-AVG02);
        }
        R方=R_SUM01/sqrt(R_SUM02*R_SUM03);
        b=(SUM01-list.size()*AVG01*AVG02)/(SUM02-list.size()*AVG01*AVG01);
        a = AVG02-b*AVG01;
    }
    int getEValue(int i){
        return b*i+a;
    }

    private void initData() {
        for (int i = 0; i < list.size(); i++) {
            entries01.add(new Entry(i,getEValue(i)));
            entries02.add(new Entry(i,list.get(i)-1));
        }
    }

    private void initChart() {
        //http://t.csdn.cn/ETnml
        LineChart lineChart = (LineChart) findViewById(R.id.lineChart);
        LineDataSet lineDataSet = new LineDataSet(entries01, "折线图");
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        XAxis lineChart_xAxis = lineChart.getXAxis();
        YAxis lineChart_yAxis = lineChart.getAxisLeft();
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineDataSet.setColor(Color.BLACK);
        lineDataSet.setDrawCircleHole(false);//设置曲线值的圆点是实心还是空心
        lineDataSet.setDrawCircles(true);
        lineDataSet.setCircleColor(Color.BLACK);  // 设置数据点圆形的颜色
        lineDataSet.setLineWidth(1f); // 设置折线宽度
        lineDataSet.setCircleRadius(1f); // 设置折现点圆点半径
        MyMarkerView mv = new MyMarkerView(this,a,b,R方);
//        mv.getABR(a,b,R方);
        lineChart.setMarkerView(mv);
        //设置点上显示的值
        lineDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "";
            }
        });
        // 隐藏不希望显示部分，包括坐标轴标题、刻度等等，并禁用了双指放大等功能
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.setTouchEnabled(true);
        setXAxis(lineChart_xAxis,-10,30);
        setYAxis(lineChart_yAxis,-10,120);

        /**设置散点图--设置散点图--设置散点图--设置散点图--设置散点图--设置散点图--设置散点图--设置散点图--**/
        ScatterChart scatterChart =findViewById(R.id.scatter_chart);
/*————————————————
        版权声明：本文为CSDN博主「胖胖中式小笼包」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
        原文链接：https://blog.csdn.net/boulete/article/details/125250089*/
        ScatterDataSet scatterDataSet = new ScatterDataSet(entries02,"散点图");
        scatterDataSet.setColor(Color.BLACK);
        ScatterData scatterData = new ScatterData(scatterDataSet);
        XAxis  scatter_xAxis = scatterChart.getXAxis();
        YAxis  scatter_yAxis = scatterChart.getAxisLeft();
        scatterChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        scatterDataSet.setColor(Color.BLACK);
//        scatterDataSet.setScatterShapeHoleRadius(1f);
        scatterDataSet.setScatterShapeSize(3f);
//        这里需要注意，将散点图的背景色改为透明
        scatterChart.setBackgroundColor(Color.parseColor("#00000000"));
/*        scatterDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "";
            }
        });*/
        // 隐藏不希望显示部分，包括坐标轴标题、刻度等等，并禁用了双指放大等功能
        scatterChart.getAxisRight().setEnabled(false);
        scatterChart.getLegend().setEnabled(false);
        scatterChart.getDescription().setEnabled(false);
        scatterChart.setScaleEnabled(false);
        scatterChart.setTouchEnabled(false);
        setXAxis(scatter_xAxis,-10,30);
        setYAxis(scatter_yAxis,-10,120);
        scatterChart.setData(scatterData);
    }


    /**
     * 功能：设置图标的基本属性
     */
    void setLineChartBasicAttr(LineChart lineChart) {
        /***图表设置***/
        lineChart.setDrawGridBackground(false); //是否展示网格线
        lineChart.setDrawBorders(true); //是否显示边界
        lineChart.setDragEnabled(true); //是否可以拖动
        lineChart.setScaleEnabled(true); // 是否可以缩放
        lineChart.setTouchEnabled(true); //是否有触摸事件
        //设置XY轴动画效果
        //lineChart.animateY(2500);
        lineChart.animateX(1500);
    }
    void setScatterChartBasicAttr(ScatterChart scatterChart) {
        /***图表设置***/
        scatterChart.setDrawGridBackground(false); //是否展示网格线
        scatterChart.setDrawBorders(true); //是否显示边界
        scatterChart.setDragEnabled(true); //是否可以拖动
        scatterChart.setScaleEnabled(true); // 是否可以缩放
        scatterChart.setTouchEnabled(true); //是否有触摸事件
        //设置XY轴动画效果
        //lineChart.animateY(2500);
        scatterChart.animateX(1500);
    }

    private void setXAxis(XAxis xAxis,int min,int max) {
        xAxis.setGridColor(Color.BLACK);
        xAxis.setAxisMinimum(min);
        xAxis.setAxisMaximum(max);
        xAxis.setDrawGridLines(true);
        xAxis.setDrawAxisLine(true);
        xAxis.setEnabled(true);
  /*      xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "";
            }
        });*/

    }    private void setYAxis(YAxis yAxis,int min,int max) {

        yAxis.setGridColor(Color.BLACK);
        yAxis.setAxisMinimum(min);
        yAxis.setAxisMaximum(max);
        yAxis.setDrawGridLines(true);
        yAxis.setDrawAxisLine(true);
 /*       yAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "";
            }
        });*/
    }

}
