/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.analyser.analysis;

import gaj.classbinary.descriptors.MethodDescriptor;
import gaj.classbinary.descriptors.MethodType;
import gaj.classbinary.flags.AccessFlags;
import gaj.dependency.manager.classes.ClassDescription;
import gaj.dependency.manager.packages.ClassPackage;

public abstract class SummaryFactory {

    private SummaryFactory() {}

    public static double ratio(int numerator, int denominator, double limit) {
        return (denominator == 0) ? limit : 1.0 * numerator / denominator;
    }

    public static double ratio(double numerator, double denominator, double limit) {
        return (denominator == 0) ? limit : numerator / denominator;
    }

    public static boolean isDirectlyInstantiable(ClassDescription desc) {
        return desc.isVisible() && desc.isConcrete() && desc.isInstantiable();
    }

    public static ClassSummary summariseClass(final ClassDescription desc) {
        int visibleCount = 0, totVisibleCount = 0, totOverallCount = 0;
        for (MethodDescriptor method : desc.getMethods()) {
            if (method.getMethodType() == MethodType.Method) {
                totOverallCount++;
                final AccessFlags accessFlags = method.getAccessFlags();
                if (accessFlags.isPublic() || accessFlags.isProtected()) { // Method is visible.
                    totVisibleCount++;
                    if (accessFlags.isAbstract()) {
                        visibleCount++;
                    }
                }
            }
        }
        final double visibleAbstraction = ratio(visibleCount, totVisibleCount, 1);
        final double overallAbstraction = ratio(visibleCount, totOverallCount, 1);
        return new ClassSummary() {
            @Override
            public boolean isDirectlyInstantiable() {
                return SummaryFactory.isDirectlyInstantiable(desc);
            }

            @Override
            public double getOverallAbstraction() {
                return overallAbstraction;
            }

            @Override
            public double getVisibleAbstraction() {
                return visibleAbstraction;
            }

            @Override
            public double getEnergy() {
                return 1 - ratio(overallAbstraction, visibleAbstraction, 1);
            }
        };
    }

    public static PackageSummary summarisePackage(ClassPackage apackage) {
        int _numVisibleClasses = 0;
        double sumVisibleAbstraction = 0, sumOverallAbstraction = 0;
        for (ClassDescription desc : apackage.getClasses()) {
            ClassSummary summary = summariseClass(desc);
            sumOverallAbstraction += summary.getOverallAbstraction();
            if (desc.isVisible()) {
                _numVisibleClasses++;
                sumVisibleAbstraction += summary.getVisibleAbstraction();
            }
        }
        final int  numVisibleClasses = _numVisibleClasses;
        final double visibleAbstraction = ratio(sumVisibleAbstraction, numVisibleClasses, 1);
        final double overallAbstraction = ratio(sumOverallAbstraction, apackage.numClasses(), 1);
        return new PackageSummary() {
            @Override
            public int numVisibleClasses() {
                return numVisibleClasses;
            }

            @Override
            public double getVisibleAbstraction() {
                return visibleAbstraction;
            }

            @Override
            public double getOverallAbstraction() {
                return overallAbstraction;
            }

            @Override
            public double getEnergy() {
                return 1 - ratio(overallAbstraction, visibleAbstraction, 1);
            }
        };
    }
}
