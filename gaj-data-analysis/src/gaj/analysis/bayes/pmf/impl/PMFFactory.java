package gaj.analysis.bayes.pmf.impl;

import gaj.analysis.bayes.pmf.EmpiricalPMF;
import gaj.analysis.bayes.pmf.LabelledPMF;

public abstract class PMFFactory {

    private PMFFactory() {}
    
    public static EmpiricalPMF newEmpiricalPMF(int numElements) {
        return new EmpiricalPMFImpl(0, numElements);
    }

    public static EmpiricalPMF newEmpiricalPMF(int numElements, int startIndex) {
        return new EmpiricalPMFImpl(startIndex, numElements);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <T extends Comparable<T>> LabelledPMF<T> newLabelledPMF(T[] values) {
        return new LabelledEmpiricalPMFImpl(0, values);
    }

}
