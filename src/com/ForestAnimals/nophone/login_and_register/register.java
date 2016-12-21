package com.ForestAnimals.nophone.login_and_register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import com.ForestAnimals.nophone.R;
import com.ForestAnimals.nophone.Service.FileService;

import java.io.OutputStream;
import java.util.Random;

/**
 * Created by dell on 2016/7/11.
 */
public class register extends Activity {

    FileService service = new FileService();

    private EditText editText_register_identification;
    private EditText editText_register_password;
    private EditText editText_register_password_again;
    private EditText editText_register_vertification;
    private Button button_register_nextstep;
    private Button button_register_vertification;
    private CheckBox checkBox_register_password;

    Random random = new Random();
    int n1 = random.nextInt(9);
    int n2 = random.nextInt(9);
    int n3 = random.nextInt(9);
    int n4 = random.nextInt(9);
    int n5 = random.nextInt(9);
    int n6 = random.nextInt(9);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        initUI();
        //
    }

    /**
     * 初始化UI
     */
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

    }


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Button button = (Button) view;
            Intent intent = new Intent();
            switch (button.getId()) {
                case R.id.button_register_nextstep:
                    //跳转到登陆界面，并保存个人信息
                    if (editText_register_identification.getText().toString().length() == 0) {
                        Toast.makeText(register.this,  getString(R.string.no_identfication), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (editText_register_password.getText().toString().length() == 0) {
                        Toast.makeText(register.this,  getString(R.string.no_password), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (editText_register_password_again.getText().toString().length() == 0) {
                        Toast.makeText(register.this,  getString(R.string.no_password_again), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (!editText_register_password.getText().toString().equals(editText_register_password_again.getText().toString())) {
                        Toast.makeText(register.this,  getString(R.string.error_password), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (editText_register_vertification.getText().toString().length() == 0) {
                        Toast.makeText(register.this,  getString(R.string.no_vertification), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (Integer.parseInt(editText_register_vertification.getText().toString()) != (100000 * n1 + 10000 * n2 + 1000 * n3 + 100 * n4 + 10 * n5 + 1 * n6)) {
                        Toast.makeText(register.this,  getString(R.string.error_vertification), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    else {

                        try {
                            OutputStream outStream_user_identification = register.this.openFileOutput("editText_user_identification", MODE_PRIVATE);
                            OutputStream outStream_user_password = register.this.openFileOutput("editText_user_password", MODE_PRIVATE);
                            service.save(outStream_user_identification, editText_register_identification.getText().toString());
                            service.save(outStream_user_password, editText_register_password.getText().toString());
                            //保存信息
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    intent.setClass(register.this, register_information.class);
                    startActivity(intent);
                    break;
                case R.id.button_register_vertification:
                    //获取验证码
                {
                    Toast.makeText(register.this, getString(R.string.your_vertification) + n1 + n2 + n3 + n4 + n5 + n6, Toast.LENGTH_LONG).show();
                    break;
                }
            }
        }
    };


    private CheckBox.OnCheckedChangeListener listener2 = new CheckBox.OnCheckedChangeListener() {
        //密码加密解密
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked)
                register.this.editText_register_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            else
                register.this.editText_register_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    };


    private long exitTime = 0;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //双击后退会退出程序
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(register.this, getString(R.string.exit_app), Toast.LENGTH_SHORT).show();
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
