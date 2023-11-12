package com.example.androiddemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ImageView imageView_test,image_camera,image_gallery;
    Button btn_analysis,btn_select;
    SeekBar SeekBar_R,SeekBar_G,SeekBar_B,SeekBar_Gray;
    TextView textView,tv_r,tv_g,tv_b,tv_Gray;
    LinearLayout layout_show_color;
    ImageView imageView_absorbcolor;
    Bitmap targetBitmap;
    Intent data1;
    EditText input_x,input_y;
    int x,y;
    int dstX,dstY;
    int TAKE_PHOTO_REQUEST = 10010;
    int PICK_PHOTO_REQUEST = 1234;
    int RESULT_CANCELED = 0;
    List<Integer> list = new ArrayList<>();
    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        /*获取读写权限*/
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }
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
                    if (targetBitmap!=null){
                        Log.i("btn_pick_color", targetBitmap.toString()+"\n当前图片大小"+targetBitmap.getWidth()+","+targetBitmap.getHeight());
   /*                 if (dstX>0&&dstY>0&&dstX<targetBitmap.getWidth()&&dstY<targetBitmap.getHeight()){
                        list.add(getGray(targetBitmap,dstX,dstY));
                    }else {
                        Log.i("btn_pick_color","坐标("+x+","+y+")不合法");

                    }*/
                        getGray(dstX,dstY);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_show_color.getLayoutParams();
                        layoutParams.leftMargin = (int) (0+x1);
                        layoutParams.topMargin = (int) (0+y1-15);

//                    Log.i("坐标",imageView.getLeft()+","+imageView.getTop());
                        layout_show_color.setLayoutParams(layoutParams);
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
        image_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*"),PICK_PHOTO_REQUEST);
            }
        });
        btn_analysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.size()>0)
                {
                    Intent intent = new Intent(MainActivity.this,AnalysisActivity.class);
                    intent.putExtra("list", (Serializable) list);
                    startActivity(intent);
                }
                else Toast.makeText(MainActivity.this,"请添加数据后再进行分析",Toast.LENGTH_LONG).show();
            }
        });

        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (targetBitmap!=null){
                    Log.i("btn_select", targetBitmap.toString()+"\n当前图片大小"+targetBitmap.getWidth()+","+targetBitmap.getHeight());
                    if (dstX>0&&dstY>0&&dstX<targetBitmap.getWidth()&&dstY<targetBitmap.getHeight()){
                        list.add(getGray(dstX,dstY));
                    }else {
                        Log.i("btn_select","坐标("+x+","+y+")不合法");
                    }
                }else Toast.makeText(MainActivity.this,"请添加图片后再试",Toast.LENGTH_LONG).show();


            }
        });
        image_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, TAKE_PHOTO_REQUEST);
            }
        });

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
    }

    private void initView() {
        layout_show_color=findViewById(R.id.layout_show_color);
        imageView_absorbcolor =findViewById(R.id.imageView_absorbcolor);
        imageView_test = findViewById(R.id.image_test);
        image_camera = findViewById(R.id.image_camera);
        image_gallery = findViewById(R.id.image_gallery);
        btn_analysis=findViewById(R.id.btn_analysis);
        btn_select=findViewById(R.id.btn_select);
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
        textView = findViewById(R.id.textView);
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
    int getGray(int x,int y){
        int rgbPixel = targetBitmap.getPixel(x, y);
        int r,g,b,gray;
        r=Color.red(rgbPixel);
        g=Color.green(rgbPixel);
        b=Color.blue(rgbPixel);
        layout_show_color.setBackgroundColor(Color.rgb(r,g,b));
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
