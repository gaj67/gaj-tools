package gaj.analysis.classifier.impl;

import gaj.analysis.classifier.TrainableClassifier;
import gaj.analysis.classifier.updated.OptimisableClassifier;

public abstract class ClassifierFactory {

    private ClassifierFactory() {
    }

    /**
     * Obtains a logistic classifier.
     * 
     * @param numClasses - The number of classes in the classification scheme.
     * @param numFeatures - The length of each feature vector.
     * @return A logistic classifier.
     */
    public static OptimisableClassifier newLogisticClassifier(int numClasses, int numFeatures) {
        return new LogisticClassifier(numClasses, numFeatures);
    }

    /**
     * Binds the given classifier to the given training algorithm.
     * 
     * @param classifier - The classifier to be trained.
     * @param algo - The training algorithm class.
     * @return A trainable classifier.
     */
    public static TrainableClassifier newClassifierTrainer(
            OptimisableClassifier classifier, Class<? extends TrainingAlgorithm> algo) {
        return new TrainableClassifierImpl(classifier, algo);
    }

    /**
     * Obtains the default classifier with a gradient ascent trainer.
     * 
     * @param numClasses - The number of classes in the classification scheme.
     * @param numFeatures - The length of each feature vector.
     * @param type - The type of acceleration to apply to gradient ascent.
     * @return A trainable classifier.
     */
    public static TrainableClassifier newTrainableClassifier(int numClasses, int numFeatures, AccelerationType type) {
        return new TrainableClassifierImpl(
                newLogisticClassifier(numClasses, numFeatures),
                getGradientAscentAlgorithm(type));
    }

    private static Class<? extends TrainingAlgorithm> getGradientAscentAlgorithm(
            AccelerationType type)
    {
        switch (type) {
        case Cubic:
            return CubicGradientAscentTrainer.class;
        case Linear:
            return GradientAscentTrainer.class;
        case Quadratic:
            return QuadraticGradientAscentTrainer.class;
        default:
            throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

}
