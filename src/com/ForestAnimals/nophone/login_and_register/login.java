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
import com.ForestAnimals.nophone.MainActivity;
import com.ForestAnimals.nophone.R;
import com.ForestAnimals.nophone.help;
import com.ForestAnimals.nophone.util.FileService;
import com.ForestAnimals.nophone.util.MyThread;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2016/7/9.
 */
public class login extends Activity {

    FileService service = new FileService();

    private EditText editText_login_identification, editText_login_password;
    private Button button_login, button_login_register, button_login_help;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        initUI();

//        Intent intentmusic = new Intent(getBaseContext(),MusicService.class);
//        intentmusic.putExtra("isPlaying", true);
//        startService(intentmusic);
//        //播放背景音乐

    }


    private void initUI() {
        editText_login_identification = (EditText) findViewById(R.id.editText_login_identification);
        editText_login_password = (EditText) findViewById(R.id.editText_login_password);
        button_login = (Button) findViewById(R.id.button_login);
        button_login_register = (Button) findViewById(R.id.button_login_register);
        button_login_help = (Button) findViewById(R.id.button_login_help);

        button_login.setOnClickListener(listener);
        button_login_register.setOnClickListener(listener);
        button_login_help.setOnClickListener(listener);

        SharedPreferences information = getSharedPreferences("information", 0);
        String identification = information.getString("identification", "0");
        String password = information.getString("password", "0");
        editText_login_identification.setText(identification);
        editText_login_password.setText(password);
        //获取账号密码
    }


    private String[] connect() {
        ProgressDialog progressDialog = new ProgressDialog(login.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("连接中");
        progressDialog.setMessage("正在加载，请稍后");
        progressDialog.show();

        String url = "information/login/";
        //url最后那个‘/’不能少！

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("identification", editText_login_identification.getText().toString()));
        params.add(new BasicNameValuePair("password", editText_login_password.getText().toString()));

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
                case R.id.button_login:
                    //跳转到主界面

                    if (editText_login_identification.getText().toString().length() == 0) {
                        Toast.makeText(login.this, getString(R.string.no_identfication), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if(editText_login_password.getText().toString().length()==0)
                    {
                        Toast.makeText(login.this, getString(R.string.no_password), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    else {
                        result = connect();

                        if (result[0].equals("failed"))
                            Toast.makeText(login.this, getString(R.string.check_Internet), Toast.LENGTH_SHORT).show();
                        if (result[0].equals("not_existed"))
                            Toast.makeText(login.this, getString(R.string.not_existed), Toast.LENGTH_SHORT).show();
                        if (result[0].equals("not_matched"))
                            Toast.makeText(login.this, getString(R.string.error_password), Toast.LENGTH_SHORT).show();
                        if (result[0].equals("success")) {
                            SharedPreferences information = getSharedPreferences("information", 0);
                            information.edit().
                                    putString("identification", editText_login_identification.getText().toString()).
                                    putString("password", editText_login_password.getText().toString()).
                                    apply();
                            //保存用户名和密码到本地

                            Toast.makeText(login.this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                            intent.setClass(login.this, MainActivity.class);
                            startActivity(intent);
                        }

                        break;
                    }
                case R.id.button_login_register:
                    //跳转到注册界面
                    intent.setClass(login.this, register.class);
                    startActivity(intent);
                    break;
                case R.id.button_login_help:
                    //跳转到帮助界面
                    intent.setClass(login.this, help.class);
                    startActivity(intent);
                    break;
            }
        }
    };

}