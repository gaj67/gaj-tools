package gaj.text.freedictionary;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import org.ccil.cowan.tagsoup.jaxp.SAXParserImpl;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ParseFiles {

    public static void main(String args[]) {
        if (args.length == 0) {
            System.out.println("Arguments: <path-to-definition(s)>+");
        } else {
            for (String path : args) {
                analysePath(new File(path));
            }
        }
    }

    private static void analysePath(final File path) {
        LinkedList<File> paths = new LinkedList<>();
        paths.add(path);
        while (!paths.isEmpty()) {
            File aPath = paths.removeFirst();
            if (aPath.isFile()) {
                analyseFile(aPath);
            } else if (aPath.isDirectory()) {
                for (File anotherPath : aPath.listFiles()) {
                    paths.add(anotherPath);
                }
            } else {
                System.err.printf("Unknown path: %s%n", path);
            }
        }
    }

    private static void analyseFile(File file) {
        System.out.printf("Analysing %s ...%n", file);
        try (InputStream is = new FileInputStream(file)) {
            SAXParserImpl parser = SAXParserImpl.newInstance(null);
            DefaultHandler handler = new SectionsHandler();
            parser.parse(is, handler);
        } catch (IOException | SAXException e) {
            System.out.printf("Failed to analyse: " + file + " because: " + e.getMessage());
        }
    }

}