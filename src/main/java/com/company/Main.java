package com.company;

import java.util.Collections;
import java.util.Date;
import java.util.Arrays;


public class Main {


    // Percentage of the classes loaded from repository used for training. The rest is used for testing. This value
    // is only importaint in the testing. In application every repo class is used for training and in the setOfTest
    // method this value is modified
    public  static double TRAINING_PERCENTAGE = 0.75;


    public static void main(String[] args) {

        long time1 = getCurrentTime();

        /* The selection of the user is collected*/
        UserInput userInput =  UserInput.askForInformation();//new UserInput();
        //This is commented since the file was already cleaned. If the user downloads the repository file again use it.
        //String cleanPath = Cleaner.clean(userInput.csvPath);

        /* The classes with name and metrics to train and test are loaded */
        ClassValues[] classes = Collector.loadClassValues(userInput.csvPath, userInput.limitations);
        ClassValues[] ultimativeClasses = Collector.deleteBadMetrics(classes);

        /* Factory pattern is used to select the set of algorithm which will be used */
        AlgorithmFactory factory = new AlgorithmFactory();

        /*The kind of use for the program is selected according to the user criteria previously determined*/
        switch(userInput.applyTest){
            case "t":
                testAlgorithms(classes,userInput.algorithmsSelected, userInput.numberOfGroups, factory);
                break;
            case "a":
                applyAlgorithms(classes, userInput.algorithmsSelected, userInput.projectToAnalyse, userInput.numberOfGroups, factory);
                break;
            default:
                ProjectTest test = new ProjectTest(ultimativeClasses);
                test.runTests();
        }

        long time2 = getCurrentTime();
        System.out.println("Computing time in seconds: " + ((double)(time2-time1))/1000);

    }

    /* Use of the program for testing of an algorithm */
    private static void testAlgorithms(ClassValues[] classes, String[] algorithmsSelected, int numberOfGroups, AlgorithmFactory factory){

        ClassValues[] classesToTrain = Arrays.copyOfRange(classes, 0, (int)Math.floor(classes.length* TRAINING_PERCENTAGE));
        ClassValues[] classesToTest = Arrays.copyOfRange(classes,classesToTrain.length, classes.length-1);

        applyTest( classes, classesToTrain, classesToTest,algorithmsSelected,  numberOfGroups,  factory);
    }

    /* Use of the program for analyzing an external proyect and predict the bug proneness of its classes */
    private static void applyAlgorithms(ClassValues[] classes, String[] algorithmToTest, String projectToAnalyse,
                                      int numberOfGroups, AlgorithmFactory factory){
        ClassValues[] classAnalyzedProject = ClassValues.CollectClassesFromProject(projectToAnalyse, classes[0].variableNames);
        ClassValues[] allClasses = ClassValues.concatArrays(classAnalyzedProject, classes);


        for (String algorithmToTestName: algorithmToTest) {
            Algorithm algorithm = factory.getAlgorithm(algorithmToTestName, numberOfGroups);
            ClassValues.resetGroupBugs(classAnalyzedProject);
            ClassValues.resetGroupAscription(classes);
            allClasses = algorithm.computeClassAdscription(allClasses);
            classAnalyzedProject = algorithm.apply(classes , classAnalyzedProject);
            algorithm.plot(classAnalyzedProject);

        }
    }



    private static void applyTest(ClassValues[] classes,ClassValues[] classesToTrain, ClassValues[] classesToTest,
                                  String[] algorithmsSelected, int numberOfGroups, AlgorithmFactory factory){
        for (String algorithmToTestName: algorithmsSelected) {
            long timeAlgorithm = getCurrentTime();
            Algorithm algorithm = factory.getAlgorithm(algorithmToTestName, numberOfGroups);
            ClassValues.resetGroupAscription(classes);
            classes = algorithm.computeClassAdscription(classes);
            algorithm.test(classesToTrain, classesToTest);
            System.out.println("This algorithm took " + ((double)(getCurrentTime()-timeAlgorithm))/1000+" seconds");
        }
    }
    private static long getCurrentTime(){
        Date date= new Date();
        return date.getTime();
    }

}
