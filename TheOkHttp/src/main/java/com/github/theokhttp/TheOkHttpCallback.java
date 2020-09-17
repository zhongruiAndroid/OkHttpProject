package com.github.theokhttp;

import android.os.Handler;
import android.os.Looper;

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
public abstract  class TheOkHttpCallback<T> extends SimpleCallback {

    public TheOkHttpCallback() {
    }
    @Override
    public void success(Response response) throws IOException{
        ResponseBody body = response.body();
        if (response.code() != 200) {
            TheOkHttp.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    failure(new Exception(response.message()));
                }
            });
            return;
        }
        Type type = getType(this.getClass());
        if (type == null || type == String.class || type == Object.class) {
            giveString(body);
        } else if (type == byte[].class) {
            postResponse((T) body.bytes());
        } else if (type == InputStream.class) {
            postResponse((T) body.byteStream());
        } else if (type == Reader.class) {
            postResponse((T) body.charStream());
        } else {
            giveString(body);
        }
    }

    @Override
    public void error(Exception e) {
        TheOkHttp.single().getHandler().post(new Runnable() {
            @Override
            public void run() {
                Exception outException = e;
                if (NetworkUtils.getContext() != null && NetworkUtils.noNetwork()) {
                    outException = new NoNetworkException(onNoNetwork());
                } else if (e instanceof SocketTimeoutException && "after".equals(e.getMessage())) {
                    outException = new TimeoutException(onTimeout());
                }
                failure(outException);
            }
        });
    }

    public abstract void response(T response);

    public abstract void failure(Exception e);

    public String onNoNetwork() {
        return "无网络连接";
    }

    public String onTimeout() {
        return "网络连接超时";
    }
    private void giveString(ResponseBody body) throws IOException {
        String resultString;
        if (!TheOkHttp.single().isCloneResponseString()) {
            resultString = body.string();
        } else {
            BufferedSource source = body.source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.getBuffer();
            resultString = buffer.clone().readString(Charset.forName("UTF-8"));
        }
        postResponse((T) resultString);
    }

    private void postResponse(final T result) {
        TheOkHttp.single().getHandler().post(new Runnable() {
            @Override
            public void run() {
                response(result);
            }
        });
    }

}
