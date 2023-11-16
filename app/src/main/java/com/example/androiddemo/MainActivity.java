package com.example.androiddemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
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

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    ImageView imageView_test,image_showbigcolor,image_absorb;
    Button btn_analysis,btn_select,btn_carmer,btn_gallery,btn_pick;
    SeekBar SeekBar_R,SeekBar_G,SeekBar_B,SeekBar_Gray;
    TextView textView,tv_r,tv_g,tv_b,tv_Gray;
    LinearLayout layout_show_color;
    Bitmap targetBitmap;
    Intent data1;
    EditText input_x,input_y;
    int x,y;
    int dstX,dstY;
    int TAKE_PHOTO_REQUEST = 10010;
    int PICK_PHOTO_REQUEST = 1234;
    int RESULT_CANCELED = 0;
    int rgbPixel;

    boolean ifupdatecolor=false;
    int updateIndex=0;
    List<Integer> list = new ArrayList<>();
    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;


    private Context mContext;
    private GridView grid_photo;
    int grid_NumColumns=8;
    private BaseAdapter mAdapter = null;
    private ArrayList<Grid_Item> mData = new ArrayList<Grid_Item>();
    DBUtils dbUtils;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        dbUtils=new DBUtils(this);
        mContext = MainActivity.this;
        grid_photo = (GridView) findViewById(R.id.grid_photo);
        grid_photo.setNumColumns(grid_NumColumns);
        showGrid();
        grid_photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateIndex=position;
                Toast.makeText(mContext, "你点击了~" + (position+1) + "~项"+mData.get(position)+"size:"+mData.size(), Toast.LENGTH_SHORT).show();
                initPopWindow(view);
            }
        });

        getPermission();

        initView();

        imageView_test.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // 获取触摸点的坐标 x, y
                float x1 = motionEvent.getX();
                float y1 = motionEvent.getY();
                // 目标点的坐标
                float dst[] = new float[2];
                // 获取到ImageView的matrix
                Matrix imageMatrix = imageView_test.getImageMatrix();
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
                input_x.setText(dstX+"");
                input_y.setText(dstY+"");
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
        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*"),PICK_PHOTO_REQUEST);
            }
        });
        btn_analysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initList();
                if (list.size()>0)
                {
                    Intent intent = new Intent(MainActivity.this,AnalysisActivity.class);
                    intent.putExtra("list", (Serializable) list);
                    startActivity(intent);
                }
                else Toast.makeText(MainActivity.this,"请添加数据后再进行分析",Toast.LENGTH_LONG).show();
            }
        });

        btn_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i<list.size(); i++)
                {
                    Log.i("btn_pick","list"+ list.get(i));
                }
                for(Grid_Item kk:mData)
                {
                    Log.i("btn_pick", "mData"+(Color.red(kk.getColor())*30+Color.green(kk.getColor())*59+Color.blue(kk.getColor())*11)/100);
                    Log.i("btn_pick", "mData-Id_g"+kk.getId_g()+"");
                }

            }
        });
        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (targetBitmap!=null){
//                    Log.i("btn_select", targetBitmap.toString()+"\n当前图片大小"+targetBitmap.getWidth()+","+targetBitmap.getHeight());
                    if (dstX>0&&dstY>0&&dstX<targetBitmap.getWidth()&&dstY<targetBitmap.getHeight()){
                            Grid_Item gridItem = new Grid_Item(rgbPixel, "" + (mData.size() + 1) + "--" + 48);
//                        当前是第n个数
                            int n = mData.size()+1;
                            Log.i("btn_select", "第n各数: "+n);
                            if (n<=24)
                            {
                                gridItem.setId_g((n)%8);
                                if (n%8 == 0)
                                {//         获取前一个数的id_G加1
                                    Log.i("btn_select", "前一个数的id_g: "+n);
                                    int id_G = mData.get(n-2).getId_g();
                                    gridItem.setId_g(id_G+1);
                                }
                            }
                            else {
                                if (n%8 == 0)
                                {//         获取前一个数的id_G加1
                                    Log.i("btn_select", "前一个数的id_g: "+n);
                                    int id_G = mData.get(n-2).getId_g();
                                    gridItem.setId_g(id_G+1);
                                }else {
                                    gridItem.setId_g(8+n%8);
                                }
                            }
                            mData.add(gridItem);
                            dbUtils.insert(gridItem.getId_g(),gridItem.getColor(),gridItem.getItemGray(),gridItem.getiName());
                            showGrid();
                    }else {
                        Log.i("btn_select","坐标("+x+","+y+")不合法");
                    }
                }else Toast.makeText(MainActivity.this,"请添加图片后再试",Toast.LENGTH_LONG).show();


            }
        });

        btn_carmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, TAKE_PHOTO_REQUEST);
            }
        });

    }

    private void initPopWindow(View v) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_popip, null, false);
        Button btn_xixi = (Button) view.findViewById(R.id.btn_xixi);
        Button btn_hehe = (Button) view.findViewById(R.id.btn_hehe);
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
        btn_xixi.setOnClickListener(new View.OnClickListener() {
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
        btn_hehe.setOnClickListener(new View.OnClickListener() {
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

    /**
     * 将选择的颜色进行分组得到list
     * list画图用的数据
     */
    private void initList() {
        list=new ArrayList<>();
        //普通的__分组
        Map<Integer,List<Grid_Item>> groups = new HashMap<>();
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
//           list.add(getGray(dstX,dstY));
        Set<Map.Entry<Integer, List<Grid_Item>>> entries = groups.entrySet();
        Iterator<Map.Entry<Integer, List<Grid_Item>>> iterator = entries.iterator();
        while (iterator.hasNext())
        {
            Map.Entry<Integer, List<Grid_Item>> entry = iterator.next();
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

    }

    private void showGrid() {
        mAdapter = new MyAdapter<Grid_Item>(mData, R.layout.item_grid) {
            @Override
            public void bindView(ViewHolder holder, Grid_Item obj) {
                holder.setColor(R.id.img_icon,obj.getColor());
                holder.setText(R.id.txt_icon, obj.getiName());
            }
        };

        grid_photo.setAdapter(mAdapter);
    }

    /**
     * 获取存储卡读写权限
     */
    private void getPermission() {
        /*获取读写权限*/
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }
    }

    private void getTargetBitmapRGB(Bitmap targetBitmap) {
        int rgbPixel = targetBitmap.getPixel(x, y);
        int r,g,b,gray;
        r=Color.red(rgbPixel);
        g=Color.green(rgbPixel);
        b=Color.blue(rgbPixel);
    }

    private void clearData() {
        list = new ArrayList<>();
        mData=new ArrayList<Grid_Item>();
        showGrid();
    }

    private void initView() {
//        layout_show_color=findViewById(R.id.layout_show_color);
        imageView_test = findViewById(R.id.image_test);
        image_showbigcolor=findViewById(R.id.image_showbigcolor);
        image_absorb=findViewById(R.id.image_absorb);
        btn_analysis=findViewById(R.id.btn_analysis);
        btn_select=findViewById(R.id.btn_select);
        btn_carmer=findViewById(R.id.btn_carmera);
        btn_gallery=findViewById(R.id.btn_gallery);
        btn_pick=findViewById(R.id.btn_pick);
        input_x=findViewById(R.id.intput_X);
        input_y=findViewById(R.id.intput_y);
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
                imageView_test.setImageBitmap(targetBitmap);
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
                    imageView_test.setImageBitmap(targetBitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

/*————————————————
            版权声明：本文为CSDN博主「Modu_MrLiu」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
            原文链接：https://blog.csdn.net/qq_20451879/article/details/70314547*/
        }

    }

    int getGray(int color){
        int r,g,b,gray;
        r=Color.red(color);
        g=Color.green(color);
        b=Color.blue(color);
        gray=(r*30+g*59+b*11)/100;
        return  gray;
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
