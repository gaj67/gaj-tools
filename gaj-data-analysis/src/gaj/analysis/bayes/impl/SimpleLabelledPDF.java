package gaj.analysis.bayes.impl;

import java.util.ArrayList;
import org.eclipse.jdt.annotation.Nullable;
import gaj.analysis.bayes.LabelledPDF;
import gaj.common.annotations.PackagePrivate;

@PackagePrivate class SimpleLabelledPDF<X extends Comparable<?>> extends SimplePDF implements LabelledPDF<X> {

    private final X[] labels;

    @SuppressWarnings("unchecked")
    @PackagePrivate SimpleLabelledPDF(Iterable<X> labels) {
        super(getNumElements(labels));
        ArrayList<X> theLabels = new ArrayList<X>();
        for (X label : labels) theLabels.add(label);
        this.labels = (X[]) theLabels.toArray();
    }

    private static int getNumElements(Iterable<?> labels) {
        int num = 0;
        for (@SuppressWarnings("unused") Object label : labels) num++; 
        return num;
    }

    @Override
    public @Nullable X label(int index) {
        return (index >= 0 && index <= size()) ? labels[index] : null;
    }

    @Override
    public int index(X label) {
        // Simple lookup; should sort a copy of labels and use binary search.
        for (int i = 0; i < size(); i++) {
            if (labels[i] == label) return i;
        }
        return -1;
    }

}
