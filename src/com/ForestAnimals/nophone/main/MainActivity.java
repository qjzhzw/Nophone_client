package com.ForestAnimals.nophone.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.ForestAnimals.nophone.R;
import com.ForestAnimals.nophone.course.course_fragment;
import com.ForestAnimals.nophone.user.*;
import com.ForestAnimals.nophone.market.market_fragment;
import com.ForestAnimals.nophone.setting.setting_fragment;
import com.ForestAnimals.nophone.tree.tree_fragment;

public class MainActivity extends FragmentActivity implements OnClickListener {
    /**
     * Called when the activity is first created.
     */
    private TextView all_title;
    // 5个tab布局
    private RelativeLayout tree_button, setting_button, market_button, course_button, user_button;
    // 底部标签切换的Fragment
    private Fragment knowFragment;
    private Fragment tree_window_Fragment;
    private Fragment setting_window_Fragment;
    private Fragment market_window_Fragment;
    private Fragment course_window_Fragment;
    private Fragment user_window_Fragment;
    // 底部标签图片
    private ImageView tree_button_image, setting_button_image, market_button_image, course_button_image, user_button_image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initUI();
        initTab();

        tree_button_image.setImageResource(R.drawable.tree_yellow);
        //进入MainActivity后下面第一个图标“树”是被选中的状态
    }


    /**
     * 初始化UI
     */

    private void initUI() {
        tree_button = (RelativeLayout) findViewById(R.id.tab_tree);
        setting_button = (RelativeLayout) findViewById(R.id.tab_setting);
        market_button = (RelativeLayout) findViewById(R.id.tab_market);
        course_button = (RelativeLayout) findViewById(R.id.tab_courses);
        user_button = (RelativeLayout) findViewById(R.id.tab_user);
        tree_button.setOnClickListener(this);
        setting_button.setOnClickListener(this);
        market_button.setOnClickListener(this);
        course_button.setOnClickListener(this);
        user_button.setOnClickListener(this);
        all_title = (TextView) findViewById(R.id.all_title);

        tree_button_image = (ImageView) findViewById(R.id.tab_tree_pic);
        setting_button_image = (ImageView) findViewById(R.id.tab_setting_pic);
        course_button_image = (ImageView) findViewById(R.id.tab_courses_pic);
        market_button_image = (ImageView) findViewById(R.id.tab_market_pic);
        user_button_image = (ImageView) findViewById(R.id.tab_user_pic);

    }

    private void initTab() {
        if (tree_window_Fragment == null) {
            tree_window_Fragment = new tree_fragment();
        }
        if (!tree_window_Fragment.isAdded()) {
            // 提交事务
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_layout, tree_window_Fragment).commit();


            // 记录当前Fragment
            knowFragment = tree_window_Fragment;
            // 设置图片文本的变化
            //knowImg.setImageResource(R.drawable.btn_know_pre);

        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab_tree:// 树
                all_title.setText(R.string.tree_title);
                click_tree_Layout();
                tree_button_image.setImageResource(R.drawable.tree_yellow);
                setting_button_image.setImageResource(R.drawable.setting);
                course_button_image.setImageResource(R.drawable.course);
                market_button_image.setImageResource(R.drawable.market);
                user_button_image.setImageResource(R.drawable.user);
                break;
            case R.id.tab_setting: // 设置
                all_title.setText(R.string.setting_title);
                click_setting_Layout();
                setting_button_image.setImageResource(R.drawable.setting_yellow);
                tree_button_image.setImageResource(R.drawable.tree);
                course_button_image.setImageResource(R.drawable.course);
                market_button_image.setImageResource(R.drawable.market);
                user_button_image.setImageResource(R.drawable.user);
                break;
            case R.id.tab_courses: //课程表
                all_title.setText(R.string.course_title);
                click_course_Layout();
                course_button_image.setImageResource(R.drawable.course_yellow);
                tree_button_image.setImageResource(R.drawable.tree);
                setting_button_image.setImageResource(R.drawable.setting);
                market_button_image.setImageResource(R.drawable.market);
                user_button_image.setImageResource(R.drawable.user);
                break;
            case R.id.tab_market://市场
                all_title.setText(R.string.market_title);
                click_market_Layout();
                market_button_image.setImageResource(R.drawable.market_yellow);
                tree_button_image.setImageResource(R.drawable.tree);
                setting_button_image.setImageResource(R.drawable.setting);
                course_button_image.setImageResource(R.drawable.course);
                user_button_image.setImageResource(R.drawable.user);
                break;
            case R.id.tab_user://用户
                all_title.setText(R.string.user_title);
                click_user_Layout();
                user_button_image.setImageResource(R.drawable.user_yellow);
                tree_button_image.setImageResource(R.drawable.tree);
                setting_button_image.setImageResource(R.drawable.setting);
                course_button_image.setImageResource(R.drawable.course);
                market_button_image.setImageResource(R.drawable.market);
                break;
            default:
                break;
        }

    }

    private void click_tree_Layout() {


        if (tree_window_Fragment == null) {
            tree_window_Fragment = new tree_fragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), tree_window_Fragment);
    }

    private void click_setting_Layout() {
        if (setting_window_Fragment == null) {
            setting_window_Fragment = new setting_fragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), setting_window_Fragment);
    }

    private void click_course_Layout() {
        if (course_window_Fragment == null) {
            course_window_Fragment = new course_fragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), course_window_Fragment);
    }

    private void click_market_Layout() {
        if (market_window_Fragment == null) {
            market_window_Fragment = new market_fragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), market_window_Fragment);
    }

    private void click_user_Layout() {
        if (user_window_Fragment == null) {
            user_window_Fragment = new user_fragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), user_window_Fragment);
    }

    /**
     * 添加或者显示碎片
     *
     * @param transaction
     * @param fragment
     */
    private void addOrShowFragment(FragmentTransaction transaction, Fragment fragment) {
        if (knowFragment == fragment)
            return;

        if (!fragment.isAdded()) { // 如果当前fragment未被添加，则添加到Fragment管理器中
            transaction.hide(knowFragment).add(R.id.content_layout, fragment).commit();
        } else {
            transaction.hide(knowFragment).show(fragment).commit();
        }

        knowFragment = fragment;
    }


    private long exitTime = 0;
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //双击后退会退出程序
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(MainActivity.this, getString(R.string.exit_app), Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                setResult(RESULT_OK);
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}



