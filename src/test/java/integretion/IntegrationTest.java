package integretion;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.PrintStream;

/**
 * Created by POSITIV on 16.09.2015.
 */
public class IntegrationTest {

    private static ConfigurableInputStream in;
    private static LogOutputStream out;

    @BeforeClass
    public static void setup(){
        System.setIn(in);
        System.setOut(new PrintStream(out));
    }

    @Test
    public void testExit(){
    }
}
