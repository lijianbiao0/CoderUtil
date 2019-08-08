package com.hofo.lcoderutil.lang;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.util.Base64;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Biao on 2017/5/17.
 */

public class LUtilImageUtil {

    /**
     * 从View中加载图片
     *
     * @param v
     * @return
     */
    public static Bitmap loadImageByView(View v) {
        if (v == null) {
            return null;
        }
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();
        Bitmap bitmap = v.getDrawingCache();
        return bitmap;
    }

    /**
     * 图片转base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 裁剪
     *
     * @param bitmap 原图
     * @param l      原图 - 左
     * @param t      原图 - 上
     * @param r      原图 - 右
     * @param b      原图 - 下
     * @return 裁剪后图片
     */
    public static Bitmap cropBitmap(Bitmap bitmap, int l, int t, int r, int b) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int maxWidth = width - l;
        int maxHeight = height - t;

        if (r > maxWidth) {
            r = maxWidth;
        }
        if (b > maxHeight) {
            b = maxHeight;
        }
        Bitmap copy = Bitmap.createBitmap(bitmap, l, t, r - l, b - t, null, false);
        bitmap.recycle();
        return copy;
    }

    /**
     * 指定位置绘制边框
     *
     * @param bitmap
     * @param l
     * @param t
     * @param r
     * @param b
     * @param lineWidth
     * @return
     */
    public static Bitmap drawFrame(Bitmap bitmap, int l, int t, int r, int b, int lineWidth, @ColorInt int lineColor) {
        if (bitmap == null) {
            return null;
        }
        Bitmap copy = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        if (!copy.equals(bitmap)) {
            bitmap.recycle();
        }
        //创建画布
        Canvas canvas = new Canvas(copy);
        //画笔
        Paint paint = new Paint();
        //设置线宽。单位为像素
        paint.setStrokeWidth(lineWidth);
        //抗锯齿
        paint.setAntiAlias(true);
        //画笔颜色
        paint.setColor(lineColor);
        //画笔风格，描边
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(l, t, r, b, paint);
        canvas.save();
        return copy;
    }

    /**
     * 旋转
     *
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;
        if (bm == null) {
            return null;
        }

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }


    /**
     * 缩放图片大小
     *
     * @param bm
     * @param new_Width
     * @param new_Height
     * @return
     */
    public static Bitmap scaleImage(Bitmap bm, int new_Width, int new_Height) {
// 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 设置想要的大小
        int newWidth = new_Width;
        int newHeight = new_Height;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = null;
        try {
            newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                    true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newbm;
    }

    public static Bitmap scaleImage(Bitmap origin, float ratio) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(ratio, ratio);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }

    /**
     * 调整图片旋转角度
     *
     * @param bm
     * @param orientationDegree
     * @return
     */
    public static Bitmap adjustImageRotation(Bitmap bm, final int orientationDegree) {
        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);

        try {
            Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
            return bm1;
        } catch (OutOfMemoryError ex) {
        }
        return null;
    }

    /**
     * 将彩色图转换为纯黑白二色
     *
     * @param bmp
     * @return 返回转换好的位图
     */
    public static Bitmap convertToBW(Bitmap bmp, int unchangColor, int otherColor, int tmp) {
        int width = bmp.getWidth(); // 获取位图的宽
        int height = bmp.getHeight(); // 获取位图的高
        int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组
        // 设定二值化的域值，默认值为180
//        tmp = 127;
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];
                if (!(grey == unchangColor)) {
                    // 分离三原色
                    int alpha = ((grey & 0xFF000000) >> 24);
                    int red = ((grey & 0x00FF0000) >> 16);
                    int green = ((grey & 0x0000FF00) >> 8);
                    int blue = (grey & 0x000000FF);
                    if (red > tmp) {
                        red = 255;
                    } else {
                        red = 0;
                    }
                    if (blue > tmp) {
                        blue = 255;
                    } else {
                        blue = 0;
                    }
                    if (green > tmp) {
                        green = 255;
                    } else {
                        green = 0;
                    }
                    pixels[width * i + j] = alpha << 24 | red << 16 | green << 8
                            | blue;
                    if (pixels[width * i + j] == -1) {
                        pixels[width * i + j] = otherColor;
                    } else {
                        pixels[width * i + j] = unchangColor;
                    }
                }
            }
        }
        // 新建图片
        Bitmap newBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
// 设置图片数据
        newBmp.setPixels(pixels, 0, width, 0, 0, width, height);
        return newBmp;
    }
}
