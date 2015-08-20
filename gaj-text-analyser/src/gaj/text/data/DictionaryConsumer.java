package gaj.text.data;

/**
 * Provides a means of consuming multiple dictionary definitions.
 */
public interface DictionaryConsumer<T extends Tag> extends Consumer<Definition<T>> {
}
