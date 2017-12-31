package gaj.analysis.model;

/**
 * Indicates either auxiliary information provided to a data processor, or a
 * request to the processor to produce auxiliary information in its output.
 */
public interface AuxiliaryInfo {

    /**
     * A default object to use when no auxiliary information is required or
     * specified.
     */
    final AuxiliaryInfo NO_AUXILIARY_INFO = new AuxiliaryInfo() {};

}
