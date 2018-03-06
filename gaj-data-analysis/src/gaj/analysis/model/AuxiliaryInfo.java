package gaj.analysis.model;

/**
 * Indicates auxiliary information, typically provided to a model, model scorer
 * or model optimiser.
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

    /**
     * 
     * @param info - Optional auxiliary information.
     * @return A value of true (or false) if the supplied auxiliary information
     *         indicates that gradients should (or should not) be computed.
     */
    static boolean isGradientAware(AuxiliaryInfo... info) {
        for (AuxiliaryInfo infoObj : info) {
            if (infoObj instanceof gaj.analysis.model.GradientAware)
                return true;
        }
        return false;
    }

}
