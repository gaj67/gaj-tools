package gaj.text.data;

/**
 * Specifies a consumer of typed objects.
 */
public interface Consumer<T> {

    /**
     * Consumes the next object in a sequence.
     * 
     * @return A value of true (or false) if the object has (or has not) been consumed.
     */
    boolean consume(T item);

}
