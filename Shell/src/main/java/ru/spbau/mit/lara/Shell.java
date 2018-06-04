package ru.spbau.mit.lara;

import ru.spbau.mit.lara.commands.*;
import ru.spbau.mit.lara.exceptions.ExitException;
import ru.spbau.mit.lara.exceptions.NotFoundFolderException;
import ru.spbau.mit.lara.exceptions.ShellException;
import ru.spbau.mit.lara.exceptions.ShellRuntimeException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;

/**
 * It is the main class that receives the input line
 * from stdin, proceeds it and executes the command
 */
class Shell {
    private Context context;
    private HashMap<String, Command> commandStorage;

    /**
     * Initiating the fields
     */
    Shell() {
        context = new Context();
        commandStorage = new HashMap();
        /*
         * We put the commands avaliable by now in the commandStorage: the list can be enlarged.
         */
        commandStorage.put("echo", new Echo());
        commandStorage.put("cat", new Cat());
        commandStorage.put("exit", new Exit());
        commandStorage.put("pwd", new Pwd());
        commandStorage.put("wc", new Wc());
        commandStorage.put("ls", new Ls());
        commandStorage.put("cd", new Cd());
    }

    /**
     * We resort to running External Command when we cannot find the necessary one in our commandStorage
     * and return the result as the string for the sameness
     */
    private static String runExternalCommand(String[] tokens) throws IOException {

        Process process = new ProcessBuilder(tokens).start();
        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        process.getInputStream()));
        StringBuilder result = new StringBuilder();
        String line = br.readLine();
        while (line != null) {
            result.append(line);
            result.append("\n");
            line = br.readLine();
        }
        return result.toString();
    }

    /**
     * This function is written to avoid the duplicated code
     */
    private static String[] getArrayFromStringAndList(String str, List<String> lst) {
        String[] arr = new String[1 + lst.size()];
        arr[0] = str;
        for (int i = 0; i < lst.size(); i++) {
            arr[i + 1] = lst.get(i);
        }
        return arr;
    }

    /**
     * If we are given the command without the pipe and simply execute it
     */
    private void executeCommand(String commandName, List<String> arguments) throws ExitException, NotFoundFolderException {
        if (commandStorage.containsKey(commandName)) {
            System.out.println(commandStorage.get(commandName).execute(arguments));
        }
        else {
            String[] argsArray = getArrayFromStringAndList(commandName, arguments);
            try {
                System.out.println(runExternalCommand(argsArray));
            } catch (IOException e) {
                System.out.println(
                        "caught IOException while running external command " + commandName);
            }
        }
    }

    /**
     * Firstly, we execute the first command, than we pass the results to the next commands in a row
     */
    private void executePipeline(List<ArrayList<String>> tokensList) throws ExitException, NotFoundFolderException{
        ArrayList<String> tokens = tokensList.get(0);
        String commandName = tokens.get(0);
        List<String> arguments = tokens.subList(1, tokens.size());
        List<String> message = new ArrayList<String>();
        if (commandStorage.containsKey(commandName)) {
            String[] lines_array = commandStorage.get(commandName)
                    .execute(arguments)
                    .split("\\n");
            Collections.addAll(message, lines_array);
        } else {
            try {
                String[] argsArray = getArrayFromStringAndList(commandName, arguments);
                String[] linesArray = runExternalCommand(argsArray).split("\\n");
                Collections.addAll(message, linesArray);
            } catch (IOException e) {
                message.clear();
            }
        }
        for (int i = 1; i < tokensList.size(); i++) {
            commandName = tokensList.get(i).get(0);
            if (commandStorage.containsKey(commandName)) {
                message = commandStorage.get(commandName)
                        .pipedExecute(message);
            } else {
                throw new ShellRuntimeException();
            }
        }

        for (String line : message) {
            System.out.println(line);
        }
    }

    /**
     * The main shell method. we arrive here when the input line is written in Main class
     */
    void processLine(String inputLine) throws ShellException, ExitException, NotFoundFolderException {
        List<ArrayList<String>> tokensList = Tokenizer.Tokenize(inputLine, context);
        if (tokensList.size() == 1 && tokensList.get(0).size() > 0) {
            ArrayList<String> tokens = tokensList.get(0);
            String commandName = tokens.get(0);
            executeCommand(commandName, tokens.subList(1, tokens.size()));
        }
        if (tokensList.size() > 1) {
            executePipeline(tokensList);
        }
    }


}
