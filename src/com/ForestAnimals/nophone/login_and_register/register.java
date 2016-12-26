package com.ForestAnimals.nophone.login_and_register;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.*;
import com.ForestAnimals.nophone.R;
import com.ForestAnimals.nophone.util.FileService;
import com.ForestAnimals.nophone.util.MyThread;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by dell on 2016/7/11.
 */
public class register extends Activity {

    FileService service = new FileService();

    private EditText editText_register_identification,
            editText_register_password,
            editText_register_password_again,
            editText_register_vertification;
    private Button button_register_nextstep,
            button_register_vertification;
    private CheckBox checkBox_register_password;

    Random random = new Random();
    int[] n = new int[6];
    String n_str = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        initUI();//初始化UI
    }


    private void initUI() {
        editText_register_identification = (EditText) findViewById(R.id.editText_register_identification);
        editText_register_password = (EditText) findViewById(R.id.editText_register_password);
        editText_register_password_again = (EditText) findViewById(R.id.editText_register_password_again);
        editText_register_vertification = (EditText) findViewById(R.id.editText_register_vertification);
        button_register_nextstep = (Button) findViewById(R.id.button_register_nextstep);
        button_register_vertification = (Button) findViewById(R.id.button_register_vertification);
        checkBox_register_password = (CheckBox) findViewById(R.id.checkBox_register_password);

        button_register_nextstep.setOnClickListener(listener);
        button_register_vertification.setOnClickListener(listener);
        checkBox_register_password.setOnCheckedChangeListener(listener2);

        for (int i = 0; i < 6; i++) {
            n[i] = random.nextInt(9);
            n_str += n[i];
        }
    }


    private String[] connect() {
        String url = "information/register/";
        //url最后那个‘/’不能少！

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("identification", editText_register_identification.getText().toString()));
        params.add(new BasicNameValuePair("password", editText_register_password.getText().toString()));

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
                case R.id.button_register_nextstep:
                    //跳转到下一个注册界面，并保存个人信息
                    if (editText_register_identification.getText().toString().length() == 0) {
                        Toast.makeText(register.this, getString(R.string.no_identfication), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (editText_register_password.getText().toString().length() == 0) {
                        Toast.makeText(register.this, getString(R.string.no_password), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (editText_register_password_again.getText().toString().length() == 0) {
                        Toast.makeText(register.this, getString(R.string.no_password_again), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (!editText_register_password.getText().toString().equals(editText_register_password_again.getText().toString())) {
                        Toast.makeText(register.this, getString(R.string.not_consistent_password), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (editText_register_vertification.getText().toString().length() == 0) {
                        Toast.makeText(register.this, getString(R.string.no_vertification), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (!editText_register_vertification.getText().toString().equals(n_str)) {
                        Toast.makeText(register.this, getString(R.string.error_vertification), Toast.LENGTH_SHORT).show();
                        break;
                    } else {
                        result = connect();

                        if (result[0].equals("failed"))
                            Toast.makeText(register.this, getString(R.string.check_Internet), Toast.LENGTH_SHORT).show();
                        if (result[0].equals("registered"))
                            Toast.makeText(register.this, getString(R.string.register_used), Toast.LENGTH_SHORT).show();
                        if (result[0].equals("success")) {
                            SharedPreferences information = getSharedPreferences("information", 0);
                            information.edit().
                                    putString("identification", editText_register_identification.getText().toString()).
                                    putString("password", editText_register_password.getText().toString()).
                                    apply();
                            //保存用户名和密码到本地

                            Toast.makeText(register.this, getString(R.string.register_success), Toast.LENGTH_SHORT).show();
                            intent.setClass(register.this, register_information.class);
                            startActivity(intent);
                        }

                        break;
                    }
                case R.id.button_register_vertification:
                    //获取验证码
                    Toast.makeText(register.this, getString(R.string.your_vertification) + n_str, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };


    private CheckBox.OnCheckedChangeListener listener2 = new CheckBox.OnCheckedChangeListener() {
        //密码加密解密
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                register.this.editText_register_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                register.this.editText_register_password_again.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                register.this.editText_register_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                register.this.editText_register_password_again.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    };

}
