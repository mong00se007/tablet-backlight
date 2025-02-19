package com.example.elcapi;

/**
 * Created by xiao_ on 2018/5/15.
 */

public class jnielc
{
    public final static native int ledoff();
    public final static native int seekstart();
    public final static native int seekstop();
    public final static native int ledseek(int flag, int progress);
    public static final native int open3();
    public static final native int seekstart3();
    public static final native int seekstop3();
    public static final native int ledseek3(int var0, int var1);

    static {
        System.loadLibrary("jnielc");
    }
}
