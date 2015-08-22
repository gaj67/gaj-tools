package gaj.text.test.loaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CheckGutenbergProperties {

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println("Arguments: <path-to-dictionary>");
		} else {
			read(args[0]);
		}
	}

	private static void read(String path) throws IOException {
		try (BufferedReader in = new BufferedReader(new FileReader(path))) {
			Map<String,AtomicInteger> tagTypes = new HashMap<>();
			List<AtomicInteger> tagCounts = new ArrayList<>();
			String line = null;
			int wordCounter = 0;
			int tagCounter = 0;
			wordloop: while (true) {
				if (line == null) {
					line = in.readLine();
				}
				if (line == null) {
					break;
				}
				if (!line.contains("<h1>")) {
					line = null;
					continue;
				}
				wordCounter++;
				if (wordCounter % 1_000 == 0) {
					System.out.printf("Processing word # %d...%n", wordCounter);
					System.out.flush();
				}
				List<String> tags = new ArrayList<>();
				while (true) {
					line = in.readLine();
					if (line == null || line.contains("<h1>")) {
						countTagTypes(tagTypes, tags);
						countTagFreqs(tagCounts, tags.size());
						tagCounter += tags.size();
						continue wordloop;
					}
					if (line.contains("<hw>")) {
						tags.addAll(getTags(line));
					}
				}
			}
			System.out.printf("#words=%d, #tags=%d%n", wordCounter, tagCounter);
		}

	}

	private static List<String> getTags(String line) {
		List<String> tags = new ArrayList<>();    	
		int sidx = -1;
		while (true) {
			sidx = (sidx < 0) ? line.indexOf("<tt>") : line.indexOf("<tt>", sidx);
			if (sidx < 0) {
				break;
			}
			sidx += 4;
			int eidx = line.indexOf("</tt>", sidx);
			if (eidx < 0) throw new IllegalStateException("Missing tag end on line: " + line);
			tags.add(line.substring(sidx, eidx));
		}
		return tags;
	}

	private static void countTagTypes(Map<String,AtomicInteger> tagTypes, List<String> tags) {
		for (String tag : tags) {
			AtomicInteger count = tagTypes.get(tag);
			if (count == null) {
				tagTypes.put(tag, new AtomicInteger(1));
			} else {
				count.incrementAndGet();
			}
		}
	}

	private static void countTagFreqs(List<AtomicInteger> tagFreqs, int tagCount) {
		while (tagFreqs.size() <= tagCount) tagFreqs.add(new AtomicInteger());
		tagFreqs.get(tagCount).incrementAndGet();
	}
	
}
