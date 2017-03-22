package ru.spbau.mit.lara.commands;

import ru.spbau.mit.lara.exceptions.ExitException;

import java.util.ArrayList;
import java.util.List;

/**
 * We ask the system to provide the essential information
 */
public class Pwd implements Command {
    public String execute(List<String> tokens) throws ExitException {
        return System.getProperty("user.dir");
    }

    public List<String> pipedExecute(List<String> lines) throws ExitException {
        ArrayList<String> result = new ArrayList<String>();
        result.add(System.getProperty("user.dir"));
        return result;
    }
}