package com.application.atplexam.Controller.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.application.atplexam.Controller.NavigationController;
import com.application.atplexam.R;

public class SplashController extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_view);
        final SharedPreferences sharedPreferences = getSharedPreferences("Content_main", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        //FirebaseApp.initializeApp(this);
        //FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String autologin = sharedPreferences.getString("autologin", "");
                if (autologin.equals("true")) {
                    startActivity(new Intent(SplashController.this, NavigationController.class));
                } else {
                    startActivity(new Intent(SplashController.this, LoginController.class));
                }
            }
        }, 2000);
    }
}