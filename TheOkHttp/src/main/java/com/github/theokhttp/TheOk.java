package com.github.theokhttp;

import okhttp3.OkHttpClient;

/***
 *   created by android on 2019/9/19
 */
public class TheOk {
    private static TheOk singleObj;
    private TheOk() {
    }
    public static TheOk get() {
        if (singleObj == null) {
            synchronized (TheOk.class) {
                if (singleObj == null) {
                    singleObj = new TheOk();
                }
            }
        }
        return singleObj;
    }


    private OkHttpClient client;


}
