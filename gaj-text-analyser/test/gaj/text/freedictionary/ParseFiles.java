package gaj.text.freedictionary;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.ccil.cowan.tagsoup.jaxp.SAXParserImpl;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ParseFiles {

    public static void main(String args[]) {
        if (args.length == 0) {
            System.out.println("Arguments: <path-to-definition(s)>+");
        } else {
            for (String arg : args) {
                File path = new File(arg);
                if (path.isFile()) {
                    analyseFile(path);
                } //TODO analyseDirectory(path);
            }
        }
    }

    private static void analyseFile(File path) {
        try (InputStream is = new FileInputStream(path)) {
            SAXParserImpl parser = SAXParserImpl.newInstance(null);
            DefaultHandler handler = new SectionsHandler();
            parser.parse(is, handler);
        } catch (IOException | SAXException e) {
            System.out.printf("Failed to analyse: " + path + " because: " + e.getMessage());
        }

    }

}