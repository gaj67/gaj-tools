package gaj.analysis.model.prob;

import org.eclipse.jdt.annotation.Nullable;
import gaj.analysis.model.AuxiliaryInfo;
import gaj.analysis.model.DataObject;

/**
 * A model of the probability (likelihood) p(x) of an observation x.
 */
public interface LikelihoodModel extends ProbDataModel {

    @Override
    default ProbModelType getProbModelType() {
        return ProbModelType.LIKELIHOOD;
    }

    /**
     * Computes the data likelihood p(x).
     * 
     * @param x
     *            - The input data.
     * @param info
     *            - Optional object either specifying auxiliary information for
     *            the processor, or requesting auxiliary information be
     *            provided.
     * @return The output object.
     */
    @Override
    LikelihoodOutput process(DataObject x, @Nullable AuxiliaryInfo info);

}
