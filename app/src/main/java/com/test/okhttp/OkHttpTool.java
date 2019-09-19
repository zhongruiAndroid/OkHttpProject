package com.test.okhttp;

import com.github.theokhttp.TheOkHttpCallback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/***
 *   created by android on 2019/9/19
 */
public class OkHttpTool {
    private static OkHttpTool singleObj;
    private OkHttpClient okHttpClient;
    private OkHttpTool() {
        okHttpClient=new OkHttpClient();
    }
    public OkHttpClient newClient(){
        return new OkHttpClient();
    }
    public OkHttpClient.Builder newClientBuilder(){
        return new OkHttpClient.Builder();
    }
    public static OkHttpTool getClient(){
        if(singleObj==null){
            synchronized (OkHttpTool.class){
                if(singleObj==null){
                    singleObj=new OkHttpTool();
                }
            }
        }
        return singleObj;
    }
    public <T> void request(String url,MyCallback<T> callback){
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        Call call = okHttpClient.newCall(builder.build());
        call.enqueue(callback);
    }

    public <T> void request2(String url,MyCallback<T> callback){
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        okHttpClient.newCall(builder.build()).enqueue(callback);
    }

    public <T> void requestImage(String url,TheOkHttpCallback<T> callback){
        Request.Builder builder = new Request.Builder();
        RequestBody body=RequestBody.create(MediaType.parse( "; charset=utf-8"),"");
        FormBody.Builder body1=new FormBody.Builder();
        builder.post(body);
        builder.url(url);
        Call call = okHttpClient.newCall(builder.build());
        call.enqueue(callback);


        OkHttpClient.Builder builder1=new OkHttpClient.Builder();
        builder1.build();
    }
}
