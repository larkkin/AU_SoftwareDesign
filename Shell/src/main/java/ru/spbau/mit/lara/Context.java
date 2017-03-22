package ru.spbau.mit.lara;

import java.util.HashMap;
import java.util.Map;

/**
 * The class has three methods in it: adding to context,
 * providing information about the variable, and
 * providing the value itself
 */
class Context {
    /**
     * The class has the map of the variables that we have in current contexts.
     * Allows to deal with data easily
     */
    private HashMap<String, String> variableStorage = new HashMap();

    void addToContext(String variableName, String variableValue) {
        variableStorage.put(variableName, variableValue);
    }


    String getValue(String variableName) {
        return variableStorage.get(variableName);
    }

    boolean contains(String variableName) {
        return variableStorage.containsKey(variableName);
    }

}
