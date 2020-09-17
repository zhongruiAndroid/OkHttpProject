package com.github.theokhttp;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
public abstract class SimpleCallback  implements Callback {
    public SimpleCallback() {
    }
    public abstract void success(Response response) throws IOException;
    public abstract void error(Exception e);
    public String onNoNetwork() {
        return "无网络连接";
    }
    public String onTimeout() {
        return "网络连接超时";
    }
    @Override
    public void onFailure(final Call call, final IOException e) {
        if (call.isCanceled()) {
            return;
        }
        Exception outException = e;
        if (NetworkUtils.getContext() != null && NetworkUtils.noNetwork()) {
            outException = new NoNetworkException(onNoNetwork());
        } else if (e instanceof SocketTimeoutException && "after".equals(e.getMessage())) {
            outException = new TimeoutException(onTimeout());
        }
        error(outException);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (call.isCanceled()) {
            return;
        }
        success(response);
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
