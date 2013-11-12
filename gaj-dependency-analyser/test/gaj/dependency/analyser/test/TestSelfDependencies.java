/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.analyser.test;


import gaj.dependency.analyser.dependencies.DependenciesFactory;
import gaj.dependency.analyser.dependencies.ModifiableDependencies;
import gaj.dependency.manager.components.ClassesComponent;
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
import java.util.Map;

public class TestSelfDependencies {

    private TestSelfDependencies() {}

    public static void main(String[] args) throws IOException {
        GroupProject project = loadProject(new File("."));
        final ComponentGroup group = project.getGroup();
        ModifiableDependencies<ClassPackage> efferentMap = DependenciesFactory.newDependencies();
        // Describe package dependencies.
        for (ClassPackage apackage : project.getSourceComponent().getPackages()) {
            final String packageName = apackage.getPackageName();
            System.out.printf("Analysing package: %s%n", packageName);
            if (!apackage.hasInternalSelfReference()) {
                System.out.printf("+ WARNING: No internal dependencies!%n");
            }
            System.out.printf("+ Efferent packages:%n");
            efferentMap.addNode(apackage); // Force node to exist on graph.
            for (ClassPackage efferent : group.getEfferents(apackage, GroupDependency.IntraGroup)) {
                efferentMap.addDependency(apackage, efferent);
                System.out.printf("  -> %s%n", efferent.getPackageName());
            }
            System.out.printf("+ Afferent packages:%n");
            for (ClassPackage afferent : group.getAfferents(apackage, GroupDependency.IntraGroup)) {
                System.out.printf("  <- %s%n", afferent.getPackageName());
            }
        }
        // Check for cycles.
        System.out.println("Checking for cycles using topological sorting...");
        Collection<ClassPackage> sorted = DependenciesFactory.sort(efferentMap, false);
        if (sorted == null) {
            System.out.printf("WARNING: A cycle was detected!%n");
        } else {
            System.out.printf("Topologically sorted ordering: %s%n", sorted);
        }
    }

    private static GroupProject loadProject(File projectPath) throws IOException {
        LoadableProject project = ProjectFactory.newProject(projectPath, false);
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

    private static String pluralise(int count, String prefix, String singularSuffix, String pluralSuffix) {
        return String.format("%d %s%s", count, prefix, (count == 1) ? singularSuffix : pluralSuffix);
    }

}
