package com.example.androiddemo.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.androiddemo.entity.Grid_Item;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class SpUtil {
    Context mcontext;
    SharedPreferences sharedPreferences;
    public SpUtil() {
    }
    public SpUtil(Context context) {
        this.mcontext=context;
         sharedPreferences=context.getSharedPreferences("test",Context.MODE_PRIVATE);
    }

    public  void setFloat(String key, Float value){
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putFloat(key,value);
        editor.commit();
    }
    public  void setInt(String key, Integer value){
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putInt(key,value);
        editor.commit();
    }
    public  void setBoolean(String key, Boolean value){
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.commit();
    }
    public  void setString(String key, String value){
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }
    public  String getString(String key,String defalut){
       String s=sharedPreferences.getString(key,defalut);
       return s;
    }
    public  Float getFloat(String key){
        Float value=sharedPreferences.getFloat(key,0);
        return value;
    }
    public  Boolean getBoolean(String key){
        Boolean value=sharedPreferences.getBoolean(key,false);
        return value;
    }
    public  int getInt(String key){
        int value=sharedPreferences.getInt(key,0);
        return value;
    }
    /**
     * 保存List
     *
     * @param tag
     * @param datalist
     */
    public  void setDataList(String tag, ArrayList<Grid_Item> datalist) {
        if (null == datalist || datalist.size() <= 0)
            return;
        SharedPreferences.Editor editor= sharedPreferences.edit();
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        editor.putString(tag, strJson);
        editor.commit();
    }

    /**
     * 获取List
     *
     * @param tag
     * @return
     */
    public  ArrayList<Grid_Item> getDataList(String tag) {
        ArrayList<Grid_Item> datalist = new ArrayList<Grid_Item>();
        String strJson = getString(tag,"");
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<List<Grid_Item>>() {
        }.getType());
        return datalist;
    }
}
