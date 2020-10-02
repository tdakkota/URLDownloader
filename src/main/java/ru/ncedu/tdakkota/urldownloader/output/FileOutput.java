package ru.ncedu.tdakkota.urldownloader.output;

import java.io.*;
import java.nio.charset.Charset;

public class FileOutput implements Output, Closeable {
    private FileOutputStream output;
    private Charset charset = Charset.defaultCharset();
    private String contentType;

    public FileOutput(String path) throws IOException, FileOutputDialogException {
        this(path, new ConsoleDialog());
    }

    public FileOutput(String path, FileOutputDialog dialog) throws IOException, FileOutputDialogException {
        File file = new File(path);
        if (!file.createNewFile()) {
            dialog.ask(path);
        }

        this.output = new FileOutputStream(file);
    }

    public OutputStream getOutputStream() {
        return output;
    }

    @Override
    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    @Override
    public Charset getCharset() {
        return this.charset;
    }

    @Override
    public void setContentType(String t) {
        this.contentType = t;
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public void close() throws IOException {
        this.output.close();
    }
}
