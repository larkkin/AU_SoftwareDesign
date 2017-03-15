package ru.spbau.mit.lara.commands;

import ru.spbau.mit.lara.exceptions.ExitException;

import java.util.List;

/**
 * it's the interface that allows the commands to
 * implement it in order to unify all the commands
 */
public interface Command {
    String execute(List<String> tokens) throws ExitException;

    List<String> pipedExecute(List<String> lines) throws ExitException;
}

