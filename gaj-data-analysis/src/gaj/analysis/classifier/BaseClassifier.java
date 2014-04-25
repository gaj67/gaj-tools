package gaj.analysis.classifier;

import gaj.data.classifier.DatumScore;
import gaj.data.classifier.UpdatableClassifier;
import gaj.data.numeric.DataObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Offers a base class for an updatable classifier.
 * By default, no gradient is computed.
 */
public abstract class BaseClassifier implements UpdatableClassifier {

	/** Total number C of classes. */
	protected final int numClasses;
	/** Number F of features in a data vector. */
	protected final int numFeatures;

	protected BaseClassifier(int numClasses, int numFeatures) {
		this.numClasses = numClasses;
		this.numFeatures = numFeatures;
	}

	@Override
	public int numClasses() {
		return numClasses;
	}

	@Override
	public int numFeatures() {
		return numFeatures;
	}
	
	@Override
	public boolean hasGradient() {
		return false;
	}
	
	@Override
	public DataObject getGradient(DatumScore datumScore) {
		throw new NotImplementedException();
	}
}
