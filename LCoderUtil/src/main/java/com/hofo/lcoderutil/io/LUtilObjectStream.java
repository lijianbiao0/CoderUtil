package com.hofo.lcoderutil.io;

import android.content.Context;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class LUtilObjectStream {

    public static <T extends Serializable> void putDisk(Context mContext, T t) {
        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream(mContext.openFileOutput(t.getClass().getName(), Context.MODE_PRIVATE));
            os.writeObject(t);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
           LUtilStreamUtil.closeStream(os);
        }
    }

    public static <T extends Serializable> T getDisk(Context mContext,Class<T> t) {
        ObjectInputStream ois = null;
        T o = null;
        try {
            ois = new ObjectInputStream(mContext.openFileInput(t.getName()));
            o = (T) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
         LUtilStreamUtil.closeStream(ois);
        }
        return o;
    }

}
