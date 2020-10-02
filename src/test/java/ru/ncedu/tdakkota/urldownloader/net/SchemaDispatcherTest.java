package ru.ncedu.tdakkota.urldownloader.net;

import org.junit.jupiter.api.Test;
import ru.ncedu.tdakkota.urldownloader.output.Output;

import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SchemaDispatcherTest {
    class MockOutput implements Output {
        String contentType;
        Charset charset = Charset.defaultCharset();

        @Override
        public OutputStream getOutputStream() {
            return null;
        }

        @Override
        public void setCharset(Charset charset) {
            this.charset = charset;
        }

        @Override
        public Charset getCharset() {
            return charset;
        }

        @Override
        public void setContentType(String type) {
            contentType = type;
        }

        @Override
        public String getContentType() {
            return contentType;
        }
    }

    class MockDownloader implements Downloader {
        URI lastURI;
        Output lastOutput;

        @Override
        public void download(URI uri, Output output) throws DownloaderException {
            lastURI = uri;
            lastOutput = output;
        }
    }

    @Test
    void successfulDispatch() throws URISyntaxException, DownloaderException {
        SchemaDispatcher d = new SchemaDispatcher();
        MockDownloader downloader = new MockDownloader();
        MockOutput output = new MockOutput();

        d.register("mock", downloader);
        URI uri = new URI("mock://foobar");
        d.download(uri, output);

        assertEquals(uri, downloader.lastURI);
        assertEquals(output, downloader.lastOutput);
    }


    @Test
    void unsuccessfulDispatch() throws URISyntaxException {
        SchemaDispatcher d = new SchemaDispatcher();
        MockOutput output = new MockOutput();

        URI uri = new URI("brock://foobar");
        assertThrows(
                DownloaderException.class,
                () -> d.download(uri, output)
        );
    }
}