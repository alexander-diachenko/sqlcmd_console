package Stream.lab40;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ByteFilter {

        public static void filter (InputStream src, OutputStream dst, int bufferSize, byte filterCriteria)throws
        IOException {
        /*BODY*/
            StringBuilder text = new StringBuilder();
            byte[] buffer = new byte[bufferSize];
            byte[] result = new byte[100];
            while (src.read(buffer) != -1) {
           String str = new String(buffer);
                text.append(str);
            }
        }
    }
