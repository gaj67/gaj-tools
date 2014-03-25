package afl.features;


public class FeaturesClassCollection {

	public final FeatureMatrix features = new AddableFeatureMatrix();
	public final ClassVector classes = new AddableClassVector();

	public FeaturesClassCollection() {}

	public void add(FeatureVector vector, int klass) {
	   ((AddableFeatureMatrix)features).add(vector);
		((AddableClassVector)classes).add(klass);
	}

}
