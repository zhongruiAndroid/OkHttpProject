package com.github.theokhttp;

import android.content.Context;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.github.theokhttp.test", appContext.getPackageName());
    }
    @Test
    public void asd() {

        Uri.Builder builder=new Uri.Builder();
        builder.path("www.baidu.com");
        Map<String,String> map=new LinkedHashMap<>();
        map.put("a","1");
        map.put("b","2");
        map.put("c","3");
        map.put("d","4");
        Set<String> strings = map.keySet();
        Iterator<String> iterator = strings.iterator();
        while (iterator.hasNext()){
            String next = iterator.next();
            builder.appendQueryParameter(next,map.get(next));
        }
        System.out.println(builder.build().toString()+"[][][]");
        Log.i("=====","====="+builder.build().toString());
    }
}
