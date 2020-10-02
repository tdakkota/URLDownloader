package ru.ncedu.tdakkota.urldownloader.net;

import ru.ncedu.tdakkota.urldownloader.output.Output;

import java.net.URI;

public interface Downloader {
    public void download(URI uri, Output output) throws DownloaderException;
}
