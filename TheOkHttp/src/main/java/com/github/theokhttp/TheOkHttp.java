package com.github.theokhttp;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
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
    private List<String> ignoreContentSubType = new ArrayList<>();
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
        TheOkClientManager.get().add(getDefClient());
    }
    public OkHttpClient getDefClient(){
        if(okHttpClient==null){
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(TheOkHttpConfig.HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(TheOkHttpConfig.HTTP_WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(TheOkHttpConfig.HTTP_READ_TIMEOUT, TimeUnit.SECONDS)
                    .addInterceptor(TheOkClientBuilder.appInterceptor).build();
        }
        return okHttpClient;
    }

    public static void addIgnoreContentSubType(String contentType) {
        TheOkHttp.single().ignoreContentSubType.add(contentType);
    }

    public static void addIgnoreContentSubType(List<String> contentType) {
        TheOkHttp.single().ignoreContentSubType.addAll(contentType);
    }

    public List<String> getIgnoreContentSubType() {
        return ignoreContentSubType;
    }

    public static void setDebug(boolean debug) {
        setDebug(debug,"TheOkHttp==");
    }
    public static void setDebug(boolean debug,String logTag) {
        single().isDebug = debug;
        LG.TAG=logTag;
    }
    public static boolean isDebug() {
        return single().isDebug;
    }

    public static void setNetworkHelper(Context context) {
        TheOkHttpNetworkUtils.init(context);
    }

    public static void initClient(OkHttpClient httpClient) {
        TheOkHttp.single().setSingleClient(httpClient);
    }

    public static void initClient() {
        new TheOkClientBuilder().complete();
    }

    public void setSingleClient(OkHttpClient client) {
        if(client==null){
            return;
        }
        if(okHttpClient!=null){
            TheOkClientManager.get().remove(okHttpClient);
        }
        okHttpClient = client;
        TheOkClientManager.get().add(okHttpClient);
    }


    /*-----------------------------------FormBody-----------------------------------------*/
    public static TheOkRequestBuilder postForm() {
        return TheOkRequestBuilder.newInstance().post(new FormBody.Builder().build());
    }

    public static TheOkRequestBuilder postForm(Map map) {
        return postForm(map, false);
    }

    public static TheOkRequestBuilder postForm(Map map, boolean paramEncode) {
        return postForm(getFormBodyBuilder(map, paramEncode).build());
    }

    public static TheOkRequestBuilder postForm(FormBody formBody) {
        return TheOkRequestBuilder.newInstance().post(formBody);
    }

    /*-----------------------------------GET-----------------------------------------*/

    public static <T extends Callback> void startGet(Map map, String url, T callback) {
        Uri.Builder uri = new Uri.Builder();
        uri.encodedPath(url);
        if (map != null) {
            Set<String> keySet = map.keySet();
            for (String key : keySet) {
                uri.appendQueryParameter(key, String.valueOf(map.get(key)));
            }
        }
        startGet(uri.toString(), callback);
    }

    public static <T extends Callback> void startGet(String url, T callback) {
        TheOkRequestBuilder theRequestBuilder = TheOkRequestBuilder.newInstance();
        theRequestBuilder.url(url);
        theRequestBuilder.get();
        TheOkHttp.single().getDefClient().newCall(theRequestBuilder.build()).enqueue(callback);
    }

    public static TheOkRequestBuilder get() {
        return TheOkRequestBuilder.newInstance().get();
    }

    public static TheOkRequestBuilder get(Map map) {
        return TheOkRequestBuilder.newInstance().get().queryParamsMap(map);
    }

    /*-----------------------------------MultipartBody-----------------------------------------*/
    public static TheOkRequestBuilder postMultipart(MultipartBody multipartBody) {
        return TheOkRequestBuilder.newInstance().post(multipartBody);
    }

    /*-----------------------------------RequestBody-----------------------------------------*/
    public static TheOkRequestBuilder post() {
        return post("");
    }

    public static TheOkRequestBuilder post(Map map) {
        JSONObject jsonObject = new JSONObject(map);
        return post(jsonObject.toString());
    }

    public static TheOkRequestBuilder post(String json) {
        RequestBody requestBody = RequestBody.create(MediaType.get("application/json;charset=utf-8"), json);
        return TheOkRequestBuilder.newInstance().post(requestBody);
    }

    public static TheOkRequestBuilder post(RequestBody requestBody) {
        return TheOkRequestBuilder.newInstance().post(requestBody);
    }
    /**********************************************************************************************/


    /*************************************************helper*********************************************/
    private static FormBody.Builder getFormBodyBuilder(Map map, boolean paramEncode) {
        FormBody.Builder builder = new FormBody.Builder();
        if (map == null) {
            return builder;
        }
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            if (paramEncode) {
                builder.addEncoded(key, String.valueOf(map.get(key)));
            } else {
                builder.add(key, String.valueOf(map.get(key)));
            }
        }
        return builder;
    }
}
