package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
This class contains the name and metrics of a class contained in the local project that will be analysed. This is
useful for the analysis mode of the program
 */
public class Metric {

    //"/Users/fernandog/IdeaProjects/SZZUnleashed/";

    private static final int numberOfMetrics = 19;
    private static final String jarPath = "./ckjm_ext.jar";

    public List<String> classesList;
    public double[][] metrics;

    public Metric(String projectPath) {

        File[] projectFiles = new File(projectPath).listFiles();
        this.classesList =  getAllClasses(projectFiles);
        this.metrics = getAllMetrics(classesList);

    }

    /*
    The names of all the classes of the project are collected.
     */
    private static List<String> getAllClasses(File[] projectFiles){
        List<String> classesList=  new ArrayList<>();
        for(File file: projectFiles){
            if(file.isDirectory()){
                classesList.addAll(getAllClasses(file.listFiles()));
            }
            else if(file.getName().endsWith(".class")) {
                try {
                    classesList.add(file.getCanonicalPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return classesList;
    }

    /*
    The metrics of every class of the project are collected
     */
    private static double[][] getAllMetrics(List<String> classesList){
        double metrics[][] = new double[classesList.size()][numberOfMetrics];
        int classPosition = 0;
        for(String path: classesList){
            metrics[classPosition] = getValues(path);
            classPosition++;
        }

        return metrics;

    }

    /*
    The metrics of the class given in path are computed using the program ckjm_ext, which is not part of this project, but
    developed by the Wroclow University
     */
    private static double[] getValues(String path) {
        try {
            //The external program is loaded to make if compute the metrics
            String command = "java -jar "+ jarPath +" " + path;
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader in = new BufferedReader(  new InputStreamReader(process.getInputStream()));
            String text = in.readLine();

            //The program does not show the average Cyclic complexity, but its value for each method, so the mean is computed
            String ccText;
            String[] SplittedCC;
            double avgCC = 0;
            double numOfMethods = 0;
            while ( (ccText = in.readLine()) != null) {
                SplittedCC = ccText.split(" ");
                if(!SplittedCC[SplittedCC.length-1].equalsIgnoreCase("")){
                    avgCC += Double.parseDouble(SplittedCC[SplittedCC.length-1]);
                    numOfMethods++;
                }
            }
            avgCC /= numOfMethods;
            if( text == null){
                text = fullfillEmptyValues(path);
            }
            System.out.println(text);
            String[] metricsText = Arrays.copyOfRange(text.split(" "), 1, numberOfMetrics);
            in.close();
            double[] metrics = new double[metricsText.length + 1];
            for(int i=0;i<metricsText.length;i++){
                metrics[i] = Double.parseDouble(metricsText[i].replaceAll(",", "."));
            }
            metrics[metrics.length-1] = avgCC;
            return metrics;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    For some clases the metric computing program does not compute its values, so some values have to be introduced to
    avoid the program to crash. -1 is introduced to show that the values are not true.
     */
    private static String fullfillEmptyValues(String path){
        String text;
        Pattern pattern = Pattern.compile("(\\w+).class$");
        Matcher matcher = pattern.matcher(path);
        if (matcher.find( )) {
            text  = matcher.group(1);
        }else {
            text = "NoClass";
        }
        for(int i= 0; i<numberOfMetrics;i++ ){
            text = text.concat(" -1");
        }
        return text;
    }



}
