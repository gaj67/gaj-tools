package gaj.afl.data.finalsiren;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;

/**
 * Downloads HTML match files from Final Siren,
 * and saves them in a hierarchical directory structure.
 */
/*package-private*/ class MatchDataDownloader {

    private static final File PATH_TO_DATA = new File("data/finalsiren/match/");

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
	int team = 0;
	while (++team < 25) {
	    String filename = year + "-Team" + team + ".htm";
	    File file = new File(subdir, filename);
	    if (file.exists()) {
		System.out.printf("Warning: Skipping file: %s%n", filename);
		continue;
	    }
	    System.out.printf("Downloading file: %s%n", filename);
	    URL url;
	    try {
		url = new URL("http://finalsiren.com/Fixture.asp?TeamID="+team+"&SeasonID="+year+"&Go=Go");
	    } catch (MalformedURLException e1) {
		throw new IllegalStateException(e1);
	    }
	    try (InputStream source = url.openStream()) {
		Files.copy(source, file.toPath());
	    } catch (IOException e) {
		System.out.printf("Warning: Skipping failed download: %s%n", filename);
	    }
	}
    }
}
