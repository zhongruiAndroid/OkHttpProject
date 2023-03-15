package com.test.okhttp;

import android.text.TextUtils;

import com.github.theokhttp.TheOkHttpCallback;
import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;

import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

public abstract class RequestCallback<T> extends TheOkHttpCallback<String> {
    public abstract void success(T data, String result);

    public abstract void error(String code, String errorMsg);

    @Override
    public void response(String result) {
        if (TextUtils.isEmpty(result)) {
            toError("", "数据为空");
            return;
        }
        result = revert(result);
        try {
            if(TextUtils.equals("[]",result)){
                result = result.replace("[]", "{}");
            }else{
                result = result.replace("[]", "null");
            }
            JSONObject jsonObject = new JSONObject(result);
            boolean successResult = isSuccess(jsonObject);
            if (successResult) {
                Gson gson = new Gson();
                String contentJson = getContentJson(jsonObject);
                Type type = getSuperclassTypeParameter(getClass());
                if (type == String.class) {
                    toSuccess((T) contentJson, result);
                } else {
                    T t = (T) gson.fromJson(contentJson, type);
                    if (t == null) {
                        try {
                            if(type instanceof ParameterizedType){
                                t= (T) new ArrayList();
                            }else if(type instanceof Class){
                                String canonicalName = ((Class) type).getCanonicalName();
                                if(!TextUtils.isEmpty(canonicalName)){
                                    t=(T) Class.forName(canonicalName).newInstance();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            t=null;
                        }
                    }
                    toSuccess(t, result);
                }
            } else {
                String code = jsonObject.optString("code");
                String msg = jsonObject.optString(getMsgFile());
                toError(code, msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            toError("", "解析错误");
        }
    }

    @Override
    public void failure(String errorString, Exception e){
        toError("-1", errorString);
    }

    public String getMsgFile( ) {
        return "msg";
    }
    public boolean isSuccess(JSONObject jsonObject) {
        String data = jsonObject.optString("data");
        if (!TextUtils.isEmpty(data)&&!TextUtils.equals("null",data)) {
            return true;
        } else {
        }
        return false;
    }

    private void toSuccess(T data, String result) {
//        HandlerUtils.getMainHandler().post(new Runnable() {
//            @Override
//            public void run() {
//                success(data, result);
//            }
//        });
    }

    private void toError(String code, String errorMsg) {
//        HandlerUtils.getMainHandler().post(new Runnable() {
//            @Override
//            public void run() {
//                error(code, errorMsg);
//            }
//        });
    }

    public boolean needAutoJumpLogin() {
        return true;
    }

    public String getContentJson(JSONObject jsonObject) {
        return jsonObject.optString("data");
    }

    public String revert(String origin) {
        return origin;
    }

    private T getInstanceOfT() {
        ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
        Class<T> type = (Class<T>) superClass.getActualTypeArguments()[0];
        try {
            return type.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Class<T> getClassOfT() {
        ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
        Class<T> type = (Class<T>) superClass.getActualTypeArguments()[0];
        return type;
    }

    public Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }
}

