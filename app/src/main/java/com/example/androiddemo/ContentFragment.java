package com.example.androiddemo;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.daimajia.swipe.util.Attributes;
import com.example.androiddemo.adapter.MySwipeAdapter;
import com.example.androiddemo.entity.Grid_Item;
import com.example.androiddemo.util.SpUtil;
import com.example.androiddemo.util.SqlLiteUtils;
import com.example.androiddemo.util.Utils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.ScatterChart;
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

public class ContentFragment extends Fragment {
    float a=0,b=0;
    boolean adaptflag=false;
    float RR =0;
    int model=0;
    Button btn_adapt;

    TextView tv_expfunction,tv_xTitle,tv_yTitle,tv_history;
    LineChart lineChart;

    ScatterChart scatterChart;
    private String strContent;
    private int bgColor;
    private FragmentManager fManager;
    ArrayList<Grid_Item> mData;
    ArrayList<Integer> list;
    List<Entry> line_entries = new ArrayList<>();//回归的
    List<Entry> scatter_entries = new ArrayList<>();//实际的
    XAxis lineChart_xAxis,scatter_xAxis;
    YAxis  lineChart_yAxis,scatter_yAxis;

    boolean show_x_gridLine,show_y_gridLine;
    int xMin=0, xMax=10, yMin=0, yMax =200;
    Float initialConcentration,step ;//初始浓度与浓度步长
    String xtitle="浓度",ytitle="灰度";
    View view;
    SpUtil spUtil;
    ListView lv_myswipe;
    SqlLiteUtils sqlLiteUtils;
    Context mcontext;

    public Context getMcontext() {
        return mcontext;
    }

    public void setMcontext(Context mcontext) {
        this.mcontext = mcontext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fg_content, container, false);
        view.setBackgroundColor(bgColor);
        spUtil=new SpUtil(view.getContext());

        initViews();
        getLocalData();
        getFittingLine(list);
        setLine_entries(list);
        setScatter_entries(list);
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
        tv_history=view.findViewById(R.id.tv_history);


    }

    public void setList(ArrayList<Integer> list) {
        this.list = list;
    }


    /**
     * 获取拟合直线
     * @param list
     */
    private void getFittingLine(ArrayList<Integer>  list) {
        Log.i("ContentFragment", "getFittingLine: "+list);
        float SUM_XiYi=0,SUM_Xi=0,SUM_Yi=0,SUM_XiXi=0,AVG_X=0,AVG_Y=0,SSReg=0,SST=0;
        int n=list.size();
        for (int i = 0; i < n; i++) {
            float xi=initialConcentration+i*step;
            Log.i("ContentFragment", "initialConcentration: "+initialConcentration+","+"step"+step);
            float yi=list.get(i);
            SUM_XiYi += xi*yi;
            SUM_Xi +=xi;
            SUM_Yi +=yi;
            SUM_XiXi +=xi*xi;
        }
        AVG_X = SUM_Xi/n;
        AVG_Y = SUM_Yi/n;
        b=(SUM_XiYi-n*AVG_X*AVG_Y)/(SUM_XiXi-n*AVG_X*AVG_X);
        a = AVG_Y-b*AVG_X;
        for (int i = 0; i < list.size(); i++) {
            float xi=initialConcentration+i*step;
            float yi=list.get(i);
            float y尖=b*xi+a;
            SSReg+=((y尖-AVG_Y)*(y尖-AVG_Y));
            SST +=((yi-AVG_Y)*(yi-AVG_Y));
        }
        Log.i("ContentFragment", "getFittingLine: "+"SUM_XiYi:"+SUM_XiYi+","+"SUM_XiYi:"+SUM_Xi+","+"SUM_Xi:"+SUM_Yi+","+"SUM_XiXi:"+SUM_XiXi+","+"AVG_X:"+AVG_X+","+"AVG_Y:"+AVG_Y+","+"SSReg:"+SSReg+","+"SST:"+SST+"");
        Log.i("ContentFragment", "getFittingLine: "+"b:"+b+","+"a:"+a+","+"SUM_Xi:"+SUM_Yi+","+"SUM_XiXi:"+SUM_XiXi+","+"AVG_X:"+AVG_X+","+"AVG_Y:"+AVG_Y+","+"SSReg:"+SSReg+","+"SST:"+SST+"");

        RR =1-SSReg/SST;
        tv_expfunction.setText("y="+b+"*X+"+a+"\nR^2="+ RR);
    }
    float getEValue(float xi){
        return b*xi+a;
    }
    void setLine_entries(List<Integer> list){
        for (int i = 0; i < list.size(); i++)
            line_entries.add(new Entry(initialConcentration+i*step,getEValue(i)));
        Log.i("ContentFragment", "setLine_entries: "+line_entries);
    }

    void setScatter_entries(List<Integer> list){
        for (int i = 0; i < list.size(); i++)
            scatter_entries.add(new Entry(initialConcentration+i*step,list.get(i)));
    }

    /**
     * 加载本地存储的数据
     */
    void getLocalData(){
        /**
         * 加载坐标轴
         */
        if (spUtil.getInt("xMax")!=0){
            xMin = spUtil.getInt("xMin");
            xMax = spUtil.getInt("xMax");
        }
        if (spUtil.getInt("yMax")!=0){
            yMin = spUtil.getInt("yMin");
            yMax = spUtil.getInt("yMax");
        }
        /**
         * 加载初始浓度和步长
         */
        initialConcentration=spUtil.getFloat("initial_concentration");
        step= spUtil.getFloat("step");
        /**
         * 加载点集数据
         */
        mData=spUtil.getDataList("mData");
//        Log.i("ContentFragment", "getLocalData:-- mData--"+mData);
        list=Utils.getList(mData);

        /**
         * 加载网格线
         */
        show_x_gridLine=spUtil.getBoolean("show_x_gridLine");
        show_y_gridLine= spUtil.getBoolean("show_y_gridLine");
        /**
         * 加载标题名
         */
        String xTitle =spUtil.getString("xTitle","浓度");
        String yTitle=spUtil.getString("yTitle","灰度");
        if (xTitle.length()!=0&&yTitle.length()!=0)
        {
            setXtitle(xTitle);
            setYtitle(yTitle);
            tv_xTitle.setText(xtitle);
            tv_yTitle.setText(ytitle);
        }


    }
    private void drawChart() {
        sqlLiteUtils=new SqlLiteUtils(getContext());
        lv_myswipe=view.findViewById(R.id.lv_myswipe);
        MySwipeAdapter adapter=new MySwipeAdapter(getContext(),sqlLiteUtils.selectAllTitles());
        lv_myswipe.setAdapter(adapter);
        adapter.setMode(Attributes.Mode.Single);
        //http://t.csdn.cn/ETnml
        drawScatterChart(scatter_entries);
        drawLineChart(line_entries);
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
        MyMarkerView mv = new MyMarkerView(view.getContext(),a,b, RR);
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
        setXAxis(lineChart_xAxis);
        setYAxis(lineChart_yAxis);
    }


    public void setChartXY(int xMin,int xMax,int yMin,int yMAx){
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
        //是否绘制网格线
        xAxis.setDrawGridLines(show_x_gridLine);
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

        //是否绘制网格线
        yAxis.setDrawGridLines(show_y_gridLine);
 /*       yAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "";
            }
        });*/
    }

    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }
}
