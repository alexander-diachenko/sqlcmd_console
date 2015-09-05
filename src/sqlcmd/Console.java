package sqlcmd;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Console {

    Scanner scanner = new Scanner(System.in);

    public String read() {

        try {
            return scanner.nextLine();

        }catch (NoSuchElementException e ){

        return null;
        }
    }
}