package ru.spbau.mit.lara.commands;

import ru.spbau.mit.lara.exceptions.ExitException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lara on 26.02.17.
 */
public class Echo implements Command {
    public String execute(List<String> tokens) throws ExitException {
        StringBuilder result = new StringBuilder();
        result.append(tokens.get(0));
        for (int i = 1; i < tokens.size(); i++) {
            result.append(' ');
            result.append(tokens.get(i));
        }
        /**
         * tokens are shown separated by whitespace
         */
        return result.toString();
    }

    public List<String> pipedExecute(List<String> lines) throws ExitException {
        return new ArrayList<String>();
    }
    /**
     * to versions of echo command. Can be used to echo the values of several variables simultaneously
     */

}
