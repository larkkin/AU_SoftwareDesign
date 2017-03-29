package ru.spbau.mit.lara.commands;

import ru.spbau.mit.lara.exceptions.ExitException;
import ru.spbau.mit.lara.exceptions.ShellRuntimeException;

import java.io.*;
import java.util.List;

/**
 * This command is to display the contents of the file
 * as all the commands, it implements the Command interface
 */
public class Cat implements Command {
    public String execute(List<String> tokens) throws ExitException {
        // Currently only the one token per command cat is supported
        if (tokens.size() > 1) {
            throw new ShellRuntimeException();
        }
        StringBuilder result = new StringBuilder();
        // We finish working with file and close it by using try with resources
        try (BufferedReader br = new BufferedReader(new FileReader(new File(tokens.get(0)).getAbsoluteFile()))) {
              String line = br.readLine();
              while (line != null) {
                  result.append(line);
                  result.append("\n");
                  line = br.readLine();
              }
            }
        // The message that is shown to the user when the file cannot be found
        catch (FileNotFoundException e) {
              System.out.println("file not found");
              return "";
        }
        catch (IOException e) {
                System.out.println("some other trouble");
                return "";
            }
            return result.toString();
        }

    public List<String> pipedExecute(List<String> lines) throws ExitException {
        return lines;
    }
}
