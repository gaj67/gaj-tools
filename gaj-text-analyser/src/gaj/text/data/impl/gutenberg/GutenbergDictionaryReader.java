package gaj.text.data.impl.gutenberg;

import gaj.iterators.core.ResourceIterator;
import gaj.text.data.Definition;
import gaj.text.data.DefinitionProducer;
import gaj.text.data.Tag;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class GutenbergDictionaryReader extends ResourceIterator<Definition<Tag>> implements DefinitionProducer<Tag> {

    private final File path;
    private BufferedReader reader;

    public GutenbergDictionaryReader(File path) {
        this.path = path;
    }

    @Override
    public /*@Nullable*/ Definition<Tag> produce() {
        if (!hasNext()) {
            return null;
        }
        try {
            return next();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    protected Iterator<? extends Definition<Tag>> openResource() throws IOException {
        reader = new BufferedReader(new FileReader(path));
        return new GutenbergDefinitionIterator(reader);
    }

    @Override
    protected void closeResource() throws IOException {
        reader.close();
    }

}
