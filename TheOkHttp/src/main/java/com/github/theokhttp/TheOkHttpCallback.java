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
    protected Handler handler;
    public long contentLength;
    public MediaType contentType;
    private boolean saveContentType;

    public TheOkHttpCallback() {
        this.handler =new Handler(Looper.getMainLooper());
    }
    public abstract void response(T response);
    public abstract void failure(Exception e);
    public void  cancel(Exception e){};
    public void  noNetwork(Exception e){};
    public void  timeout(Exception e){};
    @Override
    public void onFailure(final Call call, final IOException e) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(e instanceof SocketTimeoutException &&"after".equals(e.getMessage())){
                    timeout(e);
                }
                if(call.isCanceled()){
                    cancel(e);
                    return;
                }
                if(NetworkUtils.getContext()!=null&&NetworkUtils.noNetwork()){
                    noNetwork(e);
                }
                failure(e);
            }
        });
    }
    @Override
    public void onResponse(Call call, Response response) throws IOException {
        ResponseBody body = response.body();
        if(isSaveContentType()){
            contentLength = body.contentLength();
            contentType   = body.contentType();
        }
        Type type = getType(getClass());
        if(type==null||type==String.class||type==Object.class){
            giveString(body);
        }else if(type==byte[].class){
            postResponse((T) body.bytes());
        }else if(type==InputStream.class){
            saveContentType=true;
            postResponse((T) body.byteStream());
        }else if(type==Reader.class){
            postResponse((T) body.charStream());
        }else{
            throw new IllegalStateException("TheOkHttpCallback<T> T must be String or byte[] or InputStream or Reader");
        }
    }
    private void giveString(ResponseBody body) throws IOException {
        String resultString;
        if(true){
            resultString=body.string();
        }else{
            BufferedSource source = body.source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.getBuffer();
            resultString= buffer.clone().readString(Charset.forName("UTF-8"));
        }
        postResponse((T) resultString);
    }

    private void postResponse(final T result) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                response(result);
            }
        });
    }

    public boolean isSaveContentType() {
        return saveContentType;
    }

    private Type getType(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            return null;
        }
        if(superclass instanceof ParameterizedType){
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return parameterized.getActualTypeArguments()[0];
        }
        return null;
    }
}
