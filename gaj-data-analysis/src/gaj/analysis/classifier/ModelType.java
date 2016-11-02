package gaj.analysis.classifier;

/**
 * Specifies the fundamental type of model used by a probabilistic classifier.
 */
public enum ModelType {

    /**
     * Indicates that the classifier directly models p(class|features).
     */
    DISCRIMINATIVE,
    /**
     * Indicates that the classifier directly models p(class,features).
     */
    JOINT,
    /**
     * Indicates that the classifier directly models p(features|class) and p(class).
     */
    GENERATIVE;

    public ModelType getClassifierType(Classifier classifier) {
        return (classifier instanceof GenerativeClassifier) ? GENERATIVE
                : (classifier instanceof JointClassifier) ? JOINT
                        : DISCRIMINATIVE;
    }

    public ModelType getClassifierType(Class<? extends Classifier> klass) {
        return (GenerativeClassifier.class.isAssignableFrom(klass)) ? GENERATIVE
                : (JointClassifier.class.isAssignableFrom(klass)) ? JOINT
                        : DISCRIMINATIVE;
    }

}
