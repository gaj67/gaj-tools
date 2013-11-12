/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.analyser.project;

import gaj.dependency.analyser.dependencies.Dependencies;
import gaj.dependency.analyser.dependencies.DependenciesFactory;
import gaj.dependency.analyser.dependencies.ModifiableDependencies;
import gaj.dependency.manager.dependencies.GroupDependency;
import gaj.dependency.manager.groups.ComponentGroup;
import gaj.dependency.manager.groups.GroupFactory;
import gaj.dependency.manager.packages.ClassPackage;
import gaj.dependency.manager.projects.GroupProject;

public abstract class ProjectDependenciesFactory {

    private ProjectDependenciesFactory() {}

    public static ProjectDependencies newProjectDependencies(GroupProject project) {
        return new AbstractProjectDependencies(project) {
            @Override
            public Dependencies<ClassPackage> _getInterPackageEfferentDependencies() {
                    final ComponentGroup group = GroupFactory.newGroup(project.getSourceComponent(), project.getProjectsComponent());
                    ModifiableDependencies<ClassPackage> packageEfferents = DependenciesFactory.newDependencies();
                    for (ClassPackage apackage : project.getSourceComponent().getPackages()) {
                        packageEfferents.addNode(apackage); // Add package in case no dependency results.
                        for (ClassPackage efferent : group.getEfferents(apackage, GroupDependency.IntraGroup)) {
                            packageEfferents.addDependency(apackage, efferent);
                        }
                    }
                return packageEfferents;
            }

            @Override
            public Iterable<ClassPackage> getPackages() {
                return project.getSourceComponent().getPackages();
            }
        };
    }

}
