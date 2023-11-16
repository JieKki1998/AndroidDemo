package com.example.androiddemo;

import static com.google.android.material.internal.ViewUtils.showKeyboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;

import java.util.List;

public class MyListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Integer> mData;

    public MyListAdapter() {}

    public MyListAdapter(List<Integer> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
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
            holder.et_item = (EditText) convertView.findViewById(R.id.et_item);
            holder.et_item.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    Log.i("TextWatcher",editable.toString());
                    String number = editable.toString();
                    if (number.length()!=0)
                        mData.set(position,Integer.valueOf(number));

                }
            });
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.et_item.setText(mData.get(position).toString());
        return convertView;
    }

    private class ViewHolder{
        EditText et_item;
    }

}
