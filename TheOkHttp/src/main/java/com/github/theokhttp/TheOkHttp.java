package com.github.theokhttp;

import android.net.Uri;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSource;

/***
 *   created by android on 2019/9/19
 */
public class TheOkHttp {
    private final String TAG=this.getClass().getSimpleName()+"==";
    private static TheOkHttp singleObj;
    private OkHttpClient okHttpClient;
    private boolean isDebug;
    private TheOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(TheOkHttpConfig.HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TheOkHttpConfig.HTTP_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TheOkHttpConfig.HTTP_READ_TIMEOUT, TimeUnit.SECONDS);
        if(getAppInterceptor()!=null){
            builder.addInterceptor(getAppInterceptor());
        }
        if(getNetworkInterceptor()!=null){
            builder.addNetworkInterceptor(getNetworkInterceptor());
        }
        okHttpClient=builder.build();
        TheClientManager.get().add(okHttpClient);
    }

    private Interceptor appInterceptor=new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if(isDebug()){
                long startTime = System.nanoTime();
                Response response = chain.proceed(request);
                long endTime = System.nanoTime();

                String params="";
                if(request.body()!=null){
                    Request copyRequest = request.newBuilder().build();
                    Buffer buffer=new Buffer();
                    copyRequest.body().writeTo(buffer);
                    params="params->"+buffer.readUtf8();
                }

                BufferedSource source = response.body().source();
                source.request(Long.MAX_VALUE);
                Buffer buffer = source.getBuffer();
                String resultString= buffer.clone().readString(Charset.forName("UTF-8"));

                double timeInterval=(endTime-startTime)/1e6d;
                String msg = "\nurl->" + request.url()
                        + "\nmethod->"+request.method()
                        + "\ntime->" + timeInterval+"ms"
                        + "\nheaders->" + request.headers()
                        + "\nresponse code->" + response.code()
                        + "\nresponse headers->" + response.headers()
                        + "\nresponse body->" + resultString;

                LG.print(Log.ERROR,TAG,params+msg,false);

                return response;
            }
            return chain.proceed(request);
        }
    };
    private Interceptor networkInterceptor;
    public Interceptor getAppInterceptor() {
        return appInterceptor;
    }
    public void setAppInterceptor(Interceptor appInterceptor) {
        this.appInterceptor = appInterceptor;
    }
    public void setNetworkInterceptor(Interceptor networkInterceptor) {
        this.networkInterceptor = networkInterceptor;
    }

    public Interceptor getNetworkInterceptor() {
        return networkInterceptor;
    }

    public static void setDebug(boolean debug) {
        getSingle().isDebug = debug;
    }
    public  static  boolean isDebug() {
        return getSingle().isDebug;
    }

    protected static TheOkHttp getSingle() {
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
        init(httpClient,false);
    }
    public static void init(OkHttpClient httpClient,boolean isDebug){
        TheOkHttp.getSingle().okHttpClient=httpClient;
        TheClientManager.get().add(httpClient);
    }
    public OkHttpClient getClient(){
        return okHttpClient;
    }
  /*  public static TheClientBuilder newClient(){
        return new TheClientBuilder();
    }*/
    /*-----------------------------------FormBody-----------------------------------------*/
    public static TheRequestBuilder postForm(){
        return TheRequestBuilder.newInstance().post(new FormBody.Builder().build());
    }
    public static TheRequestBuilder postForm(Map<String,Object>map){
        return postForm(map,false);
    }
    public static TheRequestBuilder postForm(Map<String,Object>map,boolean paramEncode){
        return postForm(getFormBodyBuilder(map,paramEncode).build());
    }
    public static TheRequestBuilder postForm(FormBody formBody){
        return TheRequestBuilder.newInstance().post(formBody);
    }

    private static <T extends Callback>void buildRequest(Map<String,Object>map,String url,T callback){

    }
    /*-----------------------------------GET-----------------------------------------*/

    public static <T extends Callback>void startGet(Map<String,Object>map,String url,T callback){
        Uri.Builder uri=new Uri.Builder();
        uri.path(url);
        if(map!=null){
            for (Map.Entry<String,Object> entry:map.entrySet()){
                uri.appendQueryParameter(entry.getKey(),String.valueOf(entry.getValue()));
            }
        }
        startGet(uri.toString(),callback);
    }
    public static <T extends Callback>void startGet(String url,T callback){
        TheRequestBuilder theRequestBuilder = TheRequestBuilder.newInstance();
        theRequestBuilder.get();
        theRequestBuilder.url(url);
        TheOkHttp.getSingle().okHttpClient.newCall(theRequestBuilder.build()).enqueue(callback);
    }
    /*-----------------------------------MultipartBody-----------------------------------------*/
    public static TheRequestBuilder postMultipart(MultipartBody multipartBody){
        return TheRequestBuilder.newInstance().post(multipartBody);
    }
    /*-----------------------------------RequestBody-----------------------------------------*/
    public static TheRequestBuilder post(){
        return post("");
    }
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
