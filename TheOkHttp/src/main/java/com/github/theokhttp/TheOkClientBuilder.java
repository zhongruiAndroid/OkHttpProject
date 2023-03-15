package com.github.theokhttp;

import android.util.Log;

import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.CertificatePinner;
import okhttp3.ConnectionPool;
import okhttp3.ConnectionSpec;
import okhttp3.CookieJar;
import okhttp3.Dispatcher;
import okhttp3.Dns;
import okhttp3.EventListener;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/***
 *   created by android on 2019/9/19
 */

public class TheOkClientBuilder {

    public static Interceptor appInterceptor=new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if(TheOkHttp.isDebug()){
                long startTime = System.nanoTime();
                Response response = chain.proceed(request);
                long endTime = System.nanoTime();

                String params="";
                if(request.body()!=null){
                    Request copyRequest = request.newBuilder().build();
                    Buffer buffer=new Buffer();
                    copyRequest.body().writeTo(buffer);
                    params=buffer.readUtf8();
                }
                ResponseBody body = response.body();
                String resultString="";
                MediaType mediaType = body.contentType();
                String subtype=null;
                if(mediaType!=null){
                    subtype = mediaType.subtype();
                }
                if(TheOkHttp.single().getIgnoreContentSubType().contains(subtype)){
                    resultString=subtype;
                }else{
                    BufferedSource source = body.source();
                    source.request(Long.MAX_VALUE);
                    Buffer buffer = source.getBuffer();
                    resultString= buffer.clone().readString(Charset.forName("UTF-8"));
                }


                double timeInterval=(endTime-startTime)/1e6d;
                String msg = "url->" + LG.getURLDecoderString(request.url().toString())
                        +"\nparams->" + LG.getURLDecoderString(params)
                        + "\nmethod->"+request.method()
                        + "\ntime->" + timeInterval+"ms"
                        + "\nheaders->" + request.headers()
                        + "\nresponse code->" + response.code()
                        + "\nresponse headers->" + response.headers()
                        + "\nresponse body->" + resultString;

                LG.print(Log.INFO,"TheOkHttp==",msg);

                return response;
            }
            return chain.proceed(request);
        }
    };

    private OkHttpClient.Builder builder;
    public TheOkClientBuilder( ) {
        builder = new OkHttpClient.Builder()
                .connectTimeout(TheOkHttpConfig.HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TheOkHttpConfig.HTTP_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TheOkHttpConfig.HTTP_READ_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(appInterceptor)
        ;
    }
    public TheOkClientBuilder addNetworkInterceptor(Interceptor interceptor) {
        builder.addNetworkInterceptor(interceptor);
        return this;
    }

    public TheOkClientBuilder sslSocketFactory(SSLSocketFactory sslSocketFactory) {
        builder.sslSocketFactory(sslSocketFactory);
        return this;
    }

    public TheOkClientBuilder sslSocketFactory(SSLSocketFactory sslSocketFactory, X509TrustManager trustManager) {
        builder.sslSocketFactory(sslSocketFactory,trustManager);
        return this;
    }

    public TheOkClientBuilder retryOnConnectionFailure(boolean retryOnConnectionFailure) {
        builder.retryOnConnectionFailure(retryOnConnectionFailure);
        return this;
    }
    public TheOkClientBuilder socketFactory(SocketFactory socketFactory) {
        builder.socketFactory(socketFactory);
        return this;
    }

    public TheOkClientBuilder connectionSpecs(List<ConnectionSpec> connectionSpecs) {
        builder.connectionSpecs(connectionSpecs);
        return this;
    }

    public TheOkClientBuilder connectionPool(ConnectionPool connectionPool) {
        builder.connectionPool(connectionPool);
        return this;
    }

    public TheOkClientBuilder certificatePinner(CertificatePinner certificatePinner) {
        builder.certificatePinner(certificatePinner);
        return this;
    }

    public TheOkClientBuilder callTimeout(long timeout, TimeUnit unit) {
        builder.callTimeout(timeout,unit);
        return this;
    }

    public TheOkClientBuilder callTimeout(Duration duration) {
        builder.callTimeout(duration);
        return this;
    }

    public TheOkClientBuilder cache(Cache cache) {
        builder.cache(cache);
        return this;
    }

    public TheOkClientBuilder authenticator(Authenticator authenticator) {
        builder.authenticator(authenticator);
        return this;
    }

    public TheOkClientBuilder addInterceptor(Interceptor interceptor) {
        builder.addInterceptor(interceptor);
        return this;
    }

    public TheOkClientBuilder cookieJar(CookieJar cookieJar) {
        builder.cookieJar(cookieJar);
        return this;
    }

    public TheOkClientBuilder dispatcher(Dispatcher dispatcher) {
        builder.dispatcher(dispatcher);
        return this;
    }

    public TheOkClientBuilder dns(Dns dns) {
        builder.dns(dns);
        return this;
    }

    public TheOkClientBuilder eventListener(EventListener eventListener) {
        builder.eventListener(eventListener);
        return this;
    }

    public TheOkClientBuilder eventListenerFactory(EventListener.Factory eventListenerFactory) {
        builder.eventListenerFactory(eventListenerFactory);
        return this;
    }

    public TheOkClientBuilder followRedirects(boolean followRedirects) {
        builder.followRedirects(followRedirects);
        return this;
    }

    public TheOkClientBuilder followSslRedirects(boolean followProtocolRedirects) {
        builder.followSslRedirects(followProtocolRedirects);
        return this;
    }

    public TheOkClientBuilder hostnameVerifier(HostnameVerifier hostnameVerifier) {
        builder.hostnameVerifier(hostnameVerifier);
        return this;
    }

    public TheOkClientBuilder pingInterval(long interval, TimeUnit unit) {
        builder.pingInterval(interval,unit);
        return this;
    }

    public TheOkClientBuilder pingInterval(Duration duration) {
        builder.pingInterval(duration);
        return this;
    }

    public TheOkClientBuilder protocols(List<Protocol> protocols) {
        builder.protocols(protocols);
        return this;
    }

    public TheOkClientBuilder proxy(Proxy proxy) {
        builder.proxy(proxy);
        return this;
    }

    public TheOkClientBuilder proxyAuthenticator(Authenticator proxyAuthenticator) {
        builder.proxyAuthenticator(proxyAuthenticator);
        return this;
    }

    public TheOkClientBuilder proxySelector(ProxySelector proxySelector) {
        builder.proxySelector(proxySelector);
        return this;
    }


    public TheOkClientBuilder connectTimeout(long timeoutSeconds) {
        return connectTimeout(timeoutSeconds, TimeUnit.SECONDS);
    }
    public TheOkClientBuilder connectTimeout(long timeout, TimeUnit unit) {
        builder.connectTimeout(timeout,unit);
        return this;
    }
    public TheOkClientBuilder connectTimeout(Duration duration) {
        builder.connectTimeout(duration);
        return this;
    }

    public TheOkClientBuilder writeTimeout(long timeoutSeconds) {
        return writeTimeout(timeoutSeconds, TimeUnit.SECONDS);
    }
    public TheOkClientBuilder writeTimeout(long timeout, TimeUnit unit) {
        builder.writeTimeout(timeout,unit);
        return this;
    }
    public TheOkClientBuilder writeTimeout(Duration duration) {
        builder.writeTimeout(duration);
        return this;
    }

    public TheOkClientBuilder readTimeout(long timeoutSeconds) {
        return readTimeout(timeoutSeconds, TimeUnit.SECONDS);
    }
    public TheOkClientBuilder readTimeout(long timeout, TimeUnit unit) {
        builder.readTimeout(timeout,unit);
        return this;
    }
    public TheOkClientBuilder readTimeout(Duration duration) {
        builder.readTimeout(duration);
        return this;
    }
    public void complete(){
        TheOkHttp.single().setSingleClient(builder.build());
    }
    public OkHttpClient create(){
        return builder.build();
    }
}
