package gaj.analysis.model;

/**
 * Indicates either auxiliary information provided to a data processor, or a
 * request to the processor to produce auxiliary information in its output.
 */
public interface AuxiliaryInfo {

    /**
     * Auxiliary information that additionally indicates that gradient
     * information should be computed, if possible.
     */
    static interface GradientAware extends AuxiliaryInfo, gaj.analysis.model.GradientAware {}

    /**
     * Indicates that gradient information should be computed, if possible.
     */
    final static AuxiliaryInfo COMPUTE_GRADIENT = new AuxiliaryInfo.GradientAware() {};

}
