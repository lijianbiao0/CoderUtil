package com.hofo.lcoderutil.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class LUtilFileTxt {
    /**
     * 获得文本内容
     *
     * @param path
     * @return
     */
    public static String getText(String path) {
        return LUtilFileUtil.getContentByFile(path);
    }

    public static boolean deleteFile(String path) {
        File file = new File(path);
        return file.delete();
    }

    public static void append(String filePath, String message) {
        append(filePath, message, true);
    }

    /**
     * 追加文本文件
     *
     * @param filePath 文件路径
     * @param message  追加内容
     * @param append   是否追加
     */
    public static void append(String filePath, String message, boolean append) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter(filePath, append));
            pw.append(message);
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            LUtilStreamUtil.closeStream(pw);
        }
    }
}
