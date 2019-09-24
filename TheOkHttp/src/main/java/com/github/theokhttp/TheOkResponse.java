package com.github.theokhttp;

import java.io.Serializable;

import okhttp3.Response;

/***
 *   created by zhongrui on 2019/9/24
 */
public class TheOkResponse implements Serializable {
    public Exception exception;
    public Response response;
    public String result;
}
