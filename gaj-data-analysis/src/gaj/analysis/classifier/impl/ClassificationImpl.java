package gaj.analysis.classifier.impl;

import gaj.analysis.classifier.updated.Classification;
import gaj.analysis.numeric.vector.DataVector;
import gaj.common.annotations.PackagePrivate;

@PackagePrivate class ClassificationImpl implements Classification {

    private final DataVector features;
    private final DataVector posteriors;

    @PackagePrivate ClassificationImpl(DataVector features, DataVector posteriors) {
        this.features = features;
        this.posteriors = posteriors;
    }

    @Override
    public int numClasses() {
        return posteriors.size();
    }

    @Override
    public int numFeatures() {
        return features.size();
    }

    @Override
    public DataVector features() {
        return features;
    }

    @Override
    public DataVector posteriors() {
        return posteriors;
    }

}
