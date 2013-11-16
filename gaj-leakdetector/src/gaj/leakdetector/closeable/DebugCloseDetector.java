/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.leakdetector.closeable;

import java.io.Closeable;

public class DebugCloseDetector extends AbstractCloseDetector {

    public DebugCloseDetector(Closeable resource) {
        super(resource);
        if (System.err != null) {
            System.err.println("New leak detector[" + getIdentifier() + "]: " + getCallers());
        }
    }

    @Override
    public void close() {
        if (System.err != null) {
            System.err.println("Closing leak detector[" + getIdentifier() + "]: " + getCallers());
        }
        super.close();
    }

    @Override
    protected void finalize() throws Throwable {
        if (System.err != null) {
            if (isClosed())
                System.err.println("Finalising closed leak detector[" + getIdentifier() + "]: " + getCallers());
            else
                System.err.println("Finalising UNCLOSED leak detector[" + getIdentifier() + "]: " + getCallers());
        }
        super.finalize();
    }

}
