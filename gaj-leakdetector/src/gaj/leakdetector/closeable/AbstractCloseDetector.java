/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.leakdetector.closeable;

import gaj.leakdetector.core.AbstractDetector;
import java.io.Closeable;

/**
 * A leak detector to be instrumented into a closable resource. 
 */
public abstract class AbstractCloseDetector extends AbstractDetector<Closeable> implements CloseDetector {

    volatile private boolean isClosed = false;

    //**************************************************
    // Instrumented methods.

    protected AbstractCloseDetector(Closeable resource) {
        super(resource);
    }

    @Override
    public void close() {
        isClosed = true;
    }

    /**
     * Implicitly closes the leak detector - called from the instrumented resource.
     */
    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

    //**************************************************
    // Interrogation methods.

    @Override
    public boolean isClosed() {
        return isClosed;
    }

}