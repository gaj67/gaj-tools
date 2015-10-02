package gaj.text.test.loaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Gutenberg1 {

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
            Map<String,AtomicInteger> tagTypes = new HashMap<>();
            String line = null;
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
                List<String> tags = new ArrayList<>();
                while (true) {
                    line = in.readLine();
                    if (line == null || line.contains("<h1>")) {
                    	if (tags.size() == 1) {
                    		System.out.printf("%s -> %s%n", word, tags);
                    		countTagTypes(tagTypes, tags);
                    	}
                        continue wordloop;
                    }
                    if (line.contains("<hw>")) {
                        tags.addAll(getTags(line));
                    }
                }
            }
            System.out.printf("#tag-types=%d%ntag-types=%s%n", tagTypes.size(), tagTypes);
        }
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
        int lastIdx = line.indexOf("<wordforms>");
        if (lastIdx < 0)
            lastIdx = line.length();
        if (line.contains("<mark>[Obs.]</mark>") || line.contains("<mark>[Archaic]</mark>"))
            lastIdx = -1;
        while (sidx < lastIdx) {
            sidx = (sidx < 0) ? line.indexOf(START_OF_TAG) : line.indexOf(START_OF_TAG, sidx);
            if (sidx < 0 || sidx >= lastIdx) {
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

}
