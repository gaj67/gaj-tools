package gaj.analysis.model.score;

import java.util.stream.Stream;

/**
 * Specifies a repeatable sequence of data cases for processing by a data model.
 */
public interface DataSource {

    /**
     * Obtains a stream of data cases.
     * 
     * @return The data case stream.
     */
    Stream<DataCase> stream();

}
