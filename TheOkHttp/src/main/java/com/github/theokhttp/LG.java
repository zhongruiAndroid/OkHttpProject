package com.github.theokhttp;

import android.util.Log;

import java.util.logging.Logger;

/***
 *   created by android on 2019/8/26
 */
public class LG {
    private static String TAG="TAG";
    private static int printLength=3000;
    private static final char TOP_LEFT_CORNER = '┌';
    private static final char BOTTOM_LEFT_CORNER = '└';
    private static final char MIDDLE_CORNER = '├';
    private static final char HORIZONTAL_LINE = '│';
    private static final String DOUBLE_DIVIDER = "────────────────────────────────────────────────────────";
    private static final String SINGLE_DIVIDER = "┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄";
    private static final String TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_DIVIDER + SINGLE_DIVIDER;

    private static final int MIN_STACK_OFFSET = 5;

    private static boolean showThreadInfo=true;
    private static boolean showMethodInfo=true;
    private static int methodCount = 2;
    private static int methodOffset = 0;


    public static void setTAG(String tag){
        TAG=tag;
    }
    public static void setLength(int length){
        printLength=length;
    }

    /************************** w **************************/
    public static void w(String msg){
        w(TAG,msg);
    }
    public static void w(String tag,String msg){
        print(Log.WARN,tag, msg);
    }
    /************************** v **************************/
    public static void v(String msg){
        v(TAG,msg);
    }
    public static void v(String tag,String msg){
        print(Log.VERBOSE,tag, msg);
    }
    /************************** d **************************/
    public static void d(String msg){
        d(TAG,msg);
    }
    public static void d(String tag,String msg){
        print(Log.DEBUG,tag, msg);
    }
    /************************** i **************************/
    public static void i(String msg){
        i(TAG,msg);
    }
    public static void i(String tag,String msg){
        print(Log.INFO,tag, msg);
    }
    /************************** e **************************/
    public static void e(String msg){
        e(TAG,msg);
    }
    public static void e(String tag,String msg){
        print(Log.ERROR,tag, msg);
    }

    private static void logHeaderInfo(int priority,String tag, int methodCount){
        if (showThreadInfo) {
            Log.println(priority,tag,HORIZONTAL_LINE + " Thread: " + Thread.currentThread().getName());
            Log.println(priority,tag,MIDDLE_BORDER);
        }
        if(showMethodInfo==false){
            return;
        }
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        if(trace==null){
            return;
        }
        String level = "";
        int stackOffset = getStackOffset(trace) + methodOffset;

        if (methodCount + stackOffset > trace.length) {
            methodCount = trace.length - stackOffset - 1;
        }

        for (int i = methodCount; i > 0; i--) {
            int stackIndex = i + stackOffset;
            if (stackIndex >= trace.length) {
                continue;
            }
            StringBuilder builder = new StringBuilder();
            builder.append(HORIZONTAL_LINE)
                    .append(' ')
                    .append(level)
                    .append(getSimpleClassName(trace[stackIndex].getClassName()))
                    .append(".")
                    .append(trace[stackIndex].getMethodName())
                    .append(" ")
                    .append(" (")
                    .append(trace[stackIndex].getFileName())
                    .append(":")
                    .append(trace[stackIndex].getLineNumber())
                    .append(")");
            level += "   ";
            Log.println(priority,tag,builder.toString());
        }
        Log.println(priority,tag,MIDDLE_BORDER);
    }
    private static String getSimpleClassName(String name) {
        int lastIndex = name.lastIndexOf(".");
        return name.substring(lastIndex + 1);
    }
    private static int getStackOffset(StackTraceElement[] trace) {
        for (int i = MIN_STACK_OFFSET; i < trace.length; i++) {
            StackTraceElement e = trace[i];
            String name = e.getClassName();
            if (!name.equals(LG.class.getName()) && !name.equals(Logger.class.getName())) {
                return --i;
            }
        }
        return -1;
    }

    public static void print(int priority,String tag,String msg){
        print(priority,tag,msg,true);
    }
    public static void print(int priority,String tag,String msg,boolean printHeader){
        if(msg==null){
            Log.println(priority,tag,"msg is null");
            return;
        }
        Log.println(priority,tag,TOP_BORDER);
        if(printHeader){
            logHeaderInfo(priority,tag,methodCount);
        }
        byte[] bytes = msg.getBytes();
        int byteLength = bytes.length;
        if(byteLength<=printLength){
            logContent(priority,tag,msg);
            /*logE(tag,TOP_BORDER);
            logE(tag,HORIZONTAL_LINE+msg);
            logE(tag,BOTTOM_BORDER);*/
        }else{
//            logE(tag,TOP_BORDER);
            for (int i = 0; i < byteLength; i+=printLength) {
                int count = Math.min(byteLength - i, printLength);
//                logE(tag,HORIZONTAL_LINE+new String(bytes,i,count));
//                Log.println(priority,tag,HORIZONTAL_LINE+new String(bytes,i,count));
                logContent(priority,tag,new String(bytes,i,count));
            }
//            logE(tag,BOTTOM_BORDER);
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
