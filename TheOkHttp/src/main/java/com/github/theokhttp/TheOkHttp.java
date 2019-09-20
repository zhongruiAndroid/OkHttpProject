package com.github.theokhttp;

import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.OkHttpClient;

/***
 *   created by android on 2019/9/19
 */
public class TheOkHttp {
    private static TheOkHttp singleObj;
    private OkHttpClient okHttpClient;
    private TheOkHttp() {
        okHttpClient=new OkHttpClient.Builder()
                .connectTimeout(TheOkHttpConfig.HTTP_CONNECT_TIMEOUT,TimeUnit.SECONDS)
                .writeTimeout(TheOkHttpConfig.HTTP_WRITE_TIMEOUT,TimeUnit.SECONDS)
                .readTimeout(TheOkHttpConfig.HTTP_READ_TIMEOUT,TimeUnit.SECONDS)
                .build();
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

    public static void init(OkHttpClient httpClient){
        TheOkHttp.get().okHttpClient=httpClient;
    }
    public OkHttpClient getClient(){
        return okHttpClient;
    }
    public static TheClientBuilder newClient(String url){
        return new TheClientBuilder(url);
    }
    public static <T extends Callback>void start(String url,T callback){

    }
}
