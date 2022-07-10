package com.lux.ex063service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaParser;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

//android의 4대 컴포넌트는 반드시 AndroidManifest.xml에 등록
public class MusicService extends Service {

    MediaPlayer mp;

    //startService() / startForegroundService 명령으로 이 서비스 객체가 생성되어 시작되면 자동으로
    //발동하는 라이프사이클 콜백메소드가 존재함
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //startForegroundService로 실행된 서비스는 반드시 이 메소드를 호출해야 함
        //foregroundService로 동작함.
        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder=null;
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel("ch01","ex063",NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            builder=new NotificationCompat.Builder(this,"ch01");
        }else {
            builder=new NotificationCompat.Builder(this,"");
        }

        builder.setSmallIcon(R.drawable.ic_noti);
        builder.setContentTitle("Music Service");
        builder.setContentText("Music Service Start");

        //알림창을 클릭했을때 뮤직서비스를 종료하는 버튼(ui)을 가진 메인 액티비티를 실행
        //하도록 보류중인 PendingIntent를 실행
        Intent i=new Intent(this,MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,100,i,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_MUTABLE);
        builder.setContentIntent(pendingIntent);

        Notification notification=builder.build();

        startForeground(1,notification);  //알림 공지됨.

        //MediaPlayer 객체 생성 및 실행
        if (mp==null){
            mp=MediaPlayer.create(this,R.raw.kalimba);
            mp.setVolume(0.7f,0.7f);
            mp.setLooping(true);
        }

        mp.start(); //시작 or 이어하기
        //메모리 문제로 프로세스가 강제로 killed 되는 경우가 있음.
        //다시 메모리문제가 해결되면 자동으로 다시 서비스가 실행될지 여부를 결정.
        //START_STICKY 상수값 - 여유만 생기면 자동 다시 실행
        return START_STICKY;
    }

    //stopService 명령으로 이 서비스 객체가 종료되면 자동으로
    //발동하는 라이프사이클 콜백메소드가 존재함
    @Override
    public void onDestroy() {
        //미디어 플레이어를 종료
        if (mp!=null){
            mp.stop();
            mp.release();
            mp=null;
        }

        //foreground service를 멈추도록 하며 알림도 자동 제거되도록 설정
        stopForeground(true);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
