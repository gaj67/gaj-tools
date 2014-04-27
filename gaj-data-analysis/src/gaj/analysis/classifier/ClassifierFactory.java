package gaj.analysis.classifier;

import gaj.data.classifier.TrainableClassifier;

public abstract class ClassifierFactory {

	private ClassifierFactory() {}
	
	public static TrainableClassifier 
		newDefaultClassifier(int numClasses, int numFeatures) {
		return new TrainableClassifierImpl(
				new LogisticClassifier(numClasses, numFeatures), 
				GradientAscentTrainer.class);
	}
	
}
