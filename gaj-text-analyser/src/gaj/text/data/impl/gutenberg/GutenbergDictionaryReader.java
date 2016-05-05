package gaj.text.data.impl.gutenberg;

import gaj.iterators.impl.BaseResourceIterator;
import gaj.iterators.impl.Iterators;
import gaj.text.data.Definition;
import gaj.text.data.DefinitionProducer;
import gaj.text.data.Tag;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class GutenbergDictionaryReader extends BaseResourceIterator<Definition<Tag>> implements DefinitionProducer<Tag> {

    private final File path;
    private BufferedReader reader;

    public GutenbergDictionaryReader(File path) {
        this.path = path;
    }

    @Override
    public Definition<Tag> get() {
        if (!hasNext()) {
            throw new NoSuchElementException("End of definitions");
        }
        return next();
    }

    @Override
	public Iterator<? extends Definition<Tag>> openResource() throws IOException {
        reader = new BufferedReader(new FileReader(path));
        return Iterators.newIterator(new GutenbergDefinitionProducer(reader));
    }

    @Override
	public void closeResource() throws IOException {
        reader.close();
    }

    @Override
    public Definition<Tag> halt(String message) {
    	return super.halt(message);
    }

}
