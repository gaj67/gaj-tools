/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.dependency.analyser.main;

import gaj.dependency.manager.classes.ClassDescription;
import gaj.dependency.manager.components.ComponentFactory;
import gaj.dependency.manager.components.ComponentFactory.DuplicateClassWarning;
import gaj.dependency.manager.projects.GroupProject;
import gaj.dependency.manager.projects.LoadableProject;
import gaj.dependency.manager.projects.ProjectFactory;
import java.io.File;
import java.io.IOException;

public class ExamineProjectClasses {

    /**
     * Entry point for the run-time task.
     * 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        ComponentFactory.setDuplicateClassWarning(DuplicateClassWarning.ignore);
        GroupProject project = loadProject(new File(args[0]), false);
        for (int i = 1; i < args.length; i++)
            summariseClass(project, args[i]);
    }

    private static GroupProject loadProject(File projectPath, boolean fromBuild) throws IOException {
        LoadableProject project = ProjectFactory.newProject(projectPath, fromBuild);
        System.out.printf("Loading project \"%s\"...%n", project.getName());
        project.load();
        return project;
    }

    private static void summariseClass(GroupProject project, String className) {
        ClassDescription desc = project.getSourceComponent().getClass(className);
        System.out.printf("Class: %s%n", desc);
        System.out.printf("Imports: %s%n", desc.getImportedClassNames());
        System.out.printf("Fields: %s%n", desc.getFields());
        System.out.printf("Methods: %s%n", desc.getMethods());
    }

}
