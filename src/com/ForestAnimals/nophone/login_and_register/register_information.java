package com.ForestAnimals.nophone.login_and_register;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;
import com.ForestAnimals.nophone.R;
import com.ForestAnimals.nophone.util.FileService;

import java.io.ByteArrayOutputStream;

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

    String pro_constellation;//定义星座变量
    String pro_sex;//定义性别变量

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

    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //各种按钮事件
            Button button = (Button) view;
            Intent intent = new Intent();
            switch (button.getId()) {
                case R.id.button_register_finish:
                    //跳转到登陆界面，并保存个人信息

                    //

                    intent.setClass(register_information.this, login.class);
                    startActivity(intent);
                    break;
            }

        }
    };


    private RadioGroup.OnCheckedChangeListener listener_radioGroup = new RadioGroup.OnCheckedChangeListener() {
        //性别的单选框事件
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
            switch (checkedId) {
                case R.id.radioButton_register_boy:
                    //点击汉子后的事件
                    pro_sex = radioButton_register_boy.getText().toString();
                case R.id.radioButton_register_girl:
                    //点击妹子后的事件
                    pro_sex = radioButton_register_girl.getText().toString();
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

    public void connection() {
        adapter_constellation = ArrayAdapter.createFromResource(register_information.this, R.array.constellation, android.R.layout.simple_spinner_item);
        //将可选内容与ArrayAdapter连接起来
        spinner_register_constellation.setAdapter(adapter_constellation);
        //将adapter添加到spinner中
    }


}
