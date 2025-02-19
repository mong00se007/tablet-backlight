//
// Created by xiao_ on 2018/5/15.
//
#include <com_example_elcapi_jnielc.h>
#include <jni.h>
#include <string.h>
#include <fcntl.h>
#include <android/log.h>
#include <stdio.h>
#include <unistd.h>
#include <sys/ioctl.h>
#include <sys/types.h>
#include <sys/stat.h>

#define LOG_TAG "jnielc"
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, fmt, ##args)

int fd,ret;
int fp;

#define LED_OFF 0x99





JNIEXPORT jint JNICALL Java_com_example_elcapi_jnielc_ledoff
          (JNIEnv *env, jclass object){
           ret=ioctl(fp,LED_OFF,0);
           return ret;
        }

JNIEXPORT jint JNICALL Java_com_example_elcapi_jnielc_ledseek
    (JNIEnv *env, jclass object, jint flag , jint progress){
     // fp=open("/dev/ledjni",O_RDONLY | O_NOCTTY);
      ret=ioctl(fp,flag,progress);
      //close(fp);
      return ret;
    }

JNIEXPORT jint JNICALL Java_com_example_elcapi_jnielc_seekstart
    (JNIEnv *env, jclass object){
     fp=open("/dev/ledjni",O_RDONLY | O_NOCTTY);
      LOGE("xiao open ledjni fp=%d",fp);
     return fp;
    }

JNIEXPORT jint JNICALL Java_com_example_elcapi_jnielc_seekstop
    (JNIEnv *env, jclass object){
     close(fp);
     return fp;
    }