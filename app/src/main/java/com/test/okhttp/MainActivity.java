package com.test.okhttp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.theokhttp.TheOkHttp;
import com.github.theokhttp.TheOkHttpCallback;
import com.github.theokhttp.TheOkHttpConfig;
import com.google.gson.Gson;
import com.test.okhttp.bean.BaseBean;
import com.test.okhttp.bean.TabDataRes;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView ivTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivTest=findViewById(R.id.ivTest);

        findViewById(R.id.btPost).setOnClickListener(this);
        findViewById(R.id.btPostImage).setOnClickListener(this);
        findViewById(R.id.btGetMethod).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btPost:
                post1();
                post2();
            break;
            case R.id.btPostImage:
                postImage();
            break;
            case R.id.btGetMethod:
                test();
            break;
        }
    }

    private void test() {
        String url="https://wanandroid.com/wxarticle/chapters/json";
        TheOkHttp.init(new OkHttpClient.Builder()
                .connectTimeout(TheOkHttpConfig.HTTP_CONNECT_TIMEOUT,TimeUnit.SECONDS)
                .writeTimeout(TheOkHttpConfig.HTTP_WRITE_TIMEOUT,TimeUnit.SECONDS)
                .readTimeout(TheOkHttpConfig.HTTP_READ_TIMEOUT,TimeUnit.SECONDS)
                .build());
        TheOkHttp.startGet(url, new TheOkHttpCallback<String>() {
            @Override
            public void response(String response) {
                Log.e("======","=3====="+response);
            }

            @Override
            public void failure(Exception e) {
                Log.e("======","=3====="+e.getMessage());
            }
        });

    }
    private void getMethod() {
        OkHttpClient client = new OkHttpClient();
        Method[] declaredMethods = client.getClass().getDeclaredMethods();
        int length = declaredMethods.length;
        loge(length+"");
        for (int i = 0; i < length; i++) {
            loge(declaredMethods[i].getName());
        }
    }

    private void postImage() {
        String url="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1568891094290&di=4ebd1969fe5c790f42febb4ba4f1e6d6&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201305%2F26%2F20130526140022_5fMJe.jpeg";
        OkHttpTool.getClient().requestImage(url, new TheOkHttpCallback<BaseBean>() {
            @Override
            public void response(BaseBean response) {
                Bitmap bitmap = BitmapFactory.decodeStream(null);
                ivTest.setImageBitmap(bitmap);
                loge(contentLength+"Bitmap"+contentType);
            }
            @Override
            public void failure(Exception e) {
                loge(e.getMessage());
            }
            @Override
            public boolean isSaveContentType() {
                return true;
            }
        });
    }

    private void post1() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 20; i++) {
                    String url="https://wanandroid.com/wxarticle/chapters/json";
                    int finalI = i;
                    OkHttpTool.getClient().request(url, new MyCallback<BaseBean<List<TabDataRes>>>() {
                        @Override
                        public void success(BaseBean<List<TabDataRes>> data) {
                            boolean b = Looper.getMainLooper() == Looper.myLooper();
                            Log.e("==111==="+b,getStr(data));
                            if(finalI ==2)
                            Toast.makeText(MainActivity.this,"show",Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void error(Exception e) {
                            loge(e.getMessage());
                        }
                    });
                    if(i%5==0){
                        SystemClock.sleep(100);
                    }
                }
            }
        }).start();
    }
    private void post2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 20; i++) {
                    String url="https://wanandroid.com/article/listproject/0/json";
                    OkHttpTool.getClient().request(url, new MyCallback<String>() {
                        @Override
                        public void success(String data) {
                            boolean b = Looper.getMainLooper() == Looper.myLooper();
                            Log.e("==222==="+b,""+data);
                        }
                        @Override
                        public void error(Exception e) {
                            loge(e.getMessage());
                        }
                    });
                    if(i%3==0){
                        SystemClock.sleep(50);
                    }
                }
            }
        }).start();
    }
    private void loge(String string){
        Log.e("=====","==:"+string);
    }
    private String getStr(Object object){
        return new Gson().toJson(object);
    }


    public void aa(){
        String url="https://wanandroid.com/article/listproject/0/json";
        TheOkHttp.startGet(url, new MyCallback<String>() {
            @Override
            public void success(String response) {

            }
            @Override
            public void error(Exception e) {

            }
        });
        Call call=null;
        Object tag = call.request().tag();
        String tag1 = call.request().tag(String.class);

        Request.Builder request=new Request.Builder();
        request.tag(this);
        request.tag(MainActivity.class,this);


        TheOkHttp.init(null);
        TheOkHttp.post();
    }
}
