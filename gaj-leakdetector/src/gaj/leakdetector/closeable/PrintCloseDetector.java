/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.leakdetector.closeable;

import java.io.Closeable;


public class PrintCloseDetector extends AbstractCloseDetector {

    protected PrintCloseDetector(Closeable resource) {
        super(resource);
    }

    @Override
    protected void finalize() throws Throwable {
        if (!isClosed() && System.err != null) {
            System.err.println("Failed to close resource: " + getCallers());
        }
        super.finalize();
    }

}