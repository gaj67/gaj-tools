package gaj.analysis.classifier.updated;

/**
 * Specifies the fundamental type of model used by a probabilistic classifier.
 */
public enum ClassifierModelType {

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
    GENERATIVE,

    /**
     * Indicates a classifier with no explicit model or with an unknown model.
     */
    OTHER;

    public static ClassifierModelType getClassifierModelType(Classifier classifier) {
        return (classifier instanceof GenerativeClassifier) ? GENERATIVE
                : (classifier instanceof JointClassifier) ? JOINT
                : (classifier instanceof DiscriminativeClassifier) ? DISCRIMINATIVE 
                : OTHER;
    }

    public static ClassifierModelType getClassifierModelType(Class<? extends Classifier> klass) {
        return (GenerativeClassifier.class.isAssignableFrom(klass)) ? GENERATIVE
                : (JointClassifier.class.isAssignableFrom(klass)) ? JOINT
                : (DiscriminativeClassifier.class.isAssignableFrom(klass)) ? DISCRIMINATIVE 
                : OTHER;
    }

}
