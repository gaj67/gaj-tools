package gaj.analysis.model.prob;

import org.eclipse.jdt.annotation.Nullable;
import gaj.analysis.model.AuxiliaryInfo;
import gaj.analysis.model.DataModel;
import gaj.analysis.model.DataObject;
import gaj.analysis.model.ParameterisedModel;
import gaj.analysis.numeric.vector.DataVector;

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
     * @param input
     *            - The input data in the form of a feature vector, x.
     * @param info
     *            - Optional object either specifying auxiliary information for
     *            the processor, or requesting auxiliary information be
     *            provided.
     * @return The output object containing the computed probability of the
     *         input.
     */
    @Override
    default ProbDataOutput process(DataObject input, @Nullable AuxiliaryInfo info) {
        if (!(input instanceof DataVector)) {
            throw new IllegalArgumentException("DataVector input required!");
        }
        return process((DataVector) input, info);
    }

    /**
     * Computes some probability function of the input data x, possibly
     * conditioning or conditioned by a discrete response variable y.
     * 
     * @param features
     *            - The input feature vector, x.
     * @param info
     *            - Optional object either specifying auxiliary information for
     *            the processor, or requesting auxiliary information be
     *            provided.
     * @return The output object containing the computed probability of the
     *         input.
     */
    ProbDataOutput process(DataVector features, @Nullable AuxiliaryInfo info);

}
