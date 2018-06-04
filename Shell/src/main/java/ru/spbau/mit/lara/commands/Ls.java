package ru.spbau.mit.lara.commands;

import ru.spbau.mit.lara.exceptions.ExitException;
import ru.spbau.mit.lara.exceptions.NotFoundFolderException;
import ru.spbau.mit.lara.exceptions.ShellRuntimeException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Ls implements Command {
    /** Displays contains of folder
     *  has two variants: piped and non-piped, as all the commands do
     *  Ls shows contain of current directory by default.
     *  Also you can point out path to folder
     */
    @Override
    public String execute(List<String> tokens) throws ExitException, NotFoundFolderException {
        if (tokens.size() > 1){
            throw new ShellRuntimeException();
        }
        File dir;
        if (tokens.size() == 0) {
            dir = new File(System.getProperty("user.dir"));
        } else {
            dir = new File(tokens.get(0));
        }
        dir = dir.getAbsoluteFile();
        if (!dir.isDirectory()) {
            throw new NotFoundFolderException();
        }

        StringBuilder result = new StringBuilder();
        File[] files = dir.listFiles();
        for (File file : files) {
            result.append(file.getAbsolutePath());
            result.append("\n");
        }

        return result.toString();
    }

    @Override
    public List<String> pipedExecute(List<String> lines) throws ExitException {
        ArrayList<String> result = new ArrayList<>();
        if (lines.size() > 1){
            throw new ShellRuntimeException();
        }
        File dir;

        if (lines.size() == 0) {
            dir = new File(System.getProperty("user.dir"));
        } else {
            dir = new File(lines.get(0));
        }
        dir = dir.getAbsoluteFile();
        if (!dir.isDirectory()) {
            throw new ShellRuntimeException();
        }

        File[] files = dir.listFiles();
        for (File file : files) {
            result.add(file.getAbsolutePath());
        }
        return result;
    }
}
