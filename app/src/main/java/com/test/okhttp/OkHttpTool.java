package com.test.okhttp;

import com.github.theokhttp.TheOkHttpCallback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/***
 *   created by android on 2019/9/19
 */
public class OkHttpTool {
    public static <T> void request(String url,MyCallback<T> callback){
        OkHttpClient client=new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        builder.get().url(url);
        client.newCall(builder.build()).enqueue(callback);
    }
}
