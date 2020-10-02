package ru.ncedu.tdakkota.urldownloader.app.config;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Config {
    private URL url;
    private Path output;
    private boolean help = false;
    private boolean runAfter = false;
    private String[] args;

    public Config(String[] args) {
        this.args = args;
    }

    private void fromMap(Map<String, String> args) throws ConfigException, MalformedURLException {
        if (args.containsKey("help") || args.containsKey("h")) {
            System.out.println("app.jar <-r|-h|--help> <URL> [FILE]\n");
            System.out.println("Options:");
            System.out.println("\tr -- show open dialog after download");
            System.out.println("\t-h or --help -- print this message");
            System.out.println();
            help = true;
            return;
        }

        if (!args.containsKey("url")) {
            throw new ConfigException("Missing required option: URL");
        }
        this.url = new URL(args.get("url"));

        String outputPath = this.url.getFile();
        if (!outputPath.isEmpty()) {
            outputPath = outputPath.substring(1);
        }
        if (outputPath.isEmpty()) {
            outputPath = "index.html";
        }
        if (args.containsKey("file")) {
            outputPath = args.get("file");
        }
        this.output = Paths.get(outputPath);
        this.runAfter = args.containsKey("r");
    }

    public void parse() throws ConfigException, MalformedURLException {
        final String[] positional = new String[]{"url", "file"};
        int positionalIndex = 0;

        Map<String, String> args = new HashMap<>();
        for (String arg : this.args) {
            if (arg.startsWith("--")) {
                int valueIndex = arg.indexOf("=");
                if (valueIndex < 0)
                    args.put(arg.substring(2), "");
                else
                    args.put(arg.substring(2, valueIndex), arg.substring(valueIndex));

                continue;
            } else if (arg.startsWith("-")) {
                args.put(arg.substring(1), "");
                continue;
            }

            if (positionalIndex >= positional.length) continue;
            args.put(positional[positionalIndex], arg);
            positionalIndex++;
        }

        fromMap(args);
    }

    public URL getURL() {
        return url;
    }

    public Path getOutputPath() {
        return output;
    }

    public Path getOutputDirectory() {
        if (Files.isDirectory(this.output)) {
            return this.output;
        }
        return this.output.toAbsolutePath().getParent();
    }

    public String getOutputFile() {
        if (Files.isDirectory(this.output)) {
            return Paths.get(this.output.toString(), url.getFile()).toString();
        }
        return this.output.toString();
    }

    public boolean isHelp() {
        return help;
    }

    public boolean isRunAfter() {
        return runAfter;
    }

    public String[] getArgs() {
        return args;
    }
}
