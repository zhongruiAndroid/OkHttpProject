package com.github.theokhttp;

import android.util.Log;

import java.io.IOException;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.nio.charset.Charset;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;


/***
 *   created by android on 2019/9/19
 */
public abstract class TheOkHttpCallback implements Callback {
    public abstract void response(String response);
    public abstract void failure(Exception e);
    public void getCall(Object object) {}
    @Override
    public void onFailure(Call call, IOException e) {
        getCall(call);
        failure(e);
    }
    @Override
    public void onResponse(Call call, Response response) throws IOException {
        getCall(call);
        String resultString;
        if(true){
            resultString=response.body().string();
        }else{
            BufferedSource source = response.body().source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.getBuffer();
            resultString= buffer.clone().readString(Charset.forName("UTF-8"));
        }
        response(resultString);
    }
    /*private Type getType(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            return null;
        }
        if(superclass instanceof ParameterizedType){
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return parameterized.getActualTypeArguments()[0];
        }
        return null;
    }*/
}
