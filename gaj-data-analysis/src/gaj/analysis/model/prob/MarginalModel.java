package gaj.analysis.model.prob;

import gaj.analysis.model.AuxiliaryInfo;
import gaj.analysis.model.DataObject;

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
     * @param input
     *            - The input data, x.
     * @param info
     *            - An object either specifying auxiliary information for the
     *            processor, or requesting auxiliary information be provided.
     * @return The output object.
     */
    @Override
    MarginalOutput process(DataObject input, AuxiliaryInfo info);

}
