package com.ForestAnimals.nophone.goods;

import android.widget.ImageView;

import java.util.Date;
import java.util.UUID;

/**
 *
 * Created by MyWorld on 2016/6/20.
 */
public class goods {
    private UUID goods_id;
    private String goods_name;
    private String locate_name;
    private int gold_number;
    private Date good_date;
    private ImageView goods_picture;
    public  goods(){
        goods_id=UUID.randomUUID();
        good_date=new Date();
    }
    public UUID getId(){
        return goods_id;
    }
    public  String getGoods_name(){
        return goods_name;
    }
    public void setGoods_name(String name){
        goods_name=name;
    }
    public void setGold_number(int i){
        gold_number=i;
    }
    public int getGold_number(){
        return gold_number;
    }
    public String getLocate_name(){
        return locate_name;
    }
    public void setLocate_name(String name){
        locate_name=name;
    }

    public ImageView getGoods_picture() {
        return goods_picture;
    }

    public void setGoods_picture(ImageView goods_picture) {
        this.goods_picture = goods_picture;
    }

    public Date getGood_date() {
        return good_date;
    }

    public void setGood_date(Date good_date) {
        this.good_date = good_date;
    }

    public UUID getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(UUID goods_id) {
        this.goods_id = goods_id;
    }
}
