package com.company;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


/*
Class that allows to load all the information of the metrics contained in the repository.
It also allows to modify the format of this information
 */

public class Collector {


    /*
    The metric and name information of each class is loaded and , if so decided, shuffled
     */
    public static ClassValues[] loadClassValues(String path, String limitations){
        String[][] textValues = loadAndSchuffle(path, limitations);
        ClassValues[] classes = new ClassValues[textValues.length];
        String[] variableNames = variableNames(path);
        double[][] metricandBugsValues = getValues(textValues);
        double[][] metricValues = new double[metricandBugsValues.length][metricandBugsValues[0].length-1];
        for (int i = 0; i <metricandBugsValues.length ; i++) {
            for (int j = 0; j <metricandBugsValues[i].length-1 ; j++) {
                metricValues[i][j] = metricandBugsValues[i][j];
            }
        }
        for (int i = 0; i <textValues.length ; i++) {
            classes[i] = new ClassValues(
                    textValues[i][0], textValues[i][1], textValues[i][2],
                    variableNames, metricValues[i],
                    metricandBugsValues[i][metricandBugsValues[i].length-1],
                    -1//-1 for "no group adscription yet
            );
        }
        return classes;
    }

    public static ClassValues[] deleteBadMetrics(ClassValues[] inputClasses){
        ClassValues[] outputClasses = new ClassValues[inputClasses.length];
        for (int i = 0; i <inputClasses.length ; i++) {

            double[] metricValues = new double[inputClasses[i].metricValues.length-4];
            String[] variableNames = new String[inputClasses[i].variableNames.length-4];

            outputClasses[i] = new ClassValues(inputClasses[i].project,inputClasses[i].version,
                    inputClasses[i].className,variableNames,metricValues,
                    inputClasses[i].numberOfBugs,inputClasses[i].GroupAdscription);

            int varCounter = 0;
            for (int j = 0; j <inputClasses[i].metricValues.length ; j++) {
                if(!inputClasses[i].variableNames[j].equalsIgnoreCase("wmc")&&
                   !inputClasses[i].variableNames[j].equalsIgnoreCase("lcom")&&
                   !inputClasses[i].variableNames[j].equalsIgnoreCase("rfc")&&
                   !inputClasses[i].variableNames[j].equalsIgnoreCase("loc")){

                    outputClasses[i].metricValues[varCounter] = inputClasses[i].metricValues[j];
                    outputClasses[i].variableNames[varCounter++] = inputClasses[i].variableNames[j];
                }
            }
        }
        return  outputClasses;
    }

    private static String[][] loadAndSchuffle(String path, String limitations){

        String[][] CSVText = loadCSV(path);
        ArrayList<String[]> textList;

        if(limitations.equalsIgnoreCase("v")){
            String[][] reducedText = selectOneVersion(CSVText);
            textList = new ArrayList<String[]>(Arrays.asList(reducedText));
        }else{
            textList = new ArrayList<String[]>(Arrays.asList(CSVText));
        }

        Collections.shuffle(textList);
        String[][] shuffledArray = textList.toArray(new String[textList.size()][]);

        //File file = new File(path);
        //file.delete();

        return shuffledArray;
    }

    private static double[][] getValues(String[][] CSVText){

        double[][] CSVValues = new double[CSVText.length][20];
        for (int i = 0; i <CSVText.length ; i++) {
                for (int j = 3; j < CSVText[i].length; j++) {
                    if(CSVText[i][j] !=null){

                        CSVValues[i][j-3] = Double.parseDouble(CSVText[i][j]);
                    }
                }
        }
        return CSVValues;
    }


    /*
    The information of classes contained in class object is converted to the object format used by weka library
     */
    public static Instances convertClassToWeka(ClassValues[] classes){

        try {
            ArrayList<Attribute> attribute = new ArrayList<Attribute>();
            Instance[] instances = new DenseInstance[classes.length];
            for (int i = 0; i < classes[0].variableNames.length; i++) {
                attribute.add(i, new Attribute(classes[0].variableNames[i]));
            }
            Instances wekaData = new Instances("cleanCSV", attribute, classes.length);
            for (int i = 0; i < instances.length; i++) {
                instances[i] = new DenseInstance(classes[i].metricValues.length);
                instances[i].setDataset(wekaData);
                for (int j = 0; j < classes[i].metricValues.length; j++) {
                    instances[i].setValue(j,classes[i].metricValues[j]);
                }
                wekaData.add(instances[i]);
            }
            return wekaData;
        }
        catch (java.lang.Exception e){
            System.out.println("convert string to weka crashed with following information:");
            e.printStackTrace();
            System.exit(1);
            return null;
        }

    }

    /*
    The name of the metric contained in every variable is loaded from the given file.
     */
    private static String[] variableNames(String CSVPath){

        BufferedReader buffer;
        String line;
        String[] splittedLine;
        String[] variableNames;
        String CSVDivider = ";";

        try {
            buffer = new BufferedReader(new FileReader(CSVPath));
            line = buffer.readLine();
            splittedLine = line.split(CSVDivider);
            variableNames = Arrays.copyOfRange(splittedLine, 3, splittedLine.length-1);
            return variableNames;

        } catch (java.io.IOException e) {
            e.printStackTrace();
            System.exit(1);
            String[] neverReached = {"name failed"};
            return neverReached;
        }
    }

    /*
    Only one version of each loaded class is loaded in order to avoid too much weight of a class and correlation.
     */
    private static String[][] selectOneVersion(String[][] CSVText){
        String[][] reducedText = new String[CSVText.length][CSVText[0].length];
        String currentProject = "";
        String firstVersion = "";
        int reducedPosition = 0;
        for (int i = 0; i <CSVText.length ; i++) {
            if(currentProject.equalsIgnoreCase(CSVText[i][0])){
                if(firstVersion.equalsIgnoreCase(CSVText[i][1])){
                    reducedText[reducedPosition++] = CSVText[i];
                }
            }
            else{
                reducedText[reducedPosition++] = CSVText[i];
                currentProject = CSVText[i][0];
                firstVersion = CSVText[i][1];
            }
        }
        return  Arrays.copyOfRange(reducedText, 0, reducedPosition);
    }

    /*
    The information of each class is loaded from the given file
     */
    private static String[][] loadCSV(String path){

        BufferedReader buffer;
        String line;
        int i=0;
        String[] splittedLine;
        String CSVDivider = ";";
        String[][] CSVText = new String[21733][23];

        try {
            buffer = new BufferedReader(new FileReader(path));
            buffer.readLine();

            while ((line = buffer.readLine()) != null) {
                splittedLine = line.split(CSVDivider);

                for (int j = 0; j <splittedLine.length ; j++) {
                    CSVText[i][j] = splittedLine[j];
                }
                i++;
            }

        } catch (java.io.IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return CSVText;
    }
}
