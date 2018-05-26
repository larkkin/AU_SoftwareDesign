package ru.spbau.mit.lara.exceptions;

/**
 * An innner Grep exception, could be thrown if the comand
 * fails to parse the pattern
 */
public class GrepException extends Exception {
    private Exception cause = null;
    private  String message = "";

    public GrepException(Exception e) {
        cause = e;
    }
    public GrepException(String message) {
        this.message = message;
    }

    public boolean hasCause() {
        return cause != null;
    }
    public Exception getCause() {
        return cause;
    }
    public String getMessage() {
        return message;
    }
}
