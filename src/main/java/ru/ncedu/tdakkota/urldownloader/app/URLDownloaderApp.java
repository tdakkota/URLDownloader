package ru.ncedu.tdakkota.urldownloader.app;

import org.apache.commons.io.FileUtils;
import ru.ncedu.tdakkota.urldownloader.app.config.Config;
import ru.ncedu.tdakkota.urldownloader.app.config.ConfigException;
import ru.ncedu.tdakkota.urldownloader.html.LinkCollector;
import ru.ncedu.tdakkota.urldownloader.html.LinkCollectorException;
import ru.ncedu.tdakkota.urldownloader.html.RewrittenDocument;
import ru.ncedu.tdakkota.urldownloader.net.Downloader;
import ru.ncedu.tdakkota.urldownloader.net.DownloaderException;
import ru.ncedu.tdakkota.urldownloader.net.SchemaDispatcher;
import ru.ncedu.tdakkota.urldownloader.output.FileOutput;
import ru.ncedu.tdakkota.urldownloader.output.FileOutputDialogException;
import ru.ncedu.tdakkota.urldownloader.runner.DesktopRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class URLDownloaderApp {
    private static void run(Config config) throws Exception {
        Downloader d = SchemaDispatcher.getDefault();
        FileOutput output = new FileOutput(config.getOutputFile());
        d.download(config.getURL().toURI(), output);
        output.close();

        if (output.getContentType().toLowerCase().contains("text/html")) {
            File file = output.getFile();
            String dirName = config.getURL().getHost() + "_files";

            RewrittenDocument doc = new LinkCollector(
                    new FileInputStream(file),
                    config.getURL().toURI(), dirName
            ).rewrite(output.getCharset());

            FileUtils.write(file, doc.getDoc().outerHtml(), output.getCharset());
            Map<URI, String> links = doc.getLinks();
            for (URI uri : links.keySet()) {
                d.download(uri, new FileOutput(links.get(uri)));
            }
        }

        if (config.isRunAfter())
            new DesktopRunner().run(output.getFile().getPath());
    }

    public static void main(String[] args) {
        Config config = new Config(args);
        try {
            // parse the command line arguments
            config.parse();
            if (!config.isHelp()) {
                // run app
                run(config);
            }
        } catch (ConfigException exp) {
            System.err.println("Flags parsing failed. Reason: " + exp.getMessage());
        } catch (MalformedURLException | URISyntaxException exp) {
            System.err.println("URL is invalid: " + exp.getMessage());
        } catch (LinkCollectorException exp) {
            System.err.println("Link collection failed: " + exp.getMessage());
        } catch (DownloaderException | FileOutputDialogException | IOException exp) {
            System.err.println("Download failed: " + exp.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
