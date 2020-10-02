package ru.ncedu.tdakkota.urldownloader.html;

import org.jsoup.nodes.Document;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class RewrittenDocument {
    private Document doc;
    private Map<URI, String> links;

    public RewrittenDocument(Document doc) {
        this.doc = doc;
        this.links = new HashMap<>();
    }

    public Document getDoc() {
        return doc;
    }

    public Map<URI, String> getLinks() {
        return links;
    }
}
