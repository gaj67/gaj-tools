package gaj.text.data.impl.gutenberg;

import gaj.text.data.Definition;
import gaj.text.data.Tag;
import java.util.List;

/*package-private*/ class MultiDefinition<T extends Tag> {

    private final List<T> tags;
    private final String[] words;
    private int index = 0;

    /*package-private*/ MultiDefinition(List<T> tags, String... words) {
        this.tags = tags;
        this.words = words;
    }

    /*package-private*/ /*@Nullable*/ Definition<T> getNextDefinition() {
        if (!hasNextDefinition()) return null;
        final String word = words[index++];
        return new Definition<T>() {
            @Override
            public String getText() {
                return word;
            }

            @Override
            public List<T> getTags() {
                return tags;
            }
        };
    }

    public boolean hasNextDefinition() {
        return index < words.length;
    }

}
