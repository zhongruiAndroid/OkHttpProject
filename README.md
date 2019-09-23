```java
//必须初始化
TheOkHttp.init();

//设置debug可以打印请求结果信息(非必须)
TheOkHttp.setDebug(BuildConfig.DEBUG);

//用于网络请求回调判断是否无网络(非必须)
TheOkHttp.setNetworkHelper(this);

//请求图片时，会打印很多乱码数据，设置后缀名过滤掉(非必须)[此处可以忽略，这个功能是开发给自己用的，强迫症]
TheOkHttp.addIgnoreContentSubType("jpeg");
TheOkHttp.addIgnoreContentSubType("jpg");
TheOkHttp.addIgnoreContentSubType("png");
```

#### get请求
```java
String url="https://wanandroid.com/wxarticle/list/408/1/json";

Map<String,String> map=new HashMap<String,String>();
map.put("k","Java");

//无参不传map
TheOkHttp.get(map).start(url,new TheOkHttpCallback<String>() {
   @Override
   public void response(String response) {
       
   }
   @Override
   public void failure(Exception e) {

   }
});

//如果不设置其他request参数可以直接调用
TheOkHttp.startGet(url,callback)
TheOkHttp.startGet(map,url,callback)
```


#### post请求
```java
String url="https://wanandroid.com/wxarticle/list/408/1/json";

Map<String,String> map=new HashMap<String,String>();
map.put("k","Java");

//无参不传map
TheOkHttp.post(map).start(url,new TheOkHttpCallback<String>() {
   @Override
   public void response(String response) {
       
   }
   @Override
   public void failure(Exception e) {

   }
});
```
**可根据具体业务需求直接传body**
```java
RequestBody body;
FormBody body;
MultipartBody body;

//提交json数据
TheOkHttp.post(body).start(url,callback);
//提交Form表单
TheOkHttp.postForm(body).start(url,callback);
//文件上传
TheOkHttp.postMultipart(body).start(url,callback);
```

#### 设置官方Okhttp Client参数(全局client，影响所有不设置setOkHttpClient的Request)
```java
TheOkHttp.init()
	//添加application拦截器
        .addInterceptor()
	//添加网络拦截器
        .addNetworkInterceptor()
        //设置连接超时时间
        .connectTimeout()
        //设置读取超时时间
        .readTimeout()
        //设置写入超时时间
        .writeTimeout()
        
        //...此处省略官方N个api
        
        //完成上述设置(不要漏掉这个方法，否则无效!!!，否则无效!!!，否则无效!!!)
        .complete();
        
//或者自行实例化一个client传进来
TheOkHttp.init(okHttpClient);
```
#### 设置官方Okhttp Request参数
```java
TheOkHttp
	  //设置post请求
          .post(body)
          //个别请求可能需要新设置client
          //(此处的client只会针对当前请求,不会影响初始化的全局client,也不会受全局client影响)
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
          //因为没有依赖其他json解析库,所以TheOkHttpCallback泛型只支持以下4种
          //String  byte[]  InputStream  Reader
          //如果想获取某个具体的对象，请继承TheOkHttpCallback或Callback 返回String,使用gson转换
          .start(url, new TheOkHttpCallback<InputStream>() {
      @Override
      public void response(InputStream response) {

      }
      @Override
      public void failure(Exception e) {

      }
  });

```
#### 取消请求
```java
//取消所有client的请求
TheOkClientManager.get().cancelAllRequest();

//取消某个client的请求
TheOkClientManager.get().cancelAllRequest(client);

//取消所有tag标记的请求
TheOkClientManager.get().cancelRequest(tag);

//取消某个client的所有tag标记的请求
TheOkClientManager.get().cancelRequest(client,tag);
```  
  
    
    
| 最新版本号 | [ ![Download](https://api.bintray.com/packages/zhongrui/mylibrary/TheOkHttp/images/download.svg) ](https://bintray.com/zhongrui/mylibrary/TheOkHttp/_latestVersion) |
|--------|----|
  



```gradle
implementation 'com.github:TheOkHttp:版本号看上面'
```
