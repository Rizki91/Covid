package com.fahrul.covid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.fahrul.covid.utility.SharedPrefUtil;
import com.location.aravind.getlocation.GeoLocator;

public class SplashScreen extends AppCompatActivity {
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String dataJson = SharedPrefUtil.getInstance(SplashScreen.this).getString("data_input");
                if (!TextUtils.isEmpty(dataJson)){
                    Intent intent = new Intent(SplashScreen.this, HalamanUtama.class);

                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        },1500);


    }
}
