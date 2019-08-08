package com.hofo.lcoderutil.lang;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;

public class LUtilUrlsConstant {
    private static HashMap<String, String> urls = new HashMap<>();

    static {
        upgradeUrls();
    }

    public static void upgradeUrls() {
        upgradeUrls(LUtilUrlsConstant.class, ""
                , new String[]{"BaseUrl", "serialVersionUID", "$change",});
    }

    /**
     * @param tClass
     * @param baseUrl
     * @param filter  过滤器
     */
    public static void upgradeUrls(Class tClass, String baseUrl, String[] filter) {
        Class clz = tClass;
        Field[] fields = clz.getFields();
        String filterStr = Arrays.toString(filter);
        for (Field f : fields) {
            if (filterStr.contains(f.getName())) {
                continue;
            }
            try {
                f.set(clz, "http://" + baseUrl + getRawUrl(f));
                L.e(f.getName() + ":" + f.get(f.getName()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getRawUrl(Field f) {
        String s = urls.get(f.getName());
        if (s == null) {
            try {
                urls.put(f.getName(), String.valueOf(f.get(f.getName())));
                s = urls.get(f.getName());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return s;
    }

}
