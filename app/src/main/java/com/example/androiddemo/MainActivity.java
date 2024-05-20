package com.example.androiddemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;

import com.example.androiddemo.adapter.MyGridAdapter;
import com.example.androiddemo.entity.Grid_Item;
import com.example.androiddemo.util.SqlLiteUtils;
import com.example.androiddemo.util.PermissionUtil;
import com.example.androiddemo.util.SpUtil;
import com.example.androiddemo.util.Utils;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ImageView img_showPicture,image_showbigcolor,image_absorb;
    Button btn_select_color;
    SeekBar SeekBar_R,SeekBar_G,SeekBar_B,SeekBar_Gray;
    TextView textView,tv_r,tv_g,tv_b,tv_Gray;
    LinearLayout layout_show_color;
    Bitmap targetBitmap;
    Intent data1;
    EditText editText_initial_concentration, editText_step,editText_NCol,editText_GNum;
    int x,y;
    int dstX,dstY;
    int TAKE_PHOTO_REQUEST = 10010;
    int PICK_PHOTO_REQUEST = 1234;
    int RESULT_CANCELED = 0;
    int rgbPixel;

    boolean ifupdatecolor=false;
    int updateIndex=0;
    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;


    private Context mContext;
    private GridView grid_photo;
    //gridview一行几个数
    int grid_NumColumns=11;
    //每组几个数据
    int grid_groupNum=3;
    private ArrayList<Grid_Item> mData = new ArrayList<Grid_Item>();
    SqlLiteUtils sqlLiteUtils;
    SpUtil spUtil;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        mContext = MainActivity.this;
        spUtil=new SpUtil(mContext);
        //获取读取存储权限
        new PermissionUtil(this).getPermission();
        //获取数据库工具
        sqlLiteUtils =new SqlLiteUtils(this);
        //加载控件
        initView();
        showGrid();
        grid_photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateIndex=position;
//                Toast.makeText(mContext, "你点击了~" + (position+1) + "~项"+mData.get(position)+"size:"+mData.size(), Toast.LENGTH_SHORT).show();
                initPopWindow(view);
            }
        });



        img_showPicture.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // 获取触摸点的坐标 x, y
                float x1 = motionEvent.getX();
                float y1 = motionEvent.getY();
                // 目标点的坐标
                float dst[] = new float[2];
                // 获取到ImageView的matrix
                Matrix imageMatrix = img_showPicture.getImageMatrix();
                // 创建一个逆矩阵
                Matrix inverseMatrix = new Matrix();
                // 求逆，逆矩阵被赋值
                imageMatrix.invert(inverseMatrix);
                // 通过逆矩阵映射得到目标点 dst 的值
                inverseMatrix.mapPoints(dst, new float[]{x1, y1});
                dstX= (int) dst[0];
                dstY = (int) dst[1];
                Log.i("dstX,dstY:", dstX+ ","+dstY);
                // 判断dstX, dstY在Bitmap上的位置即可
/*————————————————
                版权声明：本文为CSDN博主「阿飞__」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
                原文链接：https://blog.csdn.net/afei__/article/details/80155303*/
