package gaj.text.data;

import gaj.iterators.core.Consumer;

/**
 * Provides a means of consuming multiple dictionary definitions.
 */
public interface DefinitionConsumer<T extends Tag> extends Consumer<Definition<T>> {
}
