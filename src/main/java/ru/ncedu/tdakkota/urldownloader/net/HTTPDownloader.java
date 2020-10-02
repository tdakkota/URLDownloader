package ru.ncedu.tdakkota.urldownloader.net;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import ru.ncedu.tdakkota.urldownloader.output.Output;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;


public class HTTPDownloader implements Downloader, Closeable {
    private CloseableHttpClient client;

    public HTTPDownloader() {
        this.client = HttpClients.createDefault();
    }

    public HTTPDownloader(CloseableHttpClient client) {
        this.client = client;
    }

    private void downloadURL(URI uri, Output output) throws IOException {
        HttpGet request = new HttpGet(uri);
        CloseableHttpResponse r = this.client.execute(request);

        HttpEntity e = r.getEntity();
        output.setContentType(e.getContentType().getValue());
        e.writeTo(output.getOutputStream());
    }

    @Override
    public void download(URI uri, Output output) throws DownloaderException {
        try {
            downloadURL(uri, output);
        } catch (IOException e) {
            throw new DownloaderException("HTTP request failed: " + e.getMessage());
        }
    }

    @Override
    public void close() throws IOException {
        this.client.close();
    }
}
