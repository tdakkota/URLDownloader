package ru.ncedu.tdakkota.urldownloader.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.StringJoiner;

public class LinkCollector {
    private InputStream input;
    private URI base;
    private String outputDirName;

    public LinkCollector(InputStream input, URI base, String outputDirName) {
        this.input = input;
        this.base = base;
        this.outputDirName = outputDirName;
    }

    private URI parseURI(String s) throws LinkCollectorException {
        if (!s.contains("://")) {
            StringBuilder b = new StringBuilder();
            b.append(this.base.getScheme());
            b.append("://");
            b.append(this.base.getHost());
            if (this.base.getPort() != -1) {
                b.append(this.base.getPort());
            }
            if (!s.startsWith("/")) {
                b.append("/");
            }
            b.append(s);
            s = b.toString();
        }

        URI uri;
        try {
            uri = new URI(s);
        } catch (URISyntaxException ex) {
            throw new LinkCollectorException("Failed to parse link: " + ex.getMessage());
        }

        String host = uri.getHost();
        if (host == null || host.isEmpty()) {
            host = this.base.getHost();
        }

        String scheme = uri.getScheme();
        if (scheme == null || scheme.isEmpty()) {
            scheme = this.base.getScheme();
        }

        try {
            return new URI(scheme, uri.getUserInfo(), host, uri.getPort(), uri.getPath(), uri.getQuery(), uri.getFragment());
        } catch (URISyntaxException ex) {
            throw new LinkCollectorException("Failed to parse link: " + ex.getMessage());
        }
    }


    private void rewriteByQuery(RewrittenDocument doc, String query, String attrName) throws LinkCollectorException {
        for (Element e : doc.getDoc().select(query)) {
            Attributes attrs = e.attributes();
            if (attrs.hasKey(attrName)) {
                URI uri = parseURI(attrs.get(attrName));

                String localPath = Paths.get(
                        outputDirName,
                        uri.getHost(),
                        uri.getPath()
                ).toString();

                String path = uri.getPath();
                if (path.startsWith("/")) {
                    path = path.substring(1);
                }

                String linkPath = new StringJoiner("/").
                        add(".").
                        add(outputDirName).
                        add(uri.getHost()).
                        add(path).
                        toString();

                doc.getLinks().put(uri, localPath);
                attrs.put(attrName, linkPath);
            }
        }
    }

    public RewrittenDocument rewrite(Charset charset) throws LinkCollectorException {
        Document doc;
        try {
            doc = Jsoup.parse(this.input, charset.name(), this.base.toString());
        } catch (IOException e) {
            throw new LinkCollectorException("Failed to parse document: " + e.getMessage());
        }
        RewrittenDocument newDoc = new RewrittenDocument(doc);
        rewriteByQuery(newDoc, "link", "href");
        rewriteByQuery(newDoc, "img", "src");
        rewriteByQuery(newDoc, "script", "src");
        return newDoc;
    }
}
