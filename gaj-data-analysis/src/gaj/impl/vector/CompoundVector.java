package gaj.impl.vector;

public abstract class CompoundVector extends AbstractVector {

    protected CompoundVector(int length) {
        super(length);
    }

    @Override
    public boolean isDense() {
        return false;
    }

    @Override
    public boolean isSparse() {
        return false;
    }

    @Override
    public boolean isCompound() {
        return true;
    }

}
