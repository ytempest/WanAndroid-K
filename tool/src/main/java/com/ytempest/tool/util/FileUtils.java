package com.ytempest.tool.util;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

/**
 * @author ytempest
 * @since 2019/9/3
 */
public class FileUtils {

    /**
     * 将 src文件复制到 dest 文件
     *
     * @param src  source file
     * @param dest target file
     */
    public static void writeToFile(File src, File dest) throws IOException {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            inChannel = new FileInputStream(src).getChannel();
            outChannel = new FileOutputStream(dest).getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            IOUtils.close(inChannel);
            IOUtils.close(outChannel);
        }
    }

    /**
     * Note：从输入流读取完数据后会将流关闭
     *
     * @param input   包含了数据的输入流
     * @param desFile 需要写入数据的目的文件
     */
    public static boolean writeToFile(InputStream input, File desFile) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(desFile);
            byte[] buffer = new byte[2028];
            int len;
            while ((len = input.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        } catch (IOException e) {
            return false;
        } finally {
            IOUtils.close(out);
            IOUtils.close(input);
        }
        return true;
    }

    /**
     * 如果文件夹不存在则创建，如果存在该文件夹同名的文件也会创建文件夹
     *
     * @param dir 需要创建的文件夹
     * @return 如果不存在则会创建并返回创建结果，如果存在并且是一个文件夹则直接返回true
     */
    public static boolean createDir(File dir) {
        return dir.exists() && dir.isDirectory() || dir.mkdirs();
    }

    /**
     * 如果文件不存在则创建，如果存在该文件同名的文件夹也会创建文件
     *
     * @param file 需要创建的文件
     * @return 如果不存在则会创建并返回创建结果，如果存在并且是一个文件则直接返回true
     */
    public static boolean createFile(File file) {
        try {
            return file.exists() && file.isFile() || file.createNewFile();
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 删除文件或者文件夹
     */
    public static boolean delete(File target) {
        if (!target.exists()) {
            return true;
        }

        // 如果是文件
        if (target.isFile()) {
            return target.delete();
        }

        // 如果是文件夹
        boolean success = true;
        File[] files = target.listFiles();
        for (int i = 0, len = DataUtils.getSize(files); i < len; i++) {
            success &= delete(files[i]);
        }

        // 最后删除自己
        return success && target.delete();
    }

    /**
     * 删除文件夹下的所有文件
     */
    public static boolean deleteAllUnderDir(File dir) {
        if (!dir.exists() || !dir.isDirectory()) {
            return true;
        }

        boolean success = true;
        File[] files = dir.listFiles();
        for (int i = 0, len = DataUtils.getSize(files); i < len; i++) {
            success &= delete(files[i]);
        }
        return success;
    }

    public static boolean isFile(String path) {
        return !TextUtils.isEmpty(path) && new File(path).isFile();
    }

    public static boolean isDirectory(String path) {
        return !TextUtils.isEmpty(path) && new File(path).isDirectory();
    }

    public static boolean exists(String path) {
        return !TextUtils.isEmpty(path) && new File(path).exists();
    }
}
