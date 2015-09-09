package Stream.lab40;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test2 {
    public static void main(String[] args) throws IOException {

        final int BUFF_LEN = 4;
        final byte FILTER_CRITERIA=0;

        final List actualHistoryCall = new ArrayList<>();

        final byte[] DATA = {
                1, 0, 0, 2, 34, 5, 6, 7, 78, 8, 9, 0, 2, 3, 5, 6, 7, 7
        };

        List expectedHistoryCall = Arrays.asList(
                "read(b[4])", "write(b[4],0,1)",
                "write(b[4],3,1)", "read(b[4])",
                "write(b[4],0,4)", "read(b[4])",
                "write(b[4],0,3)", "read(b[4])",
                "write(b[4],0,4)", "read(b[4])",
                "write(b[4],0,2)", "read(b[4])"
        );


        ByteArrayInputStream dataInputStream = new ByteArrayInputStream(DATA) {
            private boolean log = true;

            @Override
            public int read() {
                actualHistoryCall.add("read()");
                return super.read();

            }

            @Override
            public int read(byte[] b) throws IOException {
                actualHistoryCall.add("read(b[" + b.length + "])");
                log = false;
                try {
                    return super.read(b);
                } finally {
                    log = true;
                }

            }

            @Override
            public int read(byte[] b, int off, int len) {
                if (log)
                    actualHistoryCall.add("read(b[" + b.length + "]," + off + "," + len + ")");
                return super.read(b, off, len);
            }
        };

        ByteArrayOutputStream dataOutputStream = new ByteArrayOutputStream() {
            private boolean log = true;

            @Override
            public void write(byte[] b) throws IOException {
                actualHistoryCall.add("write(b[" + b.length + "])");
                log = false;
                try {
                    super.write(b);
                } finally {
                    log = true;
                }
            }

            @Override
            public synchronized void write(byte[] b, int off, int len) {
                if (log)
                    actualHistoryCall.add("write(b[" + b.length + "]," + off + "," + len + ")");
                super.write(b, off, len);
            }

            @Override
            public synchronized void write(int b) {
                actualHistoryCall.add("write()");
                super.write(b);
            }
        };

        //call
        ByteFilter.filter(dataInputStream, dataOutputStream, BUFF_LEN,FILTER_CRITERIA);

        //check

        if (!actualHistoryCall.equals(expectedHistoryCall))
            throw new AssertionError("expected call list function write and read " + expectedHistoryCall + " but found " + actualHistoryCall);

        System.out.print("OK");

    }
}