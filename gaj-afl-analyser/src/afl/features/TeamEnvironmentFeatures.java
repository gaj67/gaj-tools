package afl.features;


/**
 * Encapsulates information about a team and also
 * information about the match environment.
 * <p>For technical reasons, the features from the team
 * and environment cannot simply be concatenated, otherwise
 * they would be linearly separable and the environment features
 * would cancel out when using a logistic classifier.
 * <p>Thus, the cross-product is first taken of the environment
 * features with the team features (producing bilinear terms), and
 * these are concatenated with the team features
 * (which provide linear terms).
 */
public class TeamEnvironmentFeatures {

	public final FeatureVector features;

	public TeamEnvironmentFeatures(final TeamFeatures team,
			                         final EnvironmentFeatures environment)
	{
		final FeatureVector tf = team.features;
		final FeatureVector ef = environment.features;
		int tlen = tf.length();
		FeatureVector[] features = new FeatureVector[tlen+1];
		features[0] = tf;
		for (int i = 1; i <= tlen; i++)
			features[i] = ef.scale(tf.get(i-1));
		this.features = new SparseFeatureVector(features);
	}

}
