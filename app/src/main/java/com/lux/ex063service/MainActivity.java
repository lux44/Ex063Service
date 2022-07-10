package com.lux.ex063service;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_start).setOnClickListener(view -> startMusic());
        findViewById(R.id.btn_stop).setOnClickListener(view -> stopMusic());
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    void startMusic(){
        //Oreo(api 26)버전 부터는 서비스를 그냥 start명령으로 백그라운드로 돌아가게 하면
        //중간에 자동으로 꺼버림.
        //그래서 서비스도 foreground service라는 개념을 추가함.
        //쉽게 말해, 서비스는 백그라운드에서 동작하지만 사용자가 이를 인식하도록 하기 위해서
        //반드시 Notification과 함께 실행되도록 강제한 기법

        //백그라운드 작업용 서비스 컴포넌트 실행!
        Intent intent=new Intent(this,MusicService.class);

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) startForegroundService(intent);
        else startService(intent);
    }
    void stopMusic(){
        Intent intent=new Intent(this,MusicService.class);
        stopService(intent);
    }
}