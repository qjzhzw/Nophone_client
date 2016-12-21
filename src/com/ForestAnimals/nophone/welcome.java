package com.ForestAnimals.nophone;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.*;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.ForestAnimals.nophone.login_and_register.login;

import java.util.ArrayList;
/*	@descript 实现滑动切换图片带点点
 *  @Date 2014-8-4
 *  @come：http://www.cnblogs.com/tinyphp/p/3892936.html
 */

public class welcome extends Activity {
    private ViewPager viewPager;
    private ArrayList<View> pageview;

    private Button button_welcome;
    private ImageView imageView;
    private ImageView[] imageViews;
    //包裹点点的LinearLayout
    private ViewGroup group;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.welcome);

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        //查找布局文件用LayoutInflater.inflate
        LayoutInflater inflater =getLayoutInflater();
        View view1 = inflater.inflate(R.layout.item01, null);
        View view2 = inflater.inflate(R.layout.item02, null);
        View view3 = inflater.inflate(R.layout.item03, null);
        View view4 = inflater.inflate(R.layout.item04, null);

        //将view装入数组
        pageview =new ArrayList<View>();
        pageview.add(view1);
        pageview.add(view2);
        pageview.add(view3);
        pageview.add(view4);


        button_welcome=(Button)view4.findViewById(R.id.button_welcome);
        //监听到按钮在第四个界面中
        button_welcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(welcome.this, login.class);
                startActivity(intent);

                SharedPreferences pref = getSharedPreferences("myActivityName", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("isFirstIn", false);
                editor.commit();
                //记录下已经登陆过了
            }
        });


        group = (ViewGroup)findViewById(R.id.viewGroup);

        //有多少张图就有多少个点点
        imageViews = new ImageView[pageview.size()];
        for(int i =0;i<pageview.size();i++){
            imageView = new ImageView(welcome.this);
            imageView.setLayoutParams(new LayoutParams(20,20));
            imageView.setPadding(20, 0, 20, 0);
            imageViews[i] = imageView;

            //默认第一张图显示为选中状态
            if (i == 0) {
                imageViews[i].setBackgroundResource(R.drawable.page_indicator_focused);
            } else {
                imageViews[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            }

            group.addView(imageViews[i]);
        }

        //绑定适配器
        viewPager.setAdapter(mPagerAdapter);
        //绑定监听事件
        viewPager.setOnPageChangeListener(new GuidePageChangeListener());


        SharedPreferences();
        //判断是否是第一次登陆
    }

    //数据适配器
    PagerAdapter mPagerAdapter = new PagerAdapter(){
        @Override
        //获取当前窗体界面数
        public int getCount() {
            // TODO Auto-generated method stub
            return pageview.size();
        }

        @Override
        //断是否由对象生成界面
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0==arg1;
        }
        //是从ViewGroup中移出当前View
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(pageview.get(arg1));
        }

        //返回一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
        public Object instantiateItem(View arg0, int arg1){
            ((ViewPager)arg0).addView(pageview.get(arg1));
            return pageview.get(arg1);
        }


    };



    //pageView监听器
    class GuidePageChangeListener implements OnPageChangeListener{

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        //如果切换了，就把当前的点点设置为选中背景，其他设置未选中背景
        public void onPageSelected(int arg0) {
            // TODO Auto-generated method stub
            for(int i=0;i<imageViews.length;i++){
                imageViews[arg0].setBackgroundResource(R.drawable.page_indicator_focused);
                if (arg0 != i) {
                    imageViews[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
                }
            }

        }
    }



    private void SharedPreferences()
    {
        //判断是否是第一次登陆，如果是，则跳过开场动画，直接进入登陆界面
        Boolean isFirstIn = false;
        SharedPreferences pref = getSharedPreferences("myActivityName", 0);
        //取得相应的值，如果没有该值，说明还未写入，用true作为默认值
        isFirstIn = pref.getBoolean("isFirstIn", true);
        if(isFirstIn==false)
        {
            Intent intent = new Intent();
            intent.setClass(welcome.this, login.class);
            startActivity(intent);
        }
    }


    private long exitTime = 0;
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //双击后退会退出程序
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(welcome.this, getString(R.string.exit_app), Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
