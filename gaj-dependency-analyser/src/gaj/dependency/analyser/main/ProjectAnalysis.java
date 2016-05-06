/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.analyser.main;

import static gaj.dependency.analyser.analysis.SummaryFactory.ratio;
import gaj.dependency.analyser.analysis.PackageSummary;
import gaj.dependency.analyser.analysis.SummaryFactory;
import gaj.dependency.analyser.dependencies.Cycle;
import gaj.dependency.analyser.dependencies.Dependencies;
import gaj.dependency.analyser.dependencies.DependenciesFactory;
import gaj.dependency.analyser.dependencies.PackageDependenciesFactory;
import gaj.dependency.analyser.metrics.DependencyMetrics;
import gaj.dependency.analyser.metrics.FamilyMetrics;
import gaj.dependency.analyser.metrics.MetricsFactory;
import gaj.dependency.analyser.metrics.NodeMetrics;
import gaj.dependency.analyser.project.ProjectDependencies;
import gaj.dependency.analyser.project.ProjectDependenciesFactory;
import gaj.dependency.manager.classes.ClassDescription;
import gaj.dependency.manager.components.ClassesComponent;
import gaj.dependency.manager.components.ComponentFactory;
import gaj.dependency.manager.components.ComponentFactory.DuplicateClassWarning;
import gaj.dependency.manager.dependencies.GroupDependency;
import gaj.dependency.manager.groups.ComponentGroup;
import gaj.dependency.manager.packages.ClassPackage;
import gaj.dependency.manager.projects.GroupProject;
import gaj.dependency.manager.projects.LoadableProject;
import gaj.dependency.manager.projects.ProjectFactory;
import gaj.iterators.impl.Collections;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ProjectAnalysis {

    private static final boolean CHECK_ADP = Boolean.parseBoolean(System.getProperty("checkADP", "false"));
	private static final boolean CHECK_SDP = Boolean.parseBoolean(System.getProperty("checkSDP", "false"));
    private static final boolean CHECK_SAP = Boolean.parseBoolean(System.getProperty("checkSAP", "false"));
    private static final boolean CHECK_DIP = Boolean.parseBoolean(System.getProperty("checkDIP", "true"));
    private static final boolean REPORT_ADP = Boolean.parseBoolean(System.getProperty("reportADP", "false"));
    private static final boolean REPORT_SDP = Boolean.parseBoolean(System.getProperty("reportSDP", "false"));
    private static final boolean REPORT_SAP = Boolean.parseBoolean(System.getProperty("reportSAP", "false"));
    private static final boolean REPORT_DIP = Boolean.parseBoolean(System.getProperty("reportDIP", "false"));

    /**
     * Entry point for the run-time task.
     * 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        //DuplicateClassWarning duplicationWarning = ComponentFactory.getDuplicateClassWarning();
        ComponentFactory.setDuplicateClassWarning(DuplicateClassWarning.warn);
        MetricsFactory.setUseSDP(CHECK_SDP);
        MetricsFactory.setUseSAP(CHECK_SAP);
        MetricsFactory.setUseADP(CHECK_ADP);
        MetricsFactory.setUseDIP(CHECK_DIP);
        if (args == null || args.length == 0) {
            analyseProject(Paths.get("."), false);
        } else {
            for (String projectPath : args) {
                if (projectPath.startsWith("#")) {
                    continue; // Ignore comments.
                }
                analyseProject(Paths.get(projectPath), false);
            }
        }
        //ComponentFactory.setDuplicateClassWarning(duplicationWarning);
    }

    /**
     * Entry point for the Ant task.
     * 
     * @throws IOException
     */
    public void execute() throws IOException {
        //DuplicateClassWarning duplicationWarning = ComponentFactory.getDuplicateClassWarning();
        ComponentFactory.setDuplicateClassWarning(DuplicateClassWarning.warn);
        MetricsFactory.setUseSDP(CHECK_SDP);
        MetricsFactory.setUseSAP(CHECK_SAP);
        MetricsFactory.setUseADP(CHECK_ADP);
        MetricsFactory.setUseDIP(CHECK_DIP);
        analyseProject(Paths.get("."), true);
        //ComponentFactory.setDuplicateClassWarning(duplicationWarning);
    }

    private static void analyseProject(Path projectPath, boolean fromBuild) throws IOException {
        GroupProject project = loadProject(projectPath, fromBuild);
        ProjectDependencies projDependencies = ProjectDependenciesFactory.newProjectDependencies(project);
        checkDanglingDependencies(project);
        checkIntraPackageCycles(projDependencies);
        checkInterPackageCycles(projDependencies);
        checkIntraPackageMetrics(projDependencies);
        checkInterPackageMetrics(projDependencies);
    }

    private static String pluralise(int count, String prefix, String singularSuffix, String pluralSuffix) {
        return String.format("%d %s%s", count, prefix, (count == 1) ? singularSuffix : pluralSuffix);
    }

    private static GroupProject loadProject(Path projectPath, boolean fromBuild) throws IOException {
        LoadableProject project = ProjectFactory.newProject(projectPath, fromBuild);
        System.out.printf("Loading project \"%s\"...%n", project.getName());
        project.load();
        final ComponentGroup group = project.getGroup();
        System.out.printf("Loaded %s:%n", pluralise(group.numComponents(), "component", "", "s"));
        List<ClassesComponent> components = Collections.asList(group.getComponents());
        Collections.sort(
        		components,
        		new Comparator<ClassesComponent>() {
					@Override
					public int compare(ClassesComponent cmp1, ClassesComponent cmp2) {
						return cmp1.getComponentName().compareTo(cmp2.getComponentName());
					}
        		});
        Map<String,String> sharedPackages = new HashMap<>();
        for (ClassesComponent component : components) {
            System.out.printf("+ %s has %s:%n", component.getComponentName(), pluralise(component.numPackages(), "package", "", "s"));
            for (ClassPackage apackage : component.getPackages()) {
                String sharedComponentName = sharedPackages.put(apackage.getPackageName(), apackage.getComponentName());
                if (sharedComponentName != null) {
                    System.out.printf("*** %s (%s) [shared with component %s]%n",
                            apackage, pluralise(apackage.numClasses(), "class", "", "es"), sharedComponentName);
                } else {
                    System.out.printf("  * %s (%s)%n", apackage, pluralise(apackage.numClasses(), "class", "", "es"));
                }
            }
        }
        return project;
    }

    private static void checkDanglingDependencies(GroupProject project) {
        System.out.println("=============================================================");
        System.out.println("Testing for dangling dependencies...");
        final ComponentGroup group = project.getGroup();
        boolean ok = true;
        for (ClassPackage apackage : project.getSourceComponent().getPackages()) {
            Collection<ClassPackage> externals = new LinkedList<>();
            for (ClassPackage efferent : group.getEfferents(apackage, GroupDependency.ExtraGroup)) {
                if (!isBuiltIn(efferent.getPackageName())) {
                    externals.add(efferent);
                }
            }
            if (!externals.isEmpty()) {
                ok = false;
                System.out.printf("Unresolved dependencies: %s -> %s%n", apackage, externals);
                ArrayList<ClassPackage> pairs = new ArrayList<>(2);
                pairs.add(apackage); pairs.add(null);
                for (ClassPackage external : externals) {
                    pairs.set(1, external);
                    Dependencies<ClassDescription> dependencies = PackageDependenciesFactory.getInterPackageClassDependencies(pairs);
                    for (ClassDescription afferent : dependencies.getNodes()) {
                        for (ClassDescription efferent : dependencies.getDependencies(afferent)) {
                            ClassDescription desc = group.getClass(efferent.getClassName());
                            if (desc == null) {
                                System.out.printf("WARNING: Could not locate class %s%n", efferent.getClassName());
                            }
                        }
                    }
                }
            }
        }
        if (ok) {
            System.out.println("No dangling dependencies found!");
        }
    }

    // Indicates which packages are known to be built-in to Java.
    private static boolean isBuiltIn(String name) {
        return name.startsWith("java.") || name.startsWith("javax.")
                || name.startsWith("org.w3c.dom") || name.startsWith("org.xml.sax");
    }

    private static void checkInterPackageCycles(ProjectDependencies project) {
        System.out.println("=============================================================");
        System.out.println("Checking for inter-package cycles...");
        Map<ClassPackage,Integer> cycleCounts = DependenciesFactory.getCycleCounts(project.getInterPackageEfferentDependencies());
        if (cycleCounts.isEmpty()) {
        	System.out.println("No inter-package cycles detected!");
        	return;
        }
        System.out.printf("WARNING: Detected one or more package cycles:%n");
        int minNumCycles = 0, maxNumCycles = 0;
        for (Entry<ClassPackage, Integer> entry : cycleCounts.entrySet()) {
        	final int numNodeCycles = entry.getValue();
        	if (numNodeCycles > 0) {
        		if (numNodeCycles == 1) {
        			System.out.printf("+ 1 cycle contains %s%n", entry.getKey());
        		} else {
        			System.out.printf("+ %d cycles contain %s%n", numNodeCycles, entry.getKey());
        		}
        		if (numNodeCycles > minNumCycles) {
        			minNumCycles = numNodeCycles;
        		}
        		maxNumCycles += numNodeCycles;
        	}
        }
        System.out.printf("Expect between %d and %d cycles%n", minNumCycles, maxNumCycles);
        Collection<Cycle<ClassPackage>> cycles = project.getInterPackageCycles();
        System.out.printf("WARNING: Detected %s:%n", pluralise(cycles.size(), "package cycle", "", "s"));
        int i = 0;
        for (Cycle<ClassPackage> cycle : cycles) {
        	System.out.printf("+ %d %s%n", ++i, cycle);
        	for (ClassPackage node : cycle) {
        		Integer count = cycleCounts.get(node);
        		if (count == null) {
        			// More cycles than expected - ignore.
        		} else if (count == 1) {
        			cycleCounts.remove(node); // Last expected cycle counted.
        		} else {
        			cycleCounts.put(node, count - 1); // One more expected cycle counted.
        		}
        	}
        	Collection<Cycle<ClassDescription>> classCycles = PackageDependenciesFactory.getClassCycles(cycle);
        	if (classCycles.isEmpty()) {
        		//System.out.println("  * No class cycles detected!");
        	} else {
        		System.out.printf("  * %s detected:%n", pluralise(classCycles.size(), "class cycle", "", "s"));
        		int j = 0;
        		for (Cycle<ClassDescription> classCycle : classCycles) {
        			System.out.printf("    - %d.%d %s%n", i, ++j, classCycle);
        		}
        	}
        }
        if (!cycleCounts.isEmpty()) {
        	System.out.println("WARNING: There are one or more additional, obscured cycles containing:");
        	for (ClassPackage node : cycleCounts.keySet()) {
        		System.out.printf("+ %s%n", node);
        	}
        }
    }

    private static void checkInterPackageMetrics(ProjectDependencies project) {
        System.out.println("=============================================================");
        System.out.println("Computing inter-package metrics...");
        System.out.flush();
        System.out.print(" [A=abstraction, I=instability, D=distance, F=fragility, ");
        System.out.println("aC=afferent-coverage, eC=efferent-coverage]");
        List<FamilyMetrics<ClassPackage>> packageMetrics = Collections.asList(project.getInterPackageMetrics());
        sortMetricsByScore(packageMetrics);
        int totDependencies = 0, totBadDependencies = 0;
        double totScore = 0;
        for (FamilyMetrics<ClassPackage> fmetrics : packageMetrics) {
            final NodeMetrics<ClassPackage> metrics = fmetrics.getNodeMetrics();
            final ClassPackage reference = metrics.getReference();
            System.out.printf("* %s: A=%4.2f, I=%4.2f, D=%4.2f, F=%4.2f, aC=%4.2f, eC=%4.2f, score=%5.3f%n",
                    reference,
                    metrics.getAbstraction(), metrics.getInstability(),
                    metrics.getDistance(), fmetrics.getFragility(),
                    metrics.getAfferentCoverage(), metrics.getEfferentCoverage(),
                    fmetrics.getScore());
            totScore += fmetrics.getScore();
            for (DependencyMetrics<ClassPackage> dmetrics : fmetrics.getDependencyMetrics()) {
                totDependencies++;
                if (dmetrics.hasSmell()) {
                    totBadDependencies++;
                    String smell = describeSmell(metrics, dmetrics);
                    if (smell != null) {
                        System.out.println(smell);
                    }
                }
            }
        }
        System.out.printf("Overall: F=%4.2f, score=%5.3f%n",
                1.0 * totBadDependencies / totDependencies, totScore / packageMetrics.size());
    }

    private static <T> String describeSmell(NodeMetrics<T> metrics, DependencyMetrics<T> dmetrics) {
        StringBuilder badPrinciples = new StringBuilder();
        if (REPORT_DIP && dmetrics.breaksDependencyInversionPrinciple()) {
            badPrinciples.append(" DIP");
        }
        if (REPORT_SAP && dmetrics.breaksStableAbstractionsPrinciple()) {
            badPrinciples.append(" SAP");
        }
        if (REPORT_SDP && dmetrics.breaksStableDependencyPrinciple()) {
            badPrinciples.append(" SDP");
        }
        if (REPORT_ADP && dmetrics.breaksAbstractDependencyPrinciple()) {
            badPrinciples.append(" ADP");
        }
        return (badPrinciples.length() > 0) 
            ? (dmetrics.getAfferentMetrics() == metrics) 
                ? String.format("    -> %s%s", dmetrics.getEfferentMetrics().getReference(), badPrinciples)
                : String.format("    <- %s%s", dmetrics.getAfferentMetrics().getReference(), badPrinciples)
            : null;
    }

    private static void checkIntraPackageCycles(ProjectDependencies project) {
        System.out.println("=============================================================");
        System.out.println("Checking for intra-package cycles...");
        System.out.flush();
        boolean cyclesFound = false;
        for (ClassPackage apackage : project.getPackages()) {
            Collection<Cycle<ClassDescription>> cycles = project.getIntraPackageCycles(apackage);
            if (cycles.isEmpty()) {
                continue;
            }
            cyclesFound = true;
            System.out.printf("* %s: %s detected%n", apackage, pluralise(cycles.size(), "class cycle", "", "s"));
            int j = 0;
            for (Cycle<ClassDescription> classCycle : cycles) {
                System.out.printf("    + %d %s%n", ++j, classCycle);
            }
        }
        if (!cyclesFound) {
            System.out.println("No intra-package cycles detected!");
        }
    }

    private static void checkIntraPackageMetrics(ProjectDependencies project) {
        System.out.println("=============================================================");
        System.out.println("Computing intra-package class-dependency metrics...");
        System.out.println(" [#dependencies, E=energy, vA=visible-abstraction, tA=total-abstraction, sC=self-coverage, I=instability, D=distance, F=fragility]");
        System.out.flush();
        int totDependencies = 0, totBadDependencies = 0;
        double overallScore = 0;
        List<ClassPackage> packages = Collections.asList(project.getPackages());
        sortPackagesByName(packages);
        for (ClassPackage apackage : packages) {
            List<FamilyMetrics<ClassDescription>> packageMetrics = Collections.asList(project.getIntraPackageMetrics(apackage));
            System.out.printf("* %s [%d]: ", apackage, packageMetrics.size());
            PackageSummary summary = SummaryFactory.summarisePackage(apackage);
            System.out.printf("E=%4.2f, vA=%4.2f, tA=%4.2f, sC=%4.2f, ", 
                    summary.getEnergy(), 
                    summary.getVisibleAbstraction(), summary.getOverallAbstraction(),
                    ratio(packageMetrics.size(), apackage.numClasses(), 0));
            if (packageMetrics.isEmpty()) {
                System.out.printf("I=%4.2f, F=%4.2f, score=%5.3f%n", 1.0, 0.0, 0.0);
                System.out.flush();
                continue;
            }
            int numDependencies = 0, numBadDependencies = 0;
            double sumScore = 0, sumInstability = apackage.numClasses();
            for (FamilyMetrics<ClassDescription> fmetrics : packageMetrics) {
                sumInstability += fmetrics.getNodeMetrics().getInstability() - 1;
                sumScore += fmetrics.getScore();
                for (DependencyMetrics<ClassDescription> dmetrics : fmetrics.getDependencyMetrics()) {
                    numDependencies++;
                    if (dmetrics.hasSmell()) {
                        numBadDependencies++;
                    }
                }
            }
            double pkgFragility = 1.0 * numBadDependencies / numDependencies;
            double pkgScore = sumScore / packageMetrics.size();
            double pkgInstability = sumInstability / apackage.numClasses();
            System.out.printf("I=%4.2f, F=%4.2f, score=%5.3f%n", pkgInstability, pkgFragility, pkgScore);
            System.out.flush();
            totDependencies += numDependencies;
            totBadDependencies += numBadDependencies;
            overallScore += pkgScore;
            if (numBadDependencies == 0) continue;
            sortMetricsByScore(packageMetrics);
            for (FamilyMetrics<ClassDescription> fmetrics : packageMetrics) {
                final NodeMetrics<ClassDescription> metrics = fmetrics.getNodeMetrics();
                boolean isShown = false;
                for (DependencyMetrics<ClassDescription> dmetrics : fmetrics.getDependencyMetrics()) {
                    if (dmetrics.hasSmell()) {
                        String smell = describeSmell(metrics, dmetrics);
                        if (smell != null) {
                            if (!isShown) {
                                System.out.printf("  + %s: A=%4.2f, I=%4.2f, D=%4.2f, F=%4.2f, score=%5.3f%n",
                                        metrics.getReference(), metrics.getAbstraction(),
                                        metrics.getInstability(), metrics.getDistance(),
                                        fmetrics.getFragility(), fmetrics.getScore());
                                isShown = true;
                            }
                            System.out.println(smell);
                        }
                    }
                }
            }
        }
        System.out.printf("Overall: F=%4.2f, score=%5.3f%n",
                1.0 * totBadDependencies / totDependencies, overallScore / packages.size());
    }

    private static void sortPackagesByName(List<ClassPackage> packages) {
        Collections.sort(
                packages,
                new Comparator<ClassPackage>() {
                    @Override
                    public int compare(ClassPackage apackage1, ClassPackage apackage2) {
                        return apackage1.getPackageName().compareTo(apackage2.getPackageName());
                    }
                });
    }

    private static <T> void sortMetricsByScore(List<FamilyMetrics<T>> metrics) {
        Collections.sort(
                metrics,
                new Comparator<FamilyMetrics<T>>() {
                    @Override
                    public int compare(FamilyMetrics<T> metrics1, FamilyMetrics<T> metrics2) {
                        return (int) Math.signum(metrics1.getScore() - metrics2.getScore());
                    }
                });
    }

}
