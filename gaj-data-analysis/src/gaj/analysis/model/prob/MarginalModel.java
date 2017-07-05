package gaj.analysis.model.prob;

import org.eclipse.jdt.annotation.Nullable;
import gaj.analysis.model.AuxiliaryInfo;
import gaj.analysis.numeric.vector.DataVector;

/**
 * A model of the marginal probability (likelihood) p(x) of an observation x.
 */
public interface MarginalModel extends ProbDataModel {

    @Override
    default ProbModelType getProbModelType() {
        return ProbModelType.MARGINAL;
    }

    /**
     * Computes the data likelihood p(x).
     * 
     * @param features
     *            - The input feature vector, x.
     * @param info
     *            - Optional object either specifying auxiliary information for
     *            the processor, or requesting auxiliary information be
     *            provided.
     * @return The output object.
     */
    @Override
    MarginalOutput process(DataVector features, @Nullable AuxiliaryInfo info);

}
