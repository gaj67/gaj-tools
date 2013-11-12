/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.analyser.main;

import gaj.classbinary.descriptors.ClassNameSpace;
import gaj.dependency.analyser.dependencies.Dependencies;
import gaj.dependency.analyser.dependencies.DependenciesFactory;
import gaj.dependency.analyser.dependencies.PackageDependenciesFactory;
import gaj.dependency.analyser.project.ProjectDependencies;
import gaj.dependency.analyser.project.ProjectDependenciesFactory;
import gaj.dependency.manager.classes.ClassDescription;
import gaj.dependency.manager.components.ComponentFactory;
import gaj.dependency.manager.components.ComponentFactory.DuplicateClassWarning;
import gaj.dependency.manager.packages.ClassPackage;
import gaj.dependency.manager.projects.GroupProject;
import gaj.dependency.manager.projects.LoadableProject;
import gaj.dependency.manager.projects.ProjectFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Creates .dot graph files for various inter-package and intra-package dependencies.
 * <p/>Can be converted ito image formats using Graphviz, e.g. <tt>dot -Tpng -O inter-pkg-cyc-graph.dot</tt>.
 */
public class GraphPackages {

    /**
     * Entry point for the run-time task.
     * 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        ComponentFactory.setDuplicateClassWarning(DuplicateClassWarning.ignore);
        if (args == null || args.length == 0) {
            analyseProject(new File("."), false);
        } else {
            for (String projectPath : args) {
                if (projectPath.startsWith("#")) {
                    continue; // Ignore comments.
                }
                analyseProject(new File(projectPath), false);
            }
        }
    }

    /**
     * Entry point for the Ant task.
     * 
     * @throws IOException
     */
    public void execute() throws IOException {
        ComponentFactory.setDuplicateClassWarning(DuplicateClassWarning.ignore);
        analyseProject(new File("."), true);
    }

    private static void analyseProject(File projectPath, boolean fromBuild) throws IOException {
        System.out.printf("Analysing dependencies for project: %s ...%n", projectPath.getCanonicalPath());
        GroupProject project = loadProject(projectPath, fromBuild);
        File reportPath = new File(projectPath, "logs/graphs");
        if (reportPath.exists() || reportPath.mkdirs()) {
            ProjectDependencies projectDependencies = ProjectDependenciesFactory.newProjectDependencies(project);
            Dependencies<ClassPackage> packageDependencies = projectDependencies.getInterPackageEfferentDependencies();
            Dependencies<ClassPackage> cyclicDependencies = DependenciesFactory.getCyclicDependencies(packageDependencies);
            graphInterPackageDependencies(
                    project.getSourceComponent().getPackages(),
                    packageDependencies,
                    cyclicDependencies, 
                    reportPath);
            if (!cyclicDependencies.isEmpty()) {
                graphInterPackageCycles(
                        project.getSourceComponent().getPackages(), 
                        cyclicDependencies, 
                        reportPath);
            }
            graphIntraPackageCycles(
                    project.getSourceComponent().getPackages(), 
                    projectDependencies,
                    reportPath);
            System.out.println("Done!");
        } else {
            System.err.printf("Failed to create non-existant report path: %s%n", reportPath);
        }
    }

    private static GroupProject loadProject(File projectPath, boolean fromBuild) throws IOException {
        LoadableProject project = ProjectFactory.newProject(projectPath, fromBuild);
        project.load();
        return project;
    }

    private static void graphInterPackageDependencies(Iterable<ClassPackage> packages, Dependencies<ClassPackage> packageDependencies, Dependencies<ClassPackage> cyclicDependencies, File reportPath) throws FileNotFoundException {
        File graphFile = new File(reportPath, "inter-pkg-dep-graph.dot");
        System.out.printf("Creating inter-package dependency graph: %s%n", graphFile);
        try (PrintStream buf = new PrintStream(graphFile)) {
            buf.append("digraph g {\n");
            Map<String,List<ClassPackage>> allGroupedPackages = new HashMap<>();
            for (ClassPackage apackage : packageDependencies.getNodes()) {
                String groupedPackageName = ClassNameSpace.getPackageName(apackage.getPackageName());
                List<ClassPackage> groupedPackages = allGroupedPackages.get(groupedPackageName);
                if (groupedPackages == null)
                    allGroupedPackages.put(groupedPackageName, groupedPackages = new LinkedList<>());
                groupedPackages.add(apackage);
            }
            for (Entry<String,List<ClassPackage>> entry : allGroupedPackages.entrySet()) {
                buf.append("subgraph cluster_");
                buf.append(getNodeName(entry.getKey()));
                buf.append(" {\n");
                buf.append("color=black;\n");
                buf.append("label=\"");
                buf.append(getNodeLabel(entry.getKey()));
                buf.append("\";\n");
                for (ClassPackage apackage : entry.getValue()) {
                    final boolean isCyclic = cyclicDependencies.hasNode(apackage);
                    buf.append(getNodeName(apackage.getPackageName()));
                    buf.append(" [label=\"");
                    buf.append(getVerySimpleName(apackage.getPackageName()));
                    buf.append("\"");
                    if (isCyclic) buf.append(",shape=box,color=red");
                    buf.append("];\n");
                }
                buf.append("}\n");
            }
            for (ClassPackage afferent : packages) {
                Collection<ClassPackage> efferents = packageDependencies.getDependencies(afferent);
                if (efferents.isEmpty()) continue;
                for (ClassPackage efferent : efferents) {
                    Dependencies<ClassDescription> cdependencies = PackageDependenciesFactory.getInterPackageClassDependencies(afferent, efferent);
                    int efferentCoupling = 0;
                    for (ClassDescription desc : cdependencies.getNodes())
                        efferentCoupling += cdependencies.getDependencies(desc).size();
                    buf.append(getNodeName(afferent.getPackageName()));
                    buf.append(" -> ");
                    buf.append(getNodeName(efferent.getPackageName()));
                    buf.append(" [label=\"");
                    buf.append(new Integer(efferentCoupling).toString());
                    buf.append(getVerySimpleName(afferent.getPackageName()).charAt(0));
                    buf.append(getVerySimpleName(efferent.getPackageName()).charAt(0));
                    buf.append("\", color=grey];\n");
                }
            }
            buf.append("}\n");
        }
    }

