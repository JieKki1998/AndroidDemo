package com.example.androiddemo;

import static com.example.androiddemo.DialogUtils.ShowEditAxisDialog;
import static com.example.androiddemo.DialogUtils.ShowEditAxisTitleDialog;
import static com.example.androiddemo.DialogUtils.ShowEditListDialog;

import android.annotation.SuppressLint;
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
import java.util.stream.Collectors;


public class RightFragment extends Fragment {

    private DrawerLayout drawer_layout;
    private FragmentManager fManager;
    List<Integer> list1,list2;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_right, container, false);
        fManager = getActivity().getSupportFragmentManager();
        view.findViewById(R.id.btn_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentFragment cFragment1 = new ContentFragment(list1);
                ShowEditAxisTitleDialog((AppCompatActivity) drawer_layout.getContext(),cFragment1,fManager);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });
        view.findViewById(R.id.btn_two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentFragment cFragment = new ContentFragment(list1);
                ShowEditAxisDialog((AppCompatActivity) drawer_layout.getContext(),cFragment,fManager);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });
        view.findViewById(R.id.btn_three).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int model=1;
                ShowEditListDialog((AppCompatActivity) drawer_layout.getContext(),fManager, list1,list2,model);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });
        view.findViewById(R.id.btn_thrid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int model=2;
                ShowEditListDialog((AppCompatActivity) drawer_layout.getContext(),fManager, list1,list2,model);
                drawer_layout.closeDrawer(GravityCompat.END);
            }
        });
        return view;
    }

    public void setDrawerLayout(DrawerLayout drawer_layout){
        this.drawer_layout = drawer_layout;
    }

    public void setList1(List<Integer> list1) {
        this.list1 = list1;
    }

    public void setList2(List<Integer> list2) { this.list2 = list2; }
}