package ru.ncedu.tdakkota.urldownloader.output;

public interface FileOutputDialog {
    public String ask(String was) throws FileOutputDialogException;
}
