package com.yunapp.libx.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * IO操作工具类
 */
public class IOUtil {

    private IOUtil() {
    }

    public static void closeAll(Closeable... closeables) {
        if (closeables == null) {
            return;
        }
        for (Closeable closeable : closeables) {
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
