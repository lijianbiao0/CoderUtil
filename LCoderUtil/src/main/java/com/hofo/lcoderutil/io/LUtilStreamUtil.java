package com.hofo.lcoderutil.io;

import java.io.Closeable;
import java.io.IOException;

public class LUtilStreamUtil {
    public static void closeStream(Closeable stream) {
        if (stream == null)
            return;
        try {
            stream.close();
            stream = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
