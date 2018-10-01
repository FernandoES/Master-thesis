package com.company;


/*
Class that contains all the required information of a class. If neither yet determined the values of Group Adscription
and numberOfBugs is set to -1.
 */
public class ClassValues {


    public String project;
    public String version;
    public String className;
    public String[] variableNames;
    public double[] metricValues;

    //In the classes from the repository the value of numberOfBugs is loaded, in the classes from local projects to be evaluated
    //the bug proneness value is set there.
    public double numberOfBugs;
    public int GroupAdscription;

    /*Complete constructor*/
    public ClassValues(String project, String version, String className,
                       String[] variableNames, double[] metricValues,
                       double numberOfBugs, int groupAdscription) {
        this.project = project;
        this.version = version;
        this.className = className;
        this.variableNames = variableNames;
        this.metricValues = metricValues;
        this.numberOfBugs = numberOfBugs;
        GroupAdscription = groupAdscription;
    }

    /*
    Method used in the analysis aplication of the program. It creates a Classvalues object from every class
    of the local project to be analysed
     */
    public static ClassValues[] CollectClassesFromProject(String projectPath, String[] variableNames){
        Metric metricOfAnalyzedProject = new Metric(projectPath);
        ClassValues[] classAnalyzedProject = new ClassValues[metricOfAnalyzedProject.classesList.size()];
        for (int i = 0; i <metricOfAnalyzedProject.classesList.size() ; i++) {
            classAnalyzedProject[i] = new ClassValues(projectPath,"-1",
                    metricOfAnalyzedProject.classesList.get(i), variableNames,
                    metricOfAnalyzedProject.metrics[i],-1.0,-1);
        }
        return classAnalyzedProject;
    }


    /*
    Method created for debugging reasons that shows all the information in an array of ClassValues objects
     */
    public static void plotArray(ClassValues[] classes){
        System.out.println("Number of classes in project: "+classes.length);
        for (int i = 0; i <classes.length ; i++) {
            plot(classes[i]);
        }
    }

    /*
    Method created for debugging reasons that shows all the information in a ClassValues object
     */
    public static void plot(ClassValues classToPlot){
        System.out.println("Name: "+classToPlot.className);
        System.out.println("Project: "+classToPlot.project);
        System.out.println("Version: "+classToPlot.version);
        System.out.println("Number of Bugs: "+classToPlot.numberOfBugs);
        System.out.println("Group adscription: "+classToPlot.GroupAdscription);
        if(classToPlot.numberOfBugs == -1 || classToPlot.GroupAdscription == -1) System.out.println("OHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
        for (int i = 0; i <classToPlot.variableNames.length ; i++) {
            System.out.print(classToPlot.variableNames[i]+" ");
        }
        System.out.println();
        for (int i = 0; i <classToPlot.variableNames.length ; i++) {
            System.out.print(classToPlot.metricValues[i]+" ");
        }
        System.out.println();
    }


    public static ClassValues[] concatArrays(ClassValues[] firstClasses, ClassValues[] secondGroupClasses){
        ClassValues[] allClasses = new ClassValues[firstClasses.length+secondGroupClasses.length];
        for (int i = 0; i <firstClasses.length ; i++) {
            allClasses[i] = firstClasses[i];
        }
        for (int i = 0; i <secondGroupClasses.length ; i++) {
            allClasses[i+firstClasses.length] = secondGroupClasses[i];
        }
        return allClasses;
    }

    /*
    This method is required to avoid the estimation of an algorithm to influence in the next estimation
     */
    public static void resetGroupAscription(ClassValues[] classes){
        for (int i = 0; i <classes.length ; i++) {
            classes[i].GroupAdscription = -1;
        }
    }

    /*
    This method is required to avoid the estimation of an algorithm to influence in the next estimation
     */
    public static void resetGroupBugs(ClassValues[] classes){
        for (int i = 0; i <classes.length ; i++) {
            resetSingleGroupBugs(classes[i]);
        }
    }

    /*
    This method is required to avoid the estimation of an algorithm to influence in the next estimation
     */
    private static void resetSingleGroupBugs(ClassValues classToReset){
        classToReset.GroupAdscription = -1;
        classToReset.numberOfBugs = -1;
    }
}
