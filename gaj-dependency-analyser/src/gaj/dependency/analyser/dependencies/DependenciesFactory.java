/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.analyser.dependencies;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Manages the creation and manipulation of dependencies between generic objects.
 */
public abstract class DependenciesFactory {

    private DependenciesFactory() {}

    public static <T> ModifiableDependencies<T> newDependencies() {
        return new ModifiableDependencies<T>() {
            final Map<T,Collection<T>> dependencies = new HashMap<>();

            @Override
            public boolean isEmpty() {
                return dependencies.isEmpty();
            }

            @Override
            public void addNode(T node) {
                if (!dependencies.containsKey(node)) {
                    dependencies.put(node, null);
                }
            }

            @Override
            public void removeNode(T node) {
                dependencies.remove(node);
                for (Collection<T> secondaries : dependencies.values()) {
                    if (secondaries != null) {
                        secondaries.remove(node);
                    }
                }
            }

            @Override
            public boolean hasNode(T node) {
                return dependencies.containsKey(node);
            }

            @Override
            public Collection<T> getNodes() {
                return new LinkedList<>(dependencies.keySet());
            }

            @Override
            public void addDependency(T primary, T secondary) {
                if (secondary == null) {
                    throw new IllegalArgumentException("Bah!");
                }
                addNode(secondary);
                Collection<T> secondaries = dependencies.get(primary);
                if (secondaries == null) {
                    dependencies.put(primary, secondaries = new LinkedList<>());
                }
                secondaries.add(secondary);
            }

            @Override
            public void removeDependency(T primary, T secondary) {
                Collection<T> secondaries = dependencies.get(primary);
                if (secondaries != null) {
                    secondaries.remove(secondary);
                }
            }

            @Override
            public Collection<T> getDependencies(T node) {
                if (!dependencies.containsKey(node)) {
                    return null;
                }
                Collection<T> secondaries = dependencies.get(node);
                return (secondaries == null) ? Collections.<T>emptyList() : new LinkedList<T>(secondaries);
            }

            @Override
            public T getDependency(T node) {
                Collection<T> secondaries = dependencies.get(node);
                return (secondaries != null && !secondaries.isEmpty())
                        ? secondaries.iterator().next() : null;
            }

            @Override
            public boolean hasDependencies(T node) {
                Collection<T> secondaries = dependencies.get(node);
                return (secondaries != null && !secondaries.isEmpty());
            }

            @Override
            public String toString() {
                StringBuilder buf = new StringBuilder();
                String prefix = "[ ";
                for (T primary : getNodes()) {
                    Collection<T> secondaries = getDependencies(primary);
                    if (secondaries.isEmpty()) {
                        buf.append(prefix);
                        buf.append(primary);
                    } else {
                        for (T secondary : secondaries) {
                            buf.append(prefix);
                            buf.append(primary);
                            buf.append("->");
                            buf.append(secondary);
                            prefix = ", ";
                        }
                    }
                }
                buf.append(" ]");
                return buf.toString();
            }

            @Override
            public int numNodes() {
                return dependencies.size();
            }
        };
    }

    public static <T> ModifiableDependencies<T> copyDependencies(Dependencies<T> dependencies) {
        ModifiableDependencies<T> clonedDependencies = newDependencies();
        for (T primary : dependencies.getNodes()) {
            Collection<T> secondaries = dependencies.getDependencies(primary);
            if (secondaries.isEmpty()) {
                clonedDependencies.addNode(primary);
            } else {
                for (T secondary : secondaries) {
                    clonedDependencies.addDependency(primary, secondary);
                }
            }
        }
        return clonedDependencies;
    }

    public static <T> ModifiableDependencies<T> invertDependencies(Dependencies<T> dependencies) {
        ModifiableDependencies<T> inverseDependencies = DependenciesFactory.newDependencies();
        for (T primary : dependencies.getNodes()) {
            inverseDependencies.addNode(primary);
            for (T secondary : dependencies.getDependencies(primary)) {
                inverseDependencies.addDependency(secondary, primary);
            }
        }
        return inverseDependencies;
    }

