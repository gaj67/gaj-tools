package gaj.afl.data.finalsiren;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;

/**
 * Downloads HTML match files from Final Siren,
 * and saves them in a hierarchical directory structure.
 */
/* package-private */class MatchDataDownloader {

    private static final String BASE_URL_STRING = "http://finalsiren.com/Fixture.asp";

    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Need to specify one or more years!");
        }
        for (String arg : args) {
            int year = Integer.parseInt(arg);
            downloadData(year);
        }
    }

    private static void downloadData(int year) {
        int teamId = 0;
        while (++teamId < 25) {
            File file = MatchDataIdentifiers.getMatchDataPath(teamId, year);
            if (!file.getParentFile().exists()) {
                if (!file.getParentFile().mkdirs()) {
                    throw new IllegalStateException("Could not create path: " + file.getParentFile());
                }
            }
            if (file.exists()) {
                System.out.printf("Warning: Skipping file: %s%n", file);
                continue;
            }
            System.out.printf("Downloading file: %s%n", file);
            try {
                URL url = new URL(BASE_URL_STRING + "?TeamID=" + teamId + "&SeasonID=" + year + "&Go=Go");
                URLConnection connection = url.openConnection();
                ((HttpURLConnection) connection).addRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:12.0) Gecko/20100101 Firefox/21.0");
                connection.connect();
                try (InputStream source = connection.getInputStream()) {
                    Files.copy(source, file.toPath());
                } catch (IOException e) {
                    System.out.printf("Warning: Skipping failed download: %s%n", file);
                }
            } catch (IOException e1) {
                throw new IllegalStateException(e1);
            }
        }
    }
}
