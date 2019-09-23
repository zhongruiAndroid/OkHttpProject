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

import java.io.InputStream;

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
        TheOkHttp.get().tag("a").start(imageUrl, new TheOkHttpCallback<InputStream>() {
            @Override
            public void response(InputStream response) {
                Bitmap bitmap = BitmapFactory.decodeStream(response);
                //此处图片过大会引起oom
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
}
