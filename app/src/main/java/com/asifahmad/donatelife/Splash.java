package com.asifahmad.donatelife;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread thread = new Thread() {

            @Override
            public void run() {
                try {

                    sleep(2000);
                } catch (Exception e) {

                    e.printStackTrace();
                } finally {

                    startActivity(new Intent(Splash.this,SignUp.class));
                    finish();

                }
            }
        };
        thread.start();
    }
}