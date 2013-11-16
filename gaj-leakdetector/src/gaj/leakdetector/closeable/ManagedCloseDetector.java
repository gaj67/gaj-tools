/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.leakdetector.closeable;

import gaj.leakdetector.core.ManagerFactory;
import java.io.Closeable;

public class ManagedCloseDetector extends AbstractCloseDetector {

    public ManagedCloseDetector(Closeable resource) {
        super(resource);
        ManagerFactory.getManager(Closeable.class).add(this);
    }

    @Override
    protected void finalize() throws Throwable {
        ManagerFactory.getManager(Closeable.class).remove(this);
        super.finalize();
    }

}
