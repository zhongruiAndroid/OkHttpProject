package com.test.okhttp;

import android.app.Application;

import com.github.theokhttp.NetworkUtils;
import com.github.theokhttp.TheOkHttp;

/***
 *   created by zhongrui on 2019/9/23
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NetworkUtils.init(this);
        TheOkHttp.init();
        TheOkHttp.setDebug(BuildConfig.DEBUG);
        TheOkHttp.addIgnoreContentSubType("jpeg");
    }
}
