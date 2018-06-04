package ru.spbau.mit.lara;
import ru.spbau.mit.lara.exceptions.ExitException;
import ru.spbau.mit.lara.exceptions.NotFoundFolderException;
import ru.spbau.mit.lara.exceptions.ShellException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The starting point
 */
public class Main {
    public static void main(String[] args) {

        Shell shellInstance = new Shell();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                // We show the invitation to write the command
                System.out.print("«");
                String inputLine = br.readLine();
                try {
                    shellInstance.processLine(inputLine);
                }catch (NotFoundFolderException e) {
                    System.out.println("Folder is not found");
                } catch (ShellException e) {
                    // If the result of processing the line is negative
                    System.out.println("wrong string format =(\n\tplease, try again");
                } catch (ExitException e) {
                    break;
                } catch (RuntimeException e) {
                    System.out.println("Run-time exception");
                }
            }
        } catch (IOException e) {}
    }
}
