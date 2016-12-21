package com.ForestAnimals.nophone.util;

import android.app.Activity;
import android.view.KeyEvent;
import android.widget.Toast;
import com.ForestAnimals.nophone.R;

/**
 * Created by dell on 2016/12/21.
 */
public class Exit {

    private long exitTime = 0;
    public boolean onKeyDown(int keyCode, KeyEvent event,Activity activity) {
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
