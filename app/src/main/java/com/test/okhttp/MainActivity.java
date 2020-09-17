package com.test.okhttp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.theokhttp.TheOkClientManager;
import com.github.theokhttp.TheOkHttp;
import com.github.theokhttp.TheOkHttpCallback;
import com.github.theokhttp.TheOkResponse;

import java.io.InputStream;

import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView ivTest;
    AppCompatCheckBox cbCancelRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivTest=findViewById(R.id.ivTest);
        cbCancelRequest=findViewById(R.id.cbCancelRequest);


        findViewById(R.id.btNoParamGet).setOnClickListener(this);
        findViewById(R.id.btParamGet).setOnClickListener(this);

        findViewById(R.id.btNoParamPost).setOnClickListener(this);
        findViewById(R.id.btParamPost).setOnClickListener(this);

        findViewById(R.id.btPostImage).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btNoParamGet:
                prepareRequest(0);
            break;
            case R.id.btParamGet:
                prepareRequest(1);
            break;
            case R.id.btNoParamPost:
                prepareRequest(2);
            break;
            case R.id.btParamPost:
                prepareRequest(3);
            break;
            case R.id.btPostImage:
                getImage();
            break;
        }
    }

    private void prepareRequest(int index) {
        Intent intent = new Intent(this, Main2Activity.class);
        intent.setAction(String.valueOf(index));
        startActivity(intent);
    }
    private void getImage() {
        String imageUrl="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1568891094290&di=4ebd1969fe5c790f42febb4ba4f1e6d6&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201305%2F26%2F20130526140022_5fMJe.jpeg";
        TheOkHttp.postMultipart(null).start((String) null, new TheOkHttpCallback<Object>() {
            @Override
            public void response(Object response) {

            }
            @Override
            public void failure(Exception e) {

            }
        });

        TheOkHttp.get().tag("a").start(imageUrl, new TheOkHttpCallback<InputStream>() {
            @Override
            public void response(InputStream response) {
                //此处没做处理，如果内存不够用会引起oom
                Bitmap bitmap = BitmapFactory.decodeStream(response);
                ivTest.setImageBitmap(bitmap);
                showMsg("请求成功");
            }
            @Override
            public void failure(Exception e) {
                showMsg("请求失败");
                ivTest.setImageBitmap(null);
            }
            //手动重写方法
            public void cancel(Exception e) {
                super.cancel(e);
                //请求取消之后不会执行failure();
                showMsg("请求取消");
                ivTest.setImageBitmap(null);
            }
            //手动重写方法
            public void timeout(Exception e) {
                super.timeout(e);
                //会继续执行failure();
                showMsg("连接超时");
            }
            //手动重写方法
            public void noNetwork(Exception e) {
                super.noNetwork(e);
                //会继续执行failure();
                showMsg("无网络连接");
            }
        });

        //模拟取消请求
        if(cbCancelRequest.isChecked()){
            TheOkClientManager.get().cancelRequest("a");
        }
    }

    private void showMsg(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }


     private  void test(){


         OkHttpClient okHttpClient=new OkHttpClient.Builder()
                 .cache(null)//设置缓存
                 //...省略N个官方api
                 .build();

         Headers headers=null;
         CacheControl cacheControl=null;
         RequestBody body=null;
//      FormBody body=null;
//      MultipartBody body=null;
         String url="";
         TheOkHttp
                 //设置post请求
                 .post()
                 .setOkHttpClient(okHttpClient)
                 //设置get请求
                 .get()
                 //设置delete请求
                 .delete(body)
                 //设置head请求
                 .head()
                 //设置patch请求
                 .patch(body)
                 //设置put请求
                 .put(body)
                 //设置请求方式，效果同上
                 .method("POST",body)
                 //缓存控制器
                 .cacheControl(cacheControl)
                 //设置header,覆盖相同name的header
                 .header("name","value")
                 //设置headers(清除之前所有的header)
                 .headers(headers)
                 //添加header,可以添加相同name的header,不覆盖也不清除之前的header
                 .addHeader("name","value")
                 //移除header
                 .removeHeader("name")
                 //设置请求tag，可用于取消请求
                 .tag("tag")
                 //如果请求图片，可以设置泛型为InputStream
                 //因为没有依赖其他json解析库,所以TheOkHttpCallback泛型只支持  String  byte[]  InputStream  Reader
                 //如果想获取某个具体的对象，请继承TheOkHttpCallback 返回String,使用gson转换
                 .start(url, new TheOkHttpCallback<InputStream>() {
                     @Override
                     public void response(InputStream response) {

                     }
                     @Override
                     public void failure(Exception e) {

                     }
                 });
//        TheOkHttp.post().start(url);
//        TheOkHttp.postForm(null).start(url);
//        TheOkHttp.postMultipart(null).start(url);

       /*  TheOkHttp.init(okHttpClient);

         TheOkHttp.init()
                 .addInterceptor()
                 .addNetworkInterceptor()
                 .connectTimeout()
                 .readTimeout()
                 .writeTimeout()
                 .complete();*/

//         TheOkClientManager.get().cancelAllRequest();
//         TheOkClientManager.get().cancelAllRequest(client);
//         TheOkClientManager.get().cancelRequest(tag);
//         TheOkClientManager.get().cancelRequest(client,tag);

         TheOkResponse execute = TheOkHttp
                 .get()
                 .addHeader("","")
                 .execute(url);
     }
}
