package com.company;

/*
The clustering algorithm KMeansInternal is implemented to determine groups takking the test values into acccount in order
to compute the centers.
 */

public class KMeansInternal extends Algorithm {

    public ClassValues[] computeClassAdscription(ClassValues [] classes){
        double[][] metricValues = extractMetricsFromClass(classes);
        int[] classAdscription = computeDefinitiveGroups(metricValues);
        for (int i = 0; i <classAdscription.length ; i++) {
            classes[i].GroupAdscription = classAdscription[i];
        }

        return classes;
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
