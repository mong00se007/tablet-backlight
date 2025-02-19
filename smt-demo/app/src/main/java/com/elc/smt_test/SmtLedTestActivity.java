package com.elc.smt_test;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.example.elcapi.jnielc;

import android.view.KeyEvent;
import android.widget.Switch;

public class SmtLedTestActivity extends Activity implements OnSeekBarChangeListener {
    private SeekBar seekBar_red, seekBar_blue, seekBar_green;
    private CheckBox[] checkBoxes = new CheckBox[7];

    private static final int seek_red = 0xa1;
    private static final int seek_green = 0xa2;
    private static final int seek_blue = 0xa3;
    SharedPreferences.Editor edt;
    private static Boolean isSave;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smt_led_test);

        SharedPreferences pref = getSharedPreferences("data", 0);
        isSave = pref.getBoolean("isSave", false);

        seekBar_red = (SeekBar) findViewById(R.id.SeekBar_red);
        seekBar_blue = (SeekBar) findViewById(R.id.SeekBar_blue);
        seekBar_green = (SeekBar) findViewById(R.id.SeekBar_green);

        seekBar_red.setOnSeekBarChangeListener(this);
        seekBar_blue.setOnSeekBarChangeListener(this);
        seekBar_green.setOnSeekBarChangeListener(this);

        Switch mSaveSwitch = findViewById(R.id.switch_save);
        mSaveSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                //需要保存
                edt = getSharedPreferences("data", MODE_PRIVATE).edit();
                edt.putBoolean("isSave", true);
                edt.putInt("seekBar_red", seekBar_red.getProgress());
                edt.putInt("seekBar_blue", seekBar_blue.getProgress());
                edt.putInt("seekBar_green", seekBar_green.getProgress());
                edt.apply();
            } else {
                //不需要保存
                edt = getSharedPreferences("data", MODE_PRIVATE).edit();
                edt.putBoolean("isSave", false);
                edt.apply();
            }
        });

        //如果需要保存，则需要对应上
        if (isSave) {
            int seekBar_red_pref = pref.getInt("seekBar_red", 0);
            int seekBar_blue_pref = pref.getInt("seekBar_blue", 0);
            int seekBar_green_pref = pref.getInt("seekBar_green", 0);
            seekBar_red.setProgress(seekBar_red_pref);
            seekBar_green.setProgress(seekBar_green_pref);
            seekBar_blue.setProgress(seekBar_blue_pref);
            mSaveSwitch.setChecked(true);
        }

        checkBoxes[0] =  findViewById(R.id.radio_red);
        checkBoxes[1] =  findViewById(R.id.radio_green);
        checkBoxes[2] =  findViewById(R.id.radio_blue);
        checkBoxes[3] =  findViewById(R.id.radio_yellow);
        checkBoxes[4] =  findViewById(R.id.radio_purple);
        checkBoxes[5] =  findViewById(R.id.radio_cyan);
        checkBoxes[6] =  findViewById(R.id.radio_white);

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        if (seekBar == seekBar_red) {
            jnielc.ledseek(seek_red, progress);
            if (isSave) {
                edt = getSharedPreferences("data", MODE_PRIVATE).edit();
                edt.putInt("seekBar_red", seekBar_red.getProgress());
                edt.putInt("seekBar_blue", seekBar_blue.getProgress());
                edt.putInt("seekBar_green", seekBar_green.getProgress());
                edt.apply();
            }
        }
        if (seekBar == seekBar_green) {
            jnielc.ledseek(seek_green, progress);
            if (isSave) {
                edt = getSharedPreferences("data", MODE_PRIVATE).edit();
                edt.putInt("seekBar_red", seekBar_red.getProgress());
                edt.putInt("seekBar_blue", seekBar_blue.getProgress());
                edt.putInt("seekBar_green", seekBar_green.getProgress());
                edt.apply();
            }
        }
        if (seekBar == seekBar_blue) {
            jnielc.ledseek(seek_blue, progress);
            if (isSave) {
                edt = getSharedPreferences("data", MODE_PRIVATE).edit();
                edt.putInt("seekBar_red", seekBar_red.getProgress());
                edt.putInt("seekBar_blue", seekBar_blue.getProgress());
                edt.putInt("seekBar_green", seekBar_green.getProgress());
                edt.apply();
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        jnielc.seekstart();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        jnielc.seekstop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onResume() {
        super.onResume();
    }

    @SuppressLint("NonConstantResourceId")
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.radio_red:
                if (checked) {
                    jnielc.seekstart();
                    jnielc.ledseek(seek_red, 15);
                    jnielc.ledseek(seek_green, 0);
                    jnielc.ledseek(seek_blue, 0);
                    jnielc.seekstop();
                    seekBar_red.setProgress(15);
                    seekBar_green.setProgress(0);
                    seekBar_blue.setProgress(0);
                    if (isSave) {
                        edt = getSharedPreferences("data", MODE_PRIVATE).edit();
                        edt.putInt("seekBar_red", 15);
                        edt.putInt("seekBar_blue", 0);
                        edt.putInt("seekBar_green", 0);
                        edt.apply();
                    }
                    checkBoxes[1].setChecked(false);
                    checkBoxes[2].setChecked(false);
                    checkBoxes[3].setChecked(false);
                    checkBoxes[4].setChecked(false);
                    checkBoxes[5].setChecked(false);
                    checkBoxes[6].setChecked(false);
                }
                break;
            case R.id.radio_green:
                if (checked) {
                    jnielc.seekstart();
                    jnielc.ledseek(seek_red, 0);
                    jnielc.ledseek(seek_green, 15);
                    jnielc.ledseek(seek_blue, 0);
                    jnielc.seekstop();
                    seekBar_red.setProgress(0);
                    seekBar_green.setProgress(15);
                    seekBar_blue.setProgress(0);
                    if (isSave) {
                        edt = getSharedPreferences("data", MODE_PRIVATE).edit();
                        edt.putInt("seekBar_red", 0);
                        edt.putInt("seekBar_blue", 15);
                        edt.putInt("seekBar_green", 0);
                        edt.apply();
                    }
                    checkBoxes[0].setChecked(false);
                    checkBoxes[2].setChecked(false);
                    checkBoxes[3].setChecked(false);
                    checkBoxes[4].setChecked(false);
                    checkBoxes[5].setChecked(false);
                    checkBoxes[6].setChecked(false);
                }
                break;
            case R.id.radio_blue:
                if (checked) {
                    jnielc.seekstart();
                    jnielc.ledseek(seek_red, 0);
                    jnielc.ledseek(seek_green, 0);
                    jnielc.ledseek(seek_blue, 15);
                    jnielc.seekstop();
                    seekBar_red.setProgress(0);
                    seekBar_green.setProgress(0);
                    seekBar_blue.setProgress(15);
                    if (isSave) {
                        edt = getSharedPreferences("data", MODE_PRIVATE).edit();
                        edt.putInt("seekBar_red", 0);
                        edt.putInt("seekBar_blue", 0);
                        edt.putInt("seekBar_green", 15);
                        edt.apply();
                    }
                    checkBoxes[0].setChecked(false);
                    checkBoxes[1].setChecked(false);
                    checkBoxes[3].setChecked(false);
                    checkBoxes[4].setChecked(false);
                    checkBoxes[5].setChecked(false);
                    checkBoxes[6].setChecked(false);
                }
                break;
            case R.id.radio_yellow:
                if (checked) {
                    jnielc.seekstart();
                    jnielc.ledseek(seek_red, 15);
                    jnielc.ledseek(seek_green, 15);
                    jnielc.ledseek(seek_blue, 0);
                    jnielc.seekstop();
                    seekBar_red.setProgress(15);
                    seekBar_green.setProgress(15);
                    seekBar_blue.setProgress(0);
                    if (isSave) {
                        edt = getSharedPreferences("data", MODE_PRIVATE).edit();
                        edt.putInt("seekBar_red", 15);
                        edt.putInt("seekBar_blue", 15);
                        edt.putInt("seekBar_green", 0);
                        edt.apply();
                    }
                    checkBoxes[0].setChecked(false);
                    checkBoxes[1].setChecked(false);
                    checkBoxes[2].setChecked(false);
                    checkBoxes[4].setChecked(false);
                    checkBoxes[5].setChecked(false);
                    checkBoxes[6].setChecked(false);
                }
                break;
            case R.id.radio_purple:
                if (checked) {
                    jnielc.seekstart();
                    jnielc.ledseek(seek_red, 15);
                    jnielc.ledseek(seek_green, 0);
                    jnielc.ledseek(seek_blue, 15);
                    jnielc.seekstop();
                    seekBar_red.setProgress(15);
                    seekBar_green.setProgress(0);
                    seekBar_blue.setProgress(15);
                    if (isSave) {
                        edt = getSharedPreferences("data", MODE_PRIVATE).edit();
                        edt.putInt("seekBar_red", 15);
                        edt.putInt("seekBar_blue", 0);
                        edt.putInt("seekBar_green", 15);
                        edt.apply();
                    }
                    checkBoxes[0].setChecked(false);
                    checkBoxes[1].setChecked(false);
                    checkBoxes[2].setChecked(false);
                    checkBoxes[3].setChecked(false);
                    checkBoxes[5].setChecked(false);
                    checkBoxes[6].setChecked(false);
                }
                break;
            case R.id.radio_cyan:
                if (checked) {
                    jnielc.seekstart();
                    jnielc.ledseek(seek_red, 0);
                    jnielc.ledseek(seek_green, 15);
                    jnielc.ledseek(seek_blue, 15);
                    jnielc.seekstop();
                    seekBar_red.setProgress(0);
                    seekBar_green.setProgress(15);
                    seekBar_blue.setProgress(15);
                    if (isSave) {
                        edt = getSharedPreferences("data", MODE_PRIVATE).edit();
                        edt.putInt("seekBar_red", 0);
                        edt.putInt("seekBar_blue", 15);
                        edt.putInt("seekBar_green", 15);
                        edt.apply();
                    }
                    checkBoxes[0].setChecked(false);
                    checkBoxes[1].setChecked(false);
                    checkBoxes[2].setChecked(false);
                    checkBoxes[3].setChecked(false);
                    checkBoxes[4].setChecked(false);
                    checkBoxes[6].setChecked(false);
                }
                break;
            case R.id.radio_white:
                if (checked) {
                    jnielc.seekstart();
                    jnielc.ledseek(seek_red, 15);
                    jnielc.ledseek(seek_green, 15);
                    jnielc.ledseek(seek_blue, 15);
                    jnielc.seekstop();
                    seekBar_red.setProgress(15);
                    seekBar_green.setProgress(15);
                    seekBar_blue.setProgress(15);
                    if (isSave) {
                        edt = getSharedPreferences("data", MODE_PRIVATE).edit();
                        edt.putInt("seekBar_red", 15);
                        edt.putInt("seekBar_blue", 15);
                        edt.putInt("seekBar_green", 15);
                        edt.apply();
                    }
                    checkBoxes[0].setChecked(false);
                    checkBoxes[1].setChecked(false);
                    checkBoxes[2].setChecked(false);
                    checkBoxes[3].setChecked(false);
                    checkBoxes[4].setChecked(false);
                    checkBoxes[5].setChecked(false);
                }
                break;
        }
    }
}