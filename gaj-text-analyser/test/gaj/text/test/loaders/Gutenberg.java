package gaj.text.test.loaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

public class Gutenberg {

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
                        if (!tags.isEmpty()) {
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
            System.out.printf("#tag-types=%d%n", tagTypes.size());
            System.out.printf("tag-types=%n");
            reportTagTypes(tagTypes);
        }
    }

    private static void reportTagTypes(Map<String, AtomicInteger> tagTypes) {
        Map<String, List<String>> subTypes = new HashMap<>();
        for (Entry<String, AtomicInteger> entry : tagTypes.entrySet()) {
            String key = entry.getKey();
            int count = entry.getValue().get();
            String value = "" + count;
            int idx = key.indexOf('(');
            if (idx >= 0) {
                value += "x" + key.substring(idx);
                key = key.substring(0, idx);
            }
            List<String> values = subTypes.get(key);
            if (values == null) {
                values = new ArrayList<>();
                subTypes.put(key, values);
            }
            values.add(value);
        }
        List<String> keys = new ArrayList<>(subTypes.keySet());
        Collections.sort(keys);
        for (String key : keys) {
            List<String> values = subTypes.get(key);
            System.out.printf("* %s: %d%n", key, values.size());
            for (String value : values) {
                System.out.printf("  + %s%n", value);
            }
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
        int lastIdx = line.length();
        int wfIdx = line.indexOf("<wordforms>");
        if (wfIdx >= 0) lastIdx = wfIdx;
        int etyIdx = line.indexOf("<ety>");
        if (etyIdx >= 0) lastIdx = Math.min(lastIdx, etyIdx);
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
