package gaj.analysis.model.prob.latent.impl;

import gaj.analysis.data.numeric.matrix.DataMatrix;
import gaj.analysis.data.numeric.matrix.WritableMatrix;
import gaj.analysis.data.numeric.matrix.impl.MatrixFactory;
import gaj.analysis.data.numeric.vector.DataVector;
import gaj.analysis.model.ParameterisedModel;

/**
 * Implements a parameterised model where the parameters internally take the
 * form of a matrix, but externally are represented as a vector.
 */
public class MatrixAsVectorModel implements ParameterisedModel<DataVector> {

    protected final DataMatrix params;

    /**
     * Constructs a model with modifiable parameters (initially set to zero).
     * 
     * @param numRows
     *            - The number of rows.
     * @param numColumns
     *            - The number of columns.
     */
    public MatrixAsVectorModel(int numRows, int numColumns) {
        this.params = MatrixFactory.newMatrix(numRows, numColumns);
    }

    /**
     * Constructs a model with the given parameters (by reference).
     * 
     * @param params
     *            - The R x C parameters matrix.
     */
    public MatrixAsVectorModel(DataMatrix params) {
        this.params = params;
    }

    @Override
    public int numParameters() {
        return params.size();
    }

    @Override
    public DataVector getParameters() {
        return MatrixFactory.asVector(params);
    }

    @Override
    public void setParameters(DataVector params) throws IllegalArgumentException {
        if (this.params == params) return;
        if (!(this.params instanceof WritableMatrix)) {
            throw new IllegalArgumentException("Model parameters are immutable");
        }
        MatrixFactory.setFromVector((WritableMatrix) this.params, params);
    }

}
