package ru.spbau.mit.lara;

import ru.spbau.mit.lara.exceptions.ContextException;
import ru.spbau.mit.lara.exceptions.WrongInputFormatException;

import java.util.ArrayList;

/**
 * Created by lara on 25.02.17.
 */
public class Tokenizer {

    public static ArrayList<ArrayList<String>> Tokenize(
            /**
             * we execute the tokenizing if a given inputString: the result is the lists of the lists
             * this also helps us to divide tokens per commands(in the "pipe" case)
             */
            String inputLine,
            Context contextInstance) throws WrongInputFormatException, ContextException {
        ArrayList<String> preTokens = new ArrayList<String>();
        String[] preTokensArray = inputLine.split("\\|");
        for (String s : preTokensArray) {
            preTokens.add(s);
        }
        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        for (String preToken : preTokens) {
            result.add(TokenizeWithoutPipes(preToken, contextInstance));
        }
        return result;
    }

    private static ArrayList<String> TokenizeWithoutPipes(
            /**
             * here we can deal with a list of tokens, because we don't have to divide tokens by command
             */
            String inputLine,
            Context contextInstance) throws WrongInputFormatException, ContextException {
        ArrayList<String> tokens = new ArrayList<String>();
        String currentToken = new String();
        for (int i = 0; i < inputLine.length(); i++) {
            if (inputLine.charAt(i) == '\'') {
                /**
                 * the weak quoting case
                 */
                String weakQuotizedPreToken = extractWeakQuotizedTokenPreToken(inputLine, i);
                currentToken += weakQuotizedPreToken;
                i += weakQuotizedPreToken.length() + 1;
                continue;
            }
            if (inputLine.charAt(i) == '\"') {
                /**
                 * the strong quoting case
                 */
                String strongQuotizedToken = extractStrongQuotizedTokenPreToken(inputLine, i, contextInstance);
                currentToken += strongQuotizedToken;
                do {
                    i++;
                } while (inputLine.charAt(i) != '\"');
                continue;
            }
            if (inputLine.charAt(i) == '$')  {
                /**
                 * when we need to extract the values from the context of the current instance of shell
                 */
                String contextVariable = extractContextVariable(inputLine, i, contextInstance);
                currentToken += contextVariable;
                i += contextVariable.length();
                continue;
            }
            if (inputLine.charAt(i) == ' ') {
                /**
                 * thus we see when the end of the token we are formating is located
                 */
                if (currentToken.length() > 0) {
                    tokens.add(currentToken);
                    currentToken = new String();
                }
                continue;
            }
            if (inputLine.charAt(i) == '=' && tokens.size() == 0) {
                /**
                 * when we see the '=', we have to add values to context
                 */
                String variableKey = currentToken;
                String variableVal = new String();
                i++;
                while ( i < inputLine.length() &&
                        inputLine.charAt(i) != ' ' &&
                        inputLine.charAt(i) != '\"' &&
                        inputLine.charAt(i) != '\'') {
                    variableVal +=  inputLine.charAt(i);
                    i++;
                }
                contextInstance.addToContext(variableKey, variableVal);
                currentToken = new String();
                continue;
            }
            currentToken += inputLine.charAt(i);
        }
        if (currentToken.length() > 0) {
            tokens.add(currentToken);
        }
        return tokens;
    }
    private static String extractContextVariable(
            /**
             * the helper function to extract the context variable: we also have to obtain the variable name correctly
             * (to go back in the String we are provided with)
             */
            String inputLine,
            int i,
            Context contextInstance) throws ContextException {
        i++;
        String variableName = new String();
        while (i < inputLine.length() &&
                inputLine.charAt(i) != ' ' &&
                inputLine.charAt(i) != '\"' &&
                inputLine.charAt(i) != '\'') {
            variableName = variableName + inputLine.charAt(i);
            i++;
        }
        if (contextInstance.search(variableName)) {
            return contextInstance.getValue(variableName);
        } else {
            throw new ContextException();
        }
    }
    private static String extractStrongQuotizedTokenPreToken(
            String inputLine,
            int i,
            Context contextInstance) throws  WrongInputFormatException, ContextException {
        /**
         * it is also the helper function in order to make the function "tokenize" more consize
         */
        i++;
        String quotizedToken = new String();
        while (i < inputLine.length() &&
               inputLine.charAt(i) != '\"') {
            if (inputLine.charAt(i) == '$')  {
                String contextVariable = extractContextVariable(inputLine, i, contextInstance);
                quotizedToken += contextVariable;
                i += contextVariable.length() + 1;
            } else {
                quotizedToken += inputLine.charAt(i);
                i++;
            }
        }
        if (i == inputLine.length()) {
            throw new WrongInputFormatException();
        }
        return quotizedToken;
    }
    private static String extractWeakQuotizedTokenPreToken(
            /**
             * to avoid the collisions like 'aaa bbb' - as it should be treated as one tokenPreToken
             */
            String inputLine,
            int i) throws WrongInputFormatException {
        String weakQuotizedToken = new String();
        i++;
        while (i < inputLine.length() &&
               inputLine.charAt(i) != '\'') {
            weakQuotizedToken += inputLine.charAt(i);
            i++;
        }
        if (i == inputLine.length()) {
            throw new WrongInputFormatException();
        }
        return weakQuotizedToken;
    }
}