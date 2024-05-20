package com.example.androiddemo;

import static com.example.androiddemo.util.DialogUtils.ShowEditAxisDialog;
import static com.example.androiddemo.util.DialogUtils.ShowEditAxisTitleDialog;
import static com.example.androiddemo.util.DialogUtils.showSaveDialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.androiddemo.adapter.ListUpdateActivity;
import com.example.androiddemo.entity.Grid_Item;
import com.example.androiddemo.util.SqlLiteUtils;

import java.util.ArrayList;


public class RightFragment extends Fragment {

    private DrawerLayout drawer_layout;
    private FragmentManager fManager;
    ArrayList<Grid_Item>  mData;
    SqlLiteUtils sqlLiteUtils;
    Context mcontext;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_right, container, false);
        fManager = getActivity().getSupportFragmentManager();
        mcontext=getActivity();
        sqlLiteUtils =new SqlLiteUtils(getContext());
        view.findViewById(R.id.btn_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentFragment cFragment1 = new ContentFragment();
                ShowEditAxisTitleDialog((AppCompatActivity) drawer_layout.getContext(),cFragment1,fManager);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });
        view.findViewById(R.id.btn_two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentFragment cFragment = new ContentFragment();
                ShowEditAxisDialog((AppCompatActivity) drawer_layout.getContext(),cFragment,fManager);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });
        view.findViewById(R.id.btn_three).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityOptionsCompat options11 = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mcontext);
                Intent intent=new Intent(mcontext, ListUpdateActivity.class);
                ActivityCompat.startActivity(mcontext, intent, options11.toBundle());
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });
        view.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(mcontext,"还在开发中呢",Toast.LENGTH_LONG).show();
         /*       Map<Integer,List<Grid_Item>> groups = new HashMap<>();
                Date date = new Date();
                SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String times=dateFormat.format(date).toString();
                for(Grid_Item kk:mData)
                {
                    sqlLiteUtils.insert(kk.getId_g(),kk.getColor(),kk.getItemGray(),kk.getiName(),times);
                }*/
                showSaveDialog((AppCompatActivity) mcontext,"保存");
                fManager.beginTransaction().replace(R.id.fly_content, new ContentFragment()).commit();
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });
        return view;
    }

    public void setDrawerLayout(DrawerLayout drawer_layout){
        this.drawer_layout = drawer_layout;
    }

}