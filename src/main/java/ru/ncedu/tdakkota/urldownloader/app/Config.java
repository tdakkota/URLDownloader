package ru.ncedu.tdakkota.urldownloader.app;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class Config {
    private URL url;
    private Path output;
    private String[] args;

    Config(String[] args) {
        this.args = args;
    }

    void parse() throws ConfigException, MalformedURLException {
        if (args.length < 1) {
            throw new ConfigException("Missing required option: URL");
        }
        this.url = new URL(args[0]);

        String outputPath = this.url.getFile();
        if (!outputPath.isEmpty()) {
            outputPath = outputPath.substring(1);
        }
        if (outputPath.isEmpty()) {
            outputPath = "index.html";
        }
        if (args.length > 1) {
            outputPath = args[1];
        }
        this.output = Paths.get(outputPath);
    }

    URL getURL() {
        return url;
    }


    Path getOutputPath() {
        return output;
    }

    Path getOutputDirectory() {
        if (Files.isDirectory(this.output)) {
            return this.output;
        }
        return this.output.toAbsolutePath().getParent();
    }

    String getOutputFile() {
        if (Files.isDirectory(this.output)) {
            return Paths.get(this.output.toString(), url.getFile()).toString();
        }
        return this.output.toString();
    }

    String[] getArgs() {
        return args;
    }
}
