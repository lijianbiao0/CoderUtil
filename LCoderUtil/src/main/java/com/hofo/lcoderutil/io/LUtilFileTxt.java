package com.hofo.lcoderutil.io;

import java.io.File;

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

}
