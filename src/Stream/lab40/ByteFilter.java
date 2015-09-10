package Stream.lab40;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ByteFilter {

    public static void filter(InputStream src, OutputStream dst, int bufferSize, byte filterCriteria) throws IOException {

        byte[] buffer = new byte[bufferSize];
        int len = src.read(buffer);
        while (len != -1) {
            int start = 0;
            int count = 0;
            boolean flag = true;
            for (int index = 0; index < len; index++) {
                if (buffer[index] != filterCriteria) {
                    if (flag) {
                        start = index;
                        flag = false;
                    }
                    count++;
                    if (index < len - 1) {
                        continue;
                    }
                }
                if (count != 0) {
                    dst.write(buffer, start, count);
                    count = 0;
                    flag = true;
                }
            }
            len = src.read(buffer);
            dst.close();
            src.close();
        }
    }
}

