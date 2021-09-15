package com.github.theokhttp;

import java.io.Serializable;

import okhttp3.Response;
import okhttp3.ResponseBody;

/***
 *   created by zhongrui on 2019/9/24
 */
public class TheOkResponse implements Serializable {
    public Exception exception;
    public Response response;
    public ResponseBody body;
    public String result;
    public int statusCode;
    public void close() {
        if (body != null) {
            body.close();
        }
        if (response != null) {
            response.close();
        }

    }
}
