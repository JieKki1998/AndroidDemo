package com.example.androiddemo;

import android.app.Activity;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Window;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.swipe.util.Attributes;
import com.example.androiddemo.adapter.MySwipeAdapter;
import com.example.androiddemo.util.SqlLiteUtils;

public class HistoryActivity extends AppCompatActivity {
    ListView lv_myswipe;
    SqlLiteUtils sqlLiteUtils;
    Activity activity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        Transition slide_top = TransitionInflater.from(this).inflateTransition(android.R.transition.slide_top);
        getWindow().setEnterTransition(slide_top);
        setContentView(R.layout.layout_history);


        activity=this;
        sqlLiteUtils=new SqlLiteUtils(this);
        lv_myswipe=findViewById(R.id.lv_myswipe);
        MySwipeAdapter adapter=new MySwipeAdapter(this,sqlLiteUtils.selectAllTitles());
        lv_myswipe.setAdapter(adapter);
        adapter.setMode(Attributes.Mode.Single);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sqlLiteUtils=new SqlLiteUtils(this);
        lv_myswipe=findViewById(R.id.lv_myswipe);
        MySwipeAdapter adapter=new MySwipeAdapter(this,sqlLiteUtils.selectAllTitles());
        lv_myswipe.setAdapter(adapter);
        adapter.setMode(Attributes.Mode.Single);
    }
}
