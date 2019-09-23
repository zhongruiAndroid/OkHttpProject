package com.github.theokhttp;

import android.net.Uri;

import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

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
    private static TheOkHttp getSingle() {
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
        TheOkHttp.getSingle().okHttpClient=httpClient;
    }
    public OkHttpClient getClient(){
        return okHttpClient;
    }
    public static TheClientBuilder newClient(){
        return new TheClientBuilder();
    }
    /*-----------------------------------FormBody-----------------------------------------*/
    public static TheRequestBuilder postForm(Map<String,Object>map){
        return postForm(map,false);
    }
    public static TheRequestBuilder postForm(Map<String,Object>map,boolean paramEncode){
        return postForm(getFormBodyBuilder(map,paramEncode).build());
    }
    public static TheRequestBuilder postForm(FormBody formBody){
        return TheRequestBuilder.newInstance().post(formBody);
    }
    /*-----------------------------------MultipartBody-----------------------------------------*/
    public static TheRequestBuilder postMultipart(MultipartBody multipartBody){
        return TheRequestBuilder.newInstance().post(multipartBody);
    }
    /*-----------------------------------RequestBody-----------------------------------------*/
    public static TheRequestBuilder post(Map<String,Object>map){
        JSONObject jsonObject=new JSONObject(map);
        return post(jsonObject.toString());
    }
    public static TheRequestBuilder post(String json){
        RequestBody requestBody = RequestBody.create(MediaType.get("application/json;charset=utf-8"), json);
        return TheRequestBuilder.newInstance().post(requestBody);
    }
    public static TheRequestBuilder post(RequestBody requestBody){
        return TheRequestBuilder.newInstance().post(requestBody);
    }
    /**********************************************************************************************/

    public static <T extends Callback>void start(Map<String,Object>map,String url,T callback){
        Uri.Builder uri=new Uri.Builder();
        uri.path(url);
        if(map!=null){
            for (Map.Entry<String,Object> entry:map.entrySet()){
                uri.appendQueryParameter(entry.getKey(),String.valueOf(entry.getValue()));
            }
        }
        start(uri.toString(),callback);
    }
    public static <T extends Callback>void start(String url,T callback){
        TheRequestBuilder theRequestBuilder = TheRequestBuilder.newInstance();
        theRequestBuilder.get();
        theRequestBuilder.url(url);
        TheOkHttp.getSingle().okHttpClient.newCall(theRequestBuilder.build()).enqueue(callback);
    }

    /*************************************************helper*********************************************/
    private static FormBody.Builder getFormBodyBuilder(Map<String,Object>map,boolean paramEncode){
        FormBody.Builder builder=new FormBody.Builder();
        if(map==null){
            return builder;
        }
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if(paramEncode){
                builder.addEncoded(entry.getKey(),String.valueOf(entry.getValue()));
            }else{
                builder.add(entry.getKey(),String.valueOf(entry.getValue()));
            }
        }
        return builder;
    }
}
