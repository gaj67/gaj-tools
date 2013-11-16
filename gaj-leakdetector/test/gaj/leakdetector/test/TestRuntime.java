/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.leakdetector.test;

import gaj.leakdetector.closeable.CloseDetector;
import gaj.leakdetector.core.Detector;
import gaj.leakdetector.core.Manager;
import gaj.leakdetector.core.ManagerFactory;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class TestRuntime {

    // XXX: Run this with -Xbootclasspath/p:<path-to-gaj-leakdetector.jar>
    public static void main(String[] args) throws IOException {
        for (String arg : args) {
            @SuppressWarnings({ "unused", "resource" })
            InputStream stream = new FileInputStream(arg); // XXX: Deliberately don't close!
            try (InputStream stream2 = new FileInputStream(arg)) { }
            InputStream stream3 = new FileInputStream(arg);
            stream3.close();
        }
        final Date now = new Date();
        Manager<Closeable> manager = ManagerFactory.getManager(Closeable.class);
        List<Detector<? extends Closeable>> detectors = collect(manager.getDetectors());
        Collections.sort(detectors, new Comparator<Detector<? extends Closeable>>() {
            @Override
            public int compare(Detector<? extends Closeable> detector1, Detector<? extends Closeable> detector2) {
                return (int)(detector1.getDuration(now) - detector2.getDuration(now));
            }
        });
        int numExtant = 0, i = 0;
        for (Detector<? extends Closeable> detector : detectors) {
            char type;
            if (detector.isInstrumented()) {
                numExtant++;
                type = (detector instanceof CloseDetector)
                        ? ((CloseDetector)detector).isClosed() ? 'c' : 'o' : 'e';
            } else {
                type = 'x';
            }
            System.out.printf("[%c]%2d. %s[%d]:%s <- %s%n",
                    type, ++i, detector.getIdentifier(),
                    detector.getDuration(now), detector.getResourceClassName(), detector.getCallers());
        }
        System.out.printf("Detected resources: %d extant, %d destroyed%n", 
                numExtant, manager.numManaged() - numExtant);
    }

    private static List<Detector<? extends Closeable>> collect(Iterable<Detector<? extends Closeable>> detectors) {
        List<Detector<? extends Closeable>> list = new ArrayList<>();
        for (Detector<? extends Closeable> detector : detectors) list.add(detector);
        return list;
    }

}
