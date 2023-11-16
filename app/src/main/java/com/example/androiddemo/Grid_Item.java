package com.example.androiddemo;

import android.graphics.Color;

import java.util.ArrayList;

public class Grid_Item extends ArrayList<Grid_Item> {
    private String iName;
    private int id_g;
    private int color;

    private int itemGray;



    public Grid_Item() {
    }

    public Grid_Item( int color,String iName) {
        this.iName = iName;
        this.color = color;
        this.itemGray =getGray(color);
    }
    int getGray(int color){
        int r,g,b,gray;
        r= Color.red(color);
        g=Color.green(color);
        b=Color.blue(color);
        gray=(r*30+g*59+b*11)/100;
        return  gray;
    }
    public Grid_Item( int color,String iName, int id) {
        this.iName = iName;
        this.id_g = id;
        this.color = color;
    }

    public int getItemGray() {
        return itemGray;
    }

    public void setItemGray(int itemGray) {
        this.itemGray = itemGray;
    }

    public int getId_g() {
        return id_g;
    }

    public void setId_g(int id_g) {
        this.id_g = id_g;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getiName() {
        return iName;
    }

    public void setiName(String iName) {
        this.iName = iName;
    }

    @Override
    public String toString() {
        return "Grid_Item{" +
                "iName='" + iName + '\'' +
                ", id_g=" + id_g +
                ", color=" + color +
                ", gary=" + itemGray +
                '}';
    }
}
