package ru.spbau.mit.lara.commands;


import ru.spbau.mit.lara.exceptions.ExitException;
import ru.spbau.mit.lara.exceptions.ShellRuntimeException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Command for counting stats for given file:
 * number of lines, words and bytes
 */
public class Wc implements Command {
    public String execute(List<String> tokens) throws ExitException {
        if (tokens.size() != 1) {
            System.out.println("file not found");
            return "";
        }
        StringBuilder result = new StringBuilder();
        int linesCount = 0;
        int wordsCount = 0;
        int bytesCount = 0;
        
        try (BufferedReader br = new BufferedReader(new FileReader(new File(tokens.get(0)).getAbsoluteFile()))) {
            String line = br.readLine();
            while (line != null) {
                linesCount++;
                String[] arr = line.split(" ");
                for (String s : arr) {
                    if (s.length() > 0) {
                        wordsCount++;
                    }
                }
                bytesCount += line.getBytes().length + 1;
                line = br.readLine();
            }
        }
        catch (FileNotFoundException e) {
            // when the file is not here(in the provided path)
            System.out.println("file not found");
            return "";
        } catch(IOException e) {
            System.out.println("some other trouble");
            return "";
        }
        result.append(linesCount);
        result.append(" ");
        result.append(wordsCount);
        result.append(" ");
        result.append(bytesCount);
        return result.toString();
    }
    
    public List<String> pipedExecute(List<String> lines) throws ExitException {
        StringBuilder sb = new StringBuilder();
        int linesCount = 0;
        int wordsCount = 0;
        int bytesCount = 0;

        for (String line : lines) {
            linesCount++;
            String[] arr = line.split(" ");
            for (String s : arr) {
                if (s.length() > 0) {
                    wordsCount++;
                }
            }
            bytesCount += line.getBytes().length + 1;
        }
        sb.append(linesCount);
        sb.append(" ");
        sb.append(wordsCount);
        sb.append(" ");
        sb.append(bytesCount);
        ArrayList<String> result = new ArrayList<String>();
        result.add(sb.toString());
        // we add to the List of the String our string output for the executed command
        return result;
    }
}
