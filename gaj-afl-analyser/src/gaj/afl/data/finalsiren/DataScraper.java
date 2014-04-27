package gaj.afl.data.finalsiren;

import gaj.afl.data.match.Match;
import gaj.afl.data.match.MatchFetcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Scrapes match data from FinalSiren-format HTML files.
 */
public class DataScraper implements MatchFetcher {

	private static final File PATH_TO_DATA = new File("data/finalsiren/match");

	/**
	 * Obtains all historical match records.
	 * 
	 * @return A collection of match records.
	 * @throws UncheckedIOException If any IO problem occurs.
	 * @throws IllegalParseException If the format of any input is invalid.
	 */
	@Override
	public Collection<Match> getMatches() throws UncheckedIOException, IllegalParseException {
		List<Match> records = new ArrayList<>();
		DataParser parser = new DataParser();
		for (String subdir : PATH_TO_DATA.list()) {
			try {
				int year = Integer.parseInt(subdir);
				records.addAll(scrape(parser, year));
			} catch (NumberFormatException e) {
				// Ignore.
			}
		}
		return records;
	}

	/**
	 * Obtains the historical match records for the given years.
	 * 
	 * @param years - A optional array of years. If specified, the match records
	 * returned are restricted to these years; 
	 * otherwise no records are returned.
	 * @return A collection of match records, which will contain
	 * duplications if the same year is repeated.
	 * @throws UncheckedIOException If any IO problem occurs.
	 * @throws IllegalParseException If the format of any input is invalid.
	 */
	@Override
	public Collection<Match> getMatchesByYear(int... years) throws UncheckedIOException, IllegalParseException {
		List<Match> records = new ArrayList<>();
		DataParser parser = new DataParser();
		for (int year : years)
			records.addAll(scrape(parser, year));
		return records;
	}

	/*package-private*/ static Collection<Match> scrape(DataParser parser, int year) throws UncheckedIOException, IllegalParseException {
		Map<String, Match> records = new HashMap<>();
		File dir = new File(PATH_TO_DATA, Integer.toString(year));
		for (File file : dir.listFiles()) {
			try (InputStream source = new FileInputStream(file.getAbsoluteFile())) {
				for (Match record : parser.parse(source)) {
					String key = record.getFixture().toString();
					key = Integer.toString(key.hashCode());
					records.put(key, record);
				}
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			} catch (RuntimeException e) {
				throw new IllegalParseException("Failed to parse file: " + file, e);
			}
		}
		return records.values();
	}

	public static void main(String[] args) throws IOException {
		MatchFetcher scraper = new DataScraper();
		int i = 0;
		for (Match rec : scraper.getMatches())
			System.out.printf("Record %d: %s%n", ++i, rec);
	}

}
