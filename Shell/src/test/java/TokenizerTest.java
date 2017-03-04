/**
 * Created by lara on 04.03.17.
 */
import org.junit.Test;
import ru.spbau.mit.lara.Context;
import ru.spbau.mit.lara.Tokenizer;
import ru.spbau.mit.lara.exceptions.ContextException;
import ru.spbau.mit.lara.exceptions.ShellException;
import ru.spbau.mit.lara.exceptions.WrongInputFormatException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TokenizerTest {
    @Test
    public void testWeakQuoting() {
        Context context = new Context();
        String[] expectedArray = {"aa", "bb", "cc dd", "ee"};
        List<String> expectedTokens = new ArrayList<String>();
        Collections.addAll(expectedTokens, expectedArray);
        try {
            assertEquals(expectedTokens, Tokenizer.Tokenize("aa bb \'cc dd\' ee", context).get(0));
        } catch (ShellException e) {
            fail();
        }
    }
    @Test
    public void testContextVariable() {
        Context context = new Context();
        String[] expectedArray = {"a2", "2", "a"};
        List<String> expectedTokens = new ArrayList<String>();
        Collections.addAll(expectedTokens, expectedArray);
        try {
            Tokenizer.Tokenize("a=2", context);
            assertTrue(context.search("a"));
            assertEquals(expectedTokens, Tokenizer.Tokenize("a$a $a a", context).get(0));
        } catch (ShellException e) {
            fail();
        }
        try {
            Tokenizer.Tokenize("$b", context);
            fail();
        } catch (ContextException e) {
        } catch (WrongInputFormatException we) {
            fail();
        }
    }
    @Test
    public void testStrongQuoting() {
        Context context = new Context();
        String[] expectedArray = {"aa", "b2 cc", "dd"};
        List<String> expectedTokens = new ArrayList<String>();
        Collections.addAll(expectedTokens, expectedArray);
        try {
            Tokenizer.Tokenize("a=2", context);
            assertEquals(expectedTokens, Tokenizer.Tokenize("aa \"b$a cc\" dd", context).get(0));
        } catch (ShellException e) {
            fail();
        }
    }
    @Test
    public void testPipe() {
        Context context = new Context();
        ArrayList<ArrayList<String>> expectedTokensList = new  ArrayList<ArrayList<String>>();
        expectedTokensList.add(new ArrayList<String>());
        expectedTokensList.add(new ArrayList<String>());
        String[] expectedArray = {"aa", "bb"};
        Collections.addAll(expectedTokensList.get(0), expectedArray);
        String[] expectedArray2 = {"c", "d"};
        Collections.addAll(expectedTokensList.get(1), expectedArray2);
        try {
            assertEquals(expectedTokensList, Tokenizer.Tokenize("a=2 aa bb | c d", context));
        } catch (ShellException e) {
            fail();
        }
    }

}