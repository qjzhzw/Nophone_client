package com.ForestAnimals.nophone.market;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import com.ForestAnimals.nophone.util.MyThread;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by MyWorld on 2016/6/21.
 */
public class goodsLab {
    private ArrayList<goods> goods_list;
    private static goodsLab sGoodsLab;
    private Context goods_context;

    private int number = 10;
    String[][] goods = new String[number][5];

    private goodsLab(Context the_goods_context) {
        connect();
        goods_context = the_goods_context;
        goods_list = new ArrayList<goods>();
        for (int i = 0; i < number; i++) {
            final goods the_goods = new goods();
            the_goods.setGoods_name(goods[i][0]);
            the_goods.setLocate_name(goods[i][1]);
            the_goods.setGold_number(Integer.parseInt(goods[i][3]));

            Glide.with(the_goods_context).load(goods[i][4]).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    the_goods.setGoods_picture(resource);
                }
            });

            goods_list.add(the_goods);
        }
    }

    public static goodsLab get(Context c) {
        if (sGoodsLab == null) {
            sGoodsLab = new goodsLab(c.getApplicationContext());
        }
        return sGoodsLab;
    }

    public ArrayList<goods> getGoods_list() {
        return goods_list;
    }

    public goods get_goods(UUID id) {
        for (goods c : goods_list) {
            if (c.getId().equals(id))
                return c;
        }
        return null;
    }

    private void connect() {
        String url = "information/market_goods/";
        //url最后那个‘/’不能少！

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        MyThread myThread = new MyThread(params, url);
        myThread.start();
        while (!myThread.isDone()) {
        }

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(myThread.result);
//            number = Integer.parseInt(jsonObject.getString("number"));
            System.out.println(number);
            JSONObject[] json = new JSONObject[number];

            for (int i = 0; i < number; i++) {
                json[i] = jsonObject.getJSONObject("goods" + String.valueOf(i + 1));
                goods[i][0] = json[i].getString("name");
                goods[i][1] = json[i].getString("address");
                goods[i][2] = json[i].getString("explanation");
                goods[i][3] = json[i].getString("price");
                goods[i][4] = json[i].getString("picture");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

