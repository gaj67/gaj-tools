package gaj.impl.vector;

public abstract class SparseVector extends AbstractVector {

    protected SparseVector(int length) {
        super(length);
    }

    @Override
    public boolean isDense() {
        return false;
    }

    @Override
    public boolean isSparse() {
        return true;
    }

    @Override
    public boolean isCompound() {
        return false;
    }

}
