package Stream.lab40;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ByteFilter {

    public static void filter(InputStream src, OutputStream dst, int bufferSize, byte filterCriteria) throws IOException {
<<<<<<< HEAD
        /*BODY*/
        // 1, 0, 0, 2, 34, 5, 6, 7, 78, 8, 9, 0, 2, 3, 5, 6, 7, 7

//[read(b[4]), write(b[4],0,1), write(b[4],3,1), read(b[4]), write(b[4],0,4), read(b[4]), write(b[4],0,3), read(b[4]), write(b[4],0,4), read(b[4]), write(b[4],0,2), read(b[4])]

//[read(b[4]), write(b[4],0,1), write(b[4],3,1), read(b[4]), write(b[4],0,1), write(b[4],1,1), write(b[4],2,1), write(b[4],3,1), read(b[4]), write(b[4],0,1), write(b[4],1,1), write(b[4],2,1), read(b[4]), write(b[4],0,1), write(b[4],1,1), write(b[4],2,1), write(b[4],3,1), read(b[4]), write(b[4],0,1), write(b[4],1,1), read(b[4])]

=======
>>>>>>> 64015ea19dc9e555812765a337a42108c7467347

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

