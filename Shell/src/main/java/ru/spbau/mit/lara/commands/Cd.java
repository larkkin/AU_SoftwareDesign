package ru.spbau.mit.lara.commands;

import ru.spbau.mit.lara.exceptions.ExitException;
import ru.spbau.mit.lara.exceptions.NotFoundFolderException;
import ru.spbau.mit.lara.exceptions.ShellRuntimeException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Cd implements Command {
    /** Change current directory
     *  has two variants: piped and non-piped, as all the commands do
     */
    @Override
    public String execute(List<String> tokens) throws ExitException, NotFoundFolderException {
        if (tokens.size() > 1) {
            return "Wrong amount of params";
        }

        if (tokens.size() == 0) {
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
            return dir.getAbsolutePath() + " is not directory";
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
    public List<String> pipedExecute(List<String> lines) throws ExitException, NotFoundFolderException {
        if (lines.size() > 1) {
            throw new ShellRuntimeException();
        }

        if (lines.size() == 0) {
            System.setProperty("user.dir", System.getProperty("user.home"));
            return new ArrayList<>();
        }

        String dirName = lines.get(0);
        File dir;

        if (dirName.startsWith("/")) {
            dir = new File(dirName);
        } else {
            dir = new File(System.getProperty("user.dir"), dirName);
        }

        if (!dir.isDirectory()) {
            throw  new ShellRuntimeException();
        }

        try {
            System.setProperty("user.dir", dir.getCanonicalPath());
        } catch (IOException e) {
            System.out.println("error while getting dir path");
            throw new ShellRuntimeException();
        }
        return new ArrayList<>();
    }
}
