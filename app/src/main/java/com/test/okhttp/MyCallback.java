package com.test.okhttp;

import android.util.Log;

import com.github.theokhttp.TheOkHttpCallback;
import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.test.okhttp.bean.BaseBean;
import com.test.okhttp.bean.TabDataRes;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/***
 *   created by android on 2019/9/19
 */
public abstract class MyCallback<T> extends TheOkHttpCallback {
    public abstract void success(T response);
    public abstract void error(Exception e);
    @Override
    public void response(String response) {
        Type type = getSuperclassTypeParameter(getClass());
        if(type!=null&&type!=String.class){
            success(new Gson().fromJson(response,type));
        }else{
            success((T) response);
        }
    }
    @Override
    public void failure(Exception e) {
        error(e);
    }

    public Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            return null;
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }
}
