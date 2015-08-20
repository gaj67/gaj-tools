package gaj.text.data.impl;

import gaj.text.data.Definition;
import gaj.text.data.DictionaryProducer;
import gaj.text.data.Tag;
import java.io.File;

/**
 * Implements a file-based reader of The Project Gutenberg Etext of Webster's
 * Unabridged Dictionary, 1913 edition.
 */
public class GutenbergDictionaryReader implements DictionaryProducer<Tag> {

    private final File path;

    public GutenbergDictionaryReader(File path) {
        this.path = path;
    }

    @Override
    public /*@Nullable*/ Definition<Tag> produce() {
        return null;
    }

}
