package gaj.analysis.bayes.pmf.impl;

import java.util.ArrayList;
import org.eclipse.jdt.annotation.Nullable;
import gaj.analysis.bayes.pmf.LabelledPMF;
import gaj.common.annotations.PackagePrivate;

@PackagePrivate class LabelledEmpiricalPMFImpl<T extends Comparable<?>> extends EmpiricalPMFImpl implements LabelledPMF<T> {

    private final T[] values;

    @SuppressWarnings("unchecked")
    @PackagePrivate LabelledEmpiricalPMFImpl(int startIndex, Iterable<T> values) {
        super(startIndex, getNumElements(values));
        ArrayList<T> theLabels = new ArrayList<T>();
        for (T value : values) theLabels.add(value);
        this.values = (T[]) theLabels.toArray();
    }

    @PackagePrivate LabelledEmpiricalPMFImpl(int startIndex, T[] values) {
        super(startIndex, values.length);
        this.values = values;
    }

    private static int getNumElements(Iterable<?> values) {
        int num = 0;
        for (@SuppressWarnings("unused") Object value : values) num++; 
        return num;
    }

    @Override
    public @Nullable T value(int index) {
        return (index >= start() && index <= end()) ? values[index - start()] : null;
    }

    @Override
    public int index(T value) {
        // Simple lookup; should sort a copy of labels and use binary search.
        for (int i = 0; i < size(); i++) {
            if (values[i] == value) return i + start();
        }
        return Integer.MIN_VALUE;
    }

}
