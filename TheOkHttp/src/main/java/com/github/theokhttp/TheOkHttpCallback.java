package com.github.theokhttp;

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

    public abstract void failure(String errorString, Exception e);


    private String giveString(ResponseBody body) throws IOException {
        if (body == null) {
            return "";
        }
        String resultString;
        if (!needCloneResponseString()) {
            resultString = body.string();
        } else {
            BufferedSource source = body.source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.getBuffer();
            resultString = buffer.clone().readString(Charset.forName("UTF-8"));
        }
        return resultString;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        if (call.isCanceled()) {
            return;
        }
        Exception outException = e;
        if (TheOkHttpNetworkUtils.getContext() != null && TheOkHttpNetworkUtils.noNetwork()) {
            outException = new NoNetworkException(onNoNetwork());
        } else if (e instanceof SocketTimeoutException && e.getMessage().indexOf("after") != -1) {
            outException = new TimeoutException(onTimeout());
        }
        failure("", outException);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (call.isCanceled()) {
            return;
        }
        int httpCode = response.code();
        if (httpCode == HttpURLConnection.HTTP_NOT_FOUND) {
            String s = giveString(response.body());
            close(response);
            failure(s, new Exception(onHttpNotFound()));
            return;
        } else if (httpCode == HttpURLConnection.HTTP_INTERNAL_ERROR) {
            String s = giveString(response.body());
            close(response);
            failure(s, new Exception(onHttpServerError()));
            return;
        } else if (httpCode != 200) {
            String s = giveString(response.body());
            String message = response.message();
            close(response);
            failure(s, new Exception(message));
            return;
        }
        ResponseBody body = response.body();
        Type type = getType(this.getClass());
        if (type == null || type == String.class || type == Object.class) {
            String string = giveString(body);
            close(body, response);
            response((T) string);
        } else if (type == InputStream.class) {
            T t = (T) body.byteStream();
            close(body, response);
            response(t);
        } else if (type == Reader.class) {
            T t = (T) body.charStream();
            close(body, response);
            response(t);
        } else if (type == byte[].class || (type.toString() != null && type.toString().indexOf("byte") != -1)) {
            T bytes = (T) body.bytes();
            close(body, response);
            response(bytes);
        } else {
            String string = giveString(body);
            close(body, response);
            response((T) string);
        }
    }

    public static void close(ResponseBody body) {
        if (body != null) {
            body.close();
        }

    }

    public static void close(Response response) {

        if (response != null) {
            response.close();
        }
    }

    public static void close(ResponseBody body, Response response) {
        close(body);
        close(response);
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
