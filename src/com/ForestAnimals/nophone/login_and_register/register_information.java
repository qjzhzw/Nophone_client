package com.ForestAnimals.nophone.login_and_register;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;
import com.ForestAnimals.nophone.R;
import com.ForestAnimals.nophone.util.FileService;
import com.ForestAnimals.nophone.util.MyThread;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2016/7/11.
 */
public class register_information extends Activity {

    FileService service = new FileService();

    public static final int NONE = 0,
            PHOTOHRAPH = 1,// 拍照
            PHOTOZOOM = 2, // 缩放
            PHOTORESOULT = 3;// 结果
    public static final String IMAGE_UNSPECIFIED = "image/*";

    private EditText editText_register_nickname;
    private RadioGroup radioGroup_register_sex;
    private RadioButton radioButton_register_boy,
            radioButton_register_girl;
    private EditText editText_register_birthday,
            editText_register_hobby,
            editText_register_email,
            editText_register_motto;
    private Spinner spinner_register_constellation;
    private ArrayAdapter adapter_constellation;
    private Button button_register_finish;
    private ImageView imageView_register_head;

    String pro_sex;//定义性别变量
    String pro_constellation;//定义星座变量
    String identification;//获取刚注册好的账号

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_information);

        initUI();
        connection();
    }


    private void initUI() {
        editText_register_nickname = (EditText) findViewById(R.id.editText_register_nickname);
        radioGroup_register_sex = (RadioGroup) findViewById(R.id.radioGroup_register_sex);
        radioButton_register_boy = (RadioButton) findViewById(R.id.radioButton_register_boy);
        radioButton_register_girl = (RadioButton) findViewById(R.id.radioButton_register_girl);
        editText_register_birthday = (EditText) findViewById(R.id.editText_register_birthday);
        spinner_register_constellation = (Spinner) findViewById(R.id.spinner_register_constellation);
        editText_register_hobby = (EditText) findViewById(R.id.editText_register_hobby);
        editText_register_email = (EditText) findViewById(R.id.editText_register_email);
        editText_register_motto = (EditText) findViewById(R.id.editText_register_motto);
        button_register_finish = (Button) findViewById(R.id.button_register_finish);
        imageView_register_head = (ImageView) findViewById(R.id.imageView_register_head);

        button_register_finish.setOnClickListener(listener);
        radioGroup_register_sex.setOnCheckedChangeListener(listener_radioGroup);
        spinner_register_constellation.setOnItemSelectedListener(new SpinnerXMLSelectedListener_constellation());

        imageView_register_head.setOnClickListener(new View.OnClickListener() {
            //设置图片的单击事件
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_UNSPECIFIED);
                startActivityForResult(intent, PHOTOZOOM);
            }
        });

        SharedPreferences information = getSharedPreferences("information", 0);
        identification = information.getString("identification", "0");
        //获取刚注册好的账号

    }


    private String[] connect() {
        String url = "information/register_information/";
        //url最后那个‘/’不能少！

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("identification", identification));
        params.add(new BasicNameValuePair("nickname", editText_register_nickname.getText().toString()));
        params.add(new BasicNameValuePair("sex", pro_sex));
        params.add(new BasicNameValuePair("birthday", editText_register_birthday.getText().toString()));
        params.add(new BasicNameValuePair("constellation", pro_constellation));
        params.add(new BasicNameValuePair("hobby", editText_register_hobby.getText().toString()));
        params.add(new BasicNameValuePair("email", editText_register_email.getText().toString()));
        params.add(new BasicNameValuePair("motto", editText_register_motto.getText().toString()));

        params.add(new BasicNameValuePair("head", "111"));
        //*上传头像比较麻烦，需要另外做一下

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
            //各种按钮事件
            Button button = (Button) view;
            Intent intent = new Intent();
            String result[];
            switch (button.getId()) {
                case R.id.button_register_finish:
                    //跳转到登陆界面，并保存个人信息

                    result = connect();

                    if (result[0].equals("error"))
                        Toast.makeText(register_information.this, getString(R.string.check_Internet), Toast.LENGTH_SHORT).show();
                    if (result[0].equals("success")) {
                        Toast.makeText(register_information.this, getString(R.string.register_OK), Toast.LENGTH_SHORT).show();
                        intent.setClass(register_information.this, register_information.class);
                        startActivity(intent);
                    }

                    break;
            }

        }
    };


    private RadioGroup.OnCheckedChangeListener listener_radioGroup = new RadioGroup.OnCheckedChangeListener() {
        //性别的单选框事件
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
            switch (checkedId) {
                //case后不要忘记加break，不然后面的语句都得执行
                case R.id.radioButton_register_boy:
                    //点击汉子后的事件
                    pro_sex = radioButton_register_boy.getText().toString();
                    break;
                case R.id.radioButton_register_girl:
                    //点击妹子后的事件
                    pro_sex = radioButton_register_girl.getText().toString();
                    break;
            }
        }
    };


    class SpinnerXMLSelectedListener_constellation implements AdapterView.OnItemSelectedListener
    //设置星座部分的下拉列表
    {
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
        //下拉列表中选择相应项的结果
        {
            Spinner spinner = (Spinner) parent;
            pro_constellation = (String) spinner.getItemAtPosition(position);
        }

        public void onNothingSelected(AdapterView<?> arg0)
        //下拉列表中取消选择相应项的结果
        {

        }
    }

    public void connection() {
        adapter_constellation = ArrayAdapter.createFromResource(register_information.this, R.array.constellation, android.R.layout.simple_spinner_item);
        //将可选内容与ArrayAdapter连接起来
        spinner_register_constellation.setAdapter(adapter_constellation);
        //将adapter添加到spinner中
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == NONE)
            return;

        if (data == null)
            return;

        // 读取相册缩放图片
        if (requestCode == PHOTOZOOM) {
            startPhotoZoom(data.getData());
        }
        // 处理结果
        if (requestCode == PHOTORESOULT) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0 -
                // 100)压缩文件
                imageView_register_head.setImageBitmap(photo);
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 240);
        intent.putExtra("outputY", 240);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTORESOULT);
    }


}
