package ru.ncedu.tdakkota.urldownloader.net;

import ru.ncedu.tdakkota.urldownloader.output.Output;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * SchemaDispatcher maps URI schema to concrete protocol Downloader.
 * e.g. https -> HTTPDownloader, ftp -> FTPDownloader
 */
public class SchemaDispatcher implements Downloader {
    private Map<String, Downloader> downloaderMap = new HashMap<>();

    /**
     * @return default schema-to-implementation mapping.
     */
    public static SchemaDispatcher getDefault() {
        SchemaDispatcher d = new SchemaDispatcher();

        // HTTP/HTTPS
        HTTPDownloader http = new HTTPDownloader();
        d.register("http", http);
        d.register("https", http);

        // FTP
        FTPDownloader ftp = new FTPDownloader();
        d.register("ftp", ftp);

        return d;
    }

    public SchemaDispatcher() {
    }

    public SchemaDispatcher(Map<String, Downloader> downloaderMap) {
        this.downloaderMap.putAll(downloaderMap);
    }


    /**
     * Register adds mapping to the SchemaDispatcher.
     * If mapping already exists, it would be overwritten.
     *
     * @param protocol   URI schema name.
     * @param downloader Downloader implementation.
     */
    public void register(String protocol, Downloader downloader) {
        this.downloaderMap.put(protocol, downloader);
    }

    @Override
    public void download(URI uri, Output output) throws DownloaderException {
        String protocol = uri.getScheme();
        if (!downloaderMap.containsKey(protocol)) {
            throw new DownloaderException("Unknown protocol: " + protocol);
        }

        downloaderMap.get(protocol).download(uri, output);
    }
}
