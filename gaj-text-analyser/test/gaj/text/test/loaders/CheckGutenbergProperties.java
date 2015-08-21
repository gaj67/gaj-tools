package gaj.text.test.loaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
            String line = null;
            int wordCounter = 0;
            int tagCounter = 0;
            int maxDefCounter = 0;
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
                int defCounter = 0;
                while (true) {
                    line = in.readLine();
                    if (line == null || line.contains("<h1>")) {
                        if (defCounter > maxDefCounter) {
                            maxDefCounter = defCounter;
                        }
                        if (defCounter <= 0) {
                            System.out.println("Found a word without any definitions!");
                        }
                        continue wordloop;
                    }
                    if (line.contains("<hw>")) {
                        defCounter++;
                        tagCounter += countTags(line);
                    }
                }
            }
            System.out.printf("#words=%d, #tags=%d, max#defs/word=%d%n", wordCounter, tagCounter, maxDefCounter);
        }

    }

    private static int countTags(String line) {
        int tagCounter = 0;
        int idx = -1;
        while (true) {
            idx = (idx < 0) ? line.indexOf("<tt>") : line.indexOf("<tt>", idx);
            if (idx < 0) {
                break;
            }
            idx += 4;
            tagCounter++;
        }
        return tagCounter;
    }

}