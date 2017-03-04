package ru.spbau.mit.lara.commands;

import ru.spbau.mit.lara.exceptions.ExitException;
import ru.spbau.mit.lara.exceptions.ShellRuntimeException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * this command is to display the contents of the file
 *
 */

public class Cat implements Command {
    public String execute(List<String> tokens) throws ExitException {
        if (tokens.size() > 1) {
            throw new ShellRuntimeException();
        }
        /**
         * currently only the one token per command cat is supported
         */
        StringBuilder result = new StringBuilder();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(tokens.get(0)));
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            /**
             * the message that is shown to the user when the file cannot be found
             */
            return "";
        }
        try {
            String line = br.readLine();
            while (line != null) {
                result.append(line);
                result.append("\n");
                line = br.readLine();
            }
            br.close();
            /**
             * we finish working with file and close it
             */
        } catch(IOException e) {
            System.out.println("some other trouble");
            return "";
        }
        return result.toString();
    }

    public List<String> pipedExecute(List<String> lines) throws ExitException {
        return lines;
    }
}
