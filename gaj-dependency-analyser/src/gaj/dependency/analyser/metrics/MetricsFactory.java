/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.analyser.metrics;

import java.util.Collection;
import java.util.Collections;


public abstract class MetricsFactory {

    private MetricsFactory() {}

	public static boolean useSAP() {
		return AbstractDependencyMetrics.useSAP;
	}

    public static boolean useADP() {
        return AbstractDependencyMetrics.useADP;
    }

	public static boolean useSDP() {
        return AbstractDependencyMetrics.useSDP;
	}

	public static boolean useDIP() {
        return AbstractDependencyMetrics.useDIP;
	}

	public static void setUseDIP(boolean flag) {
	    AbstractDependencyMetrics.useDIP = flag;
	}

    public static void setUseADP(boolean flag) {
        AbstractDependencyMetrics.useADP = flag;
    }

	public static void setUseSDP(boolean flag) {
	    AbstractDependencyMetrics.useSDP = flag;
	}

	public static void setUseSAP(boolean flag) {
	    AbstractDependencyMetrics.useSAP = flag;
	}

	/**
     * Creates a container for the metrics of a node that represents a collection of items.
     * <p/>Note: Dependencies between node collections may be measured most coarsely from node to node,
     * or most finely as bundles of dependencies between individual items in the respective collections, or
     * even somewhere in between (e.g. the number of items in an external collection that have a
     * dependency with one or more items in the given collection).
     * The exact degree of granularity is chosen by the caller.
     * 
     * @param node - The node representing a collection of items.
     * @param abstraction - The proportion of items in the collection that are abstract.
     * @param afferentCouplings - The number of dependencies (see the note above) from objects outside of the collection
     * to items inside of the collection.
     * @param efferentCouplings - The number of dependencies (see the note above) from items inside of the collection
     * to objects outside of the collection.
     * @param afferentCoverage - The proportion of items in the collection that have afferent dependencies.
     * @param efferentCoverage - The proportion of items in the collection that have efferent dependencies.
     * @param selfCoverage - The proportion of items in the collection that have afferent or efferent dependencies
     * with at least one other item also in the collection.
     * @return A summary of the metrics for the given node.
     */
    public static <T> NodeMetrics<T> newNodeCollectionMetrics(
            final T node, final boolean isInstantiable, 
            final double energy, final double abstraction, 
            final int afferentCouplings, final int efferentCouplings,
            final double afferentCoverage, final double efferentCoverage, final double selfCoverage) {
        return new AbstractNodeMetrics<T>() {
            @Override
            public T getReference() {
                return node;
            }

            @Override
            public int numEfferentCouplings() {
                return efferentCouplings;
            }

            @Override
            public int numAfferentCouplings() {
                return afferentCouplings;
            }

            @Override
            public double getAbstraction() {
                return abstraction;
            }

            @Override
            public double getAfferentCoverage() {
                return afferentCoverage;
            }

            @Override
            public double getEfferentCoverage() {
                return efferentCoverage;
            }

            @Override
            public double getSelfCoverage() {
                return selfCoverage;
            }

            @Override
            public boolean isInstantiable() {
                return isInstantiable;
            }

            @Override
            public double getEnergy() {
                return energy;
            }
        };
    }

    /**
     * Creates a container for the metrics of a node.
     * 
     * @param node - The node under consideration.
     * @param isInstantiable - A flag indicating whether or not the not is externally 'instantiable'.
     * @param abstraction - The degree of abstraction of the node, between 0 and 1 inclusive.
     * @param afferentCouplings - The number of dependencies from other nodes to the given node.
     * @param efferentCouplings - The number of dependencies from the given node to other nodes.
     * @return A summary of the metrics for the given node.
     */
    public static <T> NodeMetrics<T> newNodeMetrics(
            final T node, final boolean isInstantiable, 
            final double energy, final double abstraction,
            final int afferentCouplings, final int efferentCouplings) 
    {
        return new AbstractNodeMetrics<T>() {
            @Override
            public T getReference() {
                return node;
            }

            @Override
            public int numEfferentCouplings() {
                return efferentCouplings;
            }

            @Override
            public int numAfferentCouplings() {
                return afferentCouplings;
            }

            @Override
            public double getAbstraction() {
                return abstraction;
            }

            @Override
            public double getAfferentCoverage() {
                return (afferentCouplings == 0) ? 0 : 1;
            }

            @Override
            public double getEfferentCoverage() {
                return (efferentCouplings == 0) ? 0 : 1;
            }

            @Override
            public double getSelfCoverage() {
                return 1;
            }

            @Override
            public boolean isInstantiable() {
                return isInstantiable;
            }

            @Override
            public double getEnergy() {
                return energy;
            }
        };
    }

    public static <T> DependencyMetrics<T> newDependencyMetrics(final NodeMetrics<T> afferent, final NodeMetrics<T> efferent) {
        return new AbstractDependencyMetrics<T>() {
            @Override
            public NodeMetrics<T> getAfferentMetrics() {
                return afferent;
            }

            @Override
            public NodeMetrics<T> getEfferentMetrics() {
                return efferent;
            }
        };
    }

    public static <T> FamilyMetrics<T> newFamilyMetrics(final NodeMetrics<T> metrics,
            final Collection<DependencyMetrics<T>> familyMetrics) {
        final Collection<DependencyMetrics<T>> dependencies =
                Collections.unmodifiableCollection(familyMetrics);
        return new AbstractFamilyMetrics<T>() {
            @Override
            public NodeMetrics<T> getNodeMetrics() {
                return metrics;
            }

            @Override
            public Iterable<DependencyMetrics<T>> getDependencyMetrics() {
                return dependencies;
            }
        };
    }

}
