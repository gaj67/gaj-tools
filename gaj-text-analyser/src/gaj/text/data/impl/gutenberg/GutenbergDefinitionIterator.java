package gaj.text.data.impl.gutenberg;

import gaj.iterators.core.ProducerIterator;
import gaj.text.data.Definition;
import gaj.text.data.Tag;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

/**
 * Parses the word tag definitions in The Project Gutenberg Etext of Webster's
 * Unabridged Dictionary, 1913 edition.
 */
/*package-private*/ class GutenbergDefinitionIterator extends ProducerIterator<Definition<Tag>> {

    private final BufferedReader reader;
    private MultiDefinition<Tag> multiDef = null;

    /*package-private*/ GutenbergDefinitionIterator(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public /*@Nullable*/ Definition<Tag> produce() {
        while (true) {
            if (multiDef == null) {
                multiDef = getNextMultiDefinition();
                if (multiDef == null) return null;
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
            throw failure(e);
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
