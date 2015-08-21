package gaj.text.data;

import gaj.iterators.core.Producer;

/**
 * Provides a means of producing multiple dictionary definitions.
 */
public interface DefinitionProducer<T extends Tag> extends Producer<Definition<T>> {
}
