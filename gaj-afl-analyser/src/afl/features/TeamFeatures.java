package afl.features;

import gaj.afl.data.match.Team;

/**
 * Encapsulates known information about the team,
 * such as its name plus current strengths and weaknesses.
 * <p>
 *  Currently, the team name is mapped into a 1-of-N vector.
 */
public class TeamFeatures {

	public final FeatureVector features;

	public TeamFeatures(Team team) {
		this.features = SparseFeatureVector.oneOfN(Team.values().length, team.ordinal());
	}

}
