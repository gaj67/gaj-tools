package gaj.analysis.classifier.impl;

import gaj.analysis.classifier.DataScorer;
import gaj.analysis.classifier.DatumScore;
import gaj.analysis.classifier.ScoredTrainer;
import gaj.analysis.classifier.TrainableClassifier;
import gaj.analysis.classifier.updated.Classification;
import gaj.analysis.classifier.updated.OptimisableClassifier;
import gaj.analysis.data.numeric.vector.DataVector;

/*package-private*/class TrainableClassifierImpl implements TrainableClassifier {

    private final OptimisableClassifier classifier;
    private final Class<? extends TrainingAlgorithm> trainerClass;

    /* package-private */TrainableClassifierImpl(OptimisableClassifier classifier,
            Class<? extends TrainingAlgorithm> trainerClass) {
        this.classifier = classifier;
        this.trainerClass = trainerClass;
    }

    @Override
    public int numClasses() {
        return classifier.numClasses();
    }

    @Override
    public int numFeatures() {
        return classifier.numFeatures();
    }

    @Override
    public Classification classify(DataVector features) {
        return classifier.classify(features);
    }

    @Override
    public DataVector getParameters() {
        return classifier.getParameters();
    }

    @Override
    public boolean setParameters(DataVector params) {
        return classifier.setParameters(params);
    }

    @Override
    public boolean hasGradient() {
        return classifier.hasGradient();
    }

    @Override
    public DataVector getGradient(DatumScore datumScore) {
        return classifier.getGradient(datumScore);
    }

    @Override
    public ScoredTrainer getTrainer(DataScorer... scorers) {
        return new ClassifierTrainer(classifier, scorers, trainerClass);
    }

    @Override
    public int numParameters() {
        return classifier.numParameters();
    }

}
