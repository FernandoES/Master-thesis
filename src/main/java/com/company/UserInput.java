package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
Class that contains all the options selected by the user
 */

public class UserInput {

    public String csvPath;
    public String applyTest;
    public String[] algorithmsSelected;
    public String projectToAnalyse;
    public String limitations;
    public int numberOfGroups;

    private final static String DEFAULT_CSV = "./related/informationSource/cleanCSV.csv";
    private final static String DEFAULT_APPLYTEST = "t";
    private final static String DEFAULT_PROJECT = "./";
    private final static String DEFAULT_LIMITATIONS = "v";
    private final static int DEFAULT_NUMBER_GROUPS = 2;

    private UserInput(String csvPath, String applyTest, String[] algorithmsSelected,
                     String projectToAnalyse,String limitations, int numberOfGroups) {
        this.csvPath = csvPath;
        this.applyTest = applyTest;
        this.algorithmsSelected = algorithmsSelected;
        this.projectToAnalyse = projectToAnalyse;
        this.limitations = limitations;
        this.numberOfGroups = numberOfGroups;
    }

    /*constructor for default values*/
    public UserInput(){

        this(DEFAULT_CSV, DEFAULT_APPLYTEST, AlgorithmFactory.availableAlgorithms, DEFAULT_PROJECT, DEFAULT_LIMITATIONS, DEFAULT_NUMBER_GROUPS);
    }

    public static UserInput askForInformation(){

         Scanner scanner = new Scanner(System.in);


        System.out.println("Please, introduce \'A\' for applying the algorithm to a real project cointained in the computer," +
                "\'B\' for the battery of test runned for the final project in them this program was developed;\n" +
                " or \'T\' or nothing for testing");
        String applyTest = scanner.nextLine();
        if(applyTest.length() == 0){
            applyTest = DEFAULT_APPLYTEST;
        }
        applyTest = applyTest.toLowerCase();

        if(applyTest.equalsIgnoreCase("b")){
            System.out.println("Thanks, that is all the required information since  the set of testing does not require " +
                    "selection by the user of algorithms, project limitation or number of groups");
            return  new UserInput(DEFAULT_CSV, applyTest, AlgorithmFactory.availableAlgorithms, DEFAULT_PROJECT, DEFAULT_LIMITATIONS, DEFAULT_NUMBER_GROUPS);
        }

         System.out.println("Please, introduce the path of the csv which contains the training information, press enter for standard");
         String csvPath = scanner.nextLine();
         if(csvPath.length() == 0 ){
             csvPath = DEFAULT_CSV;
         }

        System.out.println("Please, introduce the number of groups you desire; or nothing for default = 2");
         int numberOfGroups = DEFAULT_NUMBER_GROUPS;
         String input = scanner.nextLine();
         if(input.length() != 0 ) {
             try {
                 numberOfGroups = Integer.parseInt(input);
             } catch (Error e) {

             }
         }

         System.out.println("Please write \"a\" to load every version of each class for training," +
                 " or \"v\" or nothing to choose just one version of each class");
         String limitations = scanner.nextLine().toLowerCase();
         if(limitations.length() == 0 ){
             limitations = "v";
         }

        List<String> algorithmList = new ArrayList<String>();
         System.out.println("Please, introduce the name of the algorithms to be applied separated by enter," +
                 " or press enter for default example");
         enumerateAlgorithms();

        algorithmList.add(scanner.nextLine());
         if( algorithmList.get(algorithmList.size() - 1 ).length() > 0){
             System.out.println("Introduce next algorithm to compare or enter for no algorithm else");
             algorithmList.add(scanner.nextLine());
         }
        algorithmList.remove(algorithmList.size() - 1 );

         if(algorithmList.size() == 0){
             for(String algorithm:  AlgorithmFactory.availableAlgorithms){
                 algorithmList.add(algorithm);
             }
         }
         String[] algorithmsSelected = algorithmList.toArray(new String[algorithmList.size()]);;

        String projectToAnalyse = "\n";
         if(applyTest.equalsIgnoreCase("a")) {
             System.out.println("Please, introduce the path of the project that sould be analysed or press enter for example");
             projectToAnalyse = scanner.nextLine();
         }
         if(projectToAnalyse.length() == 0){
             projectToAnalyse = DEFAULT_PROJECT;
         }
        System.out.println("Thanks, that is all the required information");

        return new UserInput( csvPath, applyTest, algorithmsSelected, projectToAnalyse, limitations,numberOfGroups);
     }

     //The available algorithms are listed
    private static void enumerateAlgorithms(){
        System.out.println("The algorithms by default are: ");
        for (int i = 0; i <AlgorithmFactory.availableAlgorithms.length ; i++) {
            System.out.println(AlgorithmFactory.availableAlgorithms[i]);
        }
    }


}
