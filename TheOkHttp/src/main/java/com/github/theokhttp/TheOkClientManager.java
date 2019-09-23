package com.github.theokhttp;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/***
 *   created by zhongrui on 2019/9/23
 */
public class TheOkClientManager {
    private static TheOkClientManager singleObj;
    private volatile Set<OkHttpClient> clientSet;

    private TheOkClientManager() {
        clientSet = new HashSet<>();
    }

    public static TheOkClientManager get() {
        if (singleObj == null) {
            synchronized (TheOkClientManager.class) {
                if (singleObj == null) {
                    singleObj = new TheOkClientManager();
                }
            }
        }
        return singleObj;
    }

    public void add(OkHttpClient client) {
        clientSet.add(client);
    }
    public void remove(OkHttpClient client) {
        clientSet.remove(client);
    }

    public void cancelAllRequest() {
        Iterator<OkHttpClient> iterator = clientSet.iterator();
        while (iterator.hasNext()) {
            OkHttpClient next = iterator.next();
            cancelAllRequest(next);
        }
    }
    public void cancelAllRequest(OkHttpClient client) {
        client.dispatcher().cancelAll();
    }

    public void cancelRequest(Object tag) {
        if(tag==null){
            return;
        }
        Iterator<OkHttpClient> iterator = clientSet.iterator();
        while (iterator.hasNext()){
            OkHttpClient next = iterator.next();
            cancelRequest(next,tag,false);
        }
    }

    public void cancelRequest(OkHttpClient client, Object tag ) {
        cancelRequest(client,tag,true);
    }
    private void cancelRequest(OkHttpClient client, Object tag,boolean needCheckNull) {
        if (needCheckNull&&tag == null) {
            return;
        }
        List<Call> calls = client.dispatcher().queuedCalls();
        for (Call call:calls){
            if(tag.equals(call.request().tag())){
                call.cancel();
            }
        }

        calls = client.dispatcher().runningCalls();
        for (Call call:calls){
            if(tag.equals(call.request().tag())){
                call.cancel();
            }
        }
    }

}
