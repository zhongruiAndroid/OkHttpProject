package com.test.okhttp;

import android.app.Application;

import com.github.theokhttp.NetworkUtils;
import com.github.theokhttp.TheOkHttp;

import java.util.concurrent.TimeUnit;

/***
 *   created by zhongrui on 2019/9/23
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NetworkUtils.init(this);
        TheOkHttp.init().connectTimeout(10,TimeUnit.SECONDS).complete();
        TheOkHttp.setDebug(BuildConfig.DEBUG);
    }
}
