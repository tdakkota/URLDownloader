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

    private void rewriteByQuery(RewrittenDocument doc, String query, String attrName) throws URISyntaxException {
        for (Element e : doc.getDoc().select(query)) {
            Attributes attrs = e.attributes();
            if (attrs.hasKey(attrName)) {
                URI uri = new URI(attrs.get(attrName));

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

    public RewrittenDocument rewrite(Charset charset) throws URISyntaxException, IOException {
        Document doc = Jsoup.parse(this.input, charset.name(), this.base.toString());
        RewrittenDocument newDoc = new RewrittenDocument(doc);
        rewriteByQuery(newDoc, "link", "href");
        rewriteByQuery(newDoc, "img", "src");
        rewriteByQuery(newDoc, "script", "src");
        return newDoc;
    }
}
