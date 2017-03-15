package ru.spbau.mit.lara;

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
    public void testEcho() throws ExitException {
        Command echo = new Echo();
        List<String> tokens = new ArrayList<String>();
        String[] expectedArray = {"aa", "bb", "cc  dd", "ee"};
        Collections.addAll(tokens, expectedArray);
        assertEquals("aa bb cc  dd ee", echo.execute(tokens));
    }

    @Test
    public void testWc() throws ExitException {
        Command wc = new Wc();
        List<String> lines = new ArrayList<String>();
        String[] linesArray = {"aa bb 7", "ccc rr tttt"};
        Collections.addAll(lines, linesArray);
        assertEquals("2 6 18", wc.pipedExecute(lines).get(0));
    }
}