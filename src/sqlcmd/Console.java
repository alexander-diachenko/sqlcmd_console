package sqlcmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Console {


    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public String read() {

        try {
            return reader.readLine();

        }catch (IOException e ){

        return null;
        }
    }
}