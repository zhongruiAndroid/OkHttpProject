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
        TheOkHttp.init();
        TheOkHttp.setNetworkHelper(this);
        TheOkHttp.setDebug(BuildConfig.DEBUG);
        TheOkHttp.addIgnoreContentSubType("jpeg");
    }
}
