package com.github.theokhttp;

import android.util.Log;

/***
 *   created by android on 2019/8/26
 */
 class LG {
    private static int printLength=3000;
    private static final char TOP_LEFT_CORNER = '┌';
    private static final char BOTTOM_LEFT_CORNER = '└';
    private static final char HORIZONTAL_LINE = '│';
    private static final String DOUBLE_DIVIDER = "────────────────────────────────────────────────────────";
    private static final String TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;


    public static void print(int priority,String tag,String msg   ){
        if(msg==null){
            Log.println(priority,tag,"msg is null");
            return;
        }
        Log.println(priority,tag,TOP_BORDER);

        byte[] bytes = msg.getBytes();
        int byteLength = bytes.length;
        if(byteLength<=printLength){
            logContent(priority,tag,msg);
        }else{
            for (int i = 0; i < byteLength; i+=printLength) {
                int count = Math.min(byteLength - i, printLength);
                logContent(priority,tag,new String(bytes,i,count));
            }
        }
        Log.println(priority,tag,BOTTOM_BORDER);
    }

    private static void logContent(int priority,String tag,String chunk) {
        String[] lines = chunk.split(System.getProperty("line.separator"));
        for (String line : lines) {
            Log.println(priority,tag,HORIZONTAL_LINE+line);
        }
    }
}
