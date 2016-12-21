package com.ForestAnimals.nophone.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ForestAnimals.nophone.R;
import com.ForestAnimals.nophone.Service.FileService;
import com.ForestAnimals.nophone.goods.goodsListFragment;

import java.io.InputStream;

/**
 * Created by MyWorld on 2016/6/12.
 */
public class market_fragment extends Fragment {

    FileService service=new FileService();

    private TextView market_user_name;

    public void onCreate(Bundle saveInstance) {
        super.onCreate(saveInstance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.market_layout, container,
                false);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        Fragment good_list = fm.findFragmentById(R.id.goods_Fragment_layout);
        if (good_list == null) {
            good_list = new goodsListFragment();
            fm.beginTransaction()
                    .add(R.id.goods_Fragment_layout, good_list)
                    .commit();
        }

        market_user_name=(TextView)view.findViewById(R.id.textView_market_user_name);
        read_market();

        return view;
    }

    public void read_market()
    //读取信息
    {
        try {
            InputStream inStream_user_nickname = getActivity().openFileInput("editText_user_nickname");
            market_user_name.setText(service.read(inStream_user_nickname));
            //读取信息
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