    private static String getNodeName(String name) {
        return name.replace('.', '_').replace("$", "__");
    }

    private static String getNodeLabel(String name) {
        return name.replace('$', '.');
    }

    private static String getSimpleName(String packageName) {
        int idx = packageName.lastIndexOf('.');
        return (idx < 0) ? packageName : packageName.substring(packageName.lastIndexOf('.', idx-1)+1);
    }

    private static String getVerySimpleName(String packageName) {
        int idx = packageName.lastIndexOf('.');
        return (idx < 0) ? packageName : packageName.substring(idx+1);
    }

    private static void graphInterPackageCycles(Iterable<ClassPackage> packages, Dependencies<ClassPackage> dependencies, File reportPath) throws FileNotFoundException {
        File graphFile = new File(reportPath, "inter-pkg-cyc-graph.dot");
        System.out.printf("Creating inter-package cyclic dependency graph: %s%n", graphFile);
        try (PrintStream buf = new PrintStream(graphFile)) {
            buf.append("digraph g {\n");
            //buf.append("node [shape=plaintext]\n");
            for (ClassPackage apackage : dependencies.getNodes()) {
                buf.append(getNodeName(apackage.getPackageName()));
                buf.append(" [label=\"");
                buf.append(getNodeLabel(getSimpleName(apackage.getPackageName())));
                buf.append("\"];\n");
            }
            for (ClassPackage afferent : packages) {
                Collection<ClassPackage> efferents = dependencies.getDependencies(afferent);
                if (efferents == null || efferents.isEmpty()) continue;
                for (ClassPackage efferent : efferents) {
                    Dependencies<ClassDescription> cdependencies = PackageDependenciesFactory.getInterPackageClassDependencies(afferent, efferent);
                    int efferentCoupling = 0;
                    for (ClassDescription desc : cdependencies.getNodes())
                        efferentCoupling += cdependencies.getDependencies(desc).size();
                    buf.append(getNodeName(afferent.getPackageName()));
                    buf.append(" -> ");
                    buf.append(getNodeName(efferent.getPackageName()));
                    buf.append(" [label=\"");
                    buf.append(new Integer(efferentCoupling).toString());
                    buf.append("\"];\n");
                }
            }
            buf.append("}\n");
        }
    }

    private static void graphIntraPackageCycles(Iterable<ClassPackage> packages, ProjectDependencies projectDependencies, File reportPath) {
        for (ClassPackage apackage : packages) {
            Dependencies<ClassDescription> dependencies = DependenciesFactory.getCyclicDependencies(projectDependencies.getIntraPackageEfferentDependencies(apackage));
            if (dependencies.isEmpty()) continue;
            String simpleName = getNodeName(getSimpleName(apackage.getPackageName()));
            File graphFile = new File(reportPath, simpleName + "-cyc-graph.dot");
            System.out.printf("Creating intra-package cyclic dependency graph: %s%n", graphFile);
            try (PrintStream buf = new PrintStream(graphFile)) {
                graphIntraPackageCycles(dependencies, buf);
            } catch (FileNotFoundException e) {
                System.err.printf("Failed to create report for package: %s%n", apackage.getPackageName());
            }
        }
    }

    private static void graphIntraPackageCycles(Dependencies<ClassDescription> dependencies, PrintStream buf) {
        buf.append("digraph g {\n");
        for (ClassDescription desc : dependencies.getNodes()) {
            buf.append(getNodeName(desc.getClassName()));
            buf.append(" [label=\"");
            buf.append(getNodeLabel(getVerySimpleName(desc.getClassName())));
            buf.append("\"];\n");
        }
        for (ClassDescription afferent : dependencies.getNodes()) {
            Collection<ClassDescription> efferents = dependencies.getDependencies(afferent);
            for (ClassDescription efferent : efferents) {
                buf.append(getNodeName(afferent.getClassName()));
                buf.append(" -> ");
                buf.append(getNodeName(efferent.getClassName()));
                buf.append("\n");
            }
        }
        buf.append("}\n");
    }
}
