package gaj.analysis.classifier;

import gaj.data.classifier.DataScorer;
import gaj.data.classifier.TrainableClassifier;
import gaj.data.classifier.TrainingParams;
import gaj.data.classifier.TrainingSummary;
import gaj.data.classifier.UpdatableClassifier;
import gaj.data.vector.DataVector;

/*package-private*/ class TrainableClassifierImpl implements
		TrainableClassifier {

	private final UpdatableClassifier classifier;
	private final Class<? extends ClassifierTrainer> trainerClass;

	/*package-private*/ TrainableClassifierImpl(UpdatableClassifier classifier, 
			Class<? extends ClassifierTrainer> trainerClass) {
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
	public DataVector classify(DataVector features) {
		return classifier.classify(features);
	}

	@Override
	public TrainingSummary train(TrainingParams control, DataScorer... scorers) {
		try {
			ClassifierTrainer trainer = trainerClass.newInstance();
			trainer.bindArguments(classifier, scorers);
			return trainer.train(control);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IllegalStateException(e.getMessage());
		}
	}

}
