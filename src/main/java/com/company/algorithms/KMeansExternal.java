package com.company.algorithms;

import com.company.*;

/*
The clustering algorithm KMeans is implemented to determine groups, not takking the test values in account to compute the
centers
 */

import java.util.Arrays;

public class KMeansExternal extends Algorithm {

    public ClassValues[] computeClassAdscription(ClassValues [] classes){

        ClassValues[] classesToTrain = Arrays.copyOfRange(classes, 0, (int) Math.floor(classes.length * Main.TRAINING_PERCENTAGE));
        ClassValues[] classesToTest = Arrays.copyOfRange(classes, classesToTrain.length, classes.length);

        double[][] metricValuesToTrain = extractMetricsFromClass(classesToTrain);
        double[][] metricValuesToTest = extractMetricsFromClass(classesToTest);

        int[] classAdscriptionToTrain = computeDefinitiveGroups(metricValuesToTrain);
        int[] classAdscriptionToTest = assignTestToCenters(metricValuesToTrain,metricValuesToTest,classAdscriptionToTrain);

        for (int i = 0; i <classAdscriptionToTrain.length ; i++) {
            classes[i].GroupAdscription = classAdscriptionToTrain[i];
        }
        for (int i = 0; i <classAdscriptionToTest.length ; i++) {
            classes[classAdscriptionToTrain.length+i].GroupAdscription = classAdscriptionToTest[i];
        }

        return classes;
    }

    int[] assignTestToCenters(double[][] valuesToTrain, double[][] valuesToTest, int[] classAdscriptionToTrain){
        double[][] centers = computeCenters(classAdscriptionToTrain,valuesToTrain);
        int[] classAdscriptionToTest = assignToClosestCenter(centers,valuesToTest);

        return classAdscriptionToTest;
    }


    private int[] computeDefinitiveGroups(double [][] values){
        double[][] centers = getRandomCenters(values);
        double[][] newCenters;
        int[] groups;
        boolean someChange;
        do {
            groups = assignToClosestCenter(centers, values);
            newCenters = computeCenters(groups, values);
            someChange = false;
            for(int i = 0;i<newCenters.length;i++){
                for (int j = 0; j < newCenters[i].length; j++) {
                    if(centers[i][j]!= newCenters[i][j]) {
                        someChange = true;
                        centers[i][j] = newCenters[i][j];
                    }
                }
            }
        }
        while(someChange);

        return groups;
    }


    private double[][] computeCenters(int[] groups,double [][] values){
        double[][] centers = new double[NUMBER_OF_GROUPS][values[0].length];
        int[] membersOfEachGroup = new int[NUMBER_OF_GROUPS];
        for(int i = 0; i < values.length; i++){
            membersOfEachGroup[groups[i]]++;
            for(int j = 0; j < values[i].length;j++){
                centers[groups[i]][j] += values[i][j];
            }
        }
        for(int i = 0; i< NUMBER_OF_GROUPS; i++){
            for (int j = 0; j < centers[i].length;j++){
                if(membersOfEachGroup[i]>0) {
                    centers[i][j] = centers[i][j] / membersOfEachGroup[i];
                }
            }
        }
        return centers;
    }

}
