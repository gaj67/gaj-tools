package gaj.analysis.bayes.pmf.impl.integer;

import gaj.analysis.bayes.pmf.EmpiricalIndexPMF;
import gaj.common.annotations.PackagePrivate;

@PackagePrivate class IndexPMFImpl extends BasePMFImpl<Integer> implements EmpiricalIndexPMF<Integer> {

    private final int start;
    private final int end;

    protected IndexPMFImpl(int start, int end) {
        super(computeNumElements(start, end));
        this.start = start;
        this.end = end;
    }

    private static int computeNumElements(int start, int end) {
        long size = (long) end - (long) start + 1;
        if (size > 0 && size <= Integer.MAX_VALUE) return (int) size;
        throw new IllegalArgumentException("Invalid start and end values");
    }

    @Override
    public Integer start() {
        return start;
    }

    @Override
    public Integer end() {
        return end;
    }

    @Override
    protected int index(Integer value) {
        int index = value - start;
        return (0 <= index && index < size()) ? index : -1;
    }

}
