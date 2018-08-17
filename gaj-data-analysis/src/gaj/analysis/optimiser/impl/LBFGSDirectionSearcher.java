package gaj.analysis.optimiser.impl;

import gaj.analysis.model.VectorGradientComputable;
import gaj.analysis.model.score.ScoreInfo;
import gaj.analysis.numeric.vector.DataVector;
import gaj.analysis.numeric.vector.SettableVector;
import gaj.analysis.numeric.vector.WritableVector;
import gaj.analysis.numeric.vector.impl.VectorFactory;
import gaj.analysis.optimiser.searcher.DirectionSearchParams;
import gaj.analysis.optimiser.searcher.DirectionSearchStatus;
import gaj.analysis.optimiser.searcher.DirectionSearcherType;
import gaj.analysis.optimiser.searcher.LBFGSDirectionSearchParams;

/**
 * Implements {@link DirectionSearcherType#LBFGS}.
 */
public class LBFGSDirectionSearcher extends BaseDirectionSearcher {

    /** Maximum number of updates to keep. */
    private final int maxSize;
    /** Stores the differences in model parameters, s_{k-1} = x_k - x_{k-1}. */
    private final DataVector[] s;
    /** Stores the differences in score gradients, y_{k-1} = g_k - g_{}k-1}. */
    private final DataVector[] y;
    /** Stores the update-gradient projection scalings, rho_k = 1/(y_k . s_k) */
    private final double[] rho;
    /** Temporary storage for on-the-fly projection scalings. */
    private final double[] alpha;

    /** The previous model parameters. */
    private DataVector x_km1;
    /** The previous score gradient. */
    private DataVector g_km1;
    /** The current number of updates stored, up to the maximum number allowed. */
    private int curSize = 0;
    /**
     * The current position of the latest update. Note: Once the storage is
     * full, the subsequent current position will wrap around, and the newest
     * update will replace the oldest update.
     */
    private int curPos = -1;

    /**
     * Binds the direction search to the optimiser and the search parameters.
     * 
     * @param optimiser
     *            - An updatable optimiser.
     * @param params
     *            - The parameters controlling each direction search.
     */
    public LBFGSDirectionSearcher(UpdatableOptimser optimiser, DirectionSearchParams params) {
        super(optimiser, toLBFGSParams(params));
        maxSize = getParams().getMemorySize();
        s = new DataVector[maxSize];
        y = new DataVector[maxSize];
        rho = new double[maxSize];
        alpha = new double[maxSize];
    }

    @Override
    protected LBFGSDirectionSearchParams getParams() {
        return (LBFGSDirectionSearchParams) super.getParams();
    }

    @Override
    public DirectionSearchStatus search(SettableVector direction) {
        // Get gradient.
        ScoreInfo scoreInfo = getOptimiser().getScoreInfo();
        if (scoreInfo instanceof VectorGradientComputable) {
            DataVector g_k = ((VectorGradientComputable) scoreInfo).getGradient();
            DataVector x_k = getOptimiser().getModelParameters();
            // Get direction.
            if (curPos < 0) {
                // Initially go in direction of gradient.
                direction.set(g_k);
            } else {
                computeDifferences(g_k, x_k);
                direction.set(computeDirection(g_k));
            }
            g_km1 = g_k;
            x_km1 = x_k;
            curPos = (curPos + 1) % maxSize;
            return DirectionSearchStatus.AVAILABLE;
        }
        return DirectionSearchStatus.GRADIENT_UNAVAILABLE;
    }

    private void computeDifferences(DataVector g_k, DataVector x_k) {
        s[curPos] = VectorFactory.subtract(x_k, x_km1);
        y[curPos] = VectorFactory.subtract(g_k, g_km1);
        rho[curPos] = 1.0 / VectorFactory.dot(s[curPos], y[curPos]);
        curSize = (curSize >= maxSize) ? maxSize : (curSize + 1);
    }

    private DataVector computeDirection(DataVector g_k) {
        WritableVector z = VectorFactory.copy(g_k);
        // Loop from most recent to least recent update.
        int i = curPos;
        for (int j = 0; j < curSize; j++) {
            alpha[i] = rho[i] * VectorFactory.dot(s[i], z);
            z.subtract(VectorFactory.scale(y[i], alpha[i]));
            i = (i - 1 + curSize) % curSize;
        }
        double hk = VectorFactory.dot(s[curPos], y[curPos]) / VectorFactory.dot(y[curPos], y[curPos]);
        z.multiply(hk);
        // Loop from least recent to most recent update.
        i = (curPos + 1) % curSize;
        for (int j = 0; j < curSize; j++) {
            double beta_i = rho[i] * VectorFactory.dot(y[i], z);
            z.add(VectorFactory.scale(s[i], alpha[i] - beta_i));
            i = (i + 1) % curSize;
        }
        return z;
    }

    private static LBFGSDirectionSearchParams toLBFGSParams(DirectionSearchParams params) {
        if (params instanceof LBFGSDirectionSearchParams)
            return (LBFGSDirectionSearchParams) params;
        return new LBFGSDirectionSearchParams() {
            @Override
            public int getDirectionSign() {
                return params.getDirectionSign();
            }

            @Override
            public DirectionSearcherType getDirectionSearcherType() {
                return params.getDirectionSearcherType();
            }
        };
    }

}
