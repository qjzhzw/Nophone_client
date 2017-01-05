package com.ForestAnimals.nophone.setting;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.ForestAnimals.nophone.R;
import com.ForestAnimals.nophone.shape.DoubleTimePickerDialog;
import com.ForestAnimals.nophone.shape.TimePickerDialog_setting;

import java.util.Calendar;


/**
 * Created by MyWorld on 2016/6/12.
 */
public class setting_fragment extends Fragment {

    //定义划栏按键变量，硬件管理
    private DevicePolicyManager policyManager;
    private ComponentName componentName;
    private Button button_setting_locking;
    private TextView textView_setting_start;
    private TextView textView_setting_finish;
    private Calendar calendar_start;
    private Calendar calendar_finish;
    private ToggleButton toggleButton_setting_selfstudy;
    private RadioGroup radioGroup_setting;
    private RadioButton radioButton_setting_immediate;
    private RadioButton radioButton_setting_timing;
    //自定义学习模式设置开关
    private LinearLayout linearLayout_setting_selfstudy;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_layout, container,
                false);

        //声明ID集合
        button_setting_locking = (Button) view.findViewById(R.id.button_setting_locking);
//        textView_setting_start = (TextView) view.findViewById(R.id.textView_setting_start);
//        textView_setting_finish = (TextView) view.findViewById(R.id.textView_setting_finish);
        toggleButton_setting_selfstudy = (ToggleButton) view.findViewById(R.id.toggleButton_setting_selfstudy);
        linearLayout_setting_selfstudy = (LinearLayout) view.findViewById(R.id.linearLayout_setting_selfstudy);
        radioGroup_setting = (RadioGroup) view.findViewById(R.id.radioGroup_setting);
        radioButton_setting_immediate = (RadioButton) view.findViewById(R.id.radioButton_setting_immediate);
        radioButton_setting_timing = (RadioButton) view.findViewById(R.id.radioButton_setting_timing);

        setAction();
        calendar_start = Calendar.getInstance();
        calendar_finish = Calendar.getInstance();

        linearLayout_setting_selfstudy.setVisibility(View.INVISIBLE);
        //设置定时学习模式控件为不可视

        policyManager = (DevicePolicyManager) getActivity().getSystemService(Context.DEVICE_POLICY_SERVICE);
        //获取设备管理服务（即获取锁屏需要的权限）

        componentName = new ComponentName(getActivity(), AdminReceiver.class);
        //AdminReceiver 继承自 DeviceAdminReceiver

        return view;
    }


    @TargetApi(Build.VERSION_CODES.FROYO)
    private void mylock() {
        boolean active = policyManager.isAdminActive(componentName);
        if (!active) {
            //若无权限
            activeManage();//去获得权限
            policyManager.lockNow();//并锁屏
        }
        if (active) {
            policyManager.lockNow();//直接锁屏
        }
    }


    private void activeManage()
    //激活设备管理器
    {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        //启动设备管理(隐式Intent) - 在AndroidManifest.xml中设定相应过滤器
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        //权限列表
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "------ 其他描述 ------");
        //描述(additional explanation)
        startActivityForResult(intent, 0);
    }


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Button button = (Button) view;
            switch (button.getId()) {
                case R.id.toggleButton_setting_selfstudy:
                    //划栏按键动作
                    if (toggleButton_setting_selfstudy.isChecked()) {
                        linearLayout_setting_selfstudy.setVisibility(View.VISIBLE);//可见
                    } else {
                        linearLayout_setting_selfstudy.setVisibility(View.INVISIBLE);//不可见
                    }
                    break;
                case R.id.button_setting_locking: {
                    mylock();
                    break;
                }
            }
        }
    };


    private RadioGroup.OnCheckedChangeListener listener2 = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
            switch (checkedId) {
                case R.id.radioButton_setting_immediate:
                    calendar_finish.setTimeInMillis(0 - 8 * 60 * 60 * 1000);
                    //设置日历初始时间
                    int Hour = calendar_finish.get(Calendar.HOUR_OF_DAY);
                    //使日历本身可以设置小时数
                    int Minute = calendar_finish.get(Calendar.MINUTE);
                    //使日历本身可以设置分钟数
                    TimePickerDialog_setting timePickerDialog = new TimePickerDialog_setting(getActivity(), new TimePickerDialog_setting.OnTimeSetListener() {
                        //设置“学习模式”结束时间，弹出日历
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            // TODO Auto-generated method stub
                            calendar_finish.add(Calendar.HOUR_OF_DAY, hourOfDay);
                            //设置触发小时为设置的小时数
                            calendar_finish.add(Calendar.MINUTE, minute);
                            //设置触发分钟为设置的分钟数
                            calendar_finish.add(Calendar.SECOND, 0);
                            //设置在相应分钟0秒时触发
                            calendar_finish.add(Calendar.MILLISECOND, 0);
                            //设置在相应分钟0毫秒时触发
                            Intent intent_start = new Intent(getActivity(), AlarmReceiver_silent.class);
                            Intent intent_finish = new Intent(getActivity(), AlarmReceiver_normal.class);
                            //触发时调用相应的广播
                            PendingIntent pendingIntent_start = PendingIntent.getService(getActivity(), 0, intent_start, 0);
                            PendingIntent pendingIntent_finish = PendingIntent.getService(getActivity(), 0, intent_finish, 0);
                            //开启广播
                            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                            //获取系统服务
                            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent_start);
                            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + hourOfDay * 60 * 60 * 1000 + minute * 60 * 1000, pendingIntent_finish);
                            //在设置的时刻触发事件
//                            String tmps = getString(R.string.finish_still) + format(hourOfDay) + ":" + format(minute);
//                            textView_setting_start.setText(tmps);
                            //显示学习模式剩余时间
                        }
                    }, Hour, Minute, true);
                    timePickerDialog.show();
                    //显示日历
                    break;

                case R.id.radioButton_setting_timing:
                    calendar_start.setTimeInMillis(System.currentTimeMillis());
                    calendar_finish.setTimeInMillis(System.currentTimeMillis());
                    //获取系统本身时间
                    int hour_start = calendar_start.get(Calendar.HOUR_OF_DAY);
                    int hour_finish = calendar_finish.get(Calendar.HOUR_OF_DAY);
                    //使日历本身可以设置小时数
                    int minute_start = calendar_start.get(Calendar.MINUTE);
                    int minute_finish = calendar_finish.get(Calendar.MINUTE);
                    //使日历本身可以设置分钟数
                    DoubleTimePickerDialog timePickerDialog_finish = new DoubleTimePickerDialog(getActivity(), new DoubleTimePickerDialog.OnTimeSetListener() {
                        //设置“学习模式”结束时间，弹出日历
                        @Override
                        public void onTimeSet(TimePicker view_start, TimePicker view_finish, int hourOfDay_start, int minute_start, int hourOfDay_finish, int minute_finish) {
                            // TODO Auto-generated method stub
                            calendar_start.set(Calendar.HOUR_OF_DAY, hourOfDay_start);
                            calendar_finish.set(Calendar.HOUR_OF_DAY, hourOfDay_finish);
                            //设置触发小时为设置的小时数
                            calendar_start.set(Calendar.MINUTE, minute_start);
                            calendar_finish.set(Calendar.MINUTE, minute_finish);
                            //设置触发分钟为设置的分钟数
                            calendar_start.set(Calendar.SECOND, 0);
                            calendar_finish.set(Calendar.SECOND, 0);
                            //设置在相应分钟0秒时触发
                            calendar_start.set(Calendar.MILLISECOND, 0);
                            calendar_finish.set(Calendar.MILLISECOND, 0);
                            //设置在相应分钟0毫秒时触发
                            Intent intent_start = new Intent(getActivity(), AlarmReceiver_silent.class);
                            Intent intent_finish = new Intent(getActivity(), AlarmReceiver_normal.class);
                            //触发时调用相应的广播
                            PendingIntent pendingIntent_start = PendingIntent.getService(getActivity(), 0, intent_start, 0);
                            PendingIntent pendingIntent_finish = PendingIntent.getService(getActivity(), 0, intent_finish, 0);
                            //开启广播
                            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                            //获取系统服务
                            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar_start.getTimeInMillis(), pendingIntent_start);
                            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar_finish.getTimeInMillis(), pendingIntent_finish);
                            //在设置的时刻触发事件
//                            String tmps_start = getString(R.string.study_mode) + format(hourOfDay_start) + ":" + format(minute_start) + getString(R.string.start);
//                            String tmps_finish = getString(R.string.study_mode) + format(hourOfDay_finish) + ":" + format(minute_finish) + getString(R.string.finish);
                            //获取开始和结束结束时间
//                            textView_setting_start.setText(tmps_start);
//                            textView_setting_finish.setText(tmps_finish);
                            //显示开始和结束时间
                        }
                    }, hour_start, minute_start, hour_finish, minute_finish, true);
                    timePickerDialog_finish.show();
                    //显示日历
                    break;
            }
        }
    };


    private String format(int time) {
        String str = "" + time;
        if (str.length() == 1) {
            str = "0" + str;
        }
        return str;
    }


    public void setAction()
    //设置事件集合
    {
        button_setting_locking.setOnClickListener(listener);
        toggleButton_setting_selfstudy.setOnClickListener(listener);
        radioGroup_setting.setOnCheckedChangeListener(listener2);
    }
}
