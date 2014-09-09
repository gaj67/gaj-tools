package gaj.afl.data.finalsiren;

import gaj.afl.data.core.Fixture;
import gaj.afl.data.core.Location;
import gaj.afl.data.core.Match;
import gaj.afl.data.core.Outcome;
import gaj.afl.data.core.Round;
import gaj.afl.data.core.Score;
import gaj.afl.data.core.Scores;
import gaj.afl.data.core.ShortMonth;
import gaj.afl.data.core.Team;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Scrapes match data from an input stream with Final Siren HTML format.
 */
/*package-private*/ class MatchDataParser {

    /**
     * Scrapes match data from a single HTML source representing
     * an entire season for a single team for a given year.
     *
     * @param source - The HTML ipnut stream.
     * @return A list of match results. This list is empty if no
     * results could be found.
     * @throws UncheckedIOException If an IO error occurs.
     */
    public List<Match> parse(InputStream source) throws UncheckedIOException {
	BufferedReader f = new BufferedReader(new InputStreamReader(source));
	int year = -1;
	StringBuilder buffer = new StringBuilder();
	boolean inTable = false;
	String line = null;
	while (true) {
	    if (line == null) {
		try {
		    line = f.readLine();
		} catch (IOException e) {
		    throw new UncheckedIOException(e.getMessage(), e);
		}
	    }
	    if (line == null) {
		return Collections.emptyList();
	    }
	    if (inTable) {
		int end = line.indexOf("</table");
		if (end < 0) {
		    buffer.append(line);
		    line = null;
		} else {
		    buffer.append(line.substring(0, end));
		    line = line.substring(end);
		    List<Match> res = processTable(year, buffer.toString());
		    if (res != null) {
			return res;
		    }
		    buffer.setLength(0);
		    inTable = false;
		}
	    } else {
		{
		    int idx = line.indexOf(" Fixture</h2>");
		    if (idx >= 0) {
			year = Integer.parseInt(line.substring(idx-4, idx));
		    }
		}
		int start = line.indexOf("<table");
		if (start < 0) { line = null; continue; }
		int end = line.indexOf("</table", start);
		if (end < 0) {
		    buffer.append(line.substring(start));
		    line = null;
		    inTable = true;
		} else {
		    String table = line.substring(start, end);
		    line = line.substring(end);
		    List<Match> res = processTable(year, table);
		    if (res != null) {
			return res;
		    }
		}
	    }
	}
    }

    private static List<Match> processTable(int year, String table) {
	int idx = table.indexOf("class=");
	if (idx < 0) {
	    return null;
	}
	//		if (!table.startsWith("fixturesmall", idx+7)) return null;
	List<Match> res = new LinkedList<>();
	int rowCount = -1;
	while (true) {
	    rowCount++;
	    int start = table.indexOf("<tr");
	    if (start < 0) {
		break;
	    }
	    int end = table.indexOf("</tr", start);
	    if (end < 0) {
		break;
	    }
	    String row = table.substring(start, end) ;
	    table = table.substring(end+4);
	    if (rowCount == 0)
	    {
		continue; // Skip header.
	    }
	    Match rec = processRow(year, row);
	    if (rec == null) {
		break;
	    }
	    res.add(rec);
	}
	return res;
    }

    private static Match processRow(int year, String row) {
	List<String> data = new ArrayList<String>();
	int start = 0;
	while (true) {
	    start = row.indexOf("<td", start);
	    if (start < 0) {
		break;
	    }
	    start = row.indexOf('>', start);
	    if (start < 0) {
		break;
	    }
	    int end = row.indexOf("</td", start);
	    if (end < 0) {
		break;
	    }
	    data.add(row.substring(start+1, end));
	    start = end;
	}
	Round round = parseRound(data.get(0));
	Outcome result = parseOutcome(stripTags(data.get(7)));
	if (result == null) {
	    return null;
	}
	Location location = parseLocation(stripTags(data.get(14)));
	Team homeTeam = parseTeam(stripTags(data.get(1)));
	Score homeQ1 = parseScore(data.get(2).trim());
	Score homeQ2 = parseScore(data.get(3).trim());
	Score homeQ3 = parseScore(data.get(4).trim());
	Score homeQ4 = parseScore(data.get(5).trim());
	Scores homeScores = new ScoresImpl(homeQ1, homeQ2, homeQ3, homeQ4);
	Team awayTeam = parseTeam(stripTags(data.get(8)));
	Score awayQ1 = parseScore(data.get(9).trim());
	Score awayQ2 = parseScore(data.get(10).trim());
	Score awayQ3 = parseScore(data.get(11).trim());
	Score awayQ4 = parseScore(data.get(12).trim());
	Scores awayScores = new ScoresImpl(awayQ1, awayQ2, awayQ3, awayQ4);
	Date datetime = parseDateTime(year, data.get(15));
	Fixture fixture = new FixtureImpl(round, datetime, location, homeTeam, awayTeam);
	return new MatchImpl(fixture, homeScores, awayScores, result);
    }

    private static Round parseRound(String round) {
	return Round.fromExternal(round);
    }

    private static Outcome parseOutcome(String outcome) {
	switch (outcome) {
	    case "dftd":
		return Outcome.Win;
	    case "lost to":
		return Outcome.Loss;
	    case "drew":
		return Outcome.Draw;
	    case "v":
		return null;
	    default:
		throw new IllegalArgumentException("Unknown outcome: " + outcome);
	}
    }

    private static Location parseLocation(String location) {
	return Location.fromExternal(location);
    }

    private static Team parseTeam(String team) {
	return Team.fromExternal(team);
    }

    // Date format: "month day (dayOfWeek time)".
    // Time format: hh:mm?M
    private static Date parseDateTime(int year, String date) {
	Calendar cal = new GregorianCalendar();
	cal.set(Calendar.YEAR, year);
	String[] parts = date.split(" ");
	cal.set(Calendar.MONTH, ShortMonth.valueOf(parts[0]).ordinal());
	cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf(parts[1]));
	//String dayOfWeek = Day.valueOf(parts[2].substring(1));
	cal.set(Calendar.SECOND, 0);
	if (parts.length < 4) {
	    // No time of day specified - set to noon!
	    cal.set(Calendar.HOUR_OF_DAY, 12);
	    cal.set(Calendar.MINUTE, 0);
	} else {
	    String time = parts[3].substring(0, parts[3].length()-1);
	    int idx = time.indexOf(':');
	    int hour = Integer.parseInt(time.substring(0, idx));
	    int minute = Integer.parseInt(time.substring(idx+1, time.length()-2));
	    if (time.toLowerCase().endsWith("pm")) {
		hour += 12;
	    }
	    cal.set(Calendar.HOUR_OF_DAY, hour);
	    cal.set(Calendar.MINUTE, minute);
	}
	return cal.getTime();
    }

    private static Score parseScore(String score) {
	String[] parts = score.split("[.]");
	return new MutableScoreImpl(Integer.valueOf(parts[0]), Integer.valueOf(parts[1]));
    }

    private static String stripTags(String data) {
	while (true) {
	    int start = data.indexOf('<');
	    if (start < 0) {
		return data;
	    }
	    int end = data.indexOf('>', start);
	    if (end < 0) {
		return data;
	    }
	    if (start == 0) {
		data = data.substring(end+1);
	    } else {
		data = data.substring(0, start);
	    }
	}
    }

}
