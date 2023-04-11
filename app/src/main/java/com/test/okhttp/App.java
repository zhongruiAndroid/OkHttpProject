package com.test.okhttp;

import android.app.Application;

import com.github.theokhttp.TheOkHttp;

/***
 *   created by zhongrui on 2019/9/23
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        TheOkHttp.initClient().create();
//        TheOkHttp.initClient(null);
        TheOkHttp.initClient(null);
        TheOkHttp.setNetworkHelper(this);
        TheOkHttp.setDebug(true);
        TheOkHttp.addIgnoreContentSubType("jpeg");
    }
}
