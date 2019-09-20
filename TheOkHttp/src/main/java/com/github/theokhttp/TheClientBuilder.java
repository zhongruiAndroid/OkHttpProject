package com.github.theokhttp;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.internal.Util;

import static okhttp3.internal.Util.checkDuration;

/***
 *   created by android on 2019/9/20
 */
public class TheClientBuilder {
    private final  int HTTP_CONNECT_TIMEOUT = 18;
    private final  int HTTP_WRITE_TIMEOUT = 20;
    private final  int HTTP_READ_TIMEOUT = 30;

    private int connectTimeout=HTTP_CONNECT_TIMEOUT;
    private int writeTimeout=HTTP_WRITE_TIMEOUT;
    private int readTimeout=HTTP_READ_TIMEOUT;

    private String url;
    public TheClientBuilder(String url) {
        this.url=url;
    }
    public void te(){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor()
                .addNetworkInterceptor()
                .authenticator()
                .cache()
                .sslSocketFactory()
                .callTimeout()
                .certificatePinner()
                .connectionPool()
                .connectionSpecs()
                .retryOnConnectionFailure()
                .sslSocketFactory()
                .addNetworkInterceptor()
                .build();

        OkHttpClient client = new OkHttpClient();

    }
    public TheClientBuilder connectTimeout(long timeoutSeconds) {
        return connectTimeout(timeoutSeconds,TimeUnit.SECONDS);
    }
    public TheClientBuilder writeTimeout(long timeoutSeconds) {
        return writeTimeout(timeoutSeconds,TimeUnit.SECONDS);
    }
    public TheClientBuilder readTimeout(long timeoutSeconds) {
        return readTimeout(timeoutSeconds,TimeUnit.SECONDS);
    }
    public TheClientBuilder connectTimeout(long timeout, TimeUnit unit) {
        connectTimeout = checkDuration("timeout", timeout, unit);
        return this;
    }
    public TheClientBuilder writeTimeout(long timeout, TimeUnit unit) {
        writeTimeout = checkDuration("timeout", timeout, unit);
        return this;
    }
    public TheClientBuilder readTimeout(long timeout, TimeUnit unit) {
        readTimeout = checkDuration("timeout", timeout, unit);
        return this;
    }
}
