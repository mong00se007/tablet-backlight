package com.elc.smt_test;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import java.io.IOException;
import android.util.Log;
import android.widget.TextView;
import android.text.TextUtils;


public class SmtTemperatureHumidityActivity extends Activity {

    private static final String TAG = "SmtTemperatureHumidityActivity";
    private static TextView mSmTemperature,mSmtHumidity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smt_temperature_humidity);

        mSmTemperature = findViewById(R.id.Smt_temperature);
        mSmtHumidity = findViewById(R.id.Smt_humidity);

        try {
            VirtualTerminal mVirtualTerminal = new VirtualTerminal("1", "getevent -l", "./");
            mVirtualTerminal.setListener(new VirtualTerminal.Listener() {
                @Override
                public void onCommandLineResult(VirtualTerminal.VTCommandLineResult vtCommandLineResult) {
                    String lineData = vtCommandLineResult.lineData;

                    //湿度
                    if (!TextUtils.isEmpty(lineData) && lineData.contains("event5") && lineData.contains("EV_ABS")) {
                        Log.i(TAG, lineData);
                        String[] data = lineData.split("001d");
                        String dataString = data[1].trim();
                        String lastTwoChars = dataString.substring(dataString.length() - 2);
                        int dataLast = strToInt(lastTwoChars);
                        Log.i(TAG, "result:" + dataLast);
                        runOnUiThread(() -> {
                            mSmtHumidity.setText(dataLast + "%");
                        });
                    }

                    //温度
                    if (!TextUtils.isEmpty(lineData) && lineData.contains("event4") && lineData.contains("EV_ABS")) {
                        Log.i(TAG, lineData);
                        String[] data = lineData.split("ABS_THROTTLE");
                        String dataString = data[1].trim();
                        String lastTwoChars = dataString.substring(dataString.length() - 2);
                        int dataLast = strToInt(lastTwoChars);
                        Log.i(TAG, "result:" + dataLast);
                        runOnUiThread(() -> {
                            mSmTemperature.setText(dataLast + "℃");
                        });
                    }
                }
            });

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static int strToInt(String string) {
        return getStringNumber(String.valueOf(string.charAt(0))) * 16 +
                getStringNumber(String.valueOf(string.charAt(1)));
    }

    private static int getStringNumber(String string) {
        int temp = 0;
        switch (string) {
            case "a":
                temp = 10;
                break;
            case "b":
                temp = 11;
                break;
            case "c":
                temp = 12;
                break;
            case "d":
                temp = 13;
                break;
            case "e":
                temp = 14;
                break;
            case "f":
                temp = 15;
                break;
            default:
                temp = Integer.parseInt(string);
        }
        return temp;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}