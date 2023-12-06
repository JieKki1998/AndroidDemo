package com.example.androiddemo;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androiddemo.entity.Grid_Item;
import com.example.androiddemo.util.SpUtil;

import java.util.ArrayList;

public class UpdateListItemActivity extends AppCompatActivity {
    SpUtil spUtil;
    ArrayList<Grid_Item> mData;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        Transition slide_top = TransitionInflater.from(this).inflateTransition(android.R.transition.slide_top);
        getWindow().setEnterTransition(slide_top);


        setContentView(R.layout.update_list_item_layout);
        Intent intent=getIntent();
        ArrayList<Grid_Item> itemArrayList= (ArrayList<Grid_Item>) intent.getSerializableExtra("itemArrayList");
        mData=(ArrayList<Grid_Item>) intent.getSerializableExtra("mData");
        String group=intent.getStringExtra("group");
        Integer[] indexs= (Integer[]) intent.getSerializableExtra("indexs");
        TextView tv_update_item_id;
        ArrayList<EditText> et_items=new ArrayList<EditText>();
        Button btn_update_list_item_update;
        spUtil=new SpUtil(this);
        tv_update_item_id=findViewById(R.id.tv_update_item_id);
        tv_update_item_id.setText("第"+group+"组");
        et_items.add(findViewById(R.id.et_update_item01));
        et_items.add(findViewById(R.id.et_update_item02));
        et_items.add(findViewById(R.id.et_update_item03));
        btn_update_list_item_update=findViewById(R.id.btn_update_list_item_update);
        for (int i = 0; i < indexs.length; i++) {
            Grid_Item item=mData.get(indexs[i]);
            et_items.get(i).setText(String.valueOf(item.getItemGray()+""));
        }
        btn_update_list_item_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.i("ShowEditListItemDialog", "before  mData:::"+mData);
                for (int i = 0; i < indexs.length; i++) {
                    Grid_Item item=mData.get(indexs[i]);
                    item.setItemGray(Integer.valueOf( et_items.get(i).getText().toString()));
                    mData.set(indexs[i],item);
                }
                spUtil.setDataList("mData",mData);
//                Log.i("ShowEditListItemDialog", "after  mData:::"+spUtil.getDataList("mData"));
                finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        Log.i("UpdateListItemActivity", "finish: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("UpdateListItemActivity", "onDestroy: ");
    }
}
