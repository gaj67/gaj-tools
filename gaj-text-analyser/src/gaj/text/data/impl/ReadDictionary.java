package gaj.text.data.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads the Gutenberg version of the 1913 Webster's dictionary.
 */
public class ReadDictionary {

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Arguments: <path-to-dictionary> <path-to-output>");
        } else {
            read(args[0], args[1]);
        }
    }

    private static void read(String inPath, String outPath) throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(inPath));
             BufferedWriter out = new BufferedWriter(new FileWriter(outPath)))
        {
            String line;
            while ((line = in.readLine()) != null) {
                String word = getWord(line);
                if (word == null || word.trim().isEmpty()) continue;
                while ((line = in.readLine()) != null) {
                    List<String> posTags = getPOSTags(line);
                    if (posTags == null || posTags.isEmpty()) continue;
                    write(out, word, posTags);
                }
            }
        }
    }

    private static /*@Nullable*/ String getWord(String line) {
        int startDef = line.indexOf("<h1>");
        if (startDef < 0) return null;
        startDef += 4;
        int endDef = line.indexOf("</h1>", startDef);
        return (endDef < 0) ? null : line.substring(startDef, endDef);
    }

    private static List/*@Nullable*/<String> getPOSTags(String line) {
        int startDef = line.indexOf("<tt>");
        if (startDef < 0) return null;
        List<String> posTags = new ArrayList<>();
        while (startDef >= 0 && startDef < line.length()) {
            
        }
        return posTags;
    }

    private static void write(BufferedWriter out, String word, List<String> posTags) {
        // TODO Auto-generated method stub
        
    }

}
