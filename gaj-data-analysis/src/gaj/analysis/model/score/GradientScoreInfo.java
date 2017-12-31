package gaj.analysis.model.score;

import gaj.analysis.model.GradientComputable;
import gaj.analysis.numeric.vector.DataVector;

/**
 * Indicates a score with additional gradient information.
 */
public interface GradientScoreInfo extends WeightedScoreInfo, GradientComputable<DataVector> {

}
