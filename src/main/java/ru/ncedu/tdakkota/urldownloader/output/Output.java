package ru.ncedu.tdakkota.urldownloader.output;

import java.io.OutputStream;
import java.nio.charset.Charset;

public interface Output {
    public OutputStream getOutputStream();

    public void setCharset(Charset charset);
    public Charset getCharset();

    public void setContentType(String type);
    public String getContentType();
}
