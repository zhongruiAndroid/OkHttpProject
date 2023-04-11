package com.github.theokhttp;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeoutException;

import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/***
 *   created by zhongrui on 2019/9/24
 */
public class TheOkResponse<T> implements Serializable {

    public boolean isCanceled;
    public T result;
    public int statusCode;

    public boolean isSuccess;

    public Exception exception;
    public boolean isNoNetwork;
    public boolean isTimeout;
    public boolean is404;
    public boolean is500;
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

    public static <T> TheOkResponse<T> execute(Call call) {
        return execute(call,false);
    }
    public static <T> TheOkResponse<T> execute(Call call,boolean needCloneResponseString) {
        TheOkResponse<T> res = new TheOkResponse<T>();
        res.isSuccess = false;
        try {
            Response response = call.execute();
            int httpCode = response.code();
            res.statusCode = httpCode;
            if (httpCode == 404 || httpCode == 500) {
                if (res.needGetResponseStringOnFailure()) {
                    String s = giveString(response.body(),needCloneResponseString);
                    res.result = (T) s;
                }
                TheOkHttpCallback.close(response);
                res.isCanceled = call.isCanceled();
                if (httpCode == 404) {
                    res.is404=true;
                    res.exception = new Exception(res.onHttpNotFound());
                } else if (httpCode == 500) {
                    res.is500=true;
                    res.exception = new Exception(res.onHttpServerError());
                }
                return res;
            } else if (!response.isSuccessful()) {
                String s = giveString(response.body(), needCloneResponseString);
                String message = response.message();
                TheOkHttpCallback.close(response);
                res.isCanceled = call.isCanceled();
                res.result = (T) s;
                res.exception = new Exception(message);
                return res;
            }
            res.isCanceled = call.isCanceled();
            res.isSuccess = true;
            res.result = res.onResponseSuccess(response,needCloneResponseString);

        } catch (IOException e) {
            e.printStackTrace();
            res.exception = res.onResponseFailure(e);
        }
        return res;
    }

    private Exception onResponseFailure(IOException e) {
        Exception outException;
        if (TheOkHttpNetworkUtils.getContext() != null && TheOkHttpNetworkUtils.noNetwork()) {
            isNoNetwork=true;
            outException = new NoNetworkException(onNoNetwork());
        } else if (e instanceof SocketTimeoutException && e.getMessage() != null && (e.getMessage().indexOf("after") != -1 || e.getMessage().indexOf("timeout") != -1)) {
            outException = new TimeoutException(onTimeout());
            isTimeout=true;
        } else {
            outException = new Exception(e.getMessage());
        }
        return outException;
    }

    private T onResponseSuccess(Response response,boolean needCloneResponseString) throws IOException {
        /*int httpCode = response.code();
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
        } else if (!response.isSuccessful()) {
            String s = giveString(response.body());
            String message = response.message();
            close(response);
            failure(s, new Exception(message));
            return;
        }*/
        ResponseBody body = response.body();
        Type type = getType(this.getClass());
        if (type == null || type == String.class || type == Object.class) {
            String string = giveString(body, needCloneResponseString);
            TheOkHttpCallback.close(body, response);
            return (T) string;
        } else if (type == InputStream.class) {
            T t = (T) body.byteStream();
            TheOkHttpCallback.close(body, response);
            return t;
        } else if (type == Reader.class) {
            T t = (T) body.charStream();
            TheOkHttpCallback.close(body, response);
            return t;
        } else if (type == byte[].class || (type.toString() != null && type.toString().indexOf("byte") != -1)) {
            T bytes = (T) body.bytes();
            TheOkHttpCallback.close(body, response);
            return bytes;
        } else {
            String string = giveString(body, needCloneResponseString);
            TheOkHttpCallback.close(body, response);
            return (T) string;
        }
    }

    private static String giveString(ResponseBody body, boolean needCloneResponseString) throws IOException {
        if (body == null) {
            return "";
        }
        String resultString;
        if (!needCloneResponseString) {
            resultString = body.string();
        } else {
            BufferedSource source = body.source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.getBuffer();
            resultString = buffer.clone().readString(Charset.forName("UTF-8"));
        }
        return resultString;
    }


    public boolean needGetResponseStringOnFailure() {
        return false;
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
