package com.ForestAnimals.nophone.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.ForestAnimals.nophone.R;
import com.ForestAnimals.nophone.util.FileService;

import java.io.ByteArrayOutputStream;

/**
 * Created by MyWorld on 2016/6/12.
 */
public class user_fragment extends Fragment {

    FileService service = new FileService();

    public static final int NONE = 0;
    public static final int PHOTOHRAPH = 1;// 拍照
    public static final int PHOTOZOOM = 2; // 缩放
    public static final int PHOTORESOULT = 3;// 结果
    public static final String IMAGE_UNSPECIFIED = "image/*";

    private ImageView imageView_user_head;
    private Button button_user_edit;
    private Button button_user_save;
    private Button button_user_cancel;
    private EditText editText_user_nickname;
    private ImageView imageView_user_sex;
    private EditText editText_user_birthday;
    private EditText editText_user_constellation;
    private Spinner spinner_user_constellation;
    private ArrayAdapter adapter_constellation;
    private EditText editText_user_hobby;
    private EditText editText_user_email;
    private EditText editText_user_motto;
    private TextView textView_user_level;
    private TextView textView_user_money;
    private TextView textView_user_experience;

    String pro_constellation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_layout, container,
                false);

        //声明ID集合
        imageView_user_head = (ImageView) view.findViewById(R.id.imageView_user_head);
        imageView_user_sex = (ImageView) view.findViewById(R.id.imageView_user_sex);
        button_user_edit = (Button) view.findViewById(R.id.button_user_edit);
        button_user_save = (Button) view.findViewById(R.id.button_user_save);
        button_user_cancel = (Button) view.findViewById(R.id.button_user_cancel);
        editText_user_nickname = (EditText) view.findViewById(R.id.editText_user_nickname);
        editText_user_motto = (EditText) view.findViewById(R.id.editText_user_motto);
        editText_user_birthday = (EditText) view.findViewById(R.id.editText_user_birthday);
        editText_user_constellation = (EditText) view.findViewById(R.id.editText_user_constellation);
        spinner_user_constellation = (Spinner) view.findViewById(R.id.spinner_user_constellation);
        editText_user_hobby = (EditText) view.findViewById(R.id.editText_user_hobby);
        editText_user_email = (EditText) view.findViewById(R.id.editText_user_email);

        enabled_false();//让所有文本框无法编辑
        setAction();//设置事件
        connection();//将下拉框和选项连接起来
        setText();//判断如果为空，设置初始昵称和签名

        SharedPreferences information = getActivity().getSharedPreferences("information", 0);
        String identification = information.getString("identification", "0");
        editText_user_nickname.setText(identification);
        //获取账号密码

        button_user_save.setVisibility(View.INVISIBLE);
        button_user_cancel.setVisibility(View.INVISIBLE);
        //设置“保存”和“取消编辑”按钮不可见

        return view;
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Button button = (Button) view;
            switch (button.getId()) {
                case R.id.button_user_edit:
                    //进入可编辑状态
                    enabled_true();
                    editText_user_constellation.setVisibility(View.INVISIBLE);
                    spinner_user_constellation.setVisibility(View.VISIBLE);
                    button_user_save.setVisibility(View.VISIBLE);
                    button_user_cancel.setVisibility(View.VISIBLE);
                    break;
                case R.id.button_user_cancel:
                    //返回不可编辑状态
                    enabled_false();
                    editText_user_constellation.setVisibility(View.VISIBLE);
                    spinner_user_constellation.setVisibility(View.INVISIBLE);
                    button_user_save.setVisibility(View.INVISIBLE);
                    button_user_cancel.setVisibility(View.INVISIBLE);
                    break;
                case R.id.button_user_save:
                    enabled_false();
                    break;
            }
        }
    };


    class SpinnerXMLSelectedListener_constellation implements AdapterView.OnItemSelectedListener
            //设置星座部分的下拉列表
    {
        public void onItemSelected(AdapterView<?> parent, View view,int position, long id)
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
                imageView_user_head.setImageBitmap(photo);
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

    public void enabled_false()
    //设置文本框不可编辑
    {
        editText_user_nickname.setEnabled(false);
        editText_user_motto.setEnabled(false);
        editText_user_birthday.setEnabled(false);
        editText_user_constellation.setEnabled(false);
        spinner_user_constellation.setEnabled(false);
        editText_user_hobby.setEnabled(false);
        editText_user_email.setEnabled(false);
        imageView_user_head.setEnabled(false);
    }

    public void enabled_true()
    //设置文本框可编辑
    {
        editText_user_nickname.setEnabled(true);
        editText_user_motto.setEnabled(true);
        editText_user_birthday.setEnabled(true);
        editText_user_constellation.setEnabled(true);
        spinner_user_constellation.setEnabled(true);
        editText_user_hobby.setEnabled(true);
        editText_user_email.setEnabled(true);
        imageView_user_head.setEnabled(true);
    }



    public void setText()
    //设置初始昵称和签名
    {
        if (editText_user_nickname.getText().toString().length() == 0) {
            editText_user_nickname.setText(getString(R.string.my_nickname));
        }
        if (editText_user_motto.getText().toString().length() == 0) {
            editText_user_motto.setText(getString(R.string.no_motto));
        }
    }


    public void connection() {
        adapter_constellation = ArrayAdapter.createFromResource(getActivity(), R.array.constellation, android.R.layout.simple_spinner_item);
        //将可选内容与ArrayAdapter连接起来
        spinner_user_constellation.setAdapter(adapter_constellation);
        //将adapter添加到spinner中
    }


    public void setAction()
    //设置事件集合
    {
        button_user_edit.setOnClickListener(listener);
        button_user_save.setOnClickListener(listener);
        button_user_cancel.setOnClickListener(listener);
        spinner_user_constellation.setOnItemSelectedListener(new SpinnerXMLSelectedListener_constellation());


        imageView_user_head.setOnClickListener(new View.OnClickListener() {
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

}
