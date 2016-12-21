package com.ForestAnimals.nophone.tree;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.ForestAnimals.nophone.R;

/**
 * Created by dell on 2016/8/11.
 */
public class tree_news extends Activity {
    /**
     * Called when the activity is first created.
     */

    private Button button_news_cancel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tree_news);

        initUI();
    }


    /**
     * 初始化UI
     */
    private void initUI() {
        button_news_cancel = (Button) findViewById(R.id.button_news_cancel);
        button_news_cancel.setOnClickListener(listener);
    }


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Button button = (Button) view;
            switch (button.getId()) {
                case R.id.button_news_cancel:
                    //取消推送
                    finish();
                    break;
            }
        }
    };

}