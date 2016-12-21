package com.ForestAnimals.nophone.goods;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by MyWorld on 2016/6/21.
 */
public class goodsLab {
    private ArrayList<goods> goods_list;
    private static goodsLab sGoodsLab;
    private Context goods_context;

    private goodsLab(Context the_goods_context){
        goods_context=the_goods_context;
        goods_list=new ArrayList<goods>();
        for (int i=0;i<20;i++){
            goods the_goods=new goods();
            the_goods.setGoods_name("奖品名称 "+i);
            the_goods.setLocate_name("无锡市滨湖区江南大学北区商业街二楼 仙芋世家");
            the_goods.setGold_number(i * 10);
            goods_list.add(the_goods);
        }
    }
    public static goodsLab get(Context c){
        if(sGoodsLab==null){
            sGoodsLab=new goodsLab(c.getApplicationContext());
        }
        return sGoodsLab;
    }
    public ArrayList<goods> getGoods_list(){
        return goods_list;
    }
    public goods get_goods(UUID id){
        for(goods c:goods_list){
            if(c.getId().equals(id))
                return c;
        }
        return null;
    }
}

