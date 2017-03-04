package ru.spbau.mit.lara.commands;

import ru.spbau.mit.lara.exceptions.ExitException;

import java.util.List;

/**
 * Created by lara on 26.02.17.
 */
public interface Command {
    String execute(List<String> tokens) throws ExitException;

    List<String> pipedExecute(List<String> lines) throws ExitException;
}

/**
 * it's the interface that allows the commands to
 * implement it in order to unify all the commands
 */