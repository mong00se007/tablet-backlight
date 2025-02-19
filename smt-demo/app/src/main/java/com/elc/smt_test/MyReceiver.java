package com.elc.smt_test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.elcapi.jnielc;

public class MyReceiver extends BroadcastReceiver {

    Context mContext;
    private static final int seek_red = 0xa1;
    private static final int seek_green = 0xa2;
    private static final int seek_blue = 0xa3;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Log.i("gulukai", "onReceive: 开机了");
            try {
                SharedPreferences pref = mContext.getSharedPreferences("data", 0);
                int seekBar_red = pref.getInt("seekBar_red", 0);
                int seekBar_blue = pref.getInt("seekBar_blue", 0);
                int seekBar_green = pref.getInt("seekBar_green", 0);
                boolean isSave = pref.getBoolean("isSave", false);
                if (isSave) {
                    jnielc.seekstart();
                    jnielc.ledseek(seek_red, seekBar_red);
                    jnielc.ledseek(seek_green, seekBar_green);
                    jnielc.ledseek(seek_blue, seekBar_blue);
                    jnielc.seekstop();
                }
            } catch (Exception e) {
                Log.i("gulukai", "没有文件了");
            }
        }
    }
}