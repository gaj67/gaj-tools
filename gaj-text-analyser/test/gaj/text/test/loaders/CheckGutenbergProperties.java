package gaj.text.test.loaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class CheckGutenbergProperties {

    private static final String START_OF_WORD = "<h1>";
    private static final String END_OF_WORD = "</h1>";
    private static final String START_OF_TAG = "<tag>";
    private static final String END_OF_TAG = "</tag>";

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Arguments: <path-to-dictionary>");
        } else {
            read(args[0]);
        }
    }

    private static void read(String path) throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(path))) {
            Set<String> words = new HashSet<>();
            Set<String> taglessWords = new HashSet<>();
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
                String word = getWord(line);
                if (word == null) {
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
                        if (tags.isEmpty()) {
                        	taglessWords.add(word);
                        } else {
                            words.add(word);
                        }
                        continue wordloop;
                    }
                    if (line.contains("<hw>")) {
                        tags.addAll(getTags(line));
                    }
                }
            }
            System.out.printf("#words=%d, #tags=%d%n", wordCounter, tagCounter);
            System.out.printf("#tag-types=%d%ntag-types=%s%n", tagTypes.size(), tagTypes);
            System.out.printf("tag-freqs=%s%n", tagCounts);
            for (String word : taglessWords) {
            	if (!words.contains(word)) {
            		System.out.printf("No tags for: %s%n", word);
            	}
            }
        }

    }

	private static boolean isNoGood(Set<String> words, String word,
			List<String> tags) 
	{
		return tags.isEmpty() && !words.contains(word)
				&& !word.startsWith("-") && !word.endsWith("-")
				&& !word.contains(" ");
	}

    private static String getWord(String line) {
        int sidx = line.indexOf(START_OF_WORD);
        if (sidx < 0) {
            return null;
        }
        sidx += START_OF_WORD.length();
        int eidx = line.indexOf(END_OF_WORD);
        if (eidx < 0) {
            throw new IllegalStateException("Missing end tag for word definition on line: " + line);
        }
        return line.substring(sidx, eidx);
    }

    private static List<String> getTags(String line) {
        List<String> tags = new ArrayList<>();
        int sidx = -1;
        while (true) {
            sidx = (sidx < 0) ? line.indexOf(START_OF_TAG) : line.indexOf(START_OF_TAG, sidx);
            if (sidx < 0) {
                break;
            }
            sidx += START_OF_TAG.length();
            int eidx = line.indexOf(END_OF_TAG, sidx);
            if (eidx < 0) {
                throw new IllegalStateException("Missing tag end on line: " + line);
            }
            String tag = line.substring(sidx, eidx);
            if (tag.contains("<") || tag.contains(">")) {
                throw new IllegalStateException("Embedded tag within tag: " + tag + " on line: " + line);
            }
            tags.add(tag);
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
        while (tagFreqs.size() <= tagCount) {
            tagFreqs.add(new AtomicInteger());
        }
        tagFreqs.get(tagCount).incrementAndGet();
    }

}
