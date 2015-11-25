package afl.features;

public class DenseFeatureVector implements FeatureVector {

    private final double[] values;

    /**
     * Retains a reference to the dense feature vector.
     * 
     * @param features - A dense array of features.
     */
    public DenseFeatureVector(double[] features) {
        values = features;
    }

    /**
     * Returns an all-zero dense feature vector of the given length.
     * 
     * @param length - The length of the new feature vector.
     */
    public DenseFeatureVector(int length) {
        values = new double[length];
    }

    public double get(int index) {
        return values[index];
    }

    public boolean isSparse() {
        return false;
    }

    public int length() {
        return values.length;
    }

    public FeatureVector scale(double multiplier) {
        double[] newFeatures = new double[values.length];
        if (multiplier != 0.0)
            for (int i = 0; i < values.length; i++)
                newFeatures[i] = values[i] * multiplier;
        return new SparseFeatureVector(newFeatures);
    }

    public double[] dense() {
        return values;
    }

    public double dot(FeatureVector vector) {
        assert values.length == vector.length();
        if (vector.isSparse())
            return vector.dot(this); // Let sparse class do all the work!
        double dot = 0.0;
        double[] features = vector.dense();
        for (int i = 0; i < values.length; i++)
            dot += features[i] * values[i];
        return dot;
    }

    public FeatureVector subtract(FeatureVector features) {
        if (values.length != features.length())
            throw new IllegalArgumentException(
                    String.format("Expected length %d, got %d",
                            values.length, features.length()));
        double[] newFeatures;
        if (features.isSparse()) {
            newFeatures = features.dense(); // A copy, so can re-use.
            for (int i = 0; i < values.length; i++)
                newFeatures[i] = values[i] - newFeatures[i];
        } else {
            newFeatures = new double[values.length];
            double[] otherFeatures = features.dense(); // Actual array, so copy.
            for (int i = 0; i < values.length; i++)
                newFeatures[i] = values[i] - otherFeatures[i];
        }
        return new DenseFeatureVector(newFeatures);
    }

    public FeatureVector add(FeatureVector features) {
        if (values.length != features.length())
            throw new IllegalArgumentException(
                    String.format("Expected length %d, got %d",
                            values.length, features.length()));
        double[] newFeatures;
        if (features.isSparse()) {
            newFeatures = features.dense(); // A copy, so can re-use.
            for (int i = 0; i < values.length; i++)
                newFeatures[i] += values[i];
        } else {
            newFeatures = new double[values.length];
            double[] otherFeatures = features.dense(); // Actual array, so copy.
            for (int i = 0; i < values.length; i++)
                newFeatures[i] = values[i] + otherFeatures[i];
        }
        return new DenseFeatureVector(newFeatures);
    }

}
