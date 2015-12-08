package com.cloudfoyo.magazine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.facebook.FacebookSdk;

public class Splash extends AppCompatActivity {

    public static final String APP_MODE = "development"; //Set it to "release" before release


    int SPLASH_TIMER=4000;
    boolean firsttime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialise Facebook  SDK here ..
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_splash);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getSupportActionBar().hide();
        SharedPreferences sharedPreferences=getSharedPreferences("prefs1",0);
        firsttime=sharedPreferences.getBoolean("login",false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!firsttime) {
                    Intent intent = new Intent(Splash.this, MainActivity.class);
                    startActivity(intent);
                    Splash.this.finish();
                }
                else{
                    Intent intent=new Intent(Splash.this,Login.class);
                    startActivity(intent);
                    Splash.this.finish();
                }
            }
        },(APP_MODE.equals("development"))?100:SPLASH_TIMER); //Time is precious :-P
    }
    @Override
    public void onBackPressed(){
        //Disable
    }
}
