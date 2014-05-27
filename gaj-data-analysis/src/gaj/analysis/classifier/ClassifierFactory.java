package gaj.analysis.classifier;

import gaj.data.classifier.ParameterisedClassifier;
import gaj.data.classifier.TrainableClassifier;

public abstract class ClassifierFactory {

	private ClassifierFactory() {}

	/**
	 * Obtains a logistic classifier.
	 * 
	 * @param numClasses - The number of classes in the classification scheme.
	 * @param numFeatures - The length of each feature vector.
	 * @return A logistic classifier.
	 */
	public static ParameterisedClassifier newLogisticClassifier(int numClasses, int numFeatures) {
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
			ParameterisedClassifier classifier, Class<? extends TrainingAlgorithm> algo) {
		return new TrainableClassifierImpl(classifier, algo);
	}

	/**
	 * Obtains the default trainable classifier.
	 * 
	 * @param numClasses - The number of classes in the classification scheme.
	 * @param numFeatures - The length of each feature vector.
	 * @return A trainable classifier.
	 */
	public static TrainableClassifier newTrainableClassifier(int numClasses, int numFeatures) {
		return new TrainableClassifierImpl(
				newLogisticClassifier(numClasses, numFeatures),
				GradientAscentTrainer.class);
	}

}
