package com.example.androiddemo.util;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androiddemo.entity.Grid_Item;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Utils {
    List listCopy(List list1){
        List list2=new ArrayList<Integer>();
         list2= (List) list1.stream().collect(Collectors.toList());
         return list2;
    }
   public static int getGray(int color){
        int r,g,b,gray;
        r= Color.red(color);
        g=Color.green(color);
        b=Color.blue(color);
        gray=(r*30+g*59+b*11)/100;
        return  gray;
    }
    /**
     * 将选择的点的数据进行分组，转变为list可供绘图使用
     * @param mData
     * @return List<Integer>
     */
    public static ArrayList<Integer> getList(ArrayList<Grid_Item> mData){
        ArrayList<Integer> list=new ArrayList<>();
        //普通的__分组
        Map<Integer,ArrayList<Grid_Item>> groups = new HashMap<>();
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String times=dateFormat.format(date).toString();
        for(Grid_Item kk:mData)
        {
            int id_g = kk.getId_g();
            kk.setTimes(times);
            if (!groups.containsKey(id_g))
            {
                if (id_g!=-1)
                    groups.put(id_g,new ArrayList<>());

            }
            if (id_g!=-1)
                groups.get(id_g).add(kk);
        }
//           list.add(getGray(dstX,dstY));
        Set<Map.Entry<Integer, ArrayList<Grid_Item>>> entries = groups.entrySet();
        Iterator<Map.Entry<Integer, ArrayList<Grid_Item>>> iterator = entries.iterator();
        while (iterator.hasNext())
        {
            Map.Entry<Integer, ArrayList<Grid_Item>> entry = iterator.next();
            ArrayList<Grid_Item> values = (ArrayList<Grid_Item>) entry.getValue();
            int SUM=0;
            if (values.size()!=0){
                for (Grid_Item item :values)
                {

                    SUM =SUM+item.getItemGray();
                }
                int AVG=SUM/values.size();
                list.add(AVG);
            }
            Log.i("groups-list", list.toString());
        }
        Log.i("groups", groups.toString());
        Log.i("groups", list.toString());
        return list;
    }

    public static boolean ifEditEmpty(Context context, EditText editText_initial_concentration, EditText editText_step){
        float initial_concentration;
        float step;
        boolean flag=false;
        if (editText_initial_concentration.getText().toString().length()!=0||editText_step.getText().toString().length()!=0){
            initial_concentration= Float.valueOf(editText_initial_concentration.getText().toString());
            step= Float.valueOf(editText_step.getText().toString());
            SpUtil spUtil=new SpUtil(context);
            spUtil.setFloat("initial",initial_concentration);
            spUtil.setFloat("step",step);
            flag=true;
        }else {
            DialogUtils.showNormalDialog((AppCompatActivity) context,"请输入初始浓度和浓度步长后再试！");
        }
        return flag;
    }
}
