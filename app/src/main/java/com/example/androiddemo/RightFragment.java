package com.example.androiddemo;

import static com.example.androiddemo.DialogUtils.ShowEditAxisDialog;
import static com.example.androiddemo.DialogUtils.ShowEditAxisTitleDialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.List;


public class RightFragment extends Fragment {

    private DrawerLayout drawer_layout;
    private FragmentManager fManager;
    List<Integer> list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_right, container, false);
        fManager = getActivity().getSupportFragmentManager();
        view.findViewById(R.id.btn_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentFragment cFragment1 = new ContentFragment();
                cFragment1.setList(list);
                ShowEditAxisTitleDialog((AppCompatActivity) drawer_layout.getContext(),cFragment1,fManager);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });
        view.findViewById(R.id.btn_two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentFragment cFragment1 = new ContentFragment();
                cFragment1.setList(list);
                ShowEditAxisDialog((AppCompatActivity) drawer_layout.getContext(),cFragment1,fManager);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });
        view.findViewById(R.id.btn_three).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });
        return view;
    }

    public void setDrawerLayout(DrawerLayout drawer_layout){
        this.drawer_layout = drawer_layout;
    }

    public void setList(List<Integer> list) {
        this.list = list;
    }
}