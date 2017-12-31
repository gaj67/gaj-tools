package gaj.analysis.model;

/**
 * Indicates that gradient information is either supplied or to be computed.
 */
public interface GradientAuxiliaryInfo extends AuxiliaryInfo, GradientAware {

    /**
     * A default object to specify that gradient information is either supplied
     * or to be computed.
     */
    final GradientAuxiliaryInfo GRADIENT_AUXILIARY_INFO = new GradientAuxiliaryInfo() {};

}
