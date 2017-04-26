package com.example.intelligentmanager;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;

import java.io.IOException;

/**
 * Created by 易水柔 on 2017/3/22.
 */
public class MyAlarmReceiver extends Service{
    private static AlarmManager am;
    private static PendingIntent pi;
    private int uri;
    private static MediaPlayer mMediaPlayer;
    private static final int[] musicId = {R.raw.alarm1,R.raw.alarm2,R.raw.alarm3};
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private Uri getSystemDefultRingtoneUri(Context context) {
        return RingtoneManager.getActualDefaultRingtoneUri(context,
                RingtoneManager.TYPE_RINGTONE);
    }
    public static void stopMediaPlayer(){
        if(mMediaPlayer!=null){
        mMediaPlayer.stop();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent == null)
            return super.onStartCommand(intent, flags, startId);
        final String tilte=intent.getStringExtra("title");
        final int importance=intent.getIntExtra("importance",0);
        final String content=intent.getStringExtra("content");
        Log.i("importance",importance+"");
        Log.i("title",tilte+"");
        if(importance==0){
            uri=musicId[0];
        }else if(importance==1){
            uri=musicId[1];
        }else {
            uri=musicId[2];
        }
        Log.i("闹钟",tilte);
        if (mMediaPlayer==null){
            Log.i("闹钟","null");
            mMediaPlayer = MediaPlayer.create(this, uri);
        }else {
            Log.i("闹钟","不为null");
            mMediaPlayer.stop();
            mMediaPlayer = null;
            mMediaPlayer = MediaPlayer.create(this, uri);
        }
        mMediaPlayer.setLooping(true);//设置循环
        try {
            mMediaPlayer.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.start();
        // TODO: 2017/3/23  添加notification
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(content)
                .setTitle(tilte)
                .setCancelable(false)
                .setPositiveButton("关闭闹钟", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mMediaPlayer.stop();
//                        AddTransactionActivity.startAlarmThree();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("稍后提醒", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mMediaPlayer.stop();
                        Intent intent = new Intent("ELITOR_CLOCK");
                        intent.putExtra("title",tilte);
                        intent.putExtra("content",content);
                        intent.putExtra("importance",importance);
                        pi = PendingIntent.getService(MyAlarmReceiver.this, 0,intent,0);
                        am = (AlarmManager)getApplicationContext().getSystemService(getApplicationContext().ALARM_SERVICE);
                        am.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+10000,pi);
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alert.show();


//        NotificationManager manager = (NotificationManager)
//                context.getSystemService(context.NOTIFICATION_SERVICE);
//        Notification.Builder builder = new Notification.Builder(context);
//        builder.setContentInfo("补充内容");
//        builder.setContentText("主内容区");
//        builder.setContentTitle(tilte);
//        builder.setSmallIcon(R.mipmap.ic_launcher);
//        builder.setTicker("新消息");
//        builder.setAutoCancel(true);
//        builder.setWhen(System.currentTimeMillis());
//        Intent intent1 = new Intent(context, TransactionActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
//        builder.setContentIntent(pendingIntent);
//        Notification notification = builder.build();
//        manager.notify(1, notification);

        // TODO: 2017/3/23


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
