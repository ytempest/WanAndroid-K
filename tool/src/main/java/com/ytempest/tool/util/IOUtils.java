package com.ytempest.tool.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author ytempest
 * @since 2019/9/3
 */
public class IOUtils {
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(Closeable... closeables) {
        for (int i = 0, size = DataUtils.getSize(closeables); i < size; i++) {
            Closeable closeable = closeables[i];
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
