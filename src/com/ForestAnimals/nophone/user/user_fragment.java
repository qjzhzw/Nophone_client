package com.ForestAnimals.nophone.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.ForestAnimals.nophone.R;
import com.ForestAnimals.nophone.service.FileService;
import com.ForestAnimals.nophone.util.MyThread;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MyWorld on 2016/6/12.
 */
public class user_fragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    FileService service = new FileService();

    public static final int NONE = 0;
    public static final int PHOTOHRAPH = 1;// 拍照
    public static final int PHOTOZOOM = 2; // 缩放
    public static final int PHOTORESOULT = 3;// 结果
    public static final String IMAGE_UNSPECIFIED = "image/*";

    private ImageView imageView_user_head,
            imageView_user_sex;
    private Button button_user_edit,
            button_user_save,
            button_user_cancel;
    private EditText editText_user_nickname,
            editText_user_birthday,
            editText_user_hobby,
            editText_user_email,
            editText_user_motto;
    private TextView textView_user_level,
            textView_user_money,
            textView_user_experience;
    private Spinner spinner_user_constellation;
    private ArrayAdapter adapter_constellation;

    String pro_sex;
    String pro_constellation;
    String identification;
    String result[];

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
        spinner_user_constellation = (Spinner) view.findViewById(R.id.spinner_user_constellation);
        editText_user_hobby = (EditText) view.findViewById(R.id.editText_user_hobby);
        editText_user_email = (EditText) view.findViewById(R.id.editText_user_email);
        textView_user_level = (TextView) view.findViewById(R.id.textView_user_level);
        textView_user_money = (TextView) view.findViewById(R.id.textView_user_money);
        textView_user_experience = (TextView) view.findViewById(R.id.textView_user_experience);

        enabled_false();//让所有文本框无法编辑
        setAction();//设置事件
        connection();//将下拉框和选项连接起来
        setText();//判断如果为空，设置初始昵称和签名

        SharedPreferences information = getActivity().getSharedPreferences("information", 0);
        identification = information.getString("identification", "0");
        //获取账号密码

        init();

        return view;
    }

    private void init() {
        result = connect();

        editText_user_nickname.setText(result[0]);
        editText_user_birthday.setText(result[2]);
        editText_user_hobby.setText(result[4]);
        editText_user_email.setText(result[5]);
        editText_user_motto.setText(result[6]);
        textView_user_level.setText(result[8]);
        textView_user_money.setText(result[9]);
        textView_user_experience.setText(result[10]);
        if (result[1].equals("汉子")) {
            imageView_user_sex.setImageResource(R.drawable.user_boy);
            pro_sex = "汉子";
        }
        if (result[1].equals("妹子")) {
            imageView_user_sex.setImageResource(R.drawable.user_girl);
            pro_sex = "妹子";
        }

        HashMap<Object, Integer> constellation = new HashMap<Object, Integer>();
        //将从服务器获取的星座名字和数字相对应
        for (int i = 0; i < 12; i++)
            constellation.put(this.getResources().getStringArray(R.array.constellation)[i], i);
        spinner_user_constellation.setSelection(constellation.get(result[3]));

        button_user_save.setVisibility(View.INVISIBLE);
        button_user_cancel.setVisibility(View.INVISIBLE);
        //设置“保存”和“取消编辑”按钮不可见
    }

    private String[] connect() {
        String url = "information/user/";
        //url最后那个‘/’不能少！

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("identification", identification));

        MyThread myThread = new MyThread(params, url);
        myThread.start();
        while (!myThread.isDone()) {
        }

        String[] parse_key = {
                "nickname", "sex", "birthday",
                "constellation", "hobby",
                "email", "motto", "head",
                "level", "money", "experience"};
        //需要解析的关键词

        return myThread.parseJson(parse_key);
    }


    private String[] connect_save() {
        String url = "information/register_information/";
        //url最后那个‘/’不能少！

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("identification", identification));
        params.add(new BasicNameValuePair("nickname", editText_user_nickname.getText().toString()));
        params.add(new BasicNameValuePair("sex", pro_sex));
        params.add(new BasicNameValuePair("birthday", editText_user_birthday.getText().toString()));
        params.add(new BasicNameValuePair("constellation", pro_constellation));
        params.add(new BasicNameValuePair("hobby", editText_user_hobby.getText().toString()));
        params.add(new BasicNameValuePair("email", editText_user_email.getText().toString()));
        params.add(new BasicNameValuePair("motto", editText_user_motto.getText().toString()));

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
            Button button = (Button) view;
            switch (button.getId()) {
                case R.id.button_user_edit:
                    //进入可编辑状态
                    enabled_true();
                    button_user_save.setVisibility(View.VISIBLE);
                    button_user_cancel.setVisibility(View.VISIBLE);
                    break;
                case R.id.button_user_cancel:
                    //返回不可编辑状态
                    enabled_false();
                    button_user_save.setVisibility(View.INVISIBLE);
                    button_user_cancel.setVisibility(View.INVISIBLE);
                    break;
                case R.id.button_user_save:

                    result = connect_save();

                    if (result[0].equals("error"))
                        Toast.makeText(getActivity(), getString(R.string.check_Internet), Toast.LENGTH_SHORT).show();
                    if (result[0].equals("success")) {
                        Toast.makeText(getActivity(), getString(R.string.save_success), Toast.LENGTH_SHORT).show();
                    }

                    enabled_false();
                    break;
            }
        }
    };

    @Override
    public void onRefresh() {
        //这里写下拉刷新的事件
    }


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
                photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0 - 100)压缩文件
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
