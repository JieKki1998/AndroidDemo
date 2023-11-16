package com.example.androiddemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AnalysisActivity extends AppCompatActivity {

    List<Integer> list1;
    private DrawerLayout drawer_layout;
    private FrameLayout fly_content;

    private RightFragment fg_right_menu;
    private FragmentManager fManager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        DBUtils dbUtils=new DBUtils(this);
        Toast.makeText(this,  dbUtils.select().toString(),Toast.LENGTH_LONG).show();
        fManager = getSupportFragmentManager();
        initViews();
        Intent intent = getIntent();
        list1 = (List<Integer>) intent.getSerializableExtra("list");
        Toast.makeText(this, list1.toString(),Toast.LENGTH_LONG).show();
//        Log.i("jiejie", list1 +"");
        List<Integer> list2=new ArrayList<Integer>();
        list2= list1.stream().collect(Collectors.toList());
        ContentFragment cFragment1 = new ContentFragment(list1);
        fManager.beginTransaction().replace(R.id.fly_content,cFragment1).commit();
        fg_right_menu.setList1(list1);
        fg_right_menu.setList2(list2);



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
