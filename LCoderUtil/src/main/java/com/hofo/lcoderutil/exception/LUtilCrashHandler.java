package com.hofo.lcoderutil.exception;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;


import com.hofo.lcoderutil.io.LUtilStreamUtil;
import com.hofo.lcoderutil.lang.LUtilDateFormat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 作用:
 * 1.收集错误信息
 * 2.保存错误信息
 */
public class LUtilCrashHandler implements Thread.UncaughtExceptionHandler {
    private static volatile LUtilCrashHandler singleton;
    private static Application mApplication;
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    // 保存手机信息和异常信息
    private Map<String, String> mMessage = new HashMap<>();
    private CrashExceptionLienter mCrashExceptionLienter = new CrashExceptionLienter() {
        @Override
        public void handleException(Throwable e) {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    };

    private LUtilCrashHandler() {
    }

    public static LUtilCrashHandler getInstance() {
        if (singleton == null) {
            synchronized (LUtilCrashHandler.class) {
                if (singleton == null) {
                    singleton = new LUtilCrashHandler();
                }
            }
        }
        return singleton;
    }

    /**
     * 初始化默认异常捕获
     *
     * @param app context
     */
    public void init(Application app) {
        mApplication = app;
        // 获取默认异常处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 添加自定义参数
     */
    public void addCustomInfo(String Key, String value) {
        mMessage.put(Key, value);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (!handleException(e)) {
            if (mDefaultHandler != null) {
                mDefaultHandler.uncaughtException(t, e);
            }
        }
    }

    /**
     * 是否人为捕获异常
     *
     * @param e Throwable
     * @return true:已处理 false:未处理
     */
    public boolean handleException(final Throwable e) {
        if (e == null) {// 异常是否为空
            return false;
        }
        collectErrorMessages();
        saveErrorMessages(e);

        mCrashExceptionLienter.handleException(e);
        return true;
    }

    /**
     * 2.保存错误信息
     *
     * @param e Throwable
     */
    private void saveErrorMessages(Throwable e) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : mMessage.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append("=").append(value).append("\n");
        }
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        e.printStackTrace(pw);
        Throwable cause = e.getCause();
        // 循环取出Cause
        while (cause != null) {
            cause.printStackTrace(pw);
            cause = e.getCause();
        }
        LUtilStreamUtil.closeStream(pw);
        String result = writer.toString();
        sb.append(result);
        String time = LUtilDateFormat.date2Str(new Date(), "yyyy-MM-dd HH.mm.ss");
        String fileName = "crash-" + time + ".log";

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String path = getCarshPath();
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(path + fileName);
                fos.write(sb.toString().getBytes(StandardCharsets.UTF_8));
                fos.flush();
            } catch (Exception e1) {
                e1.printStackTrace();
            } finally {
               LUtilStreamUtil.closeStream(fos);
            }
        }
    }

    public String getCarshPath() {
        return Environment.getExternalStorageDirectory().getPath() + "/crash/";
    }

    /**
     * 1.收集错误信息
     */
    private void collectErrorMessages() {
        PackageManager pm = mApplication.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(mApplication.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = TextUtils.isEmpty(pi.versionName) ? "null" : pi.versionName;
                String versionCode = "" + pi.versionCode;
                mMessage.put("versionName", versionName);
                mMessage.put("versionCode", versionCode);
            }
            // 通过反射拿到错误信息
            Field[] fields = Build.class.getFields();
            if (fields != null && fields.length > 0) {
                for (Field field : fields) {
                    field.setAccessible(true);
                    try {
                        mMessage.put(field.getName(), field.get(null).toString());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setCrashExceptionLienter(CrashExceptionLienter crashExceptionLienter) {
        mCrashExceptionLienter = crashExceptionLienter;
    }

    public interface CrashExceptionLienter {
        void handleException(Throwable e);
    }
}