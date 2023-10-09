package com.example.androiddemo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

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
        et_xMin = dialogView.findViewById(R.id.et_xMin);
        et_xMax = dialogView.findViewById(R.id.et_xMax);
        et_yMin = dialogView.findViewById(R.id.et_yMin);
        et_yMax = dialogView.findViewById(R.id.et_yMax);
        SharedPreferences sp = activity.getSharedPreferences("axis_sp_info", Context.MODE_PRIVATE);
        int xMin = sp.getInt("xMin",0);
        int xMax = sp.getInt("xMax",0);
        int yMin = sp.getInt("yMin",0);
        int yMax = sp.getInt("yMax",0);
        et_xMin.setText(xMin+"");
        et_xMax.setText(xMax+"");
        et_yMin.setText(yMin+"");
        et_yMax.setText(yMax+"");
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
                            contentFragment.setChartXY(Integer.valueOf(xMin),Integer.valueOf(xMax),Integer.valueOf(yMin),Integer.valueOf(yMax));
                            fManager.beginTransaction().replace(R.id.fly_content,contentFragment).commit();
                            // 登录成功记录本次登录信息
                            SharedPreferences sp = dialogView.getContext().getSharedPreferences("axis_sp_info", Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = sp.edit();
                            edit.putInt("xMin",Integer.valueOf(xMin));
                            edit.putInt("xMax",Integer.valueOf(xMax));
                            edit.putInt("yMin",Integer.valueOf(yMin));
                            edit.putInt("yMax",Integer.valueOf(yMax));
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
