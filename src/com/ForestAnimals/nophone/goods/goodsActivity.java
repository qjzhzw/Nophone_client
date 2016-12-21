package com.ForestAnimals.nophone.goods;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ForestAnimals.nophone.R;

/**
 * Created by MyWorld on 2016/6/21.
 */
public class goodsActivity extends FragmentActivity {
    private goods the_goods;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_meterial_layout);
        the_goods=new goods();


    }


}
