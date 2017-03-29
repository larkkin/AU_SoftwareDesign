package ru.spbau.mit.lara.commands;

import ru.spbau.mit.lara.exceptions.ExitException;
import ru.spbau.mit.lara.exceptions.ShellRuntimeException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Cd implements Command {
    @Override
    public String execute(List<String> tokens) throws ExitException {
        if (tokens.size() > 1) {
            return "wrong cd options format";
        }
        if (tokens.isEmpty()) {
            System.setProperty("user.dir", System.getProperty("user.home"));
            return "";
        }
        String dirName = tokens.get(0);
        File dir;
        if (dirName.startsWith("/")) {
            dir = new File(dirName);
        } else {
            dir = new File(System.getProperty("user.dir"), dirName);
        }
        if (!dir.isDirectory()) {
            return "not a directory";
        }
        try {
            System.setProperty("user.dir", dir.getCanonicalPath());
        } catch (IOException e) {
            System.out.println("error while getting dir path");
            throw new ShellRuntimeException();
        }
        return "";
    }

    @Override
    public List<String> pipedExecute(List<String> lines) throws ExitException {
        System.out.println("there's no piped version of cd");
        throw new ShellRuntimeException();
    }
}
