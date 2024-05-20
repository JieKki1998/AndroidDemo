package com.example.androiddemo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.example.androiddemo.AnalysisActivity;
import com.example.androiddemo.R;
import com.example.androiddemo.entity.Grid_Item;
import com.example.androiddemo.util.SpUtil;
import com.example.androiddemo.util.SqlLiteUtils;

import java.util.ArrayList;
import java.util.List;

public class MySwipeAdapter extends BaseSwipeAdapter {
    List<String> titles;
    Context context;
    SqlLiteUtils sqlLiteUtils;
    SpUtil spUtil;


    public MySwipeAdapter(Context context, List<String> titles) {
        this.context = context;
        this.titles = titles;
        sqlLiteUtils=new SqlLiteUtils(context);
        spUtil=new SpUtil(context);
    }


    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.sl_content;
    }


    @Override
    public View generateView(final int i, ViewGroup viewGroup) {


        View view = View.inflate(context, R.layout.swipe_item, null);


        return view;
    }


    @Override
    public void fillValues(final int i, View view) {
        TextView tv_item_title = (TextView) view.findViewById(R.id.tv_item_title);
        final CheckBox cb_swipe_check = (CheckBox) view.findViewById(R.id.cb_swipe_check);

        TextView tv_swipe_delect1 = (TextView) view.findViewById(R.id.tv_swipe_delect1);

        TextView tv_swipe_top1 = (TextView) view.findViewById(R.id.tv_swipe_top1);

        final SwipeLayout sl_content = (SwipeLayout) view.findViewById(R.id.sl_content);

        sl_content.setShowMode(SwipeLayout.ShowMode.PullOut);




        tv_swipe_delect1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, ""+ titles.get(i)+"——————下标：" + i, Toast.LENGTH_SHORT).show();
                if (sqlLiteUtils.deleteByTitle(titles.get(i))>=0)
                    Toast.makeText(context,  titles.get(i)+"删除成功！" , Toast.LENGTH_SHORT).show();
                titles.remove(i);
                notifyDataSetChanged();
                sl_content.close();
            }
        });


        cb_swipe_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(context, titles.get(i)+":"+i,Toast.LENGTH_SHORT).show();
//                Toast.makeText(context, sqlLiteUtils.selectByTitle(titles.get(i))+":"+i,Toast.LENGTH_SHORT).show();
                spUtil.setDataList("mData",(ArrayList<Grid_Item>)sqlLiteUtils.selectByTitle(titles.get(i)));
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context);
                Intent intent = new Intent(context, AnalysisActivity.class);
                ActivityCompat.startActivity(context, intent, optionsCompat.toBundle());
            }
        });


        tv_swipe_top1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, titles.get(i)+":"+i,Toast.LENGTH_SHORT).show();
//                items.add(items.get(position));
                titles.add(0, titles.get(i));
                titles.remove(i + 1);
                notifyDataSetChanged();
                sl_content.close();
            }
        });


        tv_item_title.setText(titles.get(i).toString());
//        if (titles.get(i).isTag()) {
//            tv.setTextColor(Color.parseColor("#ff0000"));
//            cb_swipe_check.setText("取消标记");
//        } else {
//            tv.setTextColor(Color.parseColor("#000000"));
//            cb_swipe_check.setText("标记");
//        }
    }


    @Override
    public int getCount() {
        return titles.size();
    }


    @Override
    public Object getItem(int position) {
        return titles.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    class ViewHolder{
        TextView tv;


    }
}