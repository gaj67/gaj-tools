/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.leakdetector.closeable;

import java.io.Closeable;
import java.io.IOException;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * Instruments user-specified Closeable classes with a user-specified CloseDetector.
 */
public abstract class CloseableInstrumenter {

    private static final String CLOSE_DETECTOR_FIELD = "__closeDetector";

    public static void instrument(Class<? extends Closeable> klass, Class<? extends CloseDetector> detector) throws NotFoundException, CannotCompileException, IOException {
        ClassPool pool = ClassPool.getDefault();
        CtClass _class = pool.get(klass.getName());
        boolean modified = false;
        try {
            @SuppressWarnings("unused")
            CtField field = _class.getField(CLOSE_DETECTOR_FIELD);
            //System.out.print("has detector...");
        } catch (javassist.NotFoundException e) {
            String fieldSource = "protected " + CloseDetector.class.getName() + " " + CLOSE_DETECTOR_FIELD + " = new " + detector.getName() + "(this);";
            _class.addField(CtField.make(fieldSource, _class));
            System.out.print("detector...");
            modified = true;
        }
        for (CtMethod method : _class.getDeclaredMethods()) {
            if (method.getParameterTypes().length == 0) {
                final String name = method.getName();
                if (name.equals("close")) {
                    method.insertBefore(CLOSE_DETECTOR_FIELD + ".close();");
                    System.out.print("close()...");
                    modified = true;
                } else if (name.equals("finalize")) {
                    method.insertBefore(CLOSE_DETECTOR_FIELD + ".finalize();");                
                    System.out.print("finalize()...");
                    modified = true;
                }
            }
        }
        if (modified) {
            _class.writeFile("bin");
        } else {
            System.out.print("not instrumented...");
        }
    }

    private static final ClassLoader classLoader = CloseableInstrumenter.class.getClassLoader();

    public static void main(String[] args) throws ClassNotFoundException, NotFoundException, CannotCompileException, IOException {
        if (args == null || args.length < 2) {
            System.out.println("Arguments: <leak-detector-class> <instrumented-class>+");
        } else {
            System.out.printf("Attempting to load detector class: %s ...", args[0]);
            @SuppressWarnings("unchecked")
            Class<? extends AbstractCloseDetector> detector = (Class<? extends AbstractCloseDetector>)classLoader.loadClass(args[0]);
            System.out.println(" Done!");
            final int len = args.length;
            for (int i = 1; i < len; i++) {
                if (args[i].startsWith("//")) continue;
                System.out.printf("Attempting to instrument class: %s ...", args[i]);
                System.out.flush();
                @SuppressWarnings("unchecked")
                Class<? extends Closeable> klass = (Class<? extends Closeable>)classLoader.loadClass(args[i]);
                instrument(klass, detector);
                System.out.println(" Done!");
            }
        }
    }
}
