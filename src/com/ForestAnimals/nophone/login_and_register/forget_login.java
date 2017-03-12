package com.ForestAnimals.nophone.login_and_register;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.ForestAnimals.nophone.main.MainActivity;
import com.ForestAnimals.nophone.R;
import com.ForestAnimals.nophone.main.help;
import com.ForestAnimals.nophone.service.FileService;
import com.ForestAnimals.nophone.util.MyThread;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2016/7/9.
 */
public class forget_login extends Activity {

    FileService service = new FileService();

    private EditText editText_forget_identification, editText_forget_vertification;
    private Button button_forget_vertification, button_login;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_login);

        initUI();

    }


    private void initUI() {
        editText_forget_identification = (EditText) findViewById(R.id.editText_forget_identification);
        editText_forget_vertification = (EditText) findViewById(R.id.editText_forget_vertification);
        button_forget_vertification = (Button) findViewById(R.id.button_forget_vertification);
        button_login = (Button) findViewById(R.id.button_login);

        button_forget_vertification.setOnClickListener(listener);
        button_login.setOnClickListener(listener);
    }


    private String[] connect() {
        String url = "information/forget_login/";
        //url最后那个‘/’不能少！

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("identification", editText_forget_identification.getText().toString()));
        params.add(new BasicNameValuePair("vertification", editText_forget_vertification.getText().toString()));

        MyThread myThread = new MyThread(params, url);
        myThread.start();
        while (!myThread.isDone()) {
        }

        String[] parse_key = {"status"};
        //需要解析的关键词

        return myThread.parseJson(parse_key);
    }


    private String[] connect_vertification() {
        String url = "information/vertification/";
        //url最后那个‘/’不能少！

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("identification", editText_forget_identification.getText().toString()));

        MyThread myThread = new MyThread(params, url);
        myThread.start();
        while (!myThread.isDone()) {
        }

        String[] parse_key = {"status"};
        //需要解析的关键词

        return myThread.parseJson(parse_key);
    }


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Button button = (Button) view;
            Intent intent = new Intent();
            String result[];
            switch (button.getId()) {
                case R.id.button_forget_vertification:
                    //发送验证码
                    result = connect_vertification();

                    if (result[0].equals("failed"))
                        Toast.makeText(forget_login.this, getString(R.string.check_Internet), Toast.LENGTH_SHORT).show();
                    if (result[0].equals("success")) {
                        Toast.makeText(forget_login.this, getString(R.string.vertification_success), Toast.LENGTH_SHORT).show();
                        break;
                    }
                case R.id.button_login:
                    //跳转到注册界面
                    result = connect();

                    if (result[0].equals("failed"))
                        Toast.makeText(forget_login.this, getString(R.string.check_Internet), Toast.LENGTH_SHORT).show();
                    if (result[0].equals("not_existed"))
                        Toast.makeText(forget_login.this, getString(R.string.not_existed), Toast.LENGTH_SHORT).show();
                    if (result[0].equals("error"))
                        Toast.makeText(forget_login.this, getString(R.string.error_vertification), Toast.LENGTH_SHORT).show();
                    if (result[0].equals("success")) {
                        Toast.makeText(forget_login.this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                        intent.setClass(forget_login.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    }
            }
        }
    };

}