package gaj.analysis.bayes.pmf.impl.integer;

import gaj.analysis.bayes.pmf.PMF;
import gaj.common.annotations.PackagePrivate;

/**
 * Assumes the empirical {@link PMF} has a finite domain, X.
 *
 * @param <T>
 *            - The type of all values x in X.
 */
@PackagePrivate class ArrayPMFImpl<T> extends BasePMFImpl<T> {

    private final T[] values;

    @PackagePrivate ArrayPMFImpl(T[] values) {
        super(values.length);
        this.values = values;
    }

    @Override
    protected int index(T value) {
        // Simple lookup; should sort a copy of labels and use binary search.
        for (int i = 0; i < values.length; i++) {
            if (values[i].equals(value)) return i;
        }
        return -1;
    }
 
}
