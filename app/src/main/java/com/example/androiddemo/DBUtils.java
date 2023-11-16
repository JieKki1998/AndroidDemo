package com.example.androiddemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DBUtils {
    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    ContentValues values;
    Cursor cursor;
    DBUtils(Context context){
        dbHelper=new DBHelper(context);
    }

    public void insert(int id_g,int color,int itemGray,String iName ){
        sqLiteDatabase=dbHelper.getWritableDatabase();
        values=new ContentValues();
        values.put("id_g",id_g);
        values.put("color",color);
        values.put("itemGray",itemGray);
        values.put("iName",iName);
        long id=sqLiteDatabase.insert("linetable",null,values);
        sqLiteDatabase.close();
    }
    public int update(int _id,int id_g,int color,int itemGray,String iName){
        sqLiteDatabase=dbHelper.getWritableDatabase();
        values=new ContentValues();
        values.put("id_g",id_g);
        values.put("color",color);
        values.put("itemGray",itemGray);
        values.put("iName",iName);
        int number=sqLiteDatabase.update("linetable",values,"_id=?",new String[]{_id+""});
        sqLiteDatabase.close();
        return number;
    }
    public int delete(int _id){
        sqLiteDatabase=dbHelper.getWritableDatabase();
        int number=sqLiteDatabase.delete("linetable","_id=?",new String[]{_id+""});
        return number;
    }
    public List<Grid_Item> select(){
        List<Grid_Item> itemList=new ArrayList<>();
        Grid_Item item1 = new Grid_Item();
        item1.setColor(-1111);

        sqLiteDatabase=dbHelper.getReadableDatabase();
//        cursor=sqLiteDatabase.rawQuery("select id_g,color,itemGray,iName from linetable where _id=?",new String[]{String.valueOf(1)});
        cursor=sqLiteDatabase.rawQuery("select id_g,color,itemGray,iName from linetable ",null);
        item1.setItemGray(cursor.getCount());
        itemList.add(item1);
        if (cursor.getCount()==0){
            itemList=new ArrayList<Grid_Item>();
            Grid_Item item = new Grid_Item();
            item.setColor(-222);
            itemList.add(item);
        }
        if (cursor.getCount()!=0)
        {
            Grid_Item item2 = new Grid_Item();
            item2.setColor(-333);
            item2.setItemGray(cursor.getCount());
            itemList.add(item2);
            while (cursor.moveToNext()){
                itemList=new ArrayList<Grid_Item>();
                Grid_Item item=new Grid_Item();
                item.setId_g(cursor.getInt(0));
                item.setColor(cursor.getInt(1));
                item.setItemGray(cursor.getInt(2));
                item.setiName(cursor.getString(3));
                itemList.add(item);
            }

        }
        cursor.close();
        sqLiteDatabase.close();
        return itemList;
    }
    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context,"linedatabase.db",null,1);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("CREATE TABLE linetable (_id INTEGER PRIMARY KEY AUTOINCREMENT ,id_g INTEGER,color INTEGER,itemGray INTEGER,iName VARCHAR(20))");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }
}
