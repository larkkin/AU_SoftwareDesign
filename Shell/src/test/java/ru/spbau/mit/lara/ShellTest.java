package ru.spbau.mit.lara;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.notification.RunListener;
import ru.spbau.mit.lara.commands.Command;
import ru.spbau.mit.lara.commands.Echo;
import ru.spbau.mit.lara.commands.Wc;
import ru.spbau.mit.lara.exceptions.ExitException;
import ru.spbau.mit.lara.exceptions.ShellException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.fail;
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
        shell.processLine("Ls src");
        assertEquals("main\n" +
                "test\n\n", outContent.toString());
        resetStreams();
        // no pipedExecute for external commands
    }

    @Test
    public void testLs() throws Exception
    {
        shell.processLine("ls");
        String root = System.getProperty("user.dir");
        File[] rootDir = (new File(root)).listFiles();
        StringBuilder out = new StringBuilder();
        for (File file: rootDir) {
            out.append(file.toString());
            out.append("\n");
        }
        out.append("\n");
        assertEquals(out.toString(), outContent.toString());
        resetStreams();
    }

    @Test
    public void testCd() throws Exception
    {
        String root = System.getProperty("user.dir");
        shell.processLine("cd /");
        shell.processLine("pwd");
        assertEquals("\n/\n", outContent.toString());
        resetStreams();
        shell.processLine("cd " + root + "/src/test");
        shell.processLine("cat sample.txt");
        assertEquals("\nsome text\n\n", outContent.toString());
        resetStreams();
    }

    private void resetStreams() {
        outContent.reset();
        errContent.reset();
    }



}
