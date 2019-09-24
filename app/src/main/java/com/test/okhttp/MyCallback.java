package com.test.okhttp;

import com.github.theokhttp.TheOkHttpCallback;
import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.test.okhttp.bean.BaseBean;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/***
 *   created by android on 2019/9/19
 */
public abstract class MyCallback<T> extends TheOkHttpCallback<String> {
    public abstract void success(T response);
    public abstract void error(Exception e);

    @Override
    public void response(String response) {
        Type type = getSuperclassTypeParameter(getClass());
        if(type!=null&&type!=String.class){
            BaseBean<T> baseBean = new Gson().fromJson(response,BaseBean.class);
            if(baseBean.getErrorCode()==0){
                T data = baseBean.getData();
                T obj= new Gson().fromJson(new Gson().toJson(data),type);
                success(obj);
            }else{
                error(new Exception(baseBean.getErrorMsg()));
            }
        }else{
            success((T) response);
        }
    }
    @Override
    public void failure(Exception e) {
        error(e);
    }

    private Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }
}
