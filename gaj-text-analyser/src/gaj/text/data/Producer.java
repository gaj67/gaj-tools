package gaj.text.data;

/**
 * Specifies a producer of typed objects.
 */
public interface Producer<T> {

    /**
     * Produces the next object in a sequence.
     * 
     * @return The object, or a value of null if the sequence has ended.
     */
    /*@Nullable*/ T produce();

}
