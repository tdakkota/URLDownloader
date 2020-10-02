package ru.ncedu.tdakkota.urldownloader.html;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LinkCollectorTest {

    @Test
    void collect() throws URISyntaxException, IOException {
        URI uri = new URI("http://example.org/base");
        InputStream inputStream = getClass().getClassLoader().getResource("link/test.html").openStream();
        LinkCollector collector = new LinkCollector(inputStream, uri, "example_folder");

        RewrittenDocument rewrittenDocument = collector.rewrite(Charset.defaultCharset());

        HashMap<URI, String> expectedLinks = new HashMap<>();
        expectedLinks.put(
                new URI("http://example.org/base/link/link.css"),
                Paths.get("example_folder", "example.org", "base", "link", "link.css").toString()
        );
        expectedLinks.put(
                new URI("http://example.org/base/script/script.js"),
                Paths.get("example_folder", "example.org", "base", "script", "script.js").toString()
        );
        expectedLinks.put(
                new URI("http://example.org/base/img/img.jpg"),
                Paths.get("example_folder", "example.org", "base", "img", "img.jpg").toString()
        );
        assertEquals(expectedLinks, rewrittenDocument.getLinks());

        assertEquals(
                "./example_folder/example.org/base/link/link.css",
                rewrittenDocument.getDoc().selectFirst("link").attr("href")
        );
    }
}