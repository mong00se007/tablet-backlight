package com.example.ttysprint;

public class TTY {
    public final static native int print(String ttys);
    public final static native int uart(int uarts);
    static {
        System.loadLibrary("ttyprint");
    }
}
