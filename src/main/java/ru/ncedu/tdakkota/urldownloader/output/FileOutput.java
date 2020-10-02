package ru.ncedu.tdakkota.urldownloader.output;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileOutput implements Output, Closeable {
    private File file;
    private FileOutputStream output;
    private Charset charset = Charset.defaultCharset();
    private String contentType;

    public FileOutput(String path) throws IOException, FileOutputDialogException {
        this(path, new ConsoleDialog());
    }

    public FileOutput(String path, FileOutputDialog dialog) throws IOException, FileOutputDialogException {
        if (Files.exists(Paths.get(path))) {
            path = dialog.ask(path);
            this.file = new File(path);
        } else {
            this.file = new File(path);
            File parent = this.file.getParentFile();
            if (parent != null)
                parent.mkdirs();
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

    public File getFile() {
        return file;
    }

    public String fileName() {
        return this.file.getPath();
    }

    @Override
    public void close() throws IOException {
        this.output.close();
    }
}
