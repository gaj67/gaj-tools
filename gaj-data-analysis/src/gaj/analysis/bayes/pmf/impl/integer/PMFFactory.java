package gaj.analysis.bayes.pmf.impl.integer;

import gaj.analysis.bayes.pmf.EmpiricalIndexPMF;
import gaj.analysis.bayes.pmf.EmpiricalPMF;

public abstract class PMFFactory {

    private PMFFactory() {}
    
    public static EmpiricalIndexPMF<Integer> newPMF(int start, int end) {
        return new IndexPMFImpl(start, end);
    }

    public static EmpiricalIndexPMF<Integer> newPMF(int size) {
        return new IndexPMFImpl(0, size - 1);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> EmpiricalPMF<T> newPMF(T... values) {
        return new ArrayPMFImpl(values);
    }

}
