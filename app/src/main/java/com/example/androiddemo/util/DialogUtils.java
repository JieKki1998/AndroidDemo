package com.example.androiddemo.util;

import static android.app.PendingIntent.getActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.androiddemo.ContentFragment;
import com.example.androiddemo.adapter.MyListAdapter;
import com.example.androiddemo.R;
import com.example.androiddemo.entity.Grid_Item;
import com.kyleduo.switchbutton.SwitchButton;

import java.util.ArrayList;

public class DialogUtils {
    /**
     * 显示修改坐标轴对话框
     * @param activity
     * @param contentFragment
     * @param fManager
     */
    @SuppressLint("MissingInflatedId")
    public static void ShowEditAxisDialog(AppCompatActivity activity, ContentFragment contentFragment, FragmentManager fManager) {
        AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(activity);
        final View dialogView = LayoutInflater.from(activity)
                .inflate(R.layout.dialog_axis_layout,null);//dialog_update_password.xml布局文件可以弄得再高大上一些，但是我懒！！！
        customizeDialog.setTitle("修改坐标轴范围");
        customizeDialog.setView(dialogView);
        customizeDialog.setMessage("Message");
        /*初始化自定义dialog布局的控件*/
        EditText et_xMin,et_xMax,et_yMin,et_yMax;
        SwitchButton x_gridLine,y_gridLine;
        SpUtil spUtil=new SpUtil(activity);
        int xMin,xMax,yMin,yMax;
        et_xMin = dialogView.findViewById(R.id.et_xMin);
        et_xMax = dialogView.findViewById(R.id.et_xMax);
        et_yMin = dialogView.findViewById(R.id.et_yMin);
        et_yMax = dialogView.findViewById(R.id.et_yMax);
        xMin = spUtil.getInt("xMin");
        xMax = spUtil.getInt("xMax");
        yMin = spUtil.getInt("yMin");
        yMax = spUtil.getInt("yMax");
        et_xMin.setText(xMin+"");
        et_xMax.setText(xMax+"");
        et_yMin.setText(yMin+"");
        et_yMax.setText(yMax+"");

        boolean show_x_gridLine=false,show_y_gridLine=false;
        show_x_gridLine=spUtil.getBoolean("show_x_gridLine");
        show_y_gridLine=spUtil.getBoolean("show_y_gridLine");
        x_gridLine=dialogView.findViewById(R.id.x_gridLine);
        y_gridLine=dialogView.findViewById(R.id.y_gridLine);
        x_gridLine.setChecked(show_x_gridLine);
        y_gridLine.setChecked(show_y_gridLine);
        x_gridLine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                spUtil.setBoolean("show_x_gridLine",b);
            }
        });
        y_gridLine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                spUtil.setBoolean("show_y_gridLine",b);
            }
        });


        customizeDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String xMin=et_xMin.getText().toString();
                        String xMax=et_xMax.getText().toString();
                        String yMin=et_yMin.getText().toString();
                        String yMax=et_yMax.getText().toString();

                        /*数据验证*/
                        String 提示信息啊="";
                        if(xMin.length()==0)提示信息啊+="xMin不能为空！请重试\n";
                        if (xMax.length()==0)提示信息啊+="xMax不能为空！请重试\n";
                        if (yMin.length()==0)提示信息啊+="yMin不能为空！请重试\n";
                        if (yMax.length()==0)提示信息啊+="yMax不能为空！请重试\n";
                        if (提示信息啊.length()!=0) showNormalDialog(activity,提示信息啊);
                        else {/*数据校验通过*/

                            spUtil.setInt("xMin",Integer.valueOf(xMin));
                            spUtil.setInt("xMax",Integer.valueOf(xMax));
                            spUtil.setInt("yMin",Integer.valueOf(yMin));
                            spUtil.setInt("yMax",Integer.valueOf(yMax));
                            fManager.beginTransaction().replace(R.id.fly_content,contentFragment).commit();
/*————————————————
                            版权声明：本文为CSDN博主「顽石九变」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
                            原文链接：https://blog.csdn.net/wlddhj/article/details/127820069*/
                        }
                    }
                });
        customizeDialog.setNegativeButton("取消",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        customizeDialog.show();
    }
    public static void ShowEditListItemDialog(Context mContext, ArrayList<Grid_Item> itemArrayList, ArrayList<Grid_Item> mData ) {
        AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(mContext);
        final View dialogView = LayoutInflater.from(mContext)
                .inflate(R.layout.update_list_item_layout,null);//dialog_update_password.xml布局文件可以弄得再高大上一些，但是我懒！！！
        customizeDialog.setTitle("修改ListItem");
        customizeDialog.setView(dialogView);
        customizeDialog.setMessage("Message");
        /*初始化自定义dialog布局的控件*/
        TextView tv_update_item_id;
        ArrayList<EditText> et_items=new ArrayList<EditText>();
        Button btn_update_list_item_update;
        SpUtil spUtil=new SpUtil(mContext);
        tv_update_item_id=dialogView.findViewById(R.id.tv_update_item_id);
        et_items.add(dialogView.findViewById(R.id.et_update_item01));
        et_items.add(dialogView.findViewById(R.id.et_update_item02));
        et_items.add(dialogView.findViewById(R.id.et_update_item03));
        btn_update_list_item_update=dialogView.findViewById(R.id.btn_update_list_item_update);
        for (int i = 0; i < itemArrayList.size(); i++) {
            Log.i("ShowEditListItemDialog", "i::"+i+"itemArrayList.size(): "+itemArrayList.size());
            Grid_Item item=itemArrayList.get(i);
            et_items.get(i).setText(String.valueOf(item.getItemGray()+""));
        }
        Log.i("ShowEditListItemDialog", "before  mData:::"+mData.toString());
        customizeDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < itemArrayList.size(); i++) {
                            Grid_Item item=itemArrayList.get(i);
                            item.setItemGray(Integer.valueOf( et_items.get(i).getText().toString()));
                            spUtil.setDataList("mData",mData);
                            Log.i("ShowEditListItemDialog", "after  mData:::"+spUtil.getDataList("mData"));
                        }
                    }
                });
        customizeDialog.setNegativeButton("取消",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        customizeDialog.show();
    }
    @SuppressLint("MissingInflatedId")
    public static void ShowEditAxisTitleDialog(AppCompatActivity activity, ContentFragment contentFragment, FragmentManager fManager) {
        AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(activity);
        final View dialogView = LayoutInflater.from(activity)
                .inflate(R.layout.dialog_axistitle_layout,null);//dialog_update_password.xml布局文件可以弄得再高大上一些，但是我懒！！！
        customizeDialog.setTitle("修改标题");
        customizeDialog.setView(dialogView);
        customizeDialog.setMessage("Message");
        /*初始化自定义dialog布局的控件*/
        EditText et_xTitle,et_yTitle;
        et_xTitle = dialogView.findViewById(R.id.et_xTitle);
        et_yTitle = dialogView.findViewById(R.id.et_yTitle);
        SpUtil spUtil=new SpUtil(activity);
        String xTitle = spUtil.getString("xTitle","");
        String yTitle = spUtil.getString("yTitle","");
        if (xTitle.length()!=0&&yTitle.length()!=0)
        {
            et_xTitle.setText(xTitle+"");
            et_yTitle.setText(yTitle+"");
        }
        customizeDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String xTitle=et_xTitle.getText().toString();
                        String yTitle=et_yTitle.getText().toString();
                        /*数据验证*/
                        String 提示信息啊="";
                        if(xTitle.length()==0)提示信息啊+="X轴标题不能为空！请重试\n";
                        if (yTitle.length()==0)提示信息啊+="Y轴标题不能为空！请重试\n";
                        if (提示信息啊.length()!=0) showNormalDialog(activity,提示信息啊);
                        else {/*数据校验通过*/
                            contentFragment.setXtitle(xTitle);contentFragment.setYtitle(yTitle);
                            fManager.beginTransaction().replace(R.id.fly_content,contentFragment).commit();
                            // 保存标题
                            SpUtil spUtil=new SpUtil(activity);
                            spUtil.setString("xTitle",xTitle);
                            spUtil.setString("yTitle",yTitle);
/*————————————————
                            版权声明：本文为CSDN博主「顽石九变」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
                            原文链接：https://blog.csdn.net/wlddhj/article/details/127820069*/
                        }
                    }
                });
        customizeDialog.setNegativeButton("取消",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        customizeDialog.show();
    }

    public static void ShowEditListDialog(AppCompatActivity activity , FragmentManager fManager,int model) {
        AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(activity);
        final View dialogView = LayoutInflater.from(activity)
                .inflate(R.layout.dialog_list_layout,null);
        customizeDialog.setTitle("修改list");
        customizeDialog.setView(dialogView);
        customizeDialog.setMessage("Model:"+model);
        /*初始化自定义dialog布局的控件*/
        ListView listView=dialogView.findViewById(R.id.list_view);
        MyListAdapter mAdapter=null;
        SpUtil spUtil=new SpUtil(activity);
        ArrayList<Grid_Item> mData=spUtil.getDataList("mData");
        ArrayList<Integer> list=Utils.getList(mData);
        if (model==1){
//            mAdapter= new MyListAdapter(list,dialogView.getContext());
        }
        if (model==2){
//            mAdapter = new MyListAdapter(list,dialogView.getContext());
        }
        listView.setAdapter(mAdapter);

        customizeDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ContentFragment cFragment=null;
                        if (model==1){
                            cFragment = new ContentFragment();
                            cFragment.setList(list);
                        }
                        if (model==2)
                        {
                            cFragment= new ContentFragment();
                        }
                        cFragment.setModel(model);
                        fManager.beginTransaction().replace(R.id.fly_content,cFragment).commit();

                        dialog.dismiss();
                    }
                });
        customizeDialog.setNegativeButton("取消",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
//        customizeDialog.show();
        Window window = customizeDialog.show().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
//        WindowManager m = activity.getWindowManager();
//        Display d = m.getDefaultDisplay();                      // 获取屏幕宽、高用
//        WindowManager.LayoutParams p = window.getAttributes();  // 获取对话框当前的参数值
//        p.height = (int) (d.getHeight() * 0.2);                 // 改变的是dialog框在屏幕中的位置而不是大小
//        p.width = (int) (d.getWidth() * 0.65);                  // 宽度设置为屏幕的0.65
//        window.setAttributes(p);
    }

    public static void showNormalDialog(AppCompatActivity activity, String message){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(activity);
        normalDialog.setTitle("提示对话框");
        normalDialog.setMessage(message);
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
//                        Toast.makeText(activity,"点击了！确定",Toast.LENGTH_SHORT).show();
                    }
                });
        /*normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        Toast.makeText(activity,"点击了！关闭",Toast.LENGTH_SHORT).show();
                    }
                });*/
        // 显示
        normalDialog.show();
    }
    public static void showSaveDialog(AppCompatActivity activity){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(activity);
        final View dialogView = LayoutInflater.from(activity)
                .inflate(R.layout.dialog_save_layout,null);

        /*初始化自定义dialog布局的控件*/
        EditText et_savetitle;
        et_savetitle = dialogView.findViewById(R.id.et_save_title);
        SpUtil spUtil=new SpUtil(activity);
        SqlLiteUtils sqlLiteUtils=new SqlLiteUtils(activity);
        customizeDialog.setTitle("保存对话框");
//        customizeDialog.setMessage(message);
        customizeDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
//                        Toast.makeText(activity,"点击了！确定",Toast.LENGTH_SHORT).show();
                        String title = et_savetitle.getText().toString();
                        ArrayList<Grid_Item> mData = spUtil.getDataList("mData");
                        mData.forEach(e->{
                            e.setTitle(title);
                            sqlLiteUtils.insert(e);
                        });
                    }
                });
        /*normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        Toast.makeText(activity,"点击了！关闭",Toast.LENGTH_SHORT).show();
                    }
                });*/
        // 显示
        customizeDialog.show();

    }
    @SuppressLint("MissingInflatedId")
    public static void showSaveDialog(AppCompatActivity activity,String message) {
        AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(activity);
        final View dialogView = LayoutInflater.from(activity)
                .inflate(R.layout.dialog_save_layout,null);//dialog_update_password.xml布局文件可以弄得再高大上一些，但是我懒！！！
        customizeDialog.setTitle(message);
        customizeDialog.setView(dialogView);
//        customizeDialog.setMessage(message);
        /*初始化自定义dialog布局的控件*/
        EditText et_savetitle;
        SwitchButton x_gridLine,y_gridLine;
        SpUtil spUtil=new SpUtil(activity);
        SqlLiteUtils sqlLiteUtils=new SqlLiteUtils(activity);
        et_savetitle = dialogView.findViewById(R.id.et_save_title);


        customizeDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title = et_savetitle.getText().toString();
                        ArrayList<Grid_Item> mData = spUtil.getDataList("mData");
                        mData.forEach(e-> {
                            e.setTitle(title);
                            sqlLiteUtils.insert(e);
                        });
                        FragmentManager fManager = activity.getSupportFragmentManager();
                        fManager.beginTransaction().replace(R.id.fly_content,new ContentFragment()).commit();
                    }
                });
        customizeDialog.setNegativeButton("取消",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        customizeDialog.show();
    }

}
