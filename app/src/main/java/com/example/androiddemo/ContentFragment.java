package com.example.androiddemo;

import static java.lang.Math.sqrt;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;

import java.util.ArrayList;
import java.util.List;

public class ContentFragment extends Fragment {
    int a=0,b=0;
    boolean adaptflag=false;
    double R方=0.2f;
    Button btn_adapt;
    TextView tv_expfunction,tv_xTitle,tv_yTitle;
    LineChart lineChart;
    ScatterChart scatterChart;
    private String strContent;
    private int bgColor;
    private FragmentManager fManager;
    List<Integer> list;
    List<Entry> line_entries = new ArrayList<>();//回归的
    List<Entry> scatter_entries = new ArrayList<>();//实际的
    XAxis lineChart_xAxis,scatter_xAxis;
    YAxis  lineChart_yAxis,scatter_yAxis;
    int xMin=-10, xMax=30, yMin=-10, yMax =120;
    String xtitle="浓度",ytitle="灰度";
    View view;
//    public ContentFragment(List<Integer> list) {
//        this.list=list;
//    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fg_content, container, false);
        view.setBackgroundColor(bgColor);
        initViews();
        getFittingLine(list);
        initData();
        drawChart();
        return view;
    }

    public void setXtitle(String xtitle) {
        this.xtitle = xtitle;
    }

    public void setYtitle(String ytitle) {
        this.ytitle = ytitle;
    }


    private void initViews() {
        lineChart= (LineChart) view.findViewById(R.id.lineChart);
        scatterChart=view.findViewById(R.id.scatter_chart);
        tv_expfunction = view.findViewById(R.id.tv_expfunction);
        tv_xTitle = view.findViewById(R.id.tv_xTitle);
        tv_yTitle = view.findViewById(R.id.tv_yTitle);
        btn_adapt=view.findViewById(R.id.btn_adapt);
    }

    public void setList(List<Integer> list) {
        this.list = list;
    }


    /**
     * 获取拟合直线
     * @param list
     */
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
        tv_expfunction.setText("y="+b+"*X-"+a+"\nR^2="+R方);
    }
    int getEValue(int i){
        return b*i+a;
    }
    private void initData() {
        for (int i = 0; i < list.size(); i++) {
            line_entries.add(new Entry(i,getEValue(i)));
            scatter_entries.add(new Entry(i,list.get(i)-1));
        }
    }
    void getLocalData_AxisRange(){
        SharedPreferences sp = getActivity().getSharedPreferences("axis_sp_info", Context.MODE_PRIVATE);
        xMin = sp.getInt("xMin",0);
        xMax = sp.getInt("xMax",0);
        yMin = sp.getInt("yMin",0);
        yMax = sp.getInt("yMax",0);
    }
    void getLocalData_AxisTitle(){
        SharedPreferences sp2 = view.getContext().getSharedPreferences("axistitle_sp_info", Context.MODE_PRIVATE);
        String xTitle = sp2.getString("xTitle","");
        String yTitle = sp2.getString("yTitle","");
        SharedPreferences.Editor  editor =sp2.edit();
        if (xTitle.length()!=0&&yTitle.length()!=0)
        {
            Toast.makeText(view.getContext(),yTitle,Toast.LENGTH_SHORT).show();
            setXtitle(xTitle);
            setYtitle(yTitle);
            tv_xTitle.setText(xtitle);
            tv_yTitle.setText(ytitle);
        }
    }
    private void drawChart() {
        getLocalData_AxisRange();
        getLocalData_AxisTitle();
        //http://t.csdn.cn/ETnml
        drawScatterChart(scatter_entries);
        drawLineChart(null);
        btn_adapt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawLineChart(line_entries);
            }
        });
    }

    private void drawScatterChart(List<Entry> scatter_entries) {
        /**设置散点图--设置散点图--设置散点图--设置散点图--设置散点图--设置散点图--设置散点图--设置散点图--**/

/*————————————————
        版权声明：本文为CSDN博主「胖胖中式小笼包」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
        原文链接：https://blog.csdn.net/boulete/article/details/125250089*/
        ScatterDataSet scatterDataSet = new ScatterDataSet(scatter_entries,"散点图");
        scatterDataSet.setColor(Color.BLACK);
        ScatterData scatterData = new ScatterData(scatterDataSet);
        scatter_xAxis = scatterChart.getXAxis();
        scatter_yAxis = scatterChart.getAxisLeft();
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
        setXAxis(scatter_xAxis);
        setYAxis(scatter_yAxis);
        scatterChart.setData(scatterData);
    }

    public  void ifAdapt(boolean adaptflag){
      this.adaptflag=adaptflag;
    }

    private void drawLineChart(List<Entry> line_entries) {
        LineDataSet lineDataSet = new LineDataSet(line_entries, "折线图");
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineChart.animateX(1000);//设置动画效果
        lineChart_xAxis= lineChart.getXAxis();
        lineChart_yAxis = lineChart.getAxisLeft();
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineDataSet.setColor(Color.BLACK);
        lineDataSet.setDrawCircleHole(false);//设置曲线值的圆点是实心还是空心
        lineDataSet.setDrawCircles(true);
        lineDataSet.setCircleColor(Color.BLACK);  // 设置数据点圆形的颜色
        lineDataSet.setLineWidth(1f); // 设置折线宽度
        lineDataSet.setCircleRadius(1f); // 设置折现点圆点半径
        MyMarkerView mv = new MyMarkerView(view.getContext(),a,b,R方);
//        mv.getABR(a,b,R方);
        lineChart.setMarkerView(mv);
        //设置点上显示的值
/*        lineDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return value+"";
            }
        });*/
        // 隐藏不希望显示部分，包括坐标轴标题、刻度等等，并禁用了双指放大等功能
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.setTouchEnabled(true);
        setXAxis(lineChart_xAxis);
        setYAxis(lineChart_yAxis);
    }


    void setChartXY(int xMin,int xMax,int yMin,int yMAx){
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMAx;
    }

    /**
     * 功能：设置图标的基本属性
     */
    void setLineChartBasicAttr(LineChart lineChart) {
        /***图表设置***/
        lineChart.setDrawGridBackground(false); //是否展示网格线
        lineChart.setDrawBorders(false); //是否显示边界
        lineChart.setDragEnabled(false); //是否可以拖动
        lineChart.setScaleEnabled(false); // 是否可以缩放
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
        scatterChart.setScaleEnabled(false); // 是否可以缩放
        scatterChart.setTouchEnabled(true); //是否有触摸事件
        //设置XY轴动画效果
        //lineChart.animateY(2500);
        scatterChart.animateX(1500);
    }

    private void setXAxis(XAxis xAxis) {
        xAxis.setGridColor(Color.BLACK);
        xAxis.setAxisMinimum(xMin);
        xAxis.setAxisMaximum(xMax);
        xAxis.setLabelCount(xMax-xMin+1, true);//设置X轴的刻度数量
        xAxis.setDrawGridLines(true);
        xAxis.setDrawAxisLine(true);
        xAxis.setEnabled(true);
  /*      xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "";
            }
        });*/

    }    private void setYAxis(YAxis yAxis) {

        yAxis.setGridColor(Color.BLACK);
        yAxis.setAxisMinimum(yMin);
        yAxis.setAxisMaximum(yMax);
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
