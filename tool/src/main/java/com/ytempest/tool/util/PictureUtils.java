package com.ytempest.tool.util;

import android.media.ExifInterface;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author heqidu
 * @since 2020/3/2
 */
public class PictureUtils {

    /**
     * 获取图片的旋转方向，即图片要根据条件旋转相应的角度才能正常展示，返回值如下：
     * {@link  ExifInterface#ORIENTATION_UNDEFINED}=0：未能识别
     * {@link  ExifInterface#ORIENTATION_NORMAL}=1：原图角度
     * {@link  ExifInterface#ORIENTATION_FLIP_HORIZONTAL}=2：水平翻转
     * {@link  ExifInterface#ORIENTATION_ROTATE_180}=3：旋转180度
     * {@link  ExifInterface#ORIENTATION_FLIP_VERTICAL}=4：垂直翻转
     * {@link  ExifInterface#ORIENTATION_TRANSPOSE}=5：以原图的左上角-右下角的对角线进行翻转
     * {@link  ExifInterface#ORIENTATION_ROTATE_90}=6：顺时针旋转了90度
     * {@link  ExifInterface#ORIENTATION_TRANSVERSE}=7：以原图的右上角-左下角的对角线进行翻转
     * {@link  ExifInterface#ORIENTATION_ROTATE_270}=8：逆时针旋转了90度
     */
    public static int getImageExifOrientation(File image) {
        int orientation;
        try {
            ExifInterface exifInterface = new ExifInterface(image.getAbsolutePath());
            orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        } catch (IOException e) {
            orientation = ExifInterface.ORIENTATION_UNDEFINED;
        }
        return orientation;
    }

    /**
     * 获取图片正确预览时需要旋转的角度，暂时不支持对镜像翻转图片的角度获取
     */
    public static int getImageCorrectionDegress(File image) {
        int orientation = getImageExifOrientation(image);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;

            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;

            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;

            default:
                return 0;
        }
    }

    /* exif */

    /**
     * 获取JPEG图片的Exif的属性TAG
     */
    public static List<String> getExifTagList() {
        List<String> tagList = new ArrayList<>(148);
        Field[] fields = ExifInterface.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            // Exif的顺属性信息的KEY都是以"TAG_"开头的
            if (fieldName.startsWith("TAG_")) {
                try {
                    String exifTagValue = (String) field.get(null);
                    tagList.add(exifTagValue);
                } catch (IllegalAccessException e) {
                }
            }
        }
        return tagList;
    }

    /**
     * 复制图片的exif信息到另一张图片
     */
    public static boolean copyExifTo(File srcImage, File destImage) {
        return copyExifTo(srcImage, destImage, getExifTagList());
    }

    /**
     * 复制图片的exif信息到另一张图片
     *
     * @param tagList 需要复制的exif信息的TAG列表，可以看到{@link ExifInterface#TAG_FLASH}等
     */
    public static boolean copyExifTo(File srcImage, File destImage, List<String> tagList) {
        try {
            if (tagList != null) {
                ExifInterface srcExif = new ExifInterface(srcImage.getAbsolutePath());
                ExifInterface destExif = new ExifInterface(destImage.getAbsolutePath());

                for (String tag : tagList) {
                    // 将信息复制到另一张图片中
                    String attribute = srcExif.getAttribute(tag);
                    destExif.setAttribute(tag, attribute);
                }

                // 最后需要确认保存
                destExif.saveAttributes();
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
