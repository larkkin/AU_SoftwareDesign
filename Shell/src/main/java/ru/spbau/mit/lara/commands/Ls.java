package ru.spbau.mit.lara.commands;

import ru.spbau.mit.lara.exceptions.ExitException;
import ru.spbau.mit.lara.exceptions.ShellRuntimeException;

import java.io.File;
import java.util.List;

public class Ls implements Command {
    @Override
    public String execute(List<String> tokens) throws ExitException {
        if (tokens.size() > 1) {
            return "wrong ls options format";
        }
        File dir;
        if (tokens.isEmpty()) {
            dir = new File(System.getProperty("user.dir"));
        } else {
            dir = new File(tokens.get(0));
        }
        dir = dir.getAbsoluteFile();
        if (!dir.isDirectory()) {
            return "not a directory";
        }

        StringBuilder result = new StringBuilder();
        File[] filesInDir = dir.listFiles();
        for (File file : filesInDir) {
            result.append(file.getName());
            result.append("\n");
        }
        return result.toString();
    }

    @Override
    public List<String> pipedExecute(List<String> lines) throws ExitException {
        System.out.println("there's no piped version of ls");
        throw new ShellRuntimeException();
    }
}
