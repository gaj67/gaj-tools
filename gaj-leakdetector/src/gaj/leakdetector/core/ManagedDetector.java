/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.leakdetector.core;


public class ManagedDetector<T> extends AbstractDetector<T> {

    private final Manager<T> manager;
    
    public ManagedDetector(T resource) {
        super(resource);
        @SuppressWarnings("unchecked")
        Class<? extends T> klass = (Class<? extends T>)resource.getClass();
        manager = ManagerFactory.getManager(klass);
        manager.add(this);
    }

    @Override
    protected void finalize() throws Throwable {
        manager.remove(this);
        super.finalize();
    }

}
