package gaj.text.data.impl.gutenberg;

import gaj.iterators.core.Producer;
import gaj.text.data.Definition;
import gaj.text.data.Tag;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Parses the word tag definitions in The Project Gutenberg Etext of Webster's
 * Unabridged Dictionary, 1913 edition.
 */
/*package-private*/ class GutenbergDefinitionProducer implements Producer<Definition<Tag>> {

    private final BufferedReader reader;
    private MultiDefinition<Tag> multiDef = null;

    /*package-private*/ GutenbergDefinitionProducer(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public Definition<Tag> get() {
        while (true) {
            if (multiDef == null) {
                multiDef = getNextMultiDefinition();
                if (multiDef == null) throw new NoSuchElementException("End of definitions");
            }
            if (!multiDef.hasNextDefinition()) {
                multiDef = null;
            } else {
                return multiDef.getNextDefinition();
            }
        }
    }

    private MultiDefinition<Tag> getNextMultiDefinition() {
        try {
            return parseNextDefinition();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private /*@Nullable*/ MultiDefinition<Tag> parseNextDefinition() throws IOException {
        while (true) {
            String line = reader.readLine();
            if (line == null) return null;
            String[] words = parseWordDefinition(line);
            if (words == null) continue;
            List<Tag> tags = parseWordTags();
            if (tags.isEmpty()) throw new IllegalStateException("Missing tags");
            return new MultiDefinition<>(tags, words);
        }
    }

    private String/*@Nullable*/[] parseWordDefinition(String line) {
        // XXX Can have multiple <h1>...</h1> on same line.
        int startDef = line.indexOf("<h1>");
        if (startDef < 0) return null;
        startDef += 4;
        int endDef = line.indexOf("</h1>", startDef);
        return (endDef < 0) ? null : line.substring(startDef, endDef).split(", ");
    }

    private List<Tag> parseWordTags() {
        // TODO Auto-generated method stub
        return null;
    }

}
