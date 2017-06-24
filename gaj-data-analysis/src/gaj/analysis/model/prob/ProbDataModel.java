package gaj.analysis.model.prob;

import org.eclipse.jdt.annotation.Nullable;
import gaj.analysis.model.AuxiliaryInfo;
import gaj.analysis.model.DataModel;
import gaj.analysis.model.DataObject;
import gaj.analysis.model.ParameterisedModel;

/**
 * An arbitrary probabilistic model of an observation x, possibly for a discrete
 * response variable y. Typically, y is a label for either a class (for a
 * supervised model) or a cluster (for an unsupervised model).
 */
public interface ProbDataModel extends DataModel, ParameterisedModel {

    /**
     * Specifies the type of probability distribution encapsulated by the model.
     * 
     * @return The probabilistic model type.
     */
    ProbModelType getProbModelType();

    /**
     * Computes some probability function of the input data x, possibly
     * conditioning or conditioned by a discrete response variable y.
     * 
     * @param x
     *            - The input data.
     * @param info
     *            - Optional object either specifying auxiliary information for
     *            the processor, or requesting auxiliary information be
     *            provided.
     * @return The output object containing the computed probability of the
     *         input.
     */
    @Override
    ProbDataOutput process(DataObject input, @Nullable AuxiliaryInfo info);

}
