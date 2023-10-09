package com.example.androiddemo;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DecimalFormat;

public class MyMarkerView extends MarkerView {
    int a=0;
    int b=0;
    double R方=0;
    private TextView tvContent01,tvContent02;
    private DecimalFormat format = new DecimalFormat("##0");

    public MyMarkerView(Context context,int a,int b,double R方) {
        super(context, R.layout.layout_markerview);
        this.a= a;
        this.b= b;
        this.R方=R方;
        tvContent01 = (TextView) findViewById(R.id.tvContent01);
        tvContent02 = (TextView) findViewById(R.id.tvContent02);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
//        tvContent.setText(format.format(e.getY()));

        tvContent01.setText("y="+b+"*X-"+a);
        tvContent02.setText("R^2="+R方+"("+e.getX()+","+e.getY()+")");
        super.refreshContent(e, highlight);
    }

    public void getABR(int a, int b, double r方) {
        this.a= a;
        this.b= b;
        this.R方=R方;

    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight() - 10);
    }
}