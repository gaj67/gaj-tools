/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.analyser.test;

import gaj.dependency.analyser.analysis.Analyser;
import gaj.dependency.analyser.analysis.AnalysisFactory;
import gaj.dependency.analyser.metrics.MetricsFactory;
import gaj.dependency.analyser.metrics.NodeMetrics;
import gaj.dependency.analyser.project.ProjectDependencies;
import gaj.dependency.analyser.project.ProjectDependenciesFactory;
import gaj.dependency.manager.components.ComponentFactory;
import gaj.dependency.manager.components.ComponentFactory.DuplicateClassWarning;
import gaj.dependency.manager.packages.ClassPackage;
import gaj.dependency.manager.projects.GroupProject;
import gaj.dependency.manager.projects.LoadableProject;
import gaj.dependency.manager.projects.ProjectFactory;
import java.io.File;
import java.io.IOException;

public class TestScores {

    private static final boolean USE_ADP = Boolean.parseBoolean(System.getProperty("useADP", "false"));
    private static final boolean USE_SDP = Boolean.parseBoolean(System.getProperty("useSDP", "false"));
    private static final boolean USE_SAP = Boolean.parseBoolean(System.getProperty("useSAP", "false"));
    private static final boolean USE_DIP = Boolean.parseBoolean(System.getProperty("useDIP", "true"));

    /**
     * Entry point for the run-time task.
     * 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        ComponentFactory.setDuplicateClassWarning(DuplicateClassWarning.ignore);
        MetricsFactory.setUseSDP(USE_SDP);
        MetricsFactory.setUseSAP(USE_SAP);
        MetricsFactory.setUseADP(USE_ADP);
        MetricsFactory.setUseDIP(USE_DIP);
        analyseProject(new File(args[0]), args[1]);
    }

    private static void analyseProject(File projectPath, String packageName) throws IOException {
        GroupProject project = loadProject(projectPath, false);
        ProjectDependencies projDependencies = ProjectDependenciesFactory.newProjectDependencies(project);
        checkInterPackageMetrics(projDependencies, project.getSourceComponent().getPackage(packageName));
    }

    private static GroupProject loadProject(File projectPath, boolean fromBuild) throws IOException {
        LoadableProject project = ProjectFactory.newProject(projectPath, fromBuild);
        System.out.printf("Loading project \"%s\"...%n", project.getName());
        project.load();
        return project;
    }

    private static void checkInterPackageMetrics(ProjectDependencies project, ClassPackage apackage) {
        System.out.println("=============================================================");
        System.out.println("Computing inter-package metrics...");
        System.out.flush();
        System.out.print(" [E=energy, A=abstraction, I=instability, D=distance, F=fragility, ");
        System.out.println("aC=afferent-coverage, eC=efferent-coverage, sC=self-coverage]");
        Analyser<ClassPackage> analyser = AnalysisFactory.newPackageAnalyser(
                project.getInterPackageAfferentDependencies(),
                project.getInterPackageEfferentDependencies());
        NodeMetrics<ClassPackage> metrics = analyser.getMetrics(apackage);
        System.out.printf("E=%4.2f, A=%4.2f, I=%4.2f, D=%4.2f, aC=%4.2f, eC=%4.2f, sC=%4.2f%n",
                metrics.getEnergy(),
                metrics.getAbstraction(), metrics.getInstability(),
                metrics.getDistance(),
                metrics.getAfferentCoverage(), metrics.getEfferentCoverage(), metrics.getSelfCoverage());
    }

}
