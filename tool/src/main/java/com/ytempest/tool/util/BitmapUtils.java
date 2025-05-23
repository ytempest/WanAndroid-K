package com.ytempest.tool.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @author ytempest
 * @since 2019/9/3
 * <p>
 * Note：带有输入流的方法在会在方法结束时close输入流
 */
public class BitmapUtils {

    /* decode */

    @Nullable
    public static Bitmap decodeImg(File file) {
        return decodeImg(file, -1, -1);
    }

    @Nullable
    public static Bitmap decodeImg(File imgFile, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = calculateSample(imgFile, reqWidth, reqHeight);
        return BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);
    }

    /**
     * Note：方法结束时会close输入流
     */
    @Nullable
    public static Bitmap decodeImg(InputStream in, int sampleSize) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = sampleSize;
            return BitmapFactory.decodeStream(in, null, options);
        } finally {
            IOUtils.close(in);
        }
    }

    @Nullable
    public static Bitmap decodeImg(Context context, Uri uri) {
        return decodeImg(context, uri, -1, -1);
    }

    @Nullable
    public static Bitmap decodeImg(Context context, Uri uri, int reqWidth, int reqHeight) {
        InputStream in = null;
        Bitmap bitmap;
        try {
            int sample = 1;
            if (reqWidth > 0 && reqHeight > 0) {
                in = context.getContentResolver().openInputStream(uri);
                sample = calculateSample(in, reqWidth, reqHeight);
            }

            in = context.getContentResolver().openInputStream(uri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = sample;
            bitmap = BitmapFactory.decodeStream(in, null, options);
        } catch (FileNotFoundException e) {
            bitmap = null;
        } finally {
            IOUtils.close(in);
        }
        return bitmap;
    }

    @Nullable
    public static Bitmap decodeImg(byte[] data) {
        return decodeImg(data, -1, -1);
    }

    @Nullable
    public static Bitmap decodeImg(byte[] data, int reqWidth, int reqHeight) {
        return decodeImg(data, 0, data.length, reqWidth, reqHeight);
    }

    @Nullable
    public static Bitmap decodeImg(byte[] data, int offset, int length, int reqWidth, int reqHeight) {
        int sample = 1;
        if (reqWidth > 0 && reqHeight > 0) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(data, offset, length, options);
            sample = calculateSample(options, reqWidth, reqHeight);
        }

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inSampleSize = sample;
        return BitmapFactory.decodeByteArray(data, offset, length, opt);
    }

    /* sample size */

    public static int calculateSample(Context context, Uri uri, int reqWidth, int reqHeight) {
        InputStream in = null;
        int sample;
        try {
            in = context.getContentResolver().openInputStream(uri);
            sample = calculateSample(in, reqWidth, reqHeight);
        } catch (FileNotFoundException e) {
            sample = 1;
        } finally {
            IOUtils.close(in);
        }
        return sample;
    }

    /**
     * 方法结束时会close输入流
     */
    public static int calculateSample(@Nullable InputStream in, int reqWidth, int reqHeight) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            return calculateSample(options, reqWidth, reqHeight);
        } finally {
            IOUtils.close(in);
        }
    }

    private static int calculateSample(File file, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        return calculateSample(options, reqWidth, reqHeight);
    }

    public static int calculateSample(BitmapFactory.Options options, int reqW, int reqH) {
        int sampleSize = 1;
        if (options != null) {
            int oriW = options.outWidth;
            int oriH = options.outHeight;
            if (oriW < oriH) {
                sampleSize = (int) Math.ceil(oriW * 1F / reqW);

            } else {
                sampleSize = (int) Math.ceil(oriH * 1F / reqH);
            }
        }
        return sampleSize < 1 ? 1 : sampleSize;
    }

    /* compress */

    public static boolean compressTo(Bitmap bitmap, File imgFile) {
        return compressTo(bitmap, imgFile, Bitmap.CompressFormat.JPEG, 100);
    }

    public static boolean compressTo(Bitmap bitmap, File imgFile, int quality) {
        return compressTo(bitmap, imgFile, Bitmap.CompressFormat.JPEG, quality);
    }

    public static boolean compressTo(Bitmap bitmap, File imgFile, Bitmap.CompressFormat format, int quality) {
        if (bitmap == null || imgFile.isDirectory()) {
            return false;
        }

        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(imgFile);
            bitmap.compress(format, quality, stream);
        } catch (FileNotFoundException e) {
            return false;
        } finally {
            IOUtils.close(stream);
        }

        return true;
    }

    /* scale */

    /**
     * 将Bitmap缩放到指定的百分比，不主动回收传递进来的Bitmap
     */
    public static Bitmap scaleBitmap(Bitmap bitmap, float percent) {
        int desWidth = (int) Math.ceil(bitmap.getWidth() * percent);
        int desHeight = (int) Math.ceil(bitmap.getHeight() * percent);
        return scaleBitmap(bitmap, desWidth, desHeight);
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int scaleW, int scaleH) {
        if (bitmap == null) {
            return null;
        }
        return Bitmap.createScaledBitmap(bitmap, scaleW, scaleH, false);
    }

}
