package gaj.impl.vector;

public abstract class DenseVector extends AbstractVector {

    protected DenseVector(int length) {
        super(length);
    }

    @Override
    public boolean isDense() {
        return true;
    }

    @Override
    public boolean isSparse() {
        return false;
    }

    @Override
    public boolean isCompound() {
        return false;
    }

}
