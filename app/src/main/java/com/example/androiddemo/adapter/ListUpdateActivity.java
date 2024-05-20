package com.example.androiddemo.adapter;

import android.content.Context;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androiddemo.R;
import com.example.androiddemo.util.SpUtil;

public class ListUpdateActivity extends AppCompatActivity {
    ListView listView;
    SpUtil spUtil;
    Context mcontext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        Transition slide_top = TransitionInflater.from(this).inflateTransition(android.R.transition.slide_top);
        getWindow().setEnterTransition(slide_top);
        setContentView(R.layout.dialog_list_layout);
        mcontext=ListUpdateActivity.this;
        spUtil=new SpUtil(this);
        listView=findViewById(R.id.list_view);
        MyListAdapter myListAdapter=new MyListAdapter(mcontext);
        listView.setAdapter(myListAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("ListUpdateActivity", "onResume: ");
        MyListAdapter myListAdapter=new MyListAdapter(mcontext);
        listView.setAdapter(myListAdapter);
    }
}
