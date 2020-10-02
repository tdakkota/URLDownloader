package ru.ncedu.tdakkota.urldownloader.output;

import java.io.PrintStream;
import java.util.Scanner;

public class ConsoleDialog implements FileOutputDialog {
    private Scanner scanner;
    private PrintStream out;

    public ConsoleDialog() {
        scanner = new Scanner(System.in);
        out = System.out;
    }

    public ConsoleDialog(Scanner scanner, PrintStream printStream) {
        this.scanner = scanner;
        this.out = printStream;
    }

    @Override
    public String ask(String was) throws FileOutputDialogException {
        out.printf("File %s already exists, replace it? (Y - Yes/N - No/C - Change name)\n", was);
        for (; ; ) {
            String line = scanner.nextLine();
            switch (line.trim().toUpperCase()) {
                case "Y":
                    return was;
                case "N":
                    throw new FileOutputDialogException("File already exists");
                case "C":
                    out.print("Type new name: ");
                    line = scanner.nextLine();
                    return line;
            }
        }
    }
}
