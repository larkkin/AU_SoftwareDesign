package ru.spbau.mit.lara;

import org.junit.Test;
import ru.spbau.mit.lara.exceptions.ContextException;
import ru.spbau.mit.lara.exceptions.ShellException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TokenizerTest {

    @Test
    public void testWeakQuoting() throws  ShellException {
        Context context = new Context();
        String[] expectedArray = {"aa", "bb", "cc dd", "ee"};
        List<String> expectedTokens = new ArrayList<String>();
        Collections.addAll(expectedTokens, expectedArray);
        assertEquals(expectedTokens, Tokenizer.Tokenize("aa bb \'cc dd\' ee", context).get(0));
    }

    @Test(expected = ContextException.class)
    public void testContextVariable() throws ShellException {
        Context context = new Context();
        String[] expectedArray = {"a2", "2", "a"};
        List<String> expectedTokens = new ArrayList<String>();
        Collections.addAll(expectedTokens, expectedArray);
        Tokenizer.Tokenize("a=2", context);
        assertTrue(context.contains("a"));
        assertEquals(expectedTokens, Tokenizer.Tokenize("a$a $a a", context).get(0));
        Tokenizer.Tokenize("$b", context);
    }

    @Test
    public void testStrongQuoting() throws ShellException {
        Context context = new Context();
        String[] expectedArray = {"aa", "b2 cc", "dd"};
        List<String> expectedTokens = new ArrayList<String>();
        Collections.addAll(expectedTokens, expectedArray);
        Tokenizer.Tokenize("a=2", context);
        assertEquals(expectedTokens, Tokenizer.Tokenize("aa \"b$a cc\" dd", context).get(0));
    }
    
    @Test
    public void testPipe() throws ShellException {
        Context context = new Context();
        ArrayList<ArrayList<String>> expectedTokensList = new  ArrayList<ArrayList<String>>();
        expectedTokensList.add(new ArrayList<String>());
        expectedTokensList.add(new ArrayList<String>());
        String[] expectedArray = {"aa", "bb"};
        Collections.addAll(expectedTokensList.get(0), expectedArray);
        String[] expectedArray2 = {"c", "d"};
        Collections.addAll(expectedTokensList.get(1), expectedArray2);
        assertEquals(expectedTokensList, Tokenizer.Tokenize("a=2 aa bb | c d", context));
    }

}