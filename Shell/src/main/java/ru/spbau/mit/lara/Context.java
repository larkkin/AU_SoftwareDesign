package ru.spbau.mit.lara;

import java.util.HashMap;
import java.util.Map;
/**
 * Created by lara on 26.02.17.
 */
public class Context {
    /**
     * the class has the map of the variables that we have in current contexts.
     * Allows to deal with data easily
     */
    private HashMap<String, String> variableStorage = new HashMap();

    public void addToContext(String variableName, String variableValue) {
        variableStorage.put(variableName, variableValue);
    }


    public String getValue(String variableName) {
        return variableStorage.get(variableName);
    }

    public boolean search(String variableName) {
        return variableStorage.containsKey(variableName);
    }

}
