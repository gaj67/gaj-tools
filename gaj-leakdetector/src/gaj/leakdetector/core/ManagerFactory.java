/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.leakdetector.core;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Encapsulates, for each type of resource class, a singleton instance of a leak detection manager.
 */
public abstract class ManagerFactory {

    private static final ConcurrentHashMap<Class<?>, Manager<?>> map = new ConcurrentHashMap<>();

    /**
     * Obtains a new manager instance to manage leak detectors for the given class of resources.
     * 
     * @param resourceClass - The class of the resource to be managed.
     * @return A manager instance bound to the specified resource class.
     */
    public static <T> Manager<T> newManager(Class<? extends T> resourceClass) {
        return new ManagerImpl<T>();
    }

    /**
     * Obtains a singleton manager instance to manage leak detectors 
     * for the given (preferably high-level) class of resources.
     * 
     * @param resourceClass - The class of the resource.
     * @return A manager instance bound to the specified resource class.
     */
    public static <T> Manager<T> getManager(Class<? extends T> resourceClass) {
        @SuppressWarnings("unchecked")
        Manager<T> manager = (Manager<T>)map.get(resourceClass);
        if (manager == null) {
            @SuppressWarnings("unchecked")
            Manager<T> _manager = (Manager<T>)map.putIfAbsent(resourceClass, manager = new ManagerImpl<T>());
            if (_manager != null) manager = _manager;
        }
        return manager;
    }
}
