package ru.ncedu.tdakkota.urldownloader.net;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import ru.ncedu.tdakkota.urldownloader.output.Output;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;

public class FTPDownloader implements Downloader, Closeable {
    private FTPClient client;

    public FTPDownloader() {
        this.client = new FTPClient();
        FTPClientConfig config = new FTPClientConfig();
        this.client.configure(config);
    }

    public FTPDownloader(FTPClient client) {
        this.client = client;
    }

    private void downloadURL(URI uri, Output output) throws IOException, DownloaderException {
        int port = uri.getPort();
        if (port == -1) {
            port = 21;
        }

        this.client.connect(uri.getHost(), port);
        this.login(uri);
        this.client.retrieveFile(uri.getPath(), output.getOutputStream());
    }

    private void login(URI uri) throws IOException, DownloaderException {
        String[] userInfo = uri.getUserInfo().split(":", 1);
        if (userInfo.length < 2) {
            throw new DownloaderException("Incorrect credentials: " + uri.getUserInfo());
        }
        this.client.login(userInfo[0], userInfo[1]);
    }

    @Override
    public void download(URI uri, Output output) throws DownloaderException {
        try {
            downloadURL(uri, output);
        } catch (IOException ex) {
            throw new DownloaderException("FTP download failed: " + ex.getMessage());
        }
    }

    public void close() throws IOException {
        this.client.disconnect();
    }
}
