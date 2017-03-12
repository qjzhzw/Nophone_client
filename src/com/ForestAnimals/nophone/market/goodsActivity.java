package com.ForestAnimals.nophone.market;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.ForestAnimals.nophone.R;
import com.ForestAnimals.nophone.user.Person;
import com.ForestAnimals.nophone.util.MyThread;
import com.bumptech.glide.Glide;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MyWorld on 2016/6/21.
 */
public class goodsActivity extends FragmentActivity {
    private String[] result;

    private ImageView goods_show_big;
    private TextView goods_name,
            gold_number,
            goods_store,
            goods_explanation;
    private Button goods_change;
    private String name,identification;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_meterial_layout);

        goods_name = (TextView) findViewById(R.id.goods_name);
        gold_number = (TextView) findViewById(R.id.gold_number);
        goods_store = (TextView) findViewById(R.id.goods_store);
        goods_explanation = (TextView) findViewById(R.id.goods_explanation);
        goods_show_big = (ImageView) findViewById(R.id.goods_show_big);
        goods_change = (Button) findViewById(R.id.goods_change);

        goods_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNormalDialog();
            }
        });

        SharedPreferences goods = getSharedPreferences("goods", 0);
        name = goods.getString("name", "0");
        SharedPreferences information = getSharedPreferences("information", 0);
        identification = information.getString("identification", "0");

        goods_name.setText(name);

         result = connect();
        goods_store.setText("店铺地址："+result[0]);
        goods_explanation.setText("奖品说明："+result[1]);
        gold_number.setText(result[2]);
        Glide.with(this).load(result[3]).into(goods_show_big);

    }

    private String[] connect() {
        String url = "information/goods_information/";
        //url最后那个‘/’不能少！

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("name", name));

        MyThread myThread = new MyThread(params, url);
        myThread.start();
        while (!myThread.isDone()) {
        }

        String[] parse_key = {"address", "explanation", "price", "picture"};
        //需要解析的关键词

        return myThread.parseJson(parse_key);
    }

    private void showNormalDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(goodsActivity.this);
        normalDialog.setTitle("确认");
        normalDialog.setMessage("你确定要兑换吗？");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        String[] result = connect_change();

                        if (result[0].equals("failed"))
                            Toast.makeText(goodsActivity.this, getString(R.string.check_Internet), Toast.LENGTH_SHORT).show();
                        if (result[0].equals("not enough"))
                            Toast.makeText(goodsActivity.this, getString(R.string.not_enough), Toast.LENGTH_SHORT).show();
                        if (result[0].equals("success"))
                            Toast.makeText(goodsActivity.this, getString(R.string.change_success), Toast.LENGTH_SHORT).show();
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        // 显示
        normalDialog.show();
    }


    private String[] connect_change() {
        String url = "information/goods_change/";
        //url最后那个‘/’不能少！

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("identification", identification));

        MyThread myThread = new MyThread(params, url);
        myThread.start();
        while (!myThread.isDone()) {
        }

        String[] parse_key = {"status"};
        //需要解析的关键词

        return myThread.parseJson(parse_key);
    }

}
