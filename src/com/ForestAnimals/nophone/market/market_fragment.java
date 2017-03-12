package com.ForestAnimals.nophone.market;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.ForestAnimals.nophone.R;
import com.ForestAnimals.nophone.user.Person;
import com.ForestAnimals.nophone.util.MyThread;
import com.bumptech.glide.Glide;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MyWorld on 2016/6/12.
 */
public class market_fragment extends Fragment {
    private String[] result;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView textView_market_money,
            textView_market_experience,
            textView_market_level,
            textView_market_nickname;
    private String identification;
    private ImageView imageView_user_head;
    private Fragment good_list;
    private FragmentManager fm;

    public void onCreate(Bundle saveInstance) {
        super.onCreate(saveInstance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.market_layout, container,
                false);
        fm = getActivity().getSupportFragmentManager();
         good_list = fm.findFragmentById(R.id.goods_Fragment_layout);
        if (good_list == null) {
            good_list = new goodsListFragment();
            fm.beginTransaction()
                    .add(R.id.goods_Fragment_layout, good_list)
                    .commit();
        }

        textView_market_experience = (TextView) view.findViewById(R.id.textView_market_experience);
        textView_market_level = (TextView) view.findViewById(R.id.textView_market_level);
        textView_market_money = (TextView) view.findViewById(R.id.textView_market_money);
        textView_market_nickname = (TextView) view.findViewById(R.id.textView_market_nickname);
        imageView_user_head = (ImageView) view.findViewById(R.id.imageView_user_head);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.market_swiper);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),getResources().getColor(android.R.color.darker_gray),
                getResources().getColor(android.R.color.holo_orange_light));
        SharedPreferences information = getActivity().getSharedPreferences("information", 0);
        identification = information.getString("identification", "0");
        //获取账号

         result = connect();
        textView_market_nickname.setText(result[0]);
        textView_market_level.setText(result[2]);
        textView_market_money.setText(result[3]);
        textView_market_experience.setText("" + new Person().getExperience_remained
                (Integer.parseInt(result[2]), Integer.parseInt(result[4])));
        Glide.with(getActivity()).load(result[1]).into(imageView_user_head);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        result = connect();
                        textView_market_nickname.setText(result[0]);
                        textView_market_level.setText(result[2]);
                        textView_market_money.setText(result[3]);
                        textView_market_experience.setText("" + new Person().getExperience_remained
                                (Integer.parseInt(result[2]), Integer.parseInt(result[4])));
                        Glide.with(getActivity()).load(result[1]).into(imageView_user_head);
                        good_list = new goodsListFragment();
                        fm.beginTransaction()
                                .add(R.id.goods_Fragment_layout, good_list)
                                .commit();

                    }
                },3000);
            }
        });

        return view;
    }

    private String[] connect() {
        String url = "information/market_information/";
        //url最后那个‘/’不能少！

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("identification", identification));

        MyThread myThread = new MyThread(params, url);
        myThread.start();
        while (!myThread.isDone()) {
        }

        String[] parse_key = {"nickname", "head", "level", "money", "experience"};
        //需要解析的关键词

        return myThread.parseJson(parse_key);
    }


}


