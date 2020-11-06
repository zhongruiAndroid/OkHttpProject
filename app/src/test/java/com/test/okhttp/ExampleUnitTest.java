package com.test.okhttp;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void asf() {
        String fileDirByUrl = getFileDirByUrl("http://game.com/game/index.html");
        System.out.println(fileDirByUrl);
    }
    private String getFileDirByUrl(String urlString /*"http://game.com/game/index.html"*/) {
        int lastSlash = urlString.lastIndexOf('/');
        String server = urlString.substring(0, lastSlash + 1);
        return server.replaceFirst("://", "/").replace(":", "#0A");
    }
    @Test
    public void asfd( ) {
    }
    public void test(Object o){
        System.out.println(o.equals("a"));
    }
}