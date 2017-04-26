package com.example.intelligentmanager.service;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.intelligentmanager.MyAlarmReceiver;

import java.util.List;

/**
 * Created by 易水柔 on 2017/3/23.
 */
public class TurningService extends Service {
    private SensorEventListener mListener;
    private AudioManager mAudioManager;
    private SensorManager mSensorManager;
    private int StatusFlag;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("111", "TurningService is start");

        mSensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        mAudioManager = (AudioManager) this.getSystemService(AUDIO_SERVICE);

        //状态标志初始化，静音模式为1，否则为0
        StatusFlag = (mAudioManager.getRingerMode()==AudioManager.RINGER_MODE_SILENT)? 1 : 0 ;

        mListener = new SensorEventListener() {

            //精度改变
            public void onAccuracyChanged(Sensor arg0, int arg1) {
                // TODO Auto-generated method stub

            }
            public void onSensorChanged(SensorEvent event) {
                // TODO Auto-generated method stub
                switch(StatusFlag){
                    case 1:
                        if(event.values[2] > 8){
                            Log.i("111" , " change to Ringing mode ");
                            mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC,true);
                            MyAlarmReceiver.stopMediaPlayer();
                            StatusFlag = 0;
                            break;
                        }
                    case 0:
                        if(event.values[2] < -8){
                          Log.i( "111" , "change to SilentMode");
                            mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC,false);
                            StatusFlag = 1;
                            break;
                        }
                    default:
                        break;
                }


            }
        };

        List<Sensor> mSensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        int size = mSensorList.size();
        for(int i = 0 ; i <size ; i++){
//          Log.d(TAG, "sensor");
//          Log.d(TAG, "mSensorList.size() = " + mSensorList.size());
//          Log.d(TAG, "mSensorList = " + mSensorList);

            if(mSensorList!=null && (mSensorList.size()>0)){
                Sensor sensor = mSensorList.get(0);
                mSensorManager.registerListener(mListener, sensor,SensorManager.SENSOR_DELAY_NORMAL);
            }else {
//              Log.d(TAG, "sensor is null");
            }
        }



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
