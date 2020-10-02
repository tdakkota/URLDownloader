package ru.ncedu.tdakkota.urldownloader.runner;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class DesktopRunner implements Runner {
    public void run(String file) throws RunnerException {
        Desktop desktop = Desktop.getDesktop();
        if (!desktop.isSupported(Desktop.Action.OPEN)) {
            return;
        }
        try {
            desktop.open(new File(file));
        } catch (IOException e) {
            throw new RunnerException("Failed to run program: " + e.getMessage());
        }
    }
}
