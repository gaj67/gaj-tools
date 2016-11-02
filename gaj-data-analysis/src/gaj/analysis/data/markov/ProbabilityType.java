package gaj.analysis.data.markov;

/**
 * Specifies the type of probability value under consideration,
 * with respect to the hidden Markov state.
 */
public enum ProbabilityType {
    /** A prior state probability, P(s_t). */
    Prior,
    /** An observation likelihood probability, p(x_t|s_t). */
    Conditional,
    /** A posterior state probability, P(s_t|x_t). */
    Posterior
}