package gaj.analysis.classifier;

import gaj.data.classifier.DataScorer;
import gaj.data.classifier.DatumScore;
import gaj.data.classifier.ScoredTrainer;
import gaj.data.classifier.TrainableClassifier;
import gaj.data.classifier.UpdatableClassifier;
import gaj.data.numeric.DataObject;
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
	public DataObject getParameters() {
		return classifier.getParameters();
	}

	@Override
	public boolean setParameters(DataObject params) {
		return classifier.setParameters(params);
	}

	@Override
	public boolean hasGradient() {
		return classifier.hasGradient();
	}

	@Override
	public DataObject getGradient(DatumScore datumScore) {
		return classifier.getGradient(datumScore);
	}

	@Override
	public ScoredTrainer getTrainer(DataScorer... scorers) {
		try {
			ClassifierTrainer trainer = trainerClass.newInstance();
			trainer.bindArguments(classifier, scorers);
			return trainer;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IllegalStateException(e.getMessage());
		}
	}

}
