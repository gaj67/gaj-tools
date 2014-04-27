package afl.classifier;

import java.util.Collection;

import afl.features.FeatureMatrix;

public interface SubsetClassifier extends Classifier {

   /**
    * Performs one update of the classifier parameters
    * from the supplied training data.
    * @param features - An NxF matrix of features.
    * @param classes - An Nx1 vector of class labels in {0,1,...,C-1}.
    * @param subset - The set of indices (in {0,1,...,N-1}) of data from which to obtain the parameter update.
    * @return The score of the training data using the parameters prior to the update.
    */
   double update(FeatureMatrix features, int[] classes, Collection<Integer> subset);

   /**
    * Performs one update of the classifier parameters
    * from the supplied training data.
    * @param features - An NxF matrix of features.
    * @param classes - An Nx1 vector of class labels in {0,1,...,C-1}.
    * @param leaveOut - The set of indices (in {0,1,...,N-1}) of data to exclude from the parameter update.
    * @return The score of the training data using the parameters prior to the update.
    */
   double updateExcluding(FeatureMatrix features, int[] classes, Collection<Integer> leaveOut);

   /**
    * @param features - An NxF matrix of features.
    * @param classes - An Nx1 vector of class labels in {0,1,...,C-1}.
    * @param subset - The set of indices (in {0,1,...,N-1}) of data to use in obtaining the statistics.
    * @return The statistics obtained from measuring the success or otherwise of classifying
    * the features.
    */
   Stats statistics(FeatureMatrix features, int[] classes, Collection<Integer> subset);

   /**
    * @param features - An NxF matrix of features.
    * @param classes - An Nx1 vector of class labels in {0,1,...,C-1}.
    * @param leaveOut - The set of indices (in {0,1,...,N-1}) of data to exclude from the statistics.
    * @return The statistics obtained from measuring the success or otherwise of classifying
    * the features.
    */
   Stats statisticsExcluding(FeatureMatrix features, int[] classes, Collection<Integer> leaveOut);

}
