package com.hofo.lcoderutil.io;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.util.Arrays;

/**
 * Created by Biao on 2017/5/21.
 */

public class LUtilFileUtil {
    /**
     * 检查是否只读
     */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    /**
     * 读取本地文件获取内容
     */
    public static String getContentByFile(String path) {
        BufferedReader br = null;
        try {
            File file = new File(path);
            char[] fileChars = new char[(int) file.length()];

            br = new BufferedReader(new FileReader(file));
            br.read(fileChars);

            return new String(fileChars);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            LUtilStreamUtil.closeStream(br);
        }
        return null;
    }

    /**
     * 获得扩展存储的根目录
     *
     * @return
     */
    public static String getRootPath() {
        if (isExternalStorageWritable()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return null;
    }

    /**
     * 外部存储是否可用
     *
     * @return
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

}
