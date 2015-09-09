package Stream.lab40;

import java.io.*;
import java.util.Arrays;

public class Test1 {
    public static void main(String[] args) throws IOException {
        final int BUFF_LEN = 4;
        final byte FILTER_CRITERIA=0;

        final byte[] DATA = {
                1, 0, 0, 2, 34, 5, 6, 7, 78, 8, 9, 0, 2, 3, 5, 6, 7, 7
        };

        byte[] expectedOutData = {
                1, 2, 34, 5, 6, 7, 78, 8, 9, 2, 3, 5, 6, 7, 7
        };

        //call
        byte[] actualOutData = null;

        ByteArrayInputStream dataInputStream = new ByteArrayInputStream(DATA);

        ByteArrayOutputStream dataOutputStream = new ByteArrayOutputStream();
        ByteFilter.filter(dataInputStream, dataOutputStream, BUFF_LEN,FILTER_CRITERIA);
        actualOutData = dataOutputStream.toByteArray();

        //check
        if (Arrays.equals(actualOutData, expectedOutData))
            System.out.print("OK");
        else
            throw new AssertionError("DATA equals " + Arrays.toString(DATA) + " and expected data is " + Arrays.toString(expectedOutData) + " but found " + Arrays.toString(actualOutData));
    }
}