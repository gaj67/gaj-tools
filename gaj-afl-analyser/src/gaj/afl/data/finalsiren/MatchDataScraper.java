package gaj.afl.data.finalsiren;

import gaj.afl.data.core.Match;
import gaj.afl.data.core.Round;
import gaj.afl.data.core.Team;
import gaj.afl.data.store.MatchFetcher;
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
/* package-private */class MatchDataScraper implements MatchFetcher {

    /**
     * @throws UncheckedIOException If any IO problem occurs.
     * @throws IllegalParseException If the format of any input is invalid.
     */
    private Collection<Match> _getAllMatches() throws UncheckedIOException, IllegalParseException {
        List<Match> records = new ArrayList<>();
        MatchDataParser parser = new MatchDataParser();
        for (String subdir : MatchDataIdentifiers.getMatchDataPath().list()) {
            try {
                int year = Integer.parseInt(subdir);
                records.addAll(parseMatchFiles(parser, year));
            } catch (NumberFormatException e) {
                // Ignore.
            }
        }
        return records;
    }

    /**
     * @throws UncheckedIOException If any IO problem occurs.
     * @throws IllegalParseException If the format of any input is invalid.
     */
    @Override
    public Collection<Match> getMatches(int... years) throws UncheckedIOException, IllegalParseException {
        if (years.length == 0) {
            return _getAllMatches();
        }
        List<Match> records = new ArrayList<>();
        MatchDataParser parser = new MatchDataParser();
        for (int year : years) {
            records.addAll(parseMatchFiles(parser, year));
        }
        return records;
    }

    /* package-private */static Collection<Match> parseMatchFiles(MatchDataParser parser, int year) throws UncheckedIOException, IllegalParseException {
        Map<String, Match> records = new HashMap<>();
        File dir = MatchDataIdentifiers.getMatchDataPath(year);
        for (File file : dir.listFiles()) {
            parseMatchFile(parser, records, file);
        }
        return records.values();
    }

    private static void parseMatchFile(MatchDataParser parser, Map<String, Match> records, File file) {
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

    @Override
    public Collection<Match> getMatches(Team team, int... years) {
        if (years.length == 0) {
            return _getAllMatches(team);
        }
        MatchDataParser parser = new MatchDataParser();
        Map<String, Match> records = new HashMap<>();
        for (int year : years) {
            File file = MatchDataIdentifiers.getMatchDataPath(team, year);
            parseMatchFile(parser, records, file);
        }
        return records.values();
    }

    private Collection<Match> _getAllMatches(Team team) {
        Map<String, Match> records = new HashMap<>();
        MatchDataParser parser = new MatchDataParser();
        for (String subdir : MatchDataIdentifiers.getMatchDataPath().list()) {
            try {
                int year = Integer.parseInt(subdir);
                File file = MatchDataIdentifiers.getMatchDataPath(team, year);
                parseMatchFile(parser, records, file);
            } catch (NumberFormatException e) {
                // Ignore.
            }
        }
        return records.values();
    }

    /**
     * @throws UncheckedIOException If any IO problem occurs.
     * @throws IllegalParseException If the format of any input is invalid.
     */
    @Override
    public/* @Nullable */Match getMatch(Team team, int year, Round round) {
        for (Match match : getMatches(team, year)) {
            if (match.getFixture().getRound() == round) {
                return match;
            }
        }
        return null;
    }

}
