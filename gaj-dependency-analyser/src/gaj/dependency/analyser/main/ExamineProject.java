/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.analyser.main;

import gaj.dependency.analyser.analysis.ClassSummary;
import gaj.dependency.analyser.analysis.PackageSummary;
import gaj.dependency.analyser.analysis.SummaryFactory;
import gaj.dependency.manager.classes.ClassDescription;
import gaj.dependency.manager.components.ComponentFactory;
import gaj.dependency.manager.components.ComponentFactory.DuplicateClassWarning;
import gaj.dependency.manager.groups.ComponentGroup;
import gaj.dependency.manager.packages.ClassPackage;
import gaj.dependency.manager.projects.GroupProject;
import gaj.dependency.manager.projects.LoadableProject;
import gaj.dependency.manager.projects.ProjectFactory;
import gaj.iterators.utilities.Iterables;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExamineProject {

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
        GroupProject project = loadProject(projectPath, fromBuild);
        summariseProject(project);
    }

    private static String pluralise(int count, String prefix, String singularSuffix, String pluralSuffix) {
        return String.format("%d %s%s", count, prefix, (count == 1) ? singularSuffix : pluralSuffix);
    }

    private static GroupProject loadProject(File projectPath, boolean fromBuild) throws IOException {
        LoadableProject project = ProjectFactory.newProject(projectPath, fromBuild);
        System.out.printf("Loading project \"%s\"...%n", project.getName());
        project.load();
        final ComponentGroup group = project.getGroup();
        System.out.printf("Loaded %s:%n", pluralise(group.numComponents(), "component", "", "s"));
        return project;
    }

    private static void summariseProject(GroupProject project) {
        Map<String,Integer> typeCounts = new HashMap<>();
        for (ClassPackage apackage : project.getSourceComponent().getPackages()) {
            Map<String,Integer> ptypeCounts = new HashMap<>();
            PackageSummary psummary = SummaryFactory.summarisePackage(apackage);
            System.out.println("---------------------------------------------");
            System.out.printf(
                    "Package %s: A_V=%4.2f, A_T=%4.2f, E=%4.2f%n",
                    apackage,
                    psummary.getVisibleAbstraction(), psummary.getOverallAbstraction(),
                    psummary.getEnergy());
            for (ClassDescription desc : apackage.getClasses()) {
                String type = desc.getClassDesignation();
                Integer typeCount = typeCounts.get(type);
                typeCounts.put(type, (typeCount == null) ? 1 : typeCount + 1);
                typeCount = ptypeCounts.get(type);
                ptypeCounts.put(type, (typeCount == null) ? 1 : typeCount + 1);
                ClassSummary csummary = SummaryFactory.summariseClass(desc);
                System.out.printf(
                        "  + Class %s: A_V=%4.2f, A_T=%4.2f, E=%4.2f%n",
                        desc,
                        csummary.getVisibleAbstraction(), csummary.getOverallAbstraction(),
                        csummary.getEnergy());
            }
            displayTypeCounts(ptypeCounts);
        }
        System.out.println("---------------------------------------------");
        displayTypeCounts(typeCounts);
    }

    private static void displayTypeCounts(Map<String,Integer> typeCounts) {
        List<String> types = Iterables.asList(typeCounts.keySet());
        Collections.sort(types, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        System.out.printf("Types:");
        for (String type : types) System.out.printf("\t%s", type);
        System.out.println();
        System.out.printf("Counts:");
        for (String type : types) System.out.printf("\t%d", typeCounts.get(type));
        System.out.println();
    }

}
