package com.github.theokhttp;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeoutException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;


/***
 *   created by android on 2019/9/19
 */
public abstract class TheOkHttpCallback<T> implements Callback {
    public TheOkHttpCallback() {
    }

    public String onNoNetwork() {
        return "无网络连接";
    }

    public String onTimeout() {
        return "网络连接超时";
    }

    public String onHttpNotFound() {
        return "请求路径错误";
    }

    public String onHttpServerError() {
        return "服务器错误";
    }

    public boolean needCloneResponseString() {
        return false;
    }

    public abstract void response(T response);

    public abstract void failure(Exception e);


    private void giveString(ResponseBody body) throws IOException {
        String resultString;
        if (!needCloneResponseString()) {
            resultString = body.string();
        } else {
            BufferedSource source = body.source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.getBuffer();
            resultString = buffer.clone().readString(Charset.forName("UTF-8"));
        }
        response((T) resultString);
    }

    @Override
    public void onFailure(Call call, IOException e) {
        if (call.isCanceled()) {
            return;
        }
        Exception outException = e;
        if (NetworkUtils.getContext() != null && NetworkUtils.noNetwork()) {
            outException = new NoNetworkException(onNoNetwork());
        } else if (e instanceof SocketTimeoutException && e.getMessage().indexOf("after")!=-1) {
            outException = new TimeoutException(onTimeout());
        }
        failure(outException);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (call.isCanceled()) {
            return;
        }
        int httpCode = response.code();
        if(httpCode== HttpURLConnection.HTTP_NOT_FOUND){
            failure(new Exception(onHttpNotFound()));
            return;
        }else if(httpCode== HttpURLConnection.HTTP_INTERNAL_ERROR){
            failure(new Exception(onHttpServerError()));
            return;
        }else if (httpCode != 200) {
            failure(new Exception(response.message()));
            return;
        }
        ResponseBody body = response.body();
        Type type = getType(this.getClass());
        if (type == null || type == String.class || type == Object.class) {
            giveString(body);
        } else if (type == byte[].class||type.getTypeName().indexOf("byte")!=-1) {
            response((T) body.bytes());
        } else if (type == InputStream.class) {
            response((T) body.byteStream());
        } else if (type == Reader.class) {
            response((T) body.charStream());
        } else {
            giveString(body);
        }
    }

    public Type getType(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return GsonTypeUtils.canonicalize(parameterized.getActualTypeArguments()[0]);
    }
}
