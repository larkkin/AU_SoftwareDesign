package ru.spbau.mit.lara.commands;

import ru.spbau.mit.lara.exceptions.ExitException;
import ru.spbau.mit.lara.exceptions.NotFoundFolderException;

import java.util.List;

/**
 * It's the interface that allows the commands to
 * implement it in order to unify all the commands
 */
public interface Command {
    /**
     * A method to execute the first command in a pipeline
     * or the single command.
     * @param tokens represents command line arguments/options
     * @return single string (containing newline symbols) that
     * should be printed to the stdout or passed along the pipeline
     */
    String execute(List<String> tokens) throws ExitException, NotFoundFolderException;

    /**
     * A method to execute a non-first command in a pipeline.
     * @param lines represents the output of the previous command as if it was
     *              given to the stdin
     * @return same as the execute method but now lines are
     *         stored in the list
     */
    List<String> pipedExecute(List<String> lines) throws ExitException, NotFoundFolderException;
}

