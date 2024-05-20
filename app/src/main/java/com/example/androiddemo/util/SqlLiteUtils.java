package com.example.androiddemo.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.androiddemo.entity.Grid_Item;

import java.util.ArrayList;
import java.util.List;

public class SqlLiteUtils {
    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    ContentValues values;
    Cursor cursor;
    public SqlLiteUtils(Context context){

        dbHelper=new DBHelper(context);
    }

    public void insert(Grid_Item gridItem ){
        sqLiteDatabase=dbHelper.getWritableDatabase();
        values=new ContentValues();
        values.put("id_g",gridItem.getId_g());
        values.put("color",gridItem.getColor());
        values.put("itemGray",gridItem.getItemGray());
        values.put("iName",gridItem.getiName());
        values.put("times",gridItem.getTimes());
        values.put("title",gridItem.getTitle());
        long id=sqLiteDatabase.insert("linetable",null,values);
        sqLiteDatabase.close();
    }
    public int update(String times,Grid_Item gridItem){
        sqLiteDatabase=dbHelper.getWritableDatabase();
        values=new ContentValues();
        values.put("id_g",gridItem.getId_g());
        values.put("color",gridItem.getColor());
        values.put("itemGray",gridItem.getItemGray());
        values.put("iName",gridItem.getiName());
        int number=sqLiteDatabase.update("linetable",values,"times=?",new String[]{times+""});
        sqLiteDatabase.close();
        return number;
    }
    public int deleteByTimes(String times){
        sqLiteDatabase=dbHelper.getWritableDatabase();
        int number=sqLiteDatabase.delete("linetable","times=?",new String[]{times+""});
        return number;
    }
    public int deleteByTitle(String title){
        sqLiteDatabase=dbHelper.getWritableDatabase();
        int number=sqLiteDatabase.delete("linetable","title=?",new String[]{title+""});
        return number;
    }
    public List<Grid_Item> select(String times){
        List<Grid_Item> itemList=null;
        sqLiteDatabase=dbHelper.getReadableDatabase();
//        cursor=sqLiteDatabase.rawQuery("select id_g,color,itemGray,iName from linetable where _id=?",new String[]{String.valueOf(1)});
        cursor=sqLiteDatabase.rawQuery("select id_g,color,itemGray,iName,times,title from linetable  where times=?",new String[]{times});

        if (cursor.getCount()==0){
            itemList=new ArrayList<Grid_Item>();
            Grid_Item item = new Grid_Item(-1,"-1",-1,"-1");
            itemList.add(item);
        }
        if (cursor.getCount()!=0)
        {
            itemList=new ArrayList<Grid_Item>();
            while (cursor.moveToNext()){
                Grid_Item item=new Grid_Item();
                item.setId_g(cursor.getInt(0));
                item.setColor(cursor.getInt(1));
                item.setItemGray(cursor.getInt(2));
                item.setiName(cursor.getString(3));
                item.setTimes(cursor.getString(4));
                item.setTitle(cursor.getString(5));
                itemList.add(item);
            }

        }
        cursor.close();
        sqLiteDatabase.close();
        return itemList;
    }
    public List<Grid_Item> selectByTitle(String title){
        List<Grid_Item> itemList=null;
        sqLiteDatabase=dbHelper.getReadableDatabase();
//        cursor=sqLiteDatabase.rawQuery("select id_g,color,itemGray,iName from linetable where _id=?",new String[]{String.valueOf(1)});
        cursor=sqLiteDatabase.rawQuery("select id_g,color,itemGray,iName,times,title from linetable  where title=?",new String[]{title});

        if (cursor.getCount()==0){
            itemList=new ArrayList<Grid_Item>();
            Grid_Item item = new Grid_Item(-1,"-1",-1,"-1");
            itemList.add(item);
        }
        if (cursor.getCount()!=0)
        {
            itemList=new ArrayList<Grid_Item>();
            while (cursor.moveToNext()){
                Grid_Item item=new Grid_Item();
                item.setId_g(cursor.getInt(0));
                item.setColor(cursor.getInt(1));
                item.setItemGray(cursor.getInt(2));
                item.setiName(cursor.getString(3));
                item.setTimes(cursor.getString(4));
                item.setTitle(cursor.getString(5));
                itemList.add(item);
            }

        }
        cursor.close();
        sqLiteDatabase.close();
        return itemList;
    }
    public List<String> selectAllTitles(){
        List<String> titles=null;
        sqLiteDatabase=dbHelper.getReadableDatabase();
//        cursor=sqLiteDatabase.rawQuery("select id_g,color,itemGray,iName from linetable where _id=?",new String[]{String.valueOf(1)});
        cursor=sqLiteDatabase.rawQuery("select distinct title from linetable  ",null);

        if (cursor.getCount()==0){
            titles=new ArrayList<String>();
            titles.add("0");
        }
        if (cursor.getCount()!=0)
        {
            titles=new ArrayList<String>();
            while (cursor.moveToNext()){
                titles.add(cursor.getString(0));
            }
        }
        cursor.close();
        sqLiteDatabase.close();
        return titles;
    }
    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context,"linedatabase.db",null,1);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("CREATE TABLE linetable (_id INTEGER PRIMARY KEY AUTOINCREMENT ,id_g INTEGER,color INTEGER,itemGray INTEGER,iName VARCHAR(20),times VARCHAR(20),title VARCHAR(20))");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }
}
