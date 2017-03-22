package ru.spbau.mit.lara;

import ru.spbau.mit.lara.exceptions.ContextException;
import ru.spbau.mit.lara.exceptions.WrongInputFormatException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The class for parsing the line shell is redirecting to us
 * we parse the line and divide it into atomic structures:
 * command names and arguments
 */
class Tokenizer {
    /**
     * We execute the tokenizing if a given inputString: the result is the lists of the lists
     * this also helps us to divide tokens per commands(in the "pipe" case)
     */
    static List<ArrayList<String>> Tokenize(String inputLine,
                                            Context contextInstance) throws WrongInputFormatException,
                                                                            ContextException {
        List<String> preTokens = new ArrayList<String>();
        String[] preTokensArray = inputLine.split("\\|");
        Collections.addAll(preTokens, preTokensArray);
        List<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        for (String preToken : preTokens) {
            result.add(TokenizeWithoutPipes(preToken, contextInstance));
        }
        return result;
    }

    /**
     * Here we can deal with a list of tokens, because we don't have to divide tokens by command
     */
    private static ArrayList<String> TokenizeWithoutPipes(String inputLine,
                                                          Context contextInstance) throws WrongInputFormatException,
                                                                                          ContextException {
        ArrayList<String> tokens = new ArrayList<String>();
        String currentToken = new String();
        for (int i = 0; i < inputLine.length(); i++) {
            if (inputLine.charAt(i) == '\'') {
                // The weak quoting case
                String weakQuotizedPreToken = extractWeakQuotizedTokenPreToken(inputLine, i);
                currentToken += weakQuotizedPreToken;
                i += weakQuotizedPreToken.length() + 1;
                continue;
            }
            if (inputLine.charAt(i) == '\"') {
                // The strong quoting case
                String strongQuotizedToken = extractStrongQuotizedTokenPreToken(inputLine, i, contextInstance);
                currentToken += strongQuotizedToken;
                do {
                    i++;
                } while (inputLine.charAt(i) != '\"');
                continue;
            }
            if (inputLine.charAt(i) == '$')  {
                // When we need to extract the values from the context of the current instance of shell
                String contextVariable = extractContextVariable(inputLine, i, contextInstance);
                currentToken += contextVariable;
                while (i < inputLine.length()
                        && inputLine.charAt(i) != ' '
                        && inputLine.charAt(i) != '\"'
                        && inputLine.charAt(i) != '\'') {
                    i++;
                }
                i--;
                continue;
            }
            if (inputLine.charAt(i) == ' ') {
                // Thus we see when the end of the token we are formating is located
                if (currentToken.length() > 0) {
                    tokens.add(currentToken);
                    currentToken = new String();
                }
                continue;
            }
            if (inputLine.charAt(i) == '=' && tokens.size() == 0) {
                // When we see the '=', we have to add values to context
                String variableKey = currentToken;
                String variableVal = new String();
                i++;
                while ( i < inputLine.length()
                        && inputLine.charAt(i) != ' '
                        && inputLine.charAt(i) != '\"'
                        && inputLine.charAt(i) != '\'') {
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

    /**
     * The helper function to extract the context variable: we also have to obtain the variable name correctly
     * (to go back in the String we are provided with)
     */
    private static String extractContextVariable(String inputLine,
                                                 int i,
                                                 Context contextInstance) throws ContextException {
        i++;
        String variableName = new String();
        while (i < inputLine.length()
                && inputLine.charAt(i) != ' '
                && inputLine.charAt(i) != '\"'
                && inputLine.charAt(i) != '\'') {
            variableName = variableName + inputLine.charAt(i);
            i++;
        }
        if (contextInstance.contains(variableName)) {
            return contextInstance.getValue(variableName);
        } else {
            throw new ContextException();
        }
    }

    private static String extractStrongQuotizedTokenPreToken(String inputLine,
                                                             int i,
                                                             Context contextInstance) throws  WrongInputFormatException,
                                                                                              ContextException {
        // It is also the helper function in order to make the function "tokenize" more consize
        i++;
        String quotizedToken = new String();
        while (i < inputLine.length()
               && inputLine.charAt(i) != '\"') {
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

    /**
     * To avoid the collisions like 'aaa bbb' - as it should be treated as one tokenPreToken
     */
    private static String extractWeakQuotizedTokenPreToken(String inputLine,
                                                           int i) throws WrongInputFormatException {
        String weakQuotizedToken = new String();
        i++;
        while (i < inputLine.length()
               && inputLine.charAt(i) != '\'') {
            weakQuotizedToken += inputLine.charAt(i);
            i++;
        }
        if (i == inputLine.length()) {
            throw new WrongInputFormatException();
        }
        return weakQuotizedToken;
    }
}