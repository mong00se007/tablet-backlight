package com.elc.smt_test;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Author elc_zhangfei
 * Time 2020/8/17
 */
public class VirtualTerminal {
    private static final String TAG = "VirtualTerminal";

    private final Object mReadLock = new Object();
    private final Object mWriteLock = new Object();
    private Process mProcess = null;

    private DataOutputStream mOutputStream;
    private ByteArrayOutputStream mInputBuffer = new ByteArrayOutputStream();
    private ByteArrayOutputStream mErrBuffer = new ByteArrayOutputStream();
    private Listener listener;
    private InputReaderThread mInputReaderThread;
    private InputReaderThread mErrReaderThread;

    private String END_COMMAND = "echo [$USER]`pwd`:RET=$?\n";

    private String lastPath;
    private String id;


    public VirtualTerminal(String id, String shell, String rootPath) throws IOException, InterruptedException {
        Log.d(TAG, "VirtualTerminal id: " + id + ",shell:" + shell + ",rootPath:" + rootPath);
        this.id = id;
        mProcess = Runtime.getRuntime().exec(shell);
        mOutputStream = new DataOutputStream(mProcess.getOutputStream());
        mOutputStream.writeBytes("logcat -G 2M\n");
        if (!TextUtils.isEmpty(rootPath)) {
            mOutputStream.writeBytes("cd " + rootPath + "\n");
            lastPath = rootPath;
        }
        mOutputStream.writeBytes(END_COMMAND);
        mOutputStream.flush();
        mInputReaderThread = new InputReaderThread(mProcess.getInputStream(), "input");
        mErrReaderThread = new InputReaderThread(mProcess.getErrorStream(), "error");
        Thread.sleep(50);
        mInputReaderThread.start();
        mErrReaderThread.start();
    }

    public void runCommand(String command) throws Exception {
        synchronized (mWriteLock) {
            mInputBuffer.reset();
            mErrBuffer.reset();
        }

        // $? 表示最后运行的命令的结束代码（返回值）
        mOutputStream.writeBytes(command + "\n" + END_COMMAND);
        mOutputStream.flush();
        synchronized (mWriteLock) {
            mWriteLock.notifyAll();
        }
    }

    public void shutdown() {
        Log.d(TAG, "shutdown: ");
        mInputReaderThread.interrupt();
        mErrReaderThread.interrupt();
        listener = null;
        mProcess.destroy();

//        ProcessHelp.processRecycle(); 难以处理
    }


    public class InputReaderThread extends Thread {
        private BufferedReader mInputStream;
        private String from;

        public InputReaderThread(InputStream i, String from) {
            this.mInputStream = new BufferedReader(new InputStreamReader(i));
            this.from = from;
        }

        @Override
        public void run() {
            if (mInputStream != null) {
                try {
                    while (true) {
                        final String readLine = mInputStream.readLine();
                        if (readLine == null) {
                            Log.d(TAG, "TTT run +" + from + " readLine is null  to wait");
                            synchronized (mWriteLock) {
                                mWriteLock.wait();
                            }
                            continue;
                        }
                        if (listener != null)
                            listener.onCommandLineResult(new VTCommandLineResult(readLine));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public interface Listener {
        void onCommandLineResult(VTCommandLineResult vtCommandLineResult);
    }

    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    /**
     * A result wrapper for exec()
     */
    public class VTCommandLineResult {
        public int exitValue = -1;
        public String lineData;
        public boolean endFlag;

        VTCommandLineResult(String lineData) {
            this.lineData = lineData;
            parse(lineData);
        }


        private void parse(String lineData) {
            if (TextUtils.isEmpty(this.lineData))
                return;
            if (lineData.contains(":RET=")) {
                endFlag = true;
                if (lineData.contains(":RET=EOF")) {
//                    Log.w(TAG, "exec:[eof]" + lineData);
                }
                if (lineData.contains(":RET=0")) {
//                    Log.w(TAG, "exec:[ok]" + lineData);
                    exitValue = 0;

                } else {
//                    Log.w(TAG, "exec:[err]" + lineData);
                    exitValue = 1;
                    //可以具体些
                }
                this.lineData = lineData.substring(0, lineData.indexOf(":RET="));
                this.lineData = this.lineData.replaceAll("\\[\\]","[root]");//部分设备
                if (this.lineData.indexOf("]") != -1)
                    lastPath = this.lineData.substring(this.lineData.indexOf("]") + 1);
            }
        }

        public boolean success() {
            return exitValue == 0;
        }

        @Override
        public String toString() {
            return "VTCommandLineResult{" +
                    "exitValue=" + exitValue +
                    ", lineData='" + lineData + '\'' +
                    ", endFlag=" + endFlag +
                    '}';
        }
    }

    public String getId() {
        return id;
    }

    public String getLastPath() {
        return lastPath;
    }
}
