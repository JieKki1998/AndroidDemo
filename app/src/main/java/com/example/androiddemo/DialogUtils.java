package com.example.androiddemo;

import static android.app.PendingIntent.getActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.kyleduo.switchbutton.SwitchButton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        et_xMin = dialogView.findViewById(R.id.et_xMin);
        et_xMax = dialogView.findViewById(R.id.et_xMax);
        et_yMin = dialogView.findViewById(R.id.et_yMin);
        et_yMax = dialogView.findViewById(R.id.et_yMax);
        SharedPreferences sp = activity.getSharedPreferences("axis_sp_info", Context.MODE_PRIVATE);
        boolean show_x_gridLine=false,show_y_gridLine=false;
        show_x_gridLine=sp.getBoolean("show_x_gridLine",true);
        show_y_gridLine=sp.getBoolean("show_y_gridLine",true);
        x_gridLine=dialogView.findViewById(R.id.x_gridLine);
        y_gridLine=dialogView.findViewById(R.id.y_gridLine);
        x_gridLine.setChecked(show_x_gridLine);
        y_gridLine.setChecked(show_y_gridLine);
        SharedPreferences.Editor edit = sp.edit();
        x_gridLine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                edit.putBoolean("show_x_gridLine",b);
                edit.apply();//提交数据保存
            }
        });
        y_gridLine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                edit.putBoolean("show_y_gridLine",b);
                edit.apply();//提交数据保存
            }
        });

        int xMin = sp.getInt("xMin",0);
        int xMax = sp.getInt("xMax",0);
        int yMin = sp.getInt("yMin",0);
        int yMax = sp.getInt("yMax",0);
        et_xMin.setText(xMin+"");
        et_xMax.setText(xMax+"");
        et_yMin.setText(yMin+"");
        et_yMax.setText(yMax+"");
        x_gridLine.getText().toString();
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

                            SharedPreferences.Editor edit = sp.edit();
                            edit.putInt("xMin",Integer.valueOf(xMin));
                            edit.putInt("xMax",Integer.valueOf(xMax));
                            edit.putInt("yMin",Integer.valueOf(yMin));
                            edit.putInt("yMax",Integer.valueOf(yMax));
                            edit.apply();//提交数据保存
                            contentFragment.setChartXY(Integer.valueOf(xMin),Integer.valueOf(xMax),Integer.valueOf(yMin),Integer.valueOf(yMax));
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
        et_xTitle.setText("浓度");
        et_yTitle.setText("灰度");
        SharedPreferences sp = activity.getSharedPreferences("axistitle_sp_info", Context.MODE_PRIVATE);
        String xTitle = sp.getString("xTitle","");
        String yTitle = sp.getString("yTitle","");
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
                            SharedPreferences sp = activity.getSharedPreferences("axis_sp_info", Context.MODE_PRIVATE);
                            int xMin = sp.getInt("xMin",0);
                            int xMax = sp.getInt("xMax",0);
                            int yMin = sp.getInt("yMin",0);
                            int yMax = sp.getInt("yMax",0);
                            contentFragment.setChartXY(Integer.valueOf(xMin),Integer.valueOf(xMax),Integer.valueOf(yMin),Integer.valueOf(yMax));
                            contentFragment.setXtitle(xTitle);contentFragment.setYtitle(yTitle);
                            fManager.beginTransaction().replace(R.id.fly_content,contentFragment).commit();
                            // 保存标题
                            SharedPreferences sp2 = dialogView.getContext().getSharedPreferences("axistitle_sp_info", Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = sp2.edit();
                            edit.putString("xTitle",xTitle);
                            edit.putString("yTitle",yTitle);
                            edit.apply();//提交数据保存
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

    public static void ShowEditListDialog(AppCompatActivity activity , FragmentManager fManager, List<Integer> list1,List<Integer> list2, int model) {
        AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(activity);
        final View dialogView = LayoutInflater.from(activity)
                .inflate(R.layout.dialog_list_layout,null);//dialog_update_password.xml布局文件可以弄得再高大上一些，但是我懒！！！
        customizeDialog.setTitle("修改list");
        customizeDialog.setView(dialogView);
        customizeDialog.setMessage("Model:"+model);
        /*初始化自定义dialog布局的控件*/
        ListView list_one=dialogView.findViewById(R.id.list_one);
        MyListAdapter mAdapter=null;
        if (model==1){
            mAdapter= new MyListAdapter(list1,dialogView.getContext());
        }
        if (model==2){
            mAdapter = new MyListAdapter(list2,dialogView.getContext());
        }
        list_one.setAdapter(mAdapter);

        customizeDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ContentFragment cFragment=null;
                        if (model==1){
                            cFragment = new ContentFragment(list1);
                        }
                        if (model==2)
                        {
                            cFragment= new ContentFragment(list1, list2);
                        }
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
}
