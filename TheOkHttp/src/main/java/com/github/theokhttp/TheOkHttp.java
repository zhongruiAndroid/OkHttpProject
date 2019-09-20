package com.github.theokhttp;

import okhttp3.Callback;
import okhttp3.OkHttpClient;

/***
 *   created by android on 2019/9/19
 */
public class TheOkHttp {
    private static TheOkHttp singleObj;
    private TheOkHttp() {
    }
    public static TheOkHttp get() {
        if (singleObj == null) {
            synchronized (TheOkHttp.class) {
                if (singleObj == null) {
                    singleObj = new TheOkHttp();
                }
            }
        }
        return singleObj;
    }


    private OkHttpClient client;
    public void a(){
        OkHttpClient.Builder builder=null;
    }


    public static TheClientBuilder post(String url){
        return new TheClientBuilder(url);
    }
    public static <T extends Callback>void start(String url,T callback){

    }
}
