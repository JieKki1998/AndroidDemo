package com.example.androiddemo.entity;

import android.graphics.Color;

import com.example.androiddemo.util.Utils;

import java.io.Serializable;
import java.util.ArrayList;

public class Grid_Item implements Serializable{
    private String iName;
    private int id_g;
    private int color;
    private int itemGray;
    private String times;

    private String title;

    public Grid_Item() {
    }

    public Grid_Item( int color,String iName) {
        this.iName = iName;
        this.color = color;
        this.itemGray=Utils.getGray(color);
    }

    public Grid_Item( int color,String iName, int id_g,String times) {
        this.iName = iName;
        this.id_g = id_g;
        this.color = color;
        this.times=times;
        this.itemGray=Utils.getGray(color);
    }

    public Grid_Item(String iName, int id_g, int color, int itemGray, String times, String title) {
        this.iName = iName;
        this.id_g = id_g;
        this.color = color;
        this.itemGray = itemGray;
        this.times = times;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
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
                ", itemGray=" + itemGray +
                ", times='" + times + '\'' +
                '}';
    }
}
