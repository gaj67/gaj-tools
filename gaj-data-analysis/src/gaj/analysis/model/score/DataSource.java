package gaj.analysis.model.score;

import java.util.stream.Stream;
import gaj.analysis.data.DataObject;

/**
 * Specifies a repeatable sequence of data input cases for processing by a data
 * model.
 * 
 * <I> - The type of data case.
 */
public interface DataSource<I extends DataObject> {

    /**
     * Obtains a stream of data cases.
     * 
     * @return The data case stream.
     */
    Stream<DataCase<I>> stream();

}
