package com.example.androiddemo;

import static java.lang.Math.sqrt;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
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
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AnalysisActivity extends AppCompatActivity {

    List<Integer> list;
    private DrawerLayout drawer_layout;
    private FrameLayout fly_content;

    private RightFragment fg_right_menu;
    private FragmentManager fManager;
    //设置数据

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        fManager = getSupportFragmentManager();
        initViews();
        Intent intent = getIntent();
        list= (List<Integer>) intent.getSerializableExtra("list");
        Toast.makeText(this,list.toString(),Toast.LENGTH_LONG).show();
        Log.i("jiejie",list+"");
        ContentFragment cFragment1 = new ContentFragment();
        cFragment1.setList(list);
        fManager.beginTransaction().replace(R.id.fly_content,cFragment1).commit();
        fg_right_menu.setList(list);



    }

    private void initViews() {
        fg_right_menu = (RightFragment) fManager.findFragmentById(R.id.fg_right_menu);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        fly_content = (FrameLayout) findViewById(R.id.fly_content);
        drawer_layout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View view, float v) {

            }

            @Override
            public void onDrawerOpened(View view) {

            }

            @Override
            public void onDrawerClosed(View view) {
/*                drawer_layout.setDrawerLockMode(
                        DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);*/
            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });
        fg_right_menu.setDrawerLayout(drawer_layout);
    }






}
