import org.junit.Test;
import ru.spbau.mit.lara.commands.Command;
import ru.spbau.mit.lara.commands.Echo;
import ru.spbau.mit.lara.commands.Wc;
import ru.spbau.mit.lara.exceptions.ExitException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

public class CommandsTest {
    @Test
    public void testEcho() {
        Command echo = new Echo();
        List<String> tokens = new ArrayList<String>();
        String[] expectedArray = {"aa", "bb", "cc  dd", "ee"};
        Collections.addAll(tokens, expectedArray);
        try {
            assertEquals("aa bb cc  dd ee", echo.execute(tokens));
        } catch (ExitException e) {
            fail();
        }
    }
    @Test
    public void testWc() {
        Command wc = new Wc();
        List<String> lines = new ArrayList<String>();
        String[] linesArray = {"aa bb 7", "ccc rr tttt"};
        Collections.addAll(lines, linesArray);
        try {
            assertEquals("2 6 18", wc.pipedExecute(lines).get(0));
        } catch (ExitException e) {
            fail();
        }
    }
}