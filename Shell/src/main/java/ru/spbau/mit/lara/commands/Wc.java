package ru.spbau.mit.lara.commands;


import ru.spbau.mit.lara.exceptions.ExitException;
import ru.spbau.mit.lara.exceptions.ShellRuntimeException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Wc implements Command {
    public String execute(List<String> tokens) throws ExitException {
        if (tokens.size() > 1) {
            throw new ShellRuntimeException();
        }
        StringBuilder result = new StringBuilder();
        int linesCount = 0;
        int wordsCount = 0;
        int bytesCount = 0;

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(tokens.get(0)));
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            /**
             * when the file is not here(in the provided path)
             */
            return "";
        }
        try {
            String line = br.readLine();
            while (line != null) {
                linesCount++;
                wordsCount += line.split(" ").length;
                bytesCount += line.getBytes().length;
                line = br.readLine();
            }
            br.close();
        } catch(IOException e) {
            System.out.println("some other trouble");
            return "";
        }
        result.append(linesCount);
        result.append(" ");
        result.append(wordsCount);
        result.append(" ");
        result.append(bytesCount);
        /**
         * we return the String to which all the needed parameters are appended in order: first - lines,
         * then the words go - and, finally, bytes
         */
        return result.toString();
    }

    public List<String> pipedExecute(List<String> lines) throws ExitException {
        StringBuilder sb = new StringBuilder();
        int linesCount = 0;
        int wordsCount = 0;
        int bytesCount = 0;

        for (String line : lines) {
            linesCount++;
            wordsCount += line.split(" ").length;
            bytesCount += line.getBytes().length;
        }
        sb.append(linesCount);
        sb.append(" ");
        sb.append(wordsCount);
        sb.append(" ");
        sb.append(bytesCount);
        ArrayList<String> result = new ArrayList<String>();
        result.add(sb.toString());
        /**
         * we add to the List of the String our string output for the executed command
         */
        return result;
    }
}
