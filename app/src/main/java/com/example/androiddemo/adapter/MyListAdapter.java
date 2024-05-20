package com.example.androiddemo.adapter;

import static com.example.androiddemo.util.DialogUtils.ShowEditListItemDialog;
import static com.google.android.material.internal.ViewUtils.showKeyboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import com.example.androiddemo.R;
import com.example.androiddemo.UpdateListItemActivity;
import com.example.androiddemo.entity.Grid_Item;
import com.example.androiddemo.util.SpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Integer> list;

    ArrayList<Grid_Item> mData;
    Map<Integer,ArrayList<Grid_Item>> groups = new HashMap<>();
    SpUtil spUtil;

    public MyListAdapter( Context mContext) {
        this.list = list;
        this.mContext = mContext;
        spUtil=new SpUtil(mContext);
        setMData(null);
        for(Grid_Item kk:mData)
        {
            int id_g = kk.getId_g();
            if (!groups.containsKey(id_g))
            {
                if (id_g!=-1)
                    groups.put(id_g,new ArrayList<>());

            }
            if (id_g!=-1)
                groups.get(id_g).add(kk);
        }
    }
    public void setMData(ArrayList<Grid_Item> mData){
        if (mData==null)
        this.mData =spUtil.getDataList("mData");
        else this.mData=mData;
    }

    @Override
    public int getCount() {
        return groups.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list,parent,false);
            holder = new ViewHolder();
            holder.et_items.add((EditText) convertView.findViewById(R.id.et_item01)) ;
            holder.et_items.add((EditText) convertView.findViewById(R.id.et_item02)) ;
            holder.et_items.add((EditText) convertView.findViewById(R.id.et_item03)) ;
            holder.tv_item_id=(TextView) convertView.findViewById(R.id.tv_item_id);
            holder.btn_list_item_update=(Button) convertView.findViewById(R.id.btn_list_item_update);

            holder.btn_list_item_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayList<Grid_Item> itemArrayList=groups.get(position+1);
                    Log.i("MyListAdapter","before mData:"+mData.toString());
//                    ShowEditListItemDialog((AppCompatActivity) mContext,itemArrayList,mData);
                    Log.i("MyListAdapter","after  mData:"+mData.toString());
                    Log.i("MyListAdapter"," index:"+mData.indexOf(itemArrayList.get(0)));
                    Integer[] indexs=new Integer[itemArrayList.size()];
                    for (int i = 0; i < itemArrayList.size(); i++) {
                        indexs[i]=mData.indexOf(itemArrayList.get(i));
                    }
                    //设置动画 参考博客https://blog.csdn.net/ss1168805219/article/details/53445063
                    ActivityOptionsCompat options11 = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext);
                    Intent intent=new Intent(mContext, UpdateListItemActivity.class);
                    intent.putExtra("group",(position+1)+"");
                    intent.putExtra("itemArrayList",itemArrayList);
                    intent.putExtra("indexs",indexs);
                    intent.putExtra("mData",mData);
                    ActivityCompat.startActivity(mContext, intent, options11.toBundle());
                }
            });

            ArrayList<Grid_Item> itemArrayList=groups.get(position+1);
//            Log.i("MyListAdapter","position"+position+"groups"+groups.toString());
//            Log.i("MyListAdapter",":::itemArrayList"+itemArrayList.toString());
            for (int i=0;i<itemArrayList.size();i++)
            {
                holder.et_items.get(i).setText(itemArrayList.get(i).getItemGray()+"");
            }
            holder.tv_item_id.setText("第"+(position+1)+"组：");
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    private class ViewHolder{
        ArrayList<EditText> et_items=new ArrayList<EditText>();
        TextView tv_item_id;
        Button btn_list_item_update;

    }

}
