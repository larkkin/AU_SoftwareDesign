package ru.spbau.mit.lara; /**
 * Created by lara on 24.02.17.
 */
import ru.spbau.mit.lara.exceptions.ExitException;
import ru.spbau.mit.lara.exceptions.ShellException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
public class Main {
    public static void main(String[] args) {

        Shell shellInstance = new Shell();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                /**
                 * we show the invitation to write the command
                 */
                System.out.print("«");
                String inputLine = br.readLine();
                try {
                    shellInstance.processLine(inputLine);
                } catch (ShellException e) {
                    /**
                     * if the result of processing the line is negative
                     */
                    System.out.println("wrong string format =(\n\tplease, try again");
                } catch (ExitException exE) {
                    break;
                }
            }
        } catch(IOException e) {}
    }
}
