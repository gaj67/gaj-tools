package gaj.analysis.model.prob;

import gaj.analysis.data.DataObject;
import gaj.analysis.data.numeric.vector.DataVector;
import gaj.analysis.model.AuxiliaryInfo;
import gaj.analysis.model.ParameterisedDataModel;

/**
 * An arbitrary probabilistic model of an observation x, possibly for an 
 * additional discrete response variable y. Typically, y is a label for 
 * either a class (for a supervised model) or a cluster (for an unsupervised model).
 * 
 * <I> - The input data type.
 * <O> - The output data type of probabilities.
 * <T> - The numerical type of model parameterisation.
 */
public interface ProbDataModel<I extends DataObject, O extends ProbDataObject>
        extends ParameterisedDataModel<I, O, DataVector>
{

    /**
     * Specifies the type of probability distribution encapsulated by the model.
     * 
     * @return The probabilistic model type.
     */
    ProbModelType getProbModelType();

    /**
     * Computes the necessary probabilities as a function of the input, x.
     */
    @Override
    O process(I input, AuxiliaryInfo... info);

}
