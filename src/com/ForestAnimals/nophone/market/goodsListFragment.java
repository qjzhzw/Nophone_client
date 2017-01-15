package com.ForestAnimals.nophone.market;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ForestAnimals.nophone.R;

import java.util.ArrayList;

/**
 * Created by MyWorld on 2016/6/21.
 */

public class goodsListFragment extends ListFragment {

    private ArrayList<goods> goods_list;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getActivity().setTitle("商店");

        goods_list = goodsLab.get(getActivity()).getGoods_list();

        goods_adapter adapter=new goods_adapter(goods_list);
        setListAdapter(adapter);
    }
    public void onListItemClick(ListView l,View v,int position,long id){
        goods goods=((goods_adapter)getListAdapter()).getItem(position);
        Intent intent=new Intent(getActivity(),goodsActivity.class);

        startActivity(intent);

    }
    private class goods_adapter extends ArrayAdapter<goods>{
        public goods_adapter(ArrayList<goods>goods_list){
            super(getActivity(),0,goods_list);
        }

        @Override
        public View getView(int position,View convertView,ViewGroup parent){
            if(convertView==null){
                convertView=getActivity().getLayoutInflater()
                        .inflate(R.layout.item_goods,null);
            }

            goods g=getItem(position);
            TextView titleTextView=(TextView)convertView.findViewById(R.id.goods_item_tilte);
            titleTextView.setText(g.getGoods_name());
            TextView locationTextView=(TextView)convertView.findViewById(R.id.goods_item_location);
            locationTextView.setText(g.getLocate_name());
            TextView goldTextView=(TextView)convertView.findViewById(R.id.goods_item_goldnumber);
            goldTextView.setText(Integer.toString(g.getGold_number()));


            return convertView;
        }

    }
}
