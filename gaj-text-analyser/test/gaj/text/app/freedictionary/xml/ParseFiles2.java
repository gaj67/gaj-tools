package gaj.text.app.freedictionary.xml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.ccil.cowan.tagsoup.jaxp.SAXParserImpl;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ParseFiles2 {

    private static final Consumer<UnstructuredData> consumer =
    		new Consumer<UnstructuredData>() {
				@Override
				public void accept(UnstructuredData data) {
					System.out.printf("sectionData=%s%n", data.getData());	
				}
    };

    public static void main(String args[]) {
        if (args.length == 0) {
            System.out.println("Arguments: <path-to-definition(s)>+");
        } else {
            for (String path : args) {
                analysePath(Paths.get(path));
            }
        }
    }

    private static void analysePath(final Path path) {
    	try (Stream<Path> files = Files.walk(path).filter(p -> Files.isRegularFile(p))) {
    		files.forEach(ParseFiles2::analyseFile);
    	} catch (IOException e) {
			e.printStackTrace();
		}
    }

    private static void analyseFile(Path file) {
    	System.out.printf("DEBUG: Analysing %s...%n", file);
        try (InputStream is = Files.newInputStream(file)) {
            SAXParserImpl parser = SAXParserImpl.newInstance(null);
            DefaultHandler handler = new SectionsHandler(consumer);
            parser.parse(is, handler);
        } catch (IOException | SAXException e) {
            System.out.printf("Failed analysis of " + file + " because: " + e.getMessage());
        }
    }

}