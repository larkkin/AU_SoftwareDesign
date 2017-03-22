package ru.spbau.mit.lara.commands;

import ru.spbau.mit.lara.exceptions.ExitException;

import java.util.List;

/**
 * The exit command throws the exception and thus the application is shut down
 */
public class Exit implements Command {
    public String execute(List<String> tokens) throws ExitException {
        throw new ExitException();
    }

    public List<String> pipedExecute(List<String> lines) throws ExitException {
        throw new ExitException();
    }
}
