package gaj.analysis.classifier;

/**
 * Provides the means for scoring the accuracy of a classifier
 * against a known gold-standard data set.
 */
public interface DataScorer {

    /**
     * Indicates the total number C of classes in the classification scheme.
     * 
     * @return The number of classes.
     */
    int numClasses();

    /**
     * Indicates the number F of numerical features in any feature vector
     * to be classified.
     * 
     * @return The number of feature elements.
     */
    int numFeatures();

    /**
     * Computes the overall score of the given
     * classifier against the gold-standard data set.
     * 
     * @param classifier - A trained classifier.
     * @return The classification score.
     */
    double getClassifierScore(Classifier classifier);

    /**
     * Computes the overall score (and associated information) of the given
     * classifier against the gold-standard data set.
     * <p/>
     * Note: If the classifier is gradient-enabled, then score gradient information should be available.
     * 
     * @param classifier - A trained classifier.
     * @return The classification score information.
     */
    ClassifierScoreInfo getClassifierScoreInfo(ParameterisedClassifier classifier);

    /**
     * Indicates whether or not the scorer computes gradient
     * information.
     * 
     * @return A value of true (or false) if it is (or is not)
     * safe to call the DatumScore.getGradient() method.
     */
    boolean hasGradient();

    /**
     * Computes the individual accuracy scores (and associated information)
     * of the given classifier against each
     * data point in the gold-standard data set.
     * <p/>
     * Note: If the scorer is gradient-enabled, then score gradient information should be available.
     * 
     * @param classifier - A trained classifier.
     * @return A sequence of individual datum scores.
     */
    Iterable<? extends DatumScore> getDatumScores(Classifier classifier);

}
