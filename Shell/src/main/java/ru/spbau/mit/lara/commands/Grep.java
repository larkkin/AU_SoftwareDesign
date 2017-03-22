package ru.spbau.mit.lara.commands;

import org.apache.commons.cli.*;
import ru.spbau.mit.lara.exceptions.ExitException;
import ru.spbau.mit.lara.exceptions.GrepException;
import ru.spbau.mit.lara.exceptions.ShellRuntimeException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.regex.Pattern;

public class Grep implements Command {
    private CommandLineParser argParser = new PosixParser();
    private Options options = new Options();
    public Grep() {
        Option ignoreCase = new Option("i", "ignorecase", false, "ignore case while parsing");
        ignoreCase.setOptionalArg(true);
        options.addOption(ignoreCase);
        Option word = new Option("w", "word", false, "search for the entire word");
        word.setOptionalArg(true);
        options.addOption(word);
        Option additionalLines = new Option("A", "addLines", true, "print n lines following the result");
        additionalLines.setOptionalArg(true);
        additionalLines.setArgs(1);
        options.addOption(additionalLines);
    }

    public String execute(List<String> tokens) throws ExitException {
        Pattern pattern;
        String fileNameStr;
        try {
            PatternAndStringPair pair = computePattern(tokens);
            pattern = pair.pattern;
            fileNameStr = pair.str;
        } catch (GrepException e) {
            return "wrong grep options format";
        }
        StringBuilder result = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileNameStr))) {
            String line = br.readLine();
            while (line != null) {
                if (pattern.matcher(line).find()) {
                    result.append(line);
                    result.append("\n");
                }
                line = br.readLine();
            }
        } catch (Exception e) {
            return "somethings's wrong with the file";
        }
        return result.toString();
    }

    public List<String> pipedExecute(List<String> lines) throws ExitException {
        System.out.println("there's no piped version of grep");
        throw new ShellRuntimeException();
    }

    private PatternAndStringPair computePattern(List<String> tokens) throws GrepException {
        CommandLine commandLine = null;
        String[] tokensArray = new String[tokens.size()];
        tokensArray = tokens.toArray(tokensArray);
        try {
            commandLine = argParser.parse(options, tokensArray);
        } catch (ParseException e) {
            throw new GrepException();
        }
        String[] args = commandLine.getArgs();
        if (args.length != 2) {
            throw new GrepException();
        }
        String patternStr = args[0];
        String fileNameStr = args[1];
        Pattern pattern = null;
        if (commandLine.hasOption("w")) {
            patternStr = "(^| |\\t)" + patternStr + "( |\\t|$|\\n|\\.|\\?|!|,)";
        }
        if (commandLine.hasOption("i")) {
            pattern = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);
        } else {
            pattern = Pattern.compile(patternStr);
        }
        return new PatternAndStringPair(fileNameStr, pattern);
    }

    private class PatternAndStringPair {
        private String str;
        private Pattern pattern;

        private PatternAndStringPair(String some_str, Pattern some_pattern) {
            str = some_str;
            pattern = some_pattern;
        }
    }
}
