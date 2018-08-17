package gaj.analysis.model.score;

import java.util.stream.Stream;

/**
 * Specifies a repeatable sequence of data input cases for processing by a data
 * model.
 * 
 * <I> - The type of data case.
 */
public interface DataSource<I> {

    /**
     * Obtains a stream of data cases.
     * 
     * @return The data case stream.
     */
    Stream<Datum<I>> stream();

}
