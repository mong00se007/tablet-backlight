package com.elc.smt_test;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.util.Log;
import android.os.Handler;
import android.os.Message;
import android.content.Context;
import com.sys.gpio.gpioJni;
import android.widget.Switch;
import static com.example.ttysprint.TTY.uart;


public class SmtTestActivity extends Activity implements View.OnClickListener {

    private static Button mIRButton,mRSOneButton,mRSTwoButton;
    private static TextView mRelayOneText,mRelayTwoText,mIOneText,mIOTwoText,mIRText,mTtyS5Text,mTtyS9Text;
    private static TextView mIOneTextResult,mIOTwoTextResult,mRelayOneTextResult,mRelayTwoTextResult;
    private static Button mRelayOneTextResultButton,mRelayTwoTextResultButton,mIOneTextResultButton,mIOTwoTextResultButton,mIRTextResultButton,mTtyS5TextResultButton,mTtyS9TextResultButton;
    private static Switch mIOneSwitch,mIOTwoSwitch,mRelayOneSwitch,mRelayTwoSwitch;
    public Activity mActivity;
    public static Intent resultIntent2 = new Intent();
    private static final int MSG_SCAN = 1;
    private String logPath = "/sdcard/test1.txt";
    Context mContext;
    Handler nTtyS5Handler = new Handler();
    Handler nTtyS9Handler = new Handler();
    private static final String TAG = "SmtTestActivity";
    private int mTextButtonRy1 = 0,mTextButtonRy2 = 0,mTextButtonIO1 = 0,mTextButtonIO2 = 0,mTextButtonIR = 0,mTextButtonTty5 = 0,mTextButtonTty9 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smt_test);
        mContext = getBaseContext();

        mRelayOneSwitch = findViewById(R.id.RelayOneTest);
        mRelayTwoSwitch = findViewById(R.id.RelayTwoTest);
        mIOneSwitch = findViewById(R.id.IOneTest);
        mIOTwoSwitch = findViewById(R.id.IOTwoTest);
        mIRButton = findViewById(R.id.IRTest);
        mRSOneButton = findViewById(R.id.RSOneTest);
        mRSTwoButton = findViewById(R.id.RSTwoTest);

        mRelayOneText = findViewById(R.id.RelayOneText);
        mRelayTwoText = findViewById(R.id.RelayTwoText);
        mIOneText = findViewById(R.id.IOneText);
        mIOTwoText = findViewById(R.id.IOTwoText);
        mIRText = findViewById(R.id.IRText);
        mTtyS5Text = findViewById(R.id.TtyS5Text);
        mTtyS9Text = findViewById(R.id.TtyS9Text);

        mIOneTextResult = findViewById(R.id.IOneTextResult);
        mIOTwoTextResult = findViewById(R.id.IOTwoTextResult);
        mRelayOneTextResult = findViewById(R.id.RelayOneTextResult);
        mRelayTwoTextResult = findViewById(R.id.RelayTwoTextResult);

        mRelayOneTextResultButton = findViewById(R.id.RelayOneTextResultButton);
        mRelayTwoTextResultButton = findViewById(R.id.RelayTwoTextResultButton);
        mIOneTextResultButton = findViewById(R.id.IOneTextResultButton);
        mIOTwoTextResultButton = findViewById(R.id.IOTwoTextResultButton);
        mIRTextResultButton = findViewById(R.id.IRTextResultButton);
        mTtyS5TextResultButton = findViewById(R.id.TtyS5TextResultButton);
        mTtyS9TextResultButton = findViewById(R.id.TtyS9TextResultButton);

        mRSOneButton.setOnClickListener(this);
        mRSTwoButton.setOnClickListener(this);

        mRelayOneTextResultButton.setOnClickListener(this);
        mRelayTwoTextResultButton.setOnClickListener(this);
        mIOneTextResultButton.setOnClickListener(this);
        mIOTwoTextResultButton.setOnClickListener(this);
        mIRTextResultButton.setOnClickListener(this);
        mTtyS5TextResultButton.setOnClickListener(this);
        mTtyS9TextResultButton.setOnClickListener(this);

        //测试串口
        nTtyS5Handler.post(mTask_tty5);
        nTtyS9Handler.post(mTask_tty9);

        initView();

        //relay
        mRelayOneSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                gpioJni.ioctl_gpio(2, 0, 1);
                android.util.Log.i(TAG,"set one high");
                if (gpioJni.ioctl_gpio(2, 1, 1) == 1){
                    //说明设置电位高成功
                    mRelayOneText.setTextColor(mContext.getResources().getColor(R.color.Green));
                    mRelayOneTextResultButton.setEnabled(true);
                }
            } else {
                gpioJni.ioctl_gpio(2, 0, 0);
                android.util.Log.i(TAG,"set one low");
                if (gpioJni.ioctl_gpio(2, 1, 1) == 0){
                    //说明设置电位低成功
                    mRelayOneText.setTextColor(mContext.getResources().getColor(R.color.Green));
                    mRelayOneTextResultButton.setEnabled(true);
                }
            }
            if (gpioJni.ioctl_gpio(2, 1, 1) == 1){
                mRelayOneSwitch.setChecked(true);
                android.util.Log.i(TAG,"get one high");
                mRelayOneTextResult.setText("high");
                mRelayOneTextResult.setTextColor(mContext.getResources().getColor(R.color.Green));
            }else {
                mRelayOneSwitch.setChecked(false);
                android.util.Log.i(TAG,"get one low");
                mRelayOneTextResult.setText("low");
                mRelayOneTextResult.setTextColor(mContext.getResources().getColor(R.color.red));
            }
        });

        mRelayTwoSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                gpioJni.ioctl_gpio(3, 0, 1);
                android.util.Log.i(TAG,"set one high");
                if (gpioJni.ioctl_gpio(3, 1, 1) == 1){
                    mRelayTwoText.setTextColor(mContext.getResources().getColor(R.color.Green));
                    mRelayTwoTextResultButton.setEnabled(true);
                }
            } else {
                gpioJni.ioctl_gpio(3, 0, 0);
                android.util.Log.i(TAG,"set one low");
                if (gpioJni.ioctl_gpio(3, 1, 1) == 0){
                    mRelayTwoText.setTextColor(mContext.getResources().getColor(R.color.Green));
                    mRelayTwoTextResultButton.setEnabled(true);
                }
            }
            if (gpioJni.ioctl_gpio(3, 1, 1) == 1){
                mRelayTwoSwitch.setChecked(true);
                android.util.Log.i(TAG,"get one high");
                mRelayTwoTextResult.setText("high");
                mRelayTwoTextResult.setTextColor(mContext.getResources().getColor(R.color.Green));
            }else {
                mRelayTwoSwitch.setChecked(false);
                android.util.Log.i(TAG,"get one low");
                mRelayTwoTextResult.setText("low");
                mRelayTwoTextResult.setTextColor(mContext.getResources().getColor(R.color.red));
            }
        });


        //IO change
        mIOneSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                gpioJni.ioctl_gpio(0, 0, 1);
                android.util.Log.i(TAG,"set one high");
                if (gpioJni.ioctl_gpio(0, 1, 1) == 1){
                    mIOneText.setTextColor(mContext.getResources().getColor(R.color.Green));
                    mIOneTextResultButton.setEnabled(true);
                }
            } else {
                gpioJni.ioctl_gpio(0, 0, 0);
                android.util.Log.i(TAG,"set one low");
                if (gpioJni.ioctl_gpio(0, 1, 1) == 0){
                    mIOneText.setTextColor(mContext.getResources().getColor(R.color.Green));
                    mIOneTextResultButton.setEnabled(true);
                }
            }
            if (gpioJni.ioctl_gpio(0, 1, 1) == 1){
                mIOneSwitch.setChecked(true);
                android.util.Log.i(TAG,"get one high");
                mIOneTextResult.setText("high");
                mIOneTextResult.setTextColor(mContext.getResources().getColor(R.color.Green));
            }else {
                mIOneSwitch.setChecked(false);
                android.util.Log.i(TAG,"get one low");
                mIOneTextResult.setText("low");
                mIOneTextResult.setTextColor(mContext.getResources().getColor(R.color.red));
            }
        });

        mIOTwoSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                gpioJni.ioctl_gpio(1, 0, 1);
                if (gpioJni.ioctl_gpio(1, 1, 1) == 1){
                    mIOTwoText.setTextColor(mContext.getResources().getColor(R.color.Green));
                    mIOTwoTextResultButton.setEnabled(true);
                }
            } else {
                gpioJni.ioctl_gpio(1, 0, 0);
                if (gpioJni.ioctl_gpio(1, 1, 1) == 0){
                    mIOTwoText.setTextColor(mContext.getResources().getColor(R.color.Green));
                    mIOTwoTextResultButton.setEnabled(true);
                }
            }
            //IO 2
            if (gpioJni.ioctl_gpio(1, 1, 1) == 1){
                mIOTwoSwitch.setChecked(true);
                mIOTwoTextResult.setText("high");
                mIOTwoTextResult.setTextColor(mContext.getResources().getColor(R.color.Green));
            }else {
                mIOTwoSwitch.setChecked(false);
                mIOTwoTextResult.setText("low");
                mIOTwoTextResult.setTextColor(mContext.getResources().getColor(R.color.red));
            }
        });

    }

    public void initView(){
        //IO 1
        if (gpioJni.ioctl_gpio(0, 1, 1) == 1){
            mIOneSwitch.setChecked(true);
            mIOneTextResult.setText("high");
            mIOneTextResult.setTextColor(mContext.getResources().getColor(R.color.Green));
        }else {
            mIOneSwitch.setChecked(false);
            mIOneTextResult.setText("low");
            mIOneTextResult.setTextColor(mContext.getResources().getColor(R.color.red));
        }

        //IO 2
        if (gpioJni.ioctl_gpio(1, 1, 1) == 1){
            mIOTwoSwitch.setChecked(true);
            mIOTwoTextResult.setText("high");
            mIOTwoTextResult.setTextColor(mContext.getResources().getColor(R.color.Green));
        }else {
            mIOTwoSwitch.setChecked(false);
            mIOTwoTextResult.setText("low");
            mIOTwoTextResult.setTextColor(mContext.getResources().getColor(R.color.red));
        }

        mRelayOneTextResultButton.setEnabled(false);
        mRelayTwoTextResultButton.setEnabled(false);
        mIOneTextResultButton.setEnabled(false);
        mIOTwoTextResultButton.setEnabled(false);
        mIRTextResultButton.setEnabled(false);
        mTtyS5TextResultButton.setEnabled(false);
        mTtyS9TextResultButton.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mRelayOneTextResultButton)) {
            mTextButtonRy1 = 1;
            mRelayOneTextResultButton.setTextColor(mContext.getResources().getColor(R.color.Green));

        } else if (v.equals(mRelayTwoTextResultButton)) {
            mTextButtonRy2 = 1;
            mRelayTwoTextResultButton.setTextColor(mContext.getResources().getColor(R.color.Green));

        } else if (v.equals(mIOneTextResultButton)) {
            mTextButtonIO1 = 1;
            mIOneTextResultButton.setTextColor(mContext.getResources().getColor(R.color.Green));

        } else if (v.equals(mIOTwoTextResultButton)) {
            mTextButtonIO2 = 1;
            mIOTwoTextResultButton.setTextColor(mContext.getResources().getColor(R.color.Green));

        } else if (v.equals(mIRTextResultButton)) {
            mTextButtonIR = 1;
            mIRTextResultButton.setTextColor(mContext.getResources().getColor(R.color.Green));

        } else if (v.equals(mTtyS5TextResultButton)) {
            mTextButtonTty5 = 1;
            mTtyS5TextResultButton.setTextColor(mContext.getResources().getColor(R.color.Green));

        } else if (v.equals(mTtyS9TextResultButton)) {
            mTextButtonTty9 = 1;
            mTtyS9TextResultButton.setTextColor(mContext.getResources().getColor(R.color.Green));

        }
    }

    private Runnable mTask_tty5 = new Runnable() {
        public void run() {
            mTtyS5Handler.sendEmptyMessage(MSG_SCAN);
            nTtyS5Handler.postDelayed(mTask_tty5, 10000);
        }
    };

    private Runnable mTask_tty9 = new Runnable() {
        public void run() {
            mTtyS9Handler.sendEmptyMessage(MSG_SCAN);
            nTtyS9Handler.postDelayed(mTask_tty9, 10000);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @SuppressLint("HandlerLeak")
    Handler mTtyS5Handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SCAN:
                    new Thread(new Runnable(){
                        @Override
                        public void run(){
                            int ttys5_return = uart(5);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (ttys5_return == 1) {
                                        mTtyS5Text.setTextColor(mContext.getResources().getColor(R.color.Green));
                                        mRSOneButton.setTextSize(30);
                                        nTtyS5Handler.removeCallbacks(mTask_tty5);
                                        mRSOneButton.setText("pass");
                                        mTtyS5TextResultButton.setEnabled(true);
                                    }else{
                                        mRSOneButton.setTextSize(30);
                                        mRSOneButton.setText("fail");
                                    }
                                }
                            });
                        }
                    }).start();
                    break;
            }
        }
    };

    @SuppressLint("HandlerLeak")
    Handler mTtyS9Handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SCAN:
                    new Thread(new Runnable(){
                        @Override
                        public void run(){
                            int ttys9_return = uart(9);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (ttys9_return == 1) {
                                        mTtyS9Text.setTextColor(mContext.getResources().getColor(R.color.Green));
                                        mRSTwoButton.setText("pass");
                                        mRSTwoButton.setTextSize(30);
                                        nTtyS9Handler.removeCallbacks(mTask_tty9);
                                        mTtyS9TextResultButton.setEnabled(true);
                                    }else{
                                        mRSTwoButton.setTextSize(30);
                                        mRSTwoButton.setText("fail");
                                    }
                                }
                            });
                        }
                    }).start();
                    break;
            }
        }
    };

    /**
     public static final int KEYCODE_DPAD_UP         = 19;
     public static final int KEYCODE_DPAD_DOWN       = 20;
     public static final int KEYCODE_DPAD_LEFT       = 21;
     public static final int KEYCODE_DPAD_RIGHT      = 22;
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        switch (keyCode){
            case 19:
                mIRButton.setText("upper");
                mIRButton.setTextSize(30);
                mIRText.setTextColor(mContext.getResources().getColor(R.color.Green));
                mIRTextResultButton.setEnabled(true);
                break;
            case 20:
                mIRButton.setText("below");
                mIRButton.setTextSize(30);
                mIRText.setTextColor(mContext.getResources().getColor(R.color.Green));
                mIRTextResultButton.setEnabled(true);
                break;
            case 21:
                mIRButton.setText("left");
                mIRButton.setTextSize(30);
                mIRText.setTextColor(mContext.getResources().getColor(R.color.Green));
                mIRTextResultButton.setEnabled(true);
                break;
            case 22:
                mIRButton.setText("right");
                mIRButton.setTextSize(30);
                mIRText.setTextColor(mContext.getResources().getColor(R.color.Green));
                mIRTextResultButton.setEnabled(true);
                break;
            case 66:
                mIRButton.setText("OK");
                mIRButton.setTextSize(30);
                mIRText.setTextColor(mContext.getResources().getColor(R.color.Green));
                mIRTextResultButton.setEnabled(true);
                break;
        }
        Log.i("gulukai","keyCode-----"+keyCode);
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTtyS5Text.removeCallbacks(mTask_tty5);
        nTtyS9Handler.removeCallbacks(mTask_tty9);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTtyS5Text.removeCallbacks(mTask_tty5);
        nTtyS9Handler.removeCallbacks(mTask_tty9);
    }
}