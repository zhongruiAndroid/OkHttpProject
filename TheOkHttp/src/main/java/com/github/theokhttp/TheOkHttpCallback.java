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
public abstract  class TheOkHttpCallback<T> extends SimpleCallback {
    public TheOkHttpCallback() {
    }
    public boolean needCloneResponseString(){
        return false;
    }
    public abstract void response(T response);

    public abstract void failure(Exception e);
    @Override
    public void success(Response response) throws IOException{
        int httpCode = response.code();
        if (httpCode!= 200) {
            failure(new Exception(response.message()));
            return;
        }
        ResponseBody body = response.body();
        Type type = getType(this.getClass());
        if (type == null || type == String.class || type == Object.class) {
            giveString(body);
        } else if (type == byte[].class) {
            response((T) body.bytes());
        } else if (type == InputStream.class) {
            response((T) body.byteStream());
        } else if (type == Reader.class) {
            response((T) body.charStream());
        } else {
            giveString(body);
        }
    }

    @Override
    public void error(Exception e) {
        failure(e);
    }


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



}
