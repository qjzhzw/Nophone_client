package com.ForestAnimals.nophone.util;

import android.app.Activity;
import android.view.KeyEvent;
import android.widget.Toast;
import com.ForestAnimals.nophone.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dell on 2017/1/4.
 */
public class Util {

    /**
     * 判断型正则表达式
     * @param input
     * @param pattern
     * @return
     */
    public boolean regex(String input, String pattern) {

        Pattern r = Pattern.compile(pattern);
        //创建 Pattern对象（匹配模式）

        Matcher m = r.matcher(input);
        //创建 matcher对象

        if (m.find()) {
            return true;
        } else {
            return false;
        }
    }


    private long exitTime = 0;

    public boolean onKeyDown(int keyCode, KeyEvent event, Activity activity) {
        //双击后退会退出程序
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(activity, activity.getString(R.string.exit_app), Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                activity.setResult(activity.RESULT_OK);
                activity.finish();
                System.exit(0);
            }
            return true;
        }
        return activity.onKeyDown(keyCode, event);
    }

}
