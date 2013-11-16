/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.leakdetector.core;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * A leak detector to be instrumented on an underlying resource. 
 * @param <T>
 */
public abstract class AbstractDetector<T> implements Detector<T> {

    private final Date created = new Date();
    private final WeakReference<? extends T> resource;
    private final String identifier;
    private final List<Caller> callers;
    private final String resourceName;
    private final String instrumentName;

    protected AbstractDetector(T resource) {
        this.resource = new WeakReference<T>(resource);
        resourceName = resource.getClass().getName();
        identifier = toHex(this.hashCode(), resource.hashCode());
        callers = Collections.unmodifiableList(_getCallers());
        instrumentName = _getInstrumentedClassName();
    }

    //**************************************************
    // Interrogation methods.

    private String toHex(int hashCode1, int hashCode2) {
        StringBuilder buf = new StringBuilder();
        addHex(buf, hashCode1);
        buf.append(':');
        addHex(buf, hashCode2);
        return buf.toString();
    }

    private static void addHex(StringBuilder buf, int hashCode) {
        int flag = 0xF0000000;
        for (int i = 7; i >= 0; i--) {
            int nibble = 0xF & ((hashCode & flag) >> (i*8));
            buf.append((char)(nibble + ((nibble < 10) ? '0' : ('A'-10))));
            flag >>= 8;
        }
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public List<Caller> getCallers() {
        return callers;
    }

    private List<Caller> _getCallers() {
        final LinkedList<Caller> callers = new LinkedList<>();
        for (final StackTraceElement caller : new Exception().getStackTrace()) {
            final String className = caller.getClassName();
            final String methodName = caller.getMethodName();
            final int lineNumber = caller.getLineNumber();
            callers.addFirst(new Caller() {
                @Override
                public String getClassName() {
                    return className;
                }

                @Override
                public String getMethodName() {
                    return methodName;
                }

                @Override
                public int getLineNumber() {
                    return lineNumber;
                }

                @Override
                public String toString() {
                    return className + "/" + methodName + "/" + lineNumber;
                }
            });
        }
        return callers;
    }

    public String _getInstrumentedClassName() {
        final String myClass = getClass().getName();
        String prevName = null, curName = null;
        for (Caller caller : callers) {
            prevName = curName;
            curName = caller.getClassName();
            if (myClass.equals(curName)) break;
        }
        return prevName;
    }

    @Override
    public String getInstrumentedClassName() {
        return instrumentName;
    }

    @Override
    public String getResourceClassName() {
        return resourceName;
    }

    @Override
    public T getResource() {
        return resource.get();
    }

    @Override
    public long getDuration(Date date) {
        return date.getTime() - created.getTime();
    }

    @Override
    public boolean isInstrumented() {
        return resource != null && resource.get() != null;
    }
}