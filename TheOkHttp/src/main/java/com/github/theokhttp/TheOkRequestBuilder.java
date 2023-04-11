package com.github.theokhttp;

import android.net.Uri;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Set;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/***
 *   created by android on 2019/9/20
 */
public class TheOkRequestBuilder {
    private OkHttpClient okHttpClient;
    private int retryCount = 0;
    private Request.Builder builder;
//    private String requestMethod=TheOkHttpConfig.POST;

    private Map queryParamsMap;

    public TheOkRequestBuilder() {
        this.builder = new Request.Builder();
    }

    public static TheOkRequestBuilder newInstance() {
        return new TheOkRequestBuilder();
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

    public TheOkRequestBuilder post(RequestBody body) {
        builder.post(body);
        return this;
    }

    public TheOkRequestBuilder get() {
        builder.get();
        return this;
    }

    protected TheOkRequestBuilder url(HttpUrl url) {
        builder.url(url);
        return this;
    }

    protected TheOkRequestBuilder url(String url) {
        builder.url(url);
        return this;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public TheOkRequestBuilder setRetryCount(int retryCount) {
        this.retryCount = retryCount;
        return this;
    }

    protected TheOkRequestBuilder url(URL url) {
        builder.url(url);
        return this;
    }

    public TheOkRequestBuilder method(String method, RequestBody body) {
        if(method!=null){
            method=method.toUpperCase();
        }
        builder.method(method, body);
        return this;
    }

    public TheOkRequestBuilder addHeader(String name, String value) {
        builder.addHeader(name, value);
        return this;
    }

    public TheOkRequestBuilder cacheControl(CacheControl cacheControl) {
        builder.cacheControl(cacheControl);
        return this;
    }

    public TheOkRequestBuilder delete() {
        builder.delete();
        return this;
    }

    public TheOkRequestBuilder delete(RequestBody body) {
        builder.delete(body);
        return this;
    }

    public TheOkRequestBuilder head() {
        builder.head();
        return this;
    }

    public TheOkRequestBuilder header(String name, String value) {
        builder.header(name, value);
        return this;
    }

    public TheOkRequestBuilder headers(Headers headers) {
        builder.headers(headers);
        return this;
    }

    public TheOkRequestBuilder patch(RequestBody body) {
        builder.patch(body);
        return this;
    }

    public TheOkRequestBuilder put(RequestBody body) {
        builder.put(body);
        return this;
    }

    public TheOkRequestBuilder removeHeader(String name) {
        builder.removeHeader(name);
        return this;
    }

    public TheOkRequestBuilder tag(Object tag) {
        builder.tag(tag);
        return this;
    }

    public <T> TheOkRequestBuilder tag(Class<? super T> type, T tag) {
        builder.tag(type, tag);
        return this;
    }

    public Request build() {
        return builder.build();
    }

    public <T extends Callback> void start(HttpUrl url, T callback) {
        newCall(url(url).build(), callback);
    }

    public <T extends Callback> void start(URL url, T callback) {
        newCall(url(url).build(), callback);
    }

    public <T extends Callback> void start(String url, T callback) {
        String newUrl = url;
        if (queryParamsMap != null) {
            Uri.Builder uri = new Uri.Builder();
            uri.encodedPath(url);

            Set<String> keySet = queryParamsMap.keySet();
            for (String key : keySet) {
                uri.appendQueryParameter(key, String.valueOf(queryParamsMap.get(key)));
            }
            newUrl = uri.toString();
        }
        newCall(url(newUrl).build(), callback);
    }

    private <T extends Callback> void newCall(Request requestBuild, T callback) {
        final int[] retryNum = new int[]{0};
        getOkHttpClient().newCall(requestBuild).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callback == null) {
                    return;
                }
                if (retryCount > 0) {
                    retryNum[0]++;
                    if (retryNum[0] <= retryCount) {
                        getOkHttpClient().newCall(requestBuild);
                    } else {
                        callback.onFailure(call, e);
                    }
                } else {
                    callback.onFailure(call, e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (callback != null) {
                    callback.onResponse(call, response);
                }
            }
        });
    }

    public <T>TheOkResponse<T> execute(String url) {
        return execute(url, false);
    }

    public <T>TheOkResponse<T> execute(String url, boolean needCloneResponseString) {
        String newUrl = url;
        if (queryParamsMap != null) {
            Uri.Builder uri = new Uri.Builder();
            uri.encodedPath(url);

            Set<String> keySet = queryParamsMap.keySet();
            for (String key : keySet) {
                uri.appendQueryParameter(key, String.valueOf(queryParamsMap.get(key)));
            }
            newUrl = uri.toString();
        }

        return TheOkResponse.execute(getOkHttpClient().newCall(url(newUrl).build()),needCloneResponseString);
    }

    public TheOkRequestBuilder setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
        TheOkClientManager.get().add(okHttpClient);
        return this;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient == null ? TheOkHttp.single().getDefClient() : okHttpClient;
    }

    public TheOkRequestBuilder queryParamsMap(Map paramsMap) {
        if (paramsMap == null) {
            return this;
        }
        this.queryParamsMap = paramsMap;
        return this;
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
