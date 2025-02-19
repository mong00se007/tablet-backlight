package com.elc.smt_test;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //外接口
        findViewById(R.id.smtTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SmtTestActivity.class);
                startActivity(intent);
            }
        });

        //灯
        findViewById(R.id.smtLed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SmtLedTestActivity.class);
                startActivity(intent);
            }
        });



        //温湿度
        findViewById(R.id.smtTemperatureHumidity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SmtTemperatureHumidityActivity.class);
                startActivity(intent);
            }
        });



    }
}