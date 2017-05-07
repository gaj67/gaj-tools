package gaj.analysis.classifier;

import gaj.analysis.classifier.updated.OptimisableClassifier;

/**
 * Specifies a classifier that can have its parameters updated by training
 * (and optionally testing) against gold-standard data.
 */
public interface TrainableClassifier extends OptimisableClassifier {

    /**
     * Obtains a trainer for the classifier bound to the given training/testing data scorer(s).
     * 
     * @return A classifier trainer.
     */
    ScoredTrainer getTrainer(DataScorer... scorers);

}
