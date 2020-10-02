package ru.ncedu.tdakkota.urldownloader.output;

import java.util.Scanner;

public class ConsoleDialog implements FileOutputDialog {
    private Scanner scanner = new Scanner(System.in);

    @Override
    public String ask(String was) throws FileOutputDialogException {
        System.out.printf("File %s already exists, replace it? (Y - Yes/N - No/C - Change name)\n", was);
        for (; ; ) {
            String line = scanner.nextLine();
            switch (line.trim().toUpperCase()) {
                case "Y":
                    return was;
                case "N":
                    throw new FileOutputDialogException("File already exists");
                case "C":
                    System.out.print("Type new name: ");
                    line = scanner.nextLine();
                    return line;
            }
        }
    }
}
