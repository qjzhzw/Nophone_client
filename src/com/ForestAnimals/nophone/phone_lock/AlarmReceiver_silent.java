package com.ForestAnimals.nophone.phone_lock;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Build;
import android.os.IBinder;
import com.ForestAnimals.nophone.MainActivity;
import com.ForestAnimals.nophone.R;


/**
 * Created by dell on 2016/4/28.
 */
public class AlarmReceiver_silent extends Service
{

    private NotificationManager notificationManager;
    private DevicePolicyManager policyManager;
    private AudioManager audioManager;
    private ComponentName componentName;

    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        policyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        //获取设备管理服务（即获取锁屏需要的权限）
        componentName = new ComponentName(this, AdminReceiver.class);
        //AdminReceiver 继承自 DeviceAdminReceiver
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        audioManager = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        //使手机设置成静音模式
        mylock();
        //将手机锁屏
        showNotification("您的手机已进入学习模式");
        //在通知栏中提示“您的手机已进入学习模式”
        return super.onStartCommand(intent, flags, startId);
    }

    public void showNotification(String msg) {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification =new Notification(R.drawable.icon, "上课没疯：你已经进入学习模式！", System.currentTimeMillis());
        //定义Notification的各种属性

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;
        // FLAG_AUTO_CANCEL   该通知能被状态栏的清除按钮给清除掉
        // FLAG_NO_CLEAR      该通知不能被状态栏的清除按钮给清除掉，要和FLAG_ONGOING_EVENT共同使用
        // FLAG_ONGOING_EVENT 通知放置在正在运行
        // FLAG_INSISTENT     是否一直进行，比如音乐一直播放，直到用户响应

        notification.defaults = Notification.DEFAULT_LIGHTS;
        //notification.defaults=Notification.DEFAULT_LIGHTS|Notification.DEFAULT_SOUND;
        // DEFAULT_ALL     使用所有默认值，比如声音，震动，闪屏等等
        // DEFAULT_LIGHTS  使用默认闪光提示
        // DEFAULT_SOUNDS  使用默认提示声音
        // DEFAULT_VIBRATE 使用默认手机震动，需加上<uses-permission android:name="android.permission.VIBRATE" />权限

        notification.ledARGB = Color.BLUE;
        notification.ledOnMS =5000; //闪光时间，毫秒
        //叠加效果常量

        //Intent notificationIntent =new Intent(MainActivity.this, MainActivity.class);
        //点击该通知后要跳转的Activity
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        //加载类，如果直接通过类名，会在点击时重新加载页面，无法恢复最后页面状态。
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getService(this, 0, notificationIntent, 0);
        notification.setLatestEventInfo(this, "上课没疯", "温馨提示:" + msg, contentIntent);
        //设置通知的事件消息

        notificationManager.notify(0, notification);
        //把Notification传递给NotificationManager

    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    public void mylock()
    //锁屏代码
    {
        boolean active = policyManager.isAdminActive(componentName);
        if(!active)
        {
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
    }


}