//                x = (int) motionEvent.getX();
//                y = (int) motionEvent.getY();
//                Log.i("touched x val of cap img >>", x + "");
//                Log.i("touched y val of cap img >>", y + "");
/*————————————————
                版权声明：本文为CSDN博主「weixin_39950081」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
                原文链接：https://blog.csdn.net/weixin_39950081/article/details/111803149*/
                //修改imageview的位置
                if (motionEvent.getAction()==MotionEvent.ACTION_DOWN)
                {
                    if (targetBitmap!=null){//检查是否有图片图片后再试
//                        Log.i("btn_pick_color", targetBitmap.toString()+"\n当前图片大小"+targetBitmap.getWidth()+","+targetBitmap.getHeight());
                        if (dstX>0&&dstY>0&&dstX<targetBitmap.getWidth()&&dstY<targetBitmap.getHeight()){
                            getGray(dstX,dstY);
                            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) image_absorb.getLayoutParams();
                            layoutParams.leftMargin = (int) (x1);
                            layoutParams.topMargin = (int) (y1-15);
    //                    Log.i("坐标",imageView.getLeft()+","+imageView.getTop());
                            image_absorb.setLayoutParams(layoutParams);//修改取色器位置
//                            layout_show_color.setLayoutParams(layoutParams);
//                            image_showbigcolor.setBackgroundColor(rgbPixel);
//                            修改取色器位置
//                            image_absorb.setColorFilter(rgbPixel, PorterDuff.Mode.SRC_IN);
                            image_showbigcolor.setColorFilter(rgbPixel, PorterDuff.Mode.SRC_IN);

                        }else {
                            Toast.makeText(MainActivity.this,"坐标("+dstX+","+dstY+")不合法",Toast.LENGTH_SHORT).show();
                        }

                    }else Toast.makeText(MainActivity.this,"请添加图片后再试",Toast.LENGTH_LONG).show();
                }

                return true;
            }
        });
        findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearData();
            }
        });
       /* findViewById(R.id.btn_pick_color).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"btn_pick_color",Toast.LENGTH_LONG).show();
                if (!input_x.getText().equals(null)&&!input_y.getText().equals(null))
                {
                    x= Integer.parseInt(input_x.getText().toString());
                    y= Integer.parseInt(input_y.getText().toString());
                    Log.i("btn_pick_color", x+","+y);
                    if (targetBitmap!=null){
                        Log.i("btn_pick_color", targetBitmap.toString());
                        if (x<targetBitmap.getWidth()&&y<targetBitmap.getHeight()){
                            int rgbPixel = targetBitmap.getPixel(x, y);
                            textView.setText("rgb: r---" + Color.red(rgbPixel) + "  g-- " + Color.green(rgbPixel) +" b--"+Color.blue(rgbPixel));
                            Log.i("btn_pick_color", "pixel: " + Integer.toHexString(rgbPixel));
                            Log.i("btn_pick_color", "rgb: r---" + Color.red(rgbPixel) + "  g-- " + Color.green(rgbPixel) +" b--"+Color.blue(rgbPixel));
                            Toast.makeText(MainActivity.this,x+","+y,Toast.LENGTH_LONG).show();
                        }else {
                            Log.i("btn_pick_color","坐标("+x+","+y+")不合法");
                        }

                    }else Toast.makeText(MainActivity.this,"请添加图片后再试",Toast.LENGTH_LONG).show();
                }
            }
        });*/
        findViewById(R.id.btn_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this);
                Intent intent = new Intent(MainActivity.this,HistoryActivity.class);
                ActivityCompat.startActivity(MainActivity.this, intent, optionsCompat.toBundle());
            }
        });

        findViewById(R.id.btn_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*"),PICK_PHOTO_REQUEST);
            }
        });
        findViewById(R.id.btn_analysis).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mData.size()>0 && Utils.ifEditEmpty(mContext,editText_initial_concentration,editText_step))
                {
                    String initial_concentration=editText_initial_concentration.getText().toString();
                    String step=editText_step.getText().toString();
                    spUtil.setFloat("initial_concentration",Float.valueOf(initial_concentration));
                    spUtil.setFloat("step",Float.valueOf(step));
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this);
                    Intent intent = new Intent(MainActivity.this,AnalysisActivity.class);
                    spUtil.setDataList("mData",mData);
                    ActivityCompat.startActivity(MainActivity.this, intent, optionsCompat.toBundle());
                }
                else Toast.makeText(MainActivity.this,"请添加数据后再进行分析",Toast.LENGTH_LONG).show();
            }
        });


        btn_select_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                grid_NumColumns=Integer.valueOf(editText_NCol.getText().toString());
                grid_groupNum=Integer.valueOf(editText_GNum.getText().toString());
                grid_photo.setNumColumns(grid_NumColumns);//设置取色一行多少个
                if (targetBitmap!=null){
//                    Log.i("btn_select", targetBitmap.toString()+"\n当前图片大小"+targetBitmap.getWidth()+","+targetBitmap.getHeight());
                    if (dstX>0&&dstY>0&&dstX<targetBitmap.getWidth()&&dstY<targetBitmap.getHeight()){
                            Grid_Item gridItem = new Grid_Item(rgbPixel, "" + (mData.size() + 1) + "");
//                        当前是第n个数
                            int n = mData.size()+1;
                            Log.i("btn_select", "第n各数: "+n);
                            if (n<=grid_groupNum*grid_NumColumns)
                            {
                                gridItem.setId_g((n)%grid_NumColumns);
                                if (n%grid_NumColumns == 0)
                                {//         获取前一个数的id_G加1
                                    Log.i("btn_select", "前一个数的id_g: "+n);
                                    int id_G = mData.get(n-2).getId_g();
                                    gridItem.setId_g(id_G+1);
                                }
                            }
                            else {
                                if (n%grid_NumColumns == 0)
                                {//         获取前一个数的id_G加1
                                    Log.i("btn_select", "前一个数的id_g: "+n);
                                    int id_G = mData.get(n-2).getId_g();
                                    gridItem.setId_g(id_G+1);
                                }else {
                                    gridItem.setId_g(grid_NumColumns+n%grid_NumColumns);
                                }
                            }
                            mData.add(gridItem);
                            showGrid();
                    }else {
                        Log.i("btn_select","坐标("+x+","+y+")不合法");
                    }
                }else Toast.makeText(MainActivity.this,"请添加图片后再试",Toast.LENGTH_LONG).show();


            }
        });

        findViewById(R.id.btn_carmera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, TAKE_PHOTO_REQUEST);
            }
        });

    }

    private void initPopWindow(View v) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_popip, null, false);
        Button updateColor = (Button) view.findViewById(R.id.btn_update_color);
        Button removeColor = (Button) view.findViewById(R.id.btn_remove_color);
        //1.构造一个PopupWindow，参数依次是加载的View，宽高
        final PopupWindow popWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popWindow.setAnimationStyle(R.drawable.anim_pop);  //设置加载动画

        //这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
        //代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
        //PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
        popWindow.setTouchable(true);
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        popWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));    //要为popWindow设置一个背景才有效


        //设置popupWindow显示的位置，参数依次是参照View，x轴的偏移量，y轴的偏移量
        popWindow.showAsDropDown(v, 50, 0);

        //设置popupWindow里的按钮的事件
        updateColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "你点击了修改~", Toast.LENGTH_SHORT).show();
                Grid_Item gridItem = mData.get(updateIndex);
                gridItem.setColor(rgbPixel);
                ifupdatecolor=false;
                showGrid();
                popWindow.dismiss();
            }
        });
        removeColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "你点击了清除~", Toast.LENGTH_SHORT).show();
                Grid_Item gridItem = mData.get(updateIndex);
                gridItem.setColor(000000);
                gridItem.setId_g(-1);
                ifupdatecolor=false;
                showGrid();
                popWindow.dismiss();
            }
        });
    }


    private void showGrid() {
        grid_NumColumns=Integer.valueOf(editText_NCol.getText().toString());
        grid_groupNum=Integer.valueOf(editText_GNum.getText().toString());
        spUtil.setInt("grid_NumColumns",grid_NumColumns);
        spUtil.setInt("grid_groupNum",grid_groupNum);
        grid_photo.setNumColumns(grid_NumColumns);//设置取色一行多少个
        BaseAdapter mAdapter = new MyGridAdapter<Grid_Item>(mData, R.layout.item_grid) {
            @Override
            public void bindView(ViewHolder holder, Grid_Item obj) {
                holder.setColor(R.id.img_icon,obj.getColor());
                holder.setText(R.id.txt_icon, obj.getiName());
            }
        };
        grid_photo.setAdapter(mAdapter);
    }


    private void clearData() {
        mData=new ArrayList<Grid_Item>();
        showGrid();
    }

    private void initView() {
//        layout_show_color=findViewById(R.id.layout_show_color);
        img_showPicture = findViewById(R.id.img_show_picture);
        image_showbigcolor=findViewById(R.id.image_showbigcolor);
        image_absorb=findViewById(R.id.image_absorb);
        btn_select_color =findViewById(R.id.btn_select_color);
        editText_initial_concentration =findViewById(R.id.editText_initial_concentration);
        editText_step =findViewById(R.id.editText_step);
        //加载上次使用的浓度和步长
        editText_initial_concentration.setText(spUtil.getFloat("initial_concentration")+"");
        editText_step.setText(spUtil.getFloat("step")+"");
        editText_NCol=findViewById(R.id.editText_NCol);
        editText_GNum=findViewById(R.id.editText_GNum);
        int nc=spUtil.getInt("grid_NumColumns");
        if (nc!=0)
            editText_NCol.setText(nc+"");
        int gn=spUtil.getInt("grid_groupNum");
        if (gn!=0)
            editText_GNum.setText(gn+"");

        grid_photo = (GridView) findViewById(R.id.grid_photo);
        grid_photo.setNumColumns(grid_NumColumns);//设置取色一行多少个

        //SeekBar使用http://t.csdn.cn/8gFFM
        SeekBar_R = findViewById(R.id.SeekBar_R);
        SeekBar_G = findViewById(R.id.SeekBar_G);
        SeekBar_B = findViewById(R.id.SeekBar_B);
        SeekBar_Gray = findViewById(R.id.SeekBar_Gray);
        SeekBar_R.setMax(255);
        SeekBar_R.setProgress(0);
        SeekBar_G.setMax(255);
        SeekBar_G.setProgress(0);
        SeekBar_B.setMax(255);
        SeekBar_B.setProgress(0);
        SeekBar_Gray.setMax(255);
        SeekBar_Gray.setProgress(0);
        tv_r = findViewById(R.id.tv_r);
        tv_g = findViewById(R.id.tv_g);
        tv_b = findViewById(R.id.tv_b);
        tv_Gray = findViewById(R.id.tv_Gray);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("TAG", "resultCode:"+resultCode);
        Log.i("TAG", "requestCode:"+requestCode);

        if (resultCode == RESULT_CANCELED) {
            if (requestCode==TAKE_PHOTO_REQUEST)
                Toast.makeText(MainActivity.this, "取消了拍照", Toast.LENGTH_LONG).show();
            if (requestCode==PICK_PHOTO_REQUEST)
                Toast.makeText(MainActivity.this, "没有选择任何图片", Toast.LENGTH_LONG).show();
        }
        if (requestCode == TAKE_PHOTO_REQUEST){
            if (data!=null){
                targetBitmap = data.getParcelableExtra("data");
                img_showPicture.setImageBitmap(targetBitmap);
            }
                     /*————————————————
        版权声明：本文为CSDN博主「开飞机的老舒克」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
        原文链接：https://blog.csdn.net/xuanguofeng/article/details/52267022*/
        }

        if (requestCode == PICK_PHOTO_REQUEST){
            if (data!=null){
                ContentResolver contentResolver = getContentResolver();
                try {
                    targetBitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(data.getData()));
                    Log.i("TAG", "从相册回传bitmap："+targetBitmap);
                    /*需要裁剪图片http://t.csdn.cn/mavhX*/
                    data1=data;
                    img_showPicture.setImageBitmap(targetBitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
/*————————————————
            版权声明：本文为CSDN博主「Modu_MrLiu」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
            原文链接：https://blog.csdn.net/qq_20451879/article/details/70314547*/
        }

    }

    /**
     * 计算指定位置的灰度值
     * @return int 灰度值
     */
    int getGray(int x,int y){
        rgbPixel = targetBitmap.getPixel(x, y);
        int r,g,b,gray;
        r=Color.red(rgbPixel);
        g=Color.green(rgbPixel);
        b=Color.blue(rgbPixel);
        showGrid();
//        layout_show_color.setBackgroundColor(Color.rgb(r,g,b));
         gray=(r*30+g*59+b*11)/100;
//                textView.setText("rgb: r---" + Color.red(rgbPixel) + "  g-- " + Color.green(rgbPixel) +" b--"+Color.blue(rgbPixel));
        Log.i("JieKki", "pixel: " + Integer.toHexString(rgbPixel));
        Log.i("JieKki", "rgb: r---" + r + "  g-- " + g +" b--"+b);
//        Toast.makeText(this,"灰度值"+gray,Toast.LENGTH_SHORT).show();
        SeekBar_R.setProgress(r);
        SeekBar_G.setProgress(g);
        SeekBar_B.setProgress(b);
        SeekBar_Gray.setProgress(gray);
        tv_r.setText("R:"+r);
        tv_g.setText("G:"+g);
        tv_b.setText("B:"+b);
        tv_Gray.setText("灰:"+gray);
        return gray;
    }

    //检查权限是否获取成功
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
        }
    }
}
