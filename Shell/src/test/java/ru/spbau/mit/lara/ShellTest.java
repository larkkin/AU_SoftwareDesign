package ru.spbau.mit.lara;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.spbau.mit.lara.exceptions.ExitException;
import ru.spbau.mit.lara.exceptions.ShellException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class ShellTest {
    private final Shell shell = new Shell();
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void cleanUpStreams() {
        System.setOut(null);
        System.setErr(null);
    }

    @Test
    public void testEcho() throws ExitException, ShellException {
        // execute
        shell.processLine("echo aaa   bbb \"ccc    ddd\"");
        assertEquals("aaa bbb ccc    ddd\n", outContent.toString());
        resetStreams();
        // pipedExecute
        shell.processLine("echo aaa   bbb \"ccc    ddd\" | echo");
        assertEquals("", outContent.toString());
        resetStreams();
    }

    @Test
    public void testCat() throws ExitException, ShellException {
        // execute
        shell.processLine("cat src/test/resources/text_for_tests.txt");
        assertEquals("A duck walked up to a lemonade stand\n" +
                "And he said to the man, running the stand\n" +
                "Hey! (Bum bum bum) Got any grapes?\n" +
                "\n\n", outContent.toString());
        resetStreams();
        // pipedExecute
        shell.processLine("echo \"aaa bbb\" | cat");
        assertEquals("aaa bbb\n", outContent.toString());
        resetStreams();
    }

    @Test
    public void testWc() throws ExitException, ShellException {
        // execute
        shell.processLine("wc src/test/resources/text_for_tests.txt");
        assertEquals("4 24 115\n", outContent.toString());
        resetStreams();
        // pipedExecute
        shell.processLine("echo \"aaa bbb\" | wc");
        assertEquals("1 2 8\n", outContent.toString());
        resetStreams();
    }

    @Test
    public void testPwd() throws ExitException, ShellException {
        // execute
        shell.processLine("pwd");
        resetStreams();
        // pipedExecute
        shell.processLine("echo \"aaa bbb\" | pwd");
        resetStreams();
    }

    @Test(expected = ExitException.class)
    public void testExit() throws ExitException, ShellException {
        // execute
        shell.processLine("exit");
        resetStreams();
        // pipedExecute
        shell.processLine("echo \"aaa bbb\" | exit");
        resetStreams();
    }

    @Test
    public void testExternalLS() throws ExitException, ShellException {
        // execute
        shell.processLine("ls src");
        assertEquals("main\n" +
                "test\n\n", outContent.toString());
        resetStreams();
        // no pipedExecute for external commands
    }

    @Test
    public void testGrep() throws ExitException, ShellException {
        // execute
        shell.processLine("grep \'grapes\' src/test/resources/text_for_tests.txt");
        assertEquals("Hey! (Bum bum bum) Got any grapes?\n\n", outContent.toString());
        resetStreams();
        // no -i key
        shell.processLine("grep \'Grapes\' src/test/resources/text_for_tests.txt");
        assertEquals("\n", outContent.toString());
        resetStreams();
        // -w key
        shell.processLine("grep -w \'grapes\' src/test/resources/text_for_tests.txt");
        assertEquals("Hey! (Bum bum bum) Got any grapes?\n\n", outContent.toString());
        resetStreams();
        // no -w key
        shell.processLine("grep -w \'grape\' src/test/resources/text_for_tests.txt");
        assertEquals("\n", outContent.toString());
        resetStreams();
        // no pipedExecute for grep
    }

    private void resetStreams() {
        outContent.reset();
        errContent.reset();
    }



}
