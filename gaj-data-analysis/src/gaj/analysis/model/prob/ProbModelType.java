package gaj.analysis.model.prob;

/**
 * Indicates the type of probability model under consideration. This is
 * especially important to know for gradient computations, since the gradient
 * (with respect to the model parameters) may only be computed from the main
 * model, even if subsidiary models are also obtainable from the main model.
 */
public enum ProbModelType {

    /**
     * Models p(x).
     */
    LIKELIHOOD,

    /**
     * Models p(y|x), where y is discrete and finitely countable.
     */
    DISCRIMINATIVE,

    /**
     * Models p(y,x), where y is discrete and finitely countable.
     */
    JOINT,

    /**
     * Models p(x|y) and p(y), where y is discrete and finitely countable.
     */
    GENERATIVE,
    
    ;
    
}
