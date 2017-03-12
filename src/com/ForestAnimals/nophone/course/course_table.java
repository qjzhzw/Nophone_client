package com.ForestAnimals.nophone.course;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.ForestAnimals.nophone.R;
import com.ForestAnimals.nophone.login_and_register.login;
import com.ForestAnimals.nophone.util.MyThread;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class course_table extends Activity {

    public static JSONArray json_lesson_time,
            json_lesson_start,
            json_lesson_week,
            json_lesson_classroom,
            json_lesson_name,
            json_lesson_teacher;

    public static String[] lesson_time,
            lesson_classroom,
            lesson_name,
            lesson_teacher;
    public static int[] lesson_start,
            lesson_week;

    public static String account;
    public static String password;
    public static final int NONE = 0;
    public static final int PHOTOHRAPH = 1;// 拍照
    public static final int PHOTOZOOM = 2; // 缩放
    public static final int PHOTORESOULT = 3;// 结果
    public static final String IMAGE_UNSPECIFIED = "image/*";
    private Button button_course_change;
    private ScrollView scrollView_course;

    //颜色的集合��
    private int colors[] = {
            Color.rgb(0xee, 0xff, 0xff),
            Color.rgb(0xf0, 0x96, 0x09),
            Color.rgb(0x8c, 0xbf, 0x26),
            Color.rgb(0x00, 0xab, 0xa9),
            Color.rgb(0x99, 0x6c, 0x33),
            Color.rgb(0x3b, 0x92, 0xbc),
            Color.rgb(0xd5, 0x4d, 0x34),
            Color.rgb(0xcc, 0xcc, 0xcc),
            Color.TRANSPARENT//序号8为透明
    };

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_table);
        Intent get_transfer = getIntent();
        account = get_transfer.getStringExtra("account");
        password = get_transfer.getStringExtra("password");
        //分别表示周一到周日�����
        LinearLayout ll1 = (LinearLayout) findViewById(R.id.ll1);
        LinearLayout ll2 = (LinearLayout) findViewById(R.id.ll2);
        LinearLayout ll3 = (LinearLayout) findViewById(R.id.ll3);
        LinearLayout ll4 = (LinearLayout) findViewById(R.id.ll4);
        LinearLayout ll5 = (LinearLayout) findViewById(R.id.ll5);

        scrollView_course = (ScrollView) findViewById(R.id.scrollView_course);
        button_course_change = (Button) findViewById(R.id.button_course_change);
        button_course_change.setOnClickListener(new View.OnClickListener() {
            //设置按钮更换背景的单击事件
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_UNSPECIFIED);
                startActivityForResult(intent, PHOTOZOOM);
            }
        });


        connect();
        int len = json_lesson_time.length();
        lesson_time = new String[len];
        lesson_start = new int[len];
        lesson_week = new int[len];
        lesson_classroom = new String[len];
        lesson_name = new String[len];
        lesson_teacher = new String[len];
        for (int i = 0; i < len; i++) {
            try {
                lesson_time[i] = (String) json_lesson_time.get(i);
                lesson_start[i] = (int) json_lesson_start.get(i);
                lesson_week[i] = (int) json_lesson_week.get(i);
                lesson_classroom[i] = (String) json_lesson_classroom.get(i);
                lesson_name[i] = (String) json_lesson_name.get(i);
                lesson_teacher[i] = (String) json_lesson_teacher.get(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        System.out.println(json_lesson_time);
        System.out.println(json_lesson_start);
        System.out.println(json_lesson_week);
        System.out.println(json_lesson_classroom);
        System.out.println(json_lesson_name);
        System.out.println(json_lesson_teacher);


        //每天的课程设置
        setCourse(ll1,1);
        setCourse(ll2,2);
        setCourse(ll3,3);
        setCourse(ll4,4);
        setCourse(ll5,5);


    }


    //更改背景图片
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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
                Drawable background = new BitmapDrawable(photo);
                scrollView_course.setBackground(background);
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


    /**
     * 设置课程的方法
     *
     * @param ll
     * @param title   课程名称
     * @param teacher 老师
     * @param place   地点
     * @param classes 节数
     * @param color   背景色
     */
    void setClass(LinearLayout ll, String title, String teacher,
                  String place, int classes, int color) {
        View view = LayoutInflater.from(this).inflate(R.layout.course_item, null);
        view.setMinimumHeight(dip2px(this, classes * 48));
        view.setBackgroundColor(colors[color]);
        ((TextView) view.findViewById(R.id.course_title)).setText(title);
        ((TextView) view.findViewById(R.id.course_teacher)).setText(teacher);
        ((TextView) view.findViewById(R.id.course_place)).setText(place);

        //为课程View设置点击的监听器
        view.setOnClickListener(new OnClickClassListener());
        TextView blank1 = new TextView(this);
        TextView blank2 = new TextView(this);
        blank1.setHeight(dip2px(this, classes));
        blank2.setHeight(dip2px(this, classes));
        ll.addView(blank1);
        ll.addView(view);
        ll.addView(blank2);
    }


    /**
     * 设置无课（空白）
     *
     * @param ll
     * @param classes 无课的节数（长度）
     * @param color
     */
    void setNoClass(LinearLayout ll, int classes, int color) {
        TextView blank = new TextView(this);
        if (color == 0)
            blank.setMinHeight(dip2px(this, classes * 50));
        blank.setBackgroundColor(colors[color]);
        ll.addView(blank);
    }


    //点击课程的监听器���
    class OnClickClassListener implements OnClickListener {

        public void onClick(View v) {
            // TODO Auto-generated method stub
            String title;
            title = (String) ((TextView) v.findViewById(R.id.course_title)).getText();
            Toast.makeText(getApplicationContext(), "你想要修改的课为:" + title,
                    Toast.LENGTH_SHORT).show();
        }
    }


    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    private boolean connect() {
        String url = "information/course/";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("account", account));
        params.add(new BasicNameValuePair("password", password));

        MyThread myThread = new MyThread(params, url);
        myThread.start();
        while (!myThread.isDone()) {
        }

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(myThread.result);

            if(jsonObject.getString("status").equals("error"))
                return false;
            else {
                JSONObject content = jsonObject.getJSONObject("content");

                json_lesson_time = content.getJSONArray("lesson_time");
                json_lesson_start = content.getJSONArray("lesson_start");
                json_lesson_week = content.getJSONArray("lesson_week");
                json_lesson_classroom = content.getJSONArray("lesson_classroom");
                json_lesson_name = content.getJSONArray("lesson_name");
                json_lesson_teacher = content.getJSONArray("lesson_teacher");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return true;
    }

    public void setCourse(LinearLayout ll, int week) {

        int lesson_length = 2;
        int n = 1;//n表示现在进行到的开始节数
        for(int i = 0;i<json_lesson_time.length();i++)
        {
            if (lesson_week[i] == week && !lesson_name[i].equals("null")) {
                if(lesson_start[i] == n) {
                    setClass(ll, lesson_name[i], lesson_teacher[i], lesson_classroom[i], lesson_length, (int) (Math.random() * 6)+1);
                }
                else {
                    setNoClass(ll, lesson_start[i] - n, 0);
                    setClass(ll, lesson_name[i], lesson_teacher[i], lesson_classroom[i], lesson_length, (int) (Math.random() * 6)+1);
                }
                n = lesson_start[i] + lesson_length;
            }
            if (i == json_lesson_time.length() - 1 && n != 13)
                setNoClass(ll, 13 - n, 0);
        }

    }
}