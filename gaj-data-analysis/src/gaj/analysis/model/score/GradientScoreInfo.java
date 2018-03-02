package gaj.analysis.model.score;

import gaj.analysis.data.numeric.GradientComputable;
import gaj.analysis.data.numeric.vector.DataVector;

/**
 * Indicates a score with additional gradient information.
 */
public interface GradientScoreInfo extends WeightedScoreInfo, GradientComputable<DataVector> {

}
