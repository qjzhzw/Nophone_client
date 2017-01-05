package com.ForestAnimals.nophone.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.ForestAnimals.nophone.R;

public class test extends Activity {
    /**
     * Called when the activity is first created.
     */

    private Button button_test;
    private TextView textView_test;
    private EditText editText_test;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        button_test = (Button) findViewById(R.id.button_test);
        textView_test = (TextView) findViewById(R.id.textView_test);
        editText_test = (EditText) findViewById(R.id.editText_test);
        button_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView_test.setText("111");
            }
        });

    }

}