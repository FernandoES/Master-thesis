package com.company;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*

class that contains the set of test that are run to get the validity of the implemented algorithms for the
final project in them it was developed.

 */
public class ProjectTest {

    public  ClassValues[] classes;
    public static int EmptyGroups;
    private static AlgorithmFactory factory;
    private static int  minGroups = 2;
    private static int maxGroups = 202;

    public ProjectTest(ClassValues[] classes){
        this.classes = classes;
        this.factory = new AlgorithmFactory();
    }

    /*
    SET OF TESTS:

    - Every algorithm
    - with training percentages 25,50 and 75%
    - with every number of groups (Interval 2 - 50 )
    - Duplicate with/out versions ?
    - Run algorithms one by one
    - Optimize computing

     */

    /* Set of test used for analyzing the program in the final proyect where the program was developed */
    public  void runTests(){
        KMeansTest();
        wekaTrainingTest();
        hierachicalTest();
    }

    public void wekaTrainingTest(){
        String[] algs = {"ExpectationMaximization"};
        for (int i = 0; i <algs.length ; i++) {
            String text =emptyTest(algs[i]);
            printToFile(text, algs[i]);
        }
    }


    public void KMeansTest(){
        String[] algs = {"KMeansExternal","KMeansInternal","RandomCentersClustering","RandomMembersClustering","KMeansWeka"};
        double[] train = {0.25,0.5,0.75,0.8,0.85,0.9,0.95};
        for (int i = 0; i <algs.length ; i++) {
            for (int j = 0; j <train.length ; j++) {
                String text = emptyGroupsTest(algs[i],train[j]);
                printToFile(text, algs[i]+"-"+train[j] );
            }
        }
    }

    public String emptyGroupsTest(String algorithmName, double trainingPercentage){
        String output = "NumberOfGroups, Error, EmptyGroups, EmptyPercentage, NonEmptyGroups"+"\n";

        ClassValues[] classesToTrain = Arrays.copyOfRange(classes, 0, (int) Math.floor(classes.length * trainingPercentage));
        ClassValues[] classesToTest = Arrays.copyOfRange(classes, classesToTrain.length, classes.length - 1);

        for (int numberOfGroups = minGroups; numberOfGroups < maxGroups; numberOfGroups++) {

            Algorithm algorithm = factory.getAlgorithm(algorithmName, numberOfGroups);
            int repetition;
            double error = 0;
            EmptyGroups = 0;
            for (repetition = 0; repetition <10 ; repetition++) {
                error += runSingleAlgorithmsSingleMean(classes, classesToTrain, classesToTest, algorithm);
            }
            error /= repetition;
            EmptyGroups /= repetition;
            double emptyPercentage = (double)EmptyGroups/(double)numberOfGroups;
            output += numberOfGroups+","+error+","+EmptyGroups+","+emptyPercentage+","+(numberOfGroups-EmptyGroups)+"\n";
            System.gc();
        }
        System.out.println(trainingPercentage);

        return output;
    }

    public String emptyTest(String algorithmName ){
        String output = "";

        output +="NumberOfGroups, Error, EmptyGroups, EmptyPercentage, NonEmptyGroups, trainingPercentaje"+"\n";

        for (double trainingPercentage = 1; trainingPercentage <100 ; trainingPercentage++) {

        ClassValues[] classesToTrain = Arrays.copyOfRange(classes, 0, (int) Math.floor(classes.length * trainingPercentage/100));
        ClassValues[] classesToTest = Arrays.copyOfRange(classes, classesToTrain.length, classes.length - 1);

        int numberOfGroups = 0;
        Algorithm algorithm = factory.getAlgorithm(algorithmName, numberOfGroups);
        int repetition;
        double error = 0;
        EmptyGroups = 0;
        for (repetition = 0; repetition < 10; repetition++) {
            error += runSingleAlgorithmsSingleMean(classes, classesToTrain, classesToTest, algorithm);
        }
        error /= repetition;
        EmptyGroups /= repetition;
        numberOfGroups = Algorithm.getNumberOfGroups(classes);
        double emptyPercentage = (double) EmptyGroups / (double) numberOfGroups;
        output += numberOfGroups + "," + error + "," + EmptyGroups + "," + emptyPercentage + "," +
                (numberOfGroups - EmptyGroups) + "," + trainingPercentage/100+"\n";
        System.gc();
        }
        return output;
    }

    public void hierachicalTest(){
        String text = "";

        text +="NumberOfGroups, Error, EmptyGroups, EmptyPercentage, NonEmptyGroups, trainingPercentaje"+"\n";

        for (double trainingPercentage = 5; trainingPercentage <100 ; trainingPercentage += 5) {

            ClassValues[] classesToTrain = Arrays.copyOfRange(classes, 0, (int) Math.floor(classes.length * trainingPercentage/100));
            ClassValues[] classesToTest = Arrays.copyOfRange(classes, classesToTrain.length, classes.length - 1);

            int numberOfGroups = 0;
            Algorithm algorithm = factory.getAlgorithm("Hierachical", numberOfGroups);
            int repetition;
            double error = 0;
            EmptyGroups = 0;
            for (repetition = 0; repetition < 4; repetition++) {
                error += runSingleAlgorithmsSingleMean(classes, classesToTrain, classesToTest, algorithm);
            }
            error /= repetition;
            EmptyGroups /= repetition;
            numberOfGroups = Algorithm.getNumberOfGroups(classes);
            double emptyPercentage = (double) EmptyGroups / (double) numberOfGroups;
            text += numberOfGroups + "," + error + "," + EmptyGroups + "," + emptyPercentage + "," +
                    (numberOfGroups - EmptyGroups) + "," + trainingPercentage/100+"\n";
            System.gc();
        }
        printToFile(text, "Hierachical");
    }

    private static double runSingleAlgorithmsSingleMean(ClassValues[] classes, ClassValues[] classesToTrain, ClassValues[] classesToTest,
                                                  Algorithm algorithm){
        // classes array has the same memory direction pointers as classesToTrain and classesToTest, so updating its GroupAdscription
        // updates the GroupAdscription of both classesToTrain and classesToTest
        classes = algorithm.computeClassAdscription(classes);
        return algorithm.computeSingleError(classesToTrain, classesToTest);
    }

    private static void printToFile(String text, String name){
        BufferedWriter writer;
        String path = getCleanName("./related/testResults/a.b", name);
        try {
            writer = new BufferedWriter(new FileWriter(path));
            writer.write(text);
            writer.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    private static String getCleanName(String CSVPath, String name){

        Pattern pattern = Pattern.compile("(.+\\/)\\w+\\.\\w+$");
        Matcher matcher = pattern.matcher(CSVPath);
        File fileToTest;
        try{
            if (!matcher.find( )) {
                throw new Exception();
            }
            fileToTest = new File(matcher.group(1)+name+".csv");
            if(!fileToTest.exists()){
                return fileToTest.getAbsolutePath();
            }
            for(int i=0;i<10000;i++){
                fileToTest = new File(matcher.group(1)+name+"-"+i+".csv");
                if(!fileToTest.exists()){
                    return fileToTest.getAbsolutePath();
                }
            }
            throw new Exception();
        }
        catch (Exception e){
            System.out.println("Correct name of a file not found "+CSVPath+" "+name);
            System.exit(1);
            return "fail";
        }
    }
}