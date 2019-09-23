package com.github.theokhttp;

import java.net.URL;

import okhttp3.CacheControl;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/***
 *   created by android on 2019/9/20
 */
public class TheRequestBuilder {
    private OkHttpClient okHttpClient;

    private Request.Builder builder;
//    private String requestMethod=TheOkHttpConfig.POST;

    public TheRequestBuilder() {
        this.builder = new Request.Builder();
    }

    public static TheRequestBuilder newInstance() {
        return new TheRequestBuilder();
    }

//    public TheRequestBuilder setRequestMethod(String requestMethod) {
//        this.requestMethod = requestMethod;
//        return this;
//    }

    private void test() {
        /*Request.Builder builder=new Request.Builder()
                .post()
                .get()
                .url()
                .url()
                .url()
                .method()
                .addHeader()
                .cacheControl()
                .delete()
                .delete()
                .head()
                .header()
                .headers()
                .patch()
                .put()
                .removeHeader()
                .tag()
                .tag()*/
    }

    public TheRequestBuilder post(RequestBody body) {
        builder.post(body);
        return this;
    }

    public TheRequestBuilder get() {
        builder.get();
        return this;
    }

    protected TheRequestBuilder url(HttpUrl url) {
        builder.url(url);
        return this;
    }

    protected TheRequestBuilder url(String url) {
        builder.url(url);
        return this;
    }

    protected TheRequestBuilder url(URL url) {
        builder.url(url);
        return this;
    }

    public TheRequestBuilder method(String method, RequestBody body) {
        builder.method(method, body);
        return this;
    }

    public TheRequestBuilder addHeader(String name, String value) {
        builder.addHeader(name, value);
        return this;
    }

    public TheRequestBuilder cacheControl(CacheControl cacheControl) {
        builder.cacheControl(cacheControl);
        return this;
    }

    public TheRequestBuilder delete() {
        builder.delete();
        return this;
    }

    public TheRequestBuilder delete(RequestBody body) {
        builder.delete(body);
        return this;
    }

    public TheRequestBuilder head() {
        builder.head();
        return this;
    }

    public TheRequestBuilder header(String name, String value) {
        builder.header(name, value);
        return this;
    }

    public TheRequestBuilder headers(Headers headers) {
        builder.headers(headers);
        return this;
    }

    public TheRequestBuilder patch(RequestBody body) {
        builder.patch(body);
        return this;
    }

    public TheRequestBuilder put(RequestBody body) {
        builder.put(body);
        return this;
    }

    public TheRequestBuilder removeHeader(String name) {
        builder.removeHeader(name);
        return this;
    }

    public TheRequestBuilder tag(Object tag) {
        builder.tag(tag);
        return this;
    }

    public <T> TheRequestBuilder tag(Class<? super T> type, T tag) {
        builder.tag(type, tag);
        return this;
    }

    public Request build() {
        return builder.build();
    }

    public <T extends Callback> void start(HttpUrl url, T callback) {
        getOkHttpClient().newCall(url(url).build()).enqueue(callback);
    }

    public <T extends Callback> void start(URL url, T callback) {
        getOkHttpClient().newCall(url(url).build()).enqueue(callback);
    }

    public <T extends Callback> void start(String url, T callback) {
        getOkHttpClient().newCall(url(url).build()).enqueue(callback);
    }

    public TheRequestBuilder setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
        TheClientManager.get().add(okHttpClient);
        return this;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient==null?TheOkHttp.single().getClient():okHttpClient;
    }
    /*private void setMethod() {
        switch (requestMethod){
            case TheOkHttpConfig.GET:
                get();
            break;
            case TheOkHttpConfig.HEAD:
                head();
            break;
            case TheOkHttpConfig.POST:
                post(null);
            break;
            case TheOkHttpConfig.DELETE:
                delete();
            break;
            case TheOkHttpConfig.PUT:
                put(null);
            break;
        }
    }*/
}
