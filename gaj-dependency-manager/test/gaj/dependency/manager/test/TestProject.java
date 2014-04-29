/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.manager.test;

import gaj.dependency.manager.components.ClassesComponent;
import gaj.dependency.manager.components.ComponentFactory;
import gaj.dependency.manager.components.ComponentFactory.DuplicateClassWarning;
import gaj.dependency.manager.dependencies.GroupDependency;
import gaj.dependency.manager.groups.ComponentGroup;
import gaj.dependency.manager.packages.ClassPackage;
import gaj.dependency.manager.projects.GroupProject;
import gaj.dependency.manager.projects.LoadableProject;
import gaj.dependency.manager.projects.ProjectFactory;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class TestProject {

    private TestProject() {}

    /**
     * Entry point for the run-time task.
     * 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        DuplicateClassWarning duplicationWarning = ComponentFactory.getDuplicateClassWarning();
        ComponentFactory.setDuplicateClassWarning(DuplicateClassWarning.warn);
        File projectPath = new File(".");
        GroupProject project = loadProject(projectPath, false);
        checkDanglingDependencies(project);
        ComponentFactory.setDuplicateClassWarning(duplicationWarning);
    }

    private static String pluralise(int count, String prefix, String singularSuffix, String pluralSuffix) {
        return String.format("%d %s%s", count, prefix, (count == 1) ? singularSuffix : pluralSuffix);
    }

    private static GroupProject loadProject(File projectPath, boolean fromBuild) throws IOException {
        LoadableProject project = ProjectFactory.newProject(projectPath, fromBuild);
        System.out.printf("Loading project \"%s\"...%n", project.getName());
        project.load();
        final ComponentGroup group = project.getGroup();
        System.out.printf("Loaded %d components:%n", group.numComponents());
        Map<String,String> sharedPackages = new HashMap<>();
        for (ClassesComponent component : group.getComponents()) {
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

}
