package gaj.analysis.classifier;

import gaj.data.classifier.Classification;
import gaj.data.classifier.DataScorer;
import gaj.data.classifier.DatumScore;
import gaj.data.classifier.ParameterisedClassifier;
import gaj.data.classifier.ScoredTrainer;
import gaj.data.classifier.TrainableClassifier;
import gaj.data.vector.DataVector;

/*package-private*/class TrainableClassifierImpl implements TrainableClassifier {

    private final ParameterisedClassifier classifier;
    private final Class<? extends TrainingAlgorithm> trainerClass;

    /* package-private */TrainableClassifierImpl(ParameterisedClassifier classifier,
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