    /**
     * Performs a topological sorting of nodes using the given dependencies.
     * <p/><b>Note:</b> If the sorting is performed on the out-set dependencies {X -> {Y}},
     * then node Y will appear before node X in topological ordering.
     * Conversely, if the sorting is performed on the in-set dependencies {Y <- {X}},
     * then node X will appear before node Y in topological ordering.
     * 
     * @param dependencies - A mapping between nodes, usually giving either the in-set or
     * out-set of each node.
     * @param ignoreCycles - A Boolean indicating whether (true) or not (false) to ignore
     * the presence of cycles during sorting.
     * @return The topologically sorted collection of nodes, or a value of null if a cycle is
     * detected amongst the directed dependencies and ignoreCycles is false.
     */
    public static <T> Collection<T> sort(Dependencies<T> dependencies, boolean ignoreCycles) {
        LinkedList<T> unsorted = new LinkedList<>();
        initialiseQueue(unsorted, dependencies);
        ModifiableDependencies<T> stdDependencies = copyDependencies(dependencies);
        ModifiableDependencies<T> invDependencies = invertDependencies(dependencies);
        LinkedList<T> sorted = new LinkedList<>();
        while (true) {
            processQueue(unsorted, sorted, stdDependencies, invDependencies);
            T cycleNode = getCyclicNode(stdDependencies);
            if (cycleNode == null) {
                return sorted;
            }
            if (ignoreCycles) {
                // Break cycle.
                T secondary = stdDependencies.getDependency(cycleNode);
                stdDependencies.removeDependency(cycleNode, secondary);
                invDependencies.removeDependency(secondary, cycleNode);
                unsorted.add(cycleNode);
            } else {
                return null;
            }
        }
    }

    /**
     * Initialises the queue with those nodes having no directed dependencies in
     * the implied direction. I.e. for out-sets (or in-sets),
     * those nodes with no outward (or inward) dependencies
     * 
     * @param queue - A collection of nodes to be processed.
     * @param dependencies - A mapping of each node to its directed dependencies.
     * For out-sets, this is {X -> {Y}}, whereas for in-sets it is {Y <- {X}}.
     */
    private static <T> void initialiseQueue(Collection<T> queue, Dependencies<T> dependencies) {
        for (T primary : dependencies.getNodes()) {
            if (!dependencies.hasDependencies(primary)) {
                queue.add(primary);
            }
        }
    }

    // XXX: Destroys the dependencies!
    private static <T> void processQueue(Collection<T> queue, Collection<T> sorted,
            ModifiableDependencies<T> dependencies, ModifiableDependencies<T> inverseDependencies) {
        while (!queue.isEmpty()) {
            Iterator<T> qiter = queue.iterator();
            T node = qiter.next();
            qiter.remove();
            if (!dependencies.hasDependencies(node)) { // Allow for generalised case with cycles.
                sorted.add(node);
            }
            // Remove inverse dependencies.
            for (T inode : inverseDependencies.getDependencies(node)) {
                // Break dependency.
                inverseDependencies.removeDependency(node, inode);
                dependencies.removeDependency(inode, node);
                if (!dependencies.hasDependencies(inode)) {
                    // Adjusted inverse node now has no dependencies in the implicit direction.
                    queue.add(inode);
                }
            }
        }
    }

    /**
     * Finds a node in a cycle with the smallest, non-zero number of remaining dependencies.
     * 
     * @param dependencies - A mapping of each node to its directed dependencies.
     * For out-sets, this is {X -> {Y}}, whereas for in-sets it is {Y <- {X}}.
     * @return An arbitrary node that is part of a cycle of dependencies,
     * or a value of null if there are no cycles.
     */
    private static <T> T getCyclicNode(Dependencies<T> dependencies) {
        T cycleNode = null;
        int minNumDependencies = Integer.MAX_VALUE;
        for (T primary : dependencies.getNodes()) {
            int numDependencies = dependencies.getDependencies(primary).size();
            if (numDependencies > 0 && numDependencies < minNumDependencies) {
                // Dependencies remain - cycle detected!!
                minNumDependencies = numDependencies;
                cycleNode = primary;
            }
        }
        return cycleNode;
    }

    private static <T> LinkedList<CycleNode<T>> getCyclicNodes(Dependencies<T> dependencies, Dependencies<T> invDependencies) {
        LinkedList<CycleNode<T>> nodes = new LinkedList<>();
        for (final T primary : dependencies.getNodes()) {
            final int numCycles = Math.max(
                    dependencies.getDependencies(primary).size(),
                    invDependencies.getDependencies(primary).size());
            if (numCycles > 0) {
                nodes.add(new CycleNode<T>() {
                    @Override
                    public T getNode() {
                        return primary;
                    }

                    @Override
                    public int getCount() {
                        return numCycles;
                    }
                });
            }
        }
        return nodes;
    }

    private static interface CycleNode<T> {
        T getNode();
        int getCount();
    }

