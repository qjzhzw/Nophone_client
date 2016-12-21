package com.ForestAnimals.nophone.course;

import android.annotation.TargetApi;
import android.app.Activity;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.ForestAnimals.nophone.R;

import java.io.ByteArrayOutputStream;

public class course_table extends Activity {

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


        //每天的课程设置����
        setNoClass(ll1, 2, 0);
        setClass(ll1, "公民与多元社会", "周叙琪", "EE107", 2, 1);
        setNoClass(ll1, 1, 0);
        setClass(ll1, "JAVA应用程序设计", "周智伦", "S305", 3, 2);
        setClass(ll1, "网络程序设计", "罗嘉宁", "AA201", 3, 3);
        setNoClass(ll1, 1, 0);

        setNoClass(ll2, 1, 0);
        setClass(ll2, "视讯分析与互动技术", "谢朝和", "AA602", 3, 4);
        setClass(ll2, "班会", "周星文", "EE202", 1, 5);
        setNoClass(ll2, 7, 0);

        setNoClass(ll3, 1, 0);
        setClass(ll3, "互动电子技术", "黄世育", "S513", 3, 6);
        setNoClass(ll3, 1, 0);
        setClass(ll3, "通讯原理", "高志阳", "S106", 3, 1);
        setNoClass(ll3, 1, 0);
        setClass(ll3, "进阶微处理系统", "张嘉文", "AA202", 3, 2);

        setNoClass(ll4, 1, 0);
        setClass(ll4, "作业系统", "李明哲", "S107", 3, 3);
        setNoClass(ll4, 8, 0);

        setNoClass(ll5, 1, 0);
        setClass(ll5, "计算机结构", "李开晖", "EE505", 3, 4);
        setNoClass(ll5, 8, 0);
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
                Drawable background =new BitmapDrawable(photo);
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
}