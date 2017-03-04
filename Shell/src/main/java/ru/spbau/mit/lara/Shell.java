package ru.spbau.mit.lara;

import ru.spbau.mit.lara.commands.*;
import ru.spbau.mit.lara.exceptions.ExitException;
import ru.spbau.mit.lara.exceptions.ShellException;
import ru.spbau.mit.lara.exceptions.ShellRuntimeException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

/**
 * Created by lara on 26.02.17.
 */
public class Shell {
    private Context context;
    private HashMap<String, Command> commandStorage;

    /**
     * initiating the fields
     */
    Shell() {
        context = new Context();
        commandStorage = new HashMap();
        /**
         * we put the commands avaliable by now in the commandStorage: the list can be enlarged.
         */
        commandStorage.put("echo", new Echo());
        commandStorage.put("cat", new Cat());
        commandStorage.put("exit", new Exit());
        commandStorage.put("pwd", new Pwd());
        commandStorage.put("wc", new Wc());
    }

    private static String runExternalCommand(
            /**
             * we resort to running External Command when we cannot find the necessary one in our commandStorage
             * and return the result as the string for the sameness
             */
            String[] tokens) throws IOException {

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
    private static String[] getArrayFromStringAndList(String str, List<String> lst) {
        /**
         * this function is written to avoid the duplicated code
         */
        String[] arr = new String[1 + lst.size()];
        arr[0] = str;
        for (int i = 0; i < lst.size(); i++) {
            arr[i+1] = lst.get(i);
        }
        return arr;
    }

    private void executeCommand(String commandName,
                               List<String> arguments) throws ExitException {
        /**
         * if we are given the command without the pipe and simply execute it
         */
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
    private void executePipeline(
            /**
             * firstly, we execute the first command, than we pass the results to the next commands in a row
             */
            ArrayList<ArrayList<String>> tokens_list) throws ExitException {
        ArrayList<String> tokens = tokens_list.get(0);
        String commandName = tokens.get(0);
        List<String> arguments = tokens.subList(1, tokens.size());
        List<String> message = new ArrayList<String>();
        if (commandStorage.containsKey(commandName)) {
            String[] lines_array = commandStorage.get(commandName)
                                                 .execute(arguments)
                                                 .split("\\n");
            for (String line : lines_array) {
                message.add(line);
            }
        } else {
            try {
                String[] argsArray = getArrayFromStringAndList(commandName, arguments);
                String[] lines_array = runExternalCommand(argsArray).split("\\n");
                for (String line : lines_array) {
                    message.add(line);
                }
            } catch (IOException e) {
                message.clear();
            }
        }
        for (int i = 1; i < tokens_list.size(); i++) {
            commandName = tokens_list.get(i).get(0);
            if (commandStorage.containsKey(commandName)) {
                message = commandStorage.get(commandName)
                                                  .pipedExecute(message);
            } else throw new ShellRuntimeException();
        }

        for (String line : message) {
            System.out.println(line);
        }
    }

    public void processLine(
            /**
             * the main shell method. we arrive here when the input line is written in Main class
             */
            String inputLine) throws ShellException, ExitException {
        ArrayList<ArrayList<String>> tokens_list = Tokenizer.Tokenize(inputLine, context);
        if (tokens_list.size() == 1 && tokens_list.get(0).size() > 0) {
            ArrayList<String> tokens = tokens_list.get(0);
            String commandName = tokens.get(0);
            executeCommand(commandName, tokens.subList(1, tokens.size()));
        }
        if (tokens_list.size() > 1) {
            executePipeline(tokens_list);
        }
    }


}
