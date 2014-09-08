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
/*package-private*/ class LadderDataDownloader {

    private static final File PATH_TO_DATA = new File("data/finalsiren/ladder/");
    private static final String BASE_URL_STRING = "http://finalsiren.com/AFLLadder.asp";

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
	File subdir = new File(PATH_TO_DATA, Integer.toString(year));
	if (!subdir.exists()) {
	    if (!subdir.mkdirs()) {
		throw new IllegalStateException("Could not create path: " + subdir);
	    }
	}
	for (int round = 1; round < 24; round++) {
	    String filename = year + "-Round" + round + ".htm";
	    File file = new File(subdir, filename);
	    if (file.exists()) {
		System.out.printf("Warning: Skipping file: %s%n", filename);
		continue;
	    }
	    System.out.printf("Downloading file: %s%n", filename);
	    try {
		URL url = new URL(BASE_URL_STRING + "?AFLLadderTypeID=2&SeasonID=" + year + "&Round=" + round + "-1");
		URLConnection connection = url.openConnection();
		((HttpURLConnection) connection).addRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:12.0) Gecko/20100101 Firefox/21.0");
		connection.connect();
		try (InputStream source = connection.getInputStream()) {
		    Files.copy(source, file.toPath());
		} catch (IOException e) {
		    System.out.printf("Warning: Skipping failed download: %s%n", filename);
		}
	    } catch (IOException e1) {
		throw new IllegalStateException(e1);
	    }
	}
    }

}
