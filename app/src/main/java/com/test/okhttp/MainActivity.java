package com.test.okhttp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.theokhttp.TheOkHttpCallback;
import com.google.gson.Gson;
import com.test.okhttp.bean.BaseBean;
import com.test.okhttp.bean.TabDataRes;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView ivTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivTest=findViewById(R.id.ivTest);

        findViewById(R.id.btPost).setOnClickListener(this);
        findViewById(R.id.btPostImage).setOnClickListener(this);
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
}
