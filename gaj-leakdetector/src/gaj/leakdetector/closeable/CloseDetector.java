/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.leakdetector.closeable;

import gaj.leakdetector.core.Detector;
import java.io.Closeable;

public interface CloseDetector extends Detector<Closeable> {

    /**
     * Explicitly closes the leak detector - called from the instrumented resource when
     * its close() method is called.
     */
    public void close();

    /**
     * Indicates whether or not the leak detector has been closed by a caller explicitly 
     * closing the underlying instrumented resource. 
     * 
     * @return A value of true (or false) if the resource is closed.
     */
    public boolean isClosed();

}