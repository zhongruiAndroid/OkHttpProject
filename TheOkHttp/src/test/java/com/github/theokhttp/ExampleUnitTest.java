package com.github.theokhttp;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void sdf() {

    }
    private void test() {
        /*OkHttpClient.Builder client = new OkHttpClient.Builder()
                .sslSocketFactory()
                .sslSocketFactory()
                .addInterceptor()
                .addNetworkInterceptor()
                .authenticator()
                .cache()
                .callTimeout()
                .callTimeout()
                .certificatePinner()
                .connectionPool()
                .connectionSpecs()
                .connectTimeout()
                .connectTimeout()
                .cookieJar()
                .dispatcher()
                .dns()
                .eventListener()
                .eventListenerFactory()
                .followRedirects()
                .followSslRedirects()
                .hostnameVerifier()
                .pingInterval()
                .pingInterval()
                .protocols()
                .proxy()
                .proxyAuthenticator()
                .proxySelector()
                .readTimeout()
                .readTimeout()
                .retryOnConnectionFailure()
                .socketFactory()
                .writeTimeout()
                .writeTimeout();*/
    }
    @Test
    public void sfd() {
        List<String> list=new ArrayList<>();
        list.add("addNetworkInterceptor");
        list.add("sslSocketFactory");
        list.add("sslSocketFactory");
        list.add("retryOnConnectionFailure");
        list.add("connectionSpecs");
        list.add("connectionPool");
        list.add("certificatePinner");
        list.add("callTimeout");
        list.add("callTimeout");
        list.add("cache");
        list.add("authenticator");
        list.add("addInterceptor");
        list.add("connectTimeout");
        list.add("cookieJar");
        list.add("dispatcher");
        list.add("dns");
        list.add("eventListener");
        list.add("eventListenerFactory");
        list.add("followRedirects");
        list.add("followSslRedirects");
        list.add("hostnameVerifier");
        list.add("pingInterval");
        list.add("pingInterval");
        list.add("protocols");
        list.add("proxy");
        list.add("proxyAuthenticator");
        list.add("proxySelector");
        list.add("readTimeout");
        list.add("readTimeout");
        list.add("interceptors");
        System.out.println(list.size());
        for (int i = 0; i < list.size(); i++) {
            String a="   public TheClientBuilder "+list.get(i)+"(long aaaaaaa) {\n" +
                    "        return this;" +
                    "    }";
            System.out.println(a);
        }
    }
}