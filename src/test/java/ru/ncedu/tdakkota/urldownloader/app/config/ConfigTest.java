package ru.ncedu.tdakkota.urldownloader.app.config;

import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class ConfigTest {

    @Test
    void parse() throws MalformedURLException, ConfigException {
        Config c = new Config(new String[]{
                "-r", "http://example.org", "file.txt",
        });
        c.parse();

        assertEquals("http://example.org", c.getURL().toString());
        assertEquals(Paths.get("file.txt").toString(), c.getOutputFile());
        assertEquals(Paths.get("").toAbsolutePath(), c.getOutputDirectory());
        assertTrue(c.isRunAfter());
    }
}