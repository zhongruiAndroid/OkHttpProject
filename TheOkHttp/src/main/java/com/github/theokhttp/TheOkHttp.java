package com.github.theokhttp;

import android.net.Uri;

import org.json.JSONObject;

import java.util.Map;
import java.util.Set;
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
    protected static TheOkHttp single() {
        if (singleObj == null) {
            synchronized (TheOkHttp.class) {
                if (singleObj == null) {
                    singleObj = new TheOkHttp();
                }
            }
        }
        return singleObj;
    }
    private volatile OkHttpClient okHttpClient;
    private boolean isDebug;
    private TheOkHttp() {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(TheOkHttpConfig.HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TheOkHttpConfig.HTTP_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TheOkHttpConfig.HTTP_READ_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(TheClientBuilder.appInterceptor).build();
        TheClientManager.get().add(okHttpClient);
    }

    public static void setDebug(boolean debug) {
        single().isDebug = debug;
    }
    public  static  boolean isDebug() {
        return single().isDebug;
    }
    public static void init(OkHttpClient httpClient){
        TheOkHttp.single().setClient(httpClient);
    }
    public static TheClientBuilder init(){
        return new TheClientBuilder();
    }
    public OkHttpClient getClient(){
        return okHttpClient;
    }
    public void setClient(OkHttpClient client){
        TheClientManager.get().remove(okHttpClient);
        okHttpClient=client;
        TheClientManager.get().add(okHttpClient);
    }


    /*-----------------------------------FormBody-----------------------------------------*/
    public static TheRequestBuilder postForm(){
        return TheRequestBuilder.newInstance().post(new FormBody.Builder().build());
    }
    public static TheRequestBuilder postForm(Map map){
        return postForm(map,false);
    }
    public static TheRequestBuilder postForm(Map map,boolean paramEncode){
        return postForm(getFormBodyBuilder(map,paramEncode).build());
    }
    public static TheRequestBuilder postForm(FormBody formBody){
        return TheRequestBuilder.newInstance().post(formBody);
    }

    private static <T extends Callback>void buildRequest(Map map,String url,T callback){

    }
    /*-----------------------------------GET-----------------------------------------*/

    public static <T extends Callback>void startGet(Map map,String url,T callback){
        Uri.Builder uri=new Uri.Builder();
        uri.encodedPath(url);
        if(map!=null){
            Set<String> keySet = map.keySet();
            for (String key:keySet){
                uri.appendQueryParameter(key,String.valueOf(map.get(key)));
            }
        }
        startGet(uri.toString(),callback);
    }
    public static <T extends Callback>void startGet(String url,T callback){
        TheRequestBuilder theRequestBuilder = TheRequestBuilder.newInstance();
        theRequestBuilder.url(url);
        theRequestBuilder.get();
        TheOkHttp.single().okHttpClient.newCall(theRequestBuilder.build()).enqueue(callback);
    }
    /*-----------------------------------MultipartBody-----------------------------------------*/
    public static TheRequestBuilder postMultipart(MultipartBody multipartBody){
        return TheRequestBuilder.newInstance().post(multipartBody);
    }
    /*-----------------------------------RequestBody-----------------------------------------*/
    public static TheRequestBuilder post(){
        return post("");
    }
    public static TheRequestBuilder post(Map map){
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


    /*************************************************helper*********************************************/
    private static FormBody.Builder getFormBodyBuilder(Map map,boolean paramEncode){
        FormBody.Builder builder=new FormBody.Builder();
        if(map==null){
            return builder;
        }
        Set<String> keySet = map.keySet();
        for (String key:keySet) {
            if(paramEncode){
                builder.addEncoded(key,String.valueOf(map.get(key)));
            }else{
                builder.add(key,String.valueOf(map.get(key)));
            }
        }
        return builder;
    }
}
