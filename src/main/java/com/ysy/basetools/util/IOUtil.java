package com.ysy.basetools.util;

import com.ysy.basetools.io.FastOutputStream;
import com.ysy.basetools.model.Res;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2017/6/25.
 */
public class IOUtil {

    public static byte[] readAll(InputStream is) throws IOException {
        if (is != null) {
            int len = Math.max(is.available(), 1024);
            FastOutputStream fos = new FastOutputStream(len, 1.5);
            int c;
            while ((c = is.read()) != -1) {
                fos.write(c);
            }
            return fos.getBytes();
        } else {
            return new byte[0];
        }
    }

    public static Res close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                return Res.errE("close error", e);
            }
        }
        return Res.suc();
    }
}