    // XXX: Removes dependencies but not nodes!
    private static <T> void removeNonCycles(ModifiableDependencies<T> stdDependencies, ModifiableDependencies<T> invDependencies) {
        // Process dependencies.
        Collection<T> unsorted = new LinkedList<>();
        initialiseQueue(unsorted, stdDependencies);
        Collection<T> sorted = new LinkedList<>();
        processQueue(unsorted, sorted, stdDependencies, invDependencies);
        // Process inverse dependencies.
        initialiseQueue(unsorted, invDependencies);
        processQueue(unsorted, sorted, invDependencies, stdDependencies);
    }

    /**
     * Computes a reduced dependency structure containing only nodes that are involved in one or more cycles,
     * along with the dependencies between these nodes.
     * <p/>Note: Not all of the dependencies to or from a cyclic node are necessarily involved in a cycle!
     * 
     * @param dependencies - A mapping of each node to its directed dependencies.
     * @return The reduced (but possibly super-) set of cyclic dependencies.
     */
    public static <T> ModifiableDependencies<T> getCyclicDependencies(Dependencies<T> dependencies) {
        ModifiableDependencies<T> stdDependencies = copyDependencies(dependencies);
        ModifiableDependencies<T> invDependencies = invertDependencies(dependencies);
        removeNonCycles(stdDependencies, invDependencies);
        for (T node : stdDependencies.getNodes()) {
            if (!stdDependencies.hasDependencies(node))
                stdDependencies.removeNode(node);
        }
        return stdDependencies;
    }

    public static <T> Map<T,Integer> getCycleCounts(Dependencies<T> dependencies) {
        ModifiableDependencies<T> stdDependencies = copyDependencies(dependencies);
        ModifiableDependencies<T> invDependencies = invertDependencies(dependencies);
        removeNonCycles(stdDependencies, invDependencies);
        Map<T,Integer> counts = new HashMap<>();
        for (T node : stdDependencies.getNodes()) {
            int numNodeCycles = Math.max(stdDependencies.getDependencies(node).size(),
                    invDependencies.getDependencies(node).size());
            if (numNodeCycles > 0) {
                counts.put(node, numNodeCycles);
            }
        }
        return counts;
    }

    /**
     * Determines the (possibly empty) collection of cycles that exist
     * amongst the directed dependencies.
     * 
     * @param dependencies - The direct dependencies between primary and secondary objects,
     * e.g. afferent -> efferent.
     * @return The collection of cycles.
     */
    public static <T> Collection<Cycle<T>> getCycles(Dependencies<T> dependencies) {
        ModifiableDependencies<T> stdDependencies = copyDependencies(dependencies);
        ModifiableDependencies<T> invDependencies = invertDependencies(dependencies);
        removeNonCycles(stdDependencies, invDependencies);
        Collection<Cycle<T>> cycles = new HashSet<>();
        LinkedList<CycleNode<T>> cycleNodes = getCyclicNodes(stdDependencies, invDependencies);
        while (!cycleNodes.isEmpty()) {
            CycleNode<T> cycleNode = cycleNodes.removeFirst();
            cycles.addAll(getNodeCycles(stdDependencies, cycleNode, true));
            //stdDependencies.removeNode(cycleNode.getNode());
            //invDependencies.removeNode(cycleNode.getNode());
        }
        return cycles;
    }

    private static <T> Collection<Cycle<T>> getNodeCycles(Dependencies<T> dependencies, CycleNode<T> cnode, boolean addAll) {
        int numCycles = cnode.getCount();
        LinkedList<LinkedList<T>> searchQueue = new LinkedList<>();
        {
            LinkedList<T> searchPath = new LinkedList<>();
            searchPath.add(cnode.getNode());
            searchQueue.add(searchPath);
        }
        Collection<Cycle<T>> cycles = new HashSet<>();
        queue: while (!searchQueue.isEmpty()) {
            final LinkedList<T> searchPath = searchQueue.removeFirst();
            for (T dnode : dependencies.getDependencies(searchPath.getLast())) {
                int idx = searchPath.indexOf(dnode);
                if (idx == 0) { // Complete cycle detected.
                    cycles.add(CycleFactory.newCycle(searchPath));
                    numCycles--;
                    if (numCycles <= 0) break queue;
                } else if (idx > 0) { // Sub-cycle detected.
                    if (addAll) {
                        List<T> cycle = new LinkedList<>();
                        Iterator<T> iter = searchPath.iterator();
                        for (int i = 0; i < idx; i++) iter.next();
                        final int len = searchPath.size();
                        while (idx++ < len) cycle.add(iter.next());
                        cycles.add(CycleFactory.newCycle(cycle));
                    }
                } else { // Expand search further.
                    LinkedList<T> newSearchPath = new LinkedList<>(searchPath);
                    newSearchPath.add(dnode);
                    searchQueue.add(newSearchPath);
                }
            }
        }
        return cycles;
    }

}
