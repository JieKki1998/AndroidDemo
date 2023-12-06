package com.example.androiddemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.example.androiddemo.entity.Grid_Item;
import com.example.androiddemo.util.SpUtil;
import com.example.androiddemo.util.SqlLiteUtils;

import java.util.ArrayList;
import java.util.List;

public class AnalysisActivity extends AppCompatActivity {

    List<Integer> list;
    ArrayList<Grid_Item> mData;
    private DrawerLayout drawer_layout;
    private FrameLayout fly_content;

    private RightFragment fg_right_menu;
    private FragmentManager fManager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        SqlLiteUtils sqlLiteUtils =new SqlLiteUtils(this);
//        Toast.makeText(this,  sqlLiteUtils.select("2023-11-18 06:27:09").toString(),Toast.LENGTH_LONG).show();
//        Log.i("jiejie",  sqlLiteUtils.select("2023-11-18 06:27:09").toString() +"");
        fManager = getSupportFragmentManager();
        initViews();
        Intent intent = getIntent();
//        mData= (ArrayList<Grid_Item>)intent.getSerializableExtra("mData");
        SpUtil spUtil = new SpUtil(this);
//        Toast.makeText(this, mData.toString(),Toast.LENGTH_LONG).show();
        ContentFragment contentFragment = new ContentFragment();
        contentFragment.setMcontext(this);
        fManager.beginTransaction().replace(R.id.fly_content,contentFragment).commit();
//        fg_right_menu.setList1(list1);
//        fg_right_menu.setList2(list2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("AnalysisActivity", "onResume: ");
        ContentFragment contentFragment = new ContentFragment();
        contentFragment.setMcontext(this);
        fManager.beginTransaction().replace(R.id.fly_content,contentFragment).commit();
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
