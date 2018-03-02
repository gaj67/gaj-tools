package gaj.analysis.model.prob.marginal;

import gaj.analysis.data.DataObject;
import gaj.analysis.data.numeric.DataNumeric;
import gaj.analysis.model.AuxiliaryInfo;
import gaj.analysis.model.prob.ProbDataModel;
import gaj.analysis.model.prob.ProbModelType;

/**
 * A model of the marginal probability (likelihood) p(x) of an observation x.
 */
public interface MarginalDataModel<I extends DataObject> extends ProbDataModel<I, MarginalDataObject> {

    @Override
    default ProbModelType getProbModelType() {
        return ProbModelType.MARGINAL;
    }

    /**
     * Computes the data likelihood p(x).
     */
    @Override
    MarginalDataObject process(I input, AuxiliaryInfo... info);

}
