package com.example.globallive;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class SplashActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                AuthenticationActivity.displayActivity(com.example.globallive.SplashActivity.this);
                finish();
            }
        },2000);
    }

}